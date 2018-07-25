SUMMARY = "VyOS system-level configuration templates/scripts"
HOMEPAGE = "https://github.com/vyos/vyatta-cfg-system"
SECTION = "vyos/config"


LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = "git://github.com/vyos/vyatta-cfg-system.git;branch=current;protocol=https \
		  file://001-invoke-rc-path.patch \
		  file://002-update-timezone-node.patch \
		  file://003-fix-rsyslog-typo.patch \
		  file://004-rename-ssh-to-sshd.patch \
		  file://005-disable-ssh-keygen.patch \
		  file://006-rename-ntp-to-ntpd.patch \
		  file://007-add-sudo-secure-path.patch \
		  file://008-use-systemd-for-rsyslog-restart.patch \
		  file://009-rsyslog-disable-emergency-broadcast.patch \
		  file://git/vyatta-postconfig-bootup.script \
		  file://git/bashrc.template \
	      "

# snapshot from Apr 13, 2018:
SRCREV = "70f95999744fbb6606aebbc87ba9b326cf453728"

PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "perl"

RDEPENDS_${PN} = " \
	sudo \
	at \
	ddclient \
	dnsmasq \
	procps \
	iproute2 \
	iputils \
	traceroute \
	ethtool \
	net-tools \
	bridge-utils \
	tcpdump \
	libpam \
	rsyslog \
	less \
	lsof \
	ntp \
	openssh \
	iptables \
	conntrack-tools \
	unionfs-fuse \
	debianutils \
	perl \
	perl-misc \
	perl-lib \
	perl-module-cpan \
	libioprompt-perl \
	libnetaddrip-perl \
	libsortversions-perl \
	libfilesync-perl \
	libsocket6-perl \
	libtimedate-perl \
	libfile-slurp-perl \
	libjsonany-perl \
	libwww-perl \
	libhttpmessage-perl \
	tzdata \
	vyos-bash \
	vyos-wireless \
	util-linux-swaponoff \
	"

RDEPENDS_${PN}_append_x86 = " dmidecode"
RDEPENDS_${PN}_append_x86-64 = " dmidecode"

# add directories that otherwise wouldn't automatically get packaged up...
FILES_${PN} += "/opt /lib /usr/share"

# NOTE: this software seems not capable of being built in a separate build directory
# from the source, therefore using 'autotools-brokensep' instead of 'autotools'
inherit cpan autotools-brokensep update-rc.d

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "vyos-intfwatchd"
INITSCRIPT_PARAMS_${PN} = "start 95 2 3 4 5 ."

# additional options to be passed to the configure script:
EXTRA_OECONF = "\
    --prefix=/opt/vyatta \
    --exec_prefix=/opt/vyatta \
	--sbindir=/opt/vyatta/sbin \
	--bindir=/opt/vyatta/bin \
	--datadir=/opt/vyatta/share \
	--sysconfdir=/opt/vyatta/etc \
	"

ISSUEDIR = "${THISDIR}/files/issue"

do_install_append () {
	# copy MIB files...
	install -d ${D}/usr/share/snmp/mibs
	install ${S}/mibs/BGP4-MIB.txt ${D}/usr/share/snmp/mibs
	install ${S}/mibs/OSPF-MIB.txt ${D}/usr/share/snmp/mibs
	install ${S}/mibs/OSPF-TRAP-MIB.txt ${D}/usr/share/snmp/mibs
	install ${S}/mibs/RIPv2-MIB.txt ${D}/usr/share/snmp/mibs
	install ${S}/mibs/SOURCE-ROUTING-MIB.txt ${D}/usr/share/snmp/mibs
	install ${S}/mibs/NETWORK-SERVICES-MIB.txt ${D}/usr/share/snmp/mibs
	install ${S}/mibs/MTA-MIB.txt ${D}/usr/share/snmp/mibs
	install ${S}/mibs/DISMAN-EXPRESSION-MIB.txt ${D}/usr/share/snmp/mibs
	install ${S}/mibs/DISMAN-NSLOOKUP-MIB.txt ${D}/usr/share/snmp/mibs
	install ${S}/mibs/DISMAN-PING-MIB.txt ${D}/usr/share/snmp/mibs
	install ${S}/mibs/DISMAN-TRACEROUTE-MIB.txt ${D}/usr/share/snmp/mibs
	install ${S}/mibs/VYATTA-TRAP-MIB.txt ${D}/usr/share/snmp/mibs

	# stuff that used to be in the Debian 'postinst' script
	install -d ${D}/var/log/user
	install -d ${D}/var/core

	install -d -m 2775 ${D}/opt/vyatta/config
	install -d -m 2775 ${D}/opt/vyatta/etc/config
	install -d -m 2775 ${D}/opt/vyatta/etc/config/auth
	install -d -m 2775 ${D}/opt/vyatta/etc/config/scripts
	install -d -m 2775 ${D}/opt/vyatta/etc/config/user-data
	install -d -m 2775 ${D}/opt/vyatta/etc/config/support

	install -d ${D}/opt/vyatta/etc/logrotate
	install -d ${D}/opt/vyatta/etc/netdevice.d

	install vyatta-postconfig-bootup.script ${D}/opt/vyatta/etc/config/scripts
	install bashrc.template ${D}/opt/vyatta/etc

	if [ -f "${ISSUEDIR}/prefix_${MACHINE}" ]; then
		cd ${D}/opt/vyatta/etc
		for f in issue issue.net; do
			cat ${ISSUEDIR}/prefix_${MACHINE} ${f} > ${f}.tmp
			mv ${f}.tmp ${f}
		done
	fi
}

# Make sure to prepend variables with 'vy_' to avoid conflicts with bitbake
# variables
pkg_postinst_ontarget_${PN} () {
	vy_prefix=/opt/vyatta
	vy_exec_prefix=${vy_prefix}
	vy_sysconfdir=${vy_prefix}/etc
	vy_bindir=${vy_exec_prefix}/bin
	vy_sbindir=${vy_exec_prefix}/sbin

	# get owner/group setting changes out of the way...
	chgrp vyattacfg /opt/vyatta/config
	chgrp -R vyattacfg /opt/vyatta/etc/config

	touch /etc/sudoers
	cp -p /etc/sudoers /etc/sudoers.bak

   	# Add VyOS entries for sudoers
   	cp ${vy_sysconfdir}/sudoers /etc/sudoers.d/vyatta
   	chmod 0440 /etc/sudoers.d/vyatta

	# Turn off Debian default for %sudo
  	sed -i -e '/^%sudo/d' /etc/sudoers || true

	# TODO: check if this needs to be converted to systemd
	#update-rc.d -s vyatta-config-reboot-params start 20 2 3 4 5 .

	# Remove rsyslog logrotate since it has hardcoded assumptions about syslog
	# files
	rm -f /etc/logrotate.d/rsyslog

	# install .bashrc for 'vyos' users
	mv /opt/vyatta/etc/bashrc.template /home/vyos/.bashrc
	chown vyos:users /home/vyos/.bashrc

	# enable ssh banner
	sed -i 's/^#Banner/Banner/' /etc/ssh/sshd_config
	# make sure PermitRoot is off
	sed -i '/^PermitRootLogin/s/yes/no/' /etc/ssh/sshd_config
	# make sure PasswordAuthentication is on
	sed -i 's/^#PasswordAuthentication/PasswordAuthentication/' /etc/ssh/sshd_config
	sed -i '/^PasswordAuthentication/s/no/yes/' /etc/ssh/sshd_config

	# TODO: remove ssh v1 support
   	# add HostKeys for protocol version 1
	if [ ! -s /etc/ssh/ssh_host_key ]; then
		echo "  generating ssh RSA key..."
		ssh-keygen -q -f /etc/ssh/ssh_host_key -N '' -t rsa
	fi
	sed -i 's/^Protocol 2/Protocol 1,2/' /etc/ssh/sshd_config

   	# add UseDNS line
   	sed -i '/^UseDNS/d' /etc/ssh/sshd_config
   	echo 'UseDNS yes' >>/etc/ssh/sshd_config

   	# purge off ancient devfs stuff from /etc/securetty
	# WT: nope, OE's default is not so ancient!
   	# cp $sysconfdir/securetty /etc/securetty
   	for f in issue issue.net; do
		if [ ! -e /etc/$f.old ]; then
      		cp ${vy_sysconfdir}/$f /etc/$f
    	fi
   	done

	mkdir -p /etc/sysctl.d
   	cp ${vy_sysconfdir}/vyatta-sysctl.conf /etc/sysctl.d/30-vyatta-router.conf

	echo "kernel.printk = 3 4 1 7" > /etc/sysctl.d/40-quiet-console.conf

	# Set file capabilities - paths can be different from original Debian/VyOS
	# distro, so we always check in /bin /sbin /usr/bin and /usr/sbin
	sed -r -e '/^#/d' -e '/^[[:blank:]]*$/d' < ${vy_sysconfdir}/filecaps | \
	    while read capability path; do
			touch ${vy_sysconfdir}/5
			file_name=`basename $path`
			for p in /bin /sbin /usr/bin /usr/sbin; do
				if [ -f $p/$file_name ]; then
					# if symlink, then get symlink target. setcap doesn't work
					# on symlinks
					if [ -h $p/$file_name ]; then
					    sym_target=`readlink $p/$file_name`
						# account for 2 levels of symlinking...
						if [ -h $sym_target ]; then
						    sym_target2=`readlink $sym_target`
							touch -c $sym_target2
	       					setcap $capability $sym_target2
						else
							touch -c $sym_target
	       					setcap $capability $sym_target
						fi
					else
	       				touch -c $p/$file_name
	       				setcap $capability $p/$file_name
					fi
				fi
			done
	    done

	# Install pam_cap config
	cp ${vy_sysconfdir}/capability.conf /etc/security/capability.conf

	# Install our own version of rsyslog.conf without
	# default targets
	mv /etc/rsyslog.conf /etc/rsyslog.conf.orig
	cp ${vy_sysconfdir}/rsyslog.conf /etc/rsyslog.conf

	# TODO: VyOS wants full speed, embedded devices may want to be more
	# power-efficient
	# Install own version of cpufrequtils config
	#cp ${vy_sysconfdir}/cpufrequtils /etc/default/cpufrequtils

	# TODO: it seems that OE doesn't have an rc.local file to start with...
	# call vyatta-postconfig-bootup.script from /etc/rc.local
	if ! grep -q /opt/vyatta/etc/config/scripts/vyatta-postconfig-bootup.script \
	    /etc/rc.local
	then
	    cat <<EOF >>/etc/rc.local
# Do not remove the following call to vyatta-postconfig-bootup.script.
# Any boot time workarounds should be put in script below so that they
# get preserved for the new image during image upgrade.
POSTCONFIG=/opt/vyatta/etc/config/scripts/vyatta-postconfig-bootup.script
[ -x \$POSTCONFIG ] && \$POSTCONFIG
exit 0
EOF
	fi

	touch /etc/environment

	sed -i 's/^set /builtin set /' /usr/share/bash-completion/bash_completion

	# TODO: check the following:
	#dpkg-reconfigure -f noninteractive openssh-server
	#rm -f /etc/ssh/*.broken
	#update-rc.d -f ssh remove >/dev/null

	# TODO: OE has no /etc/pam.d/login file
	# Fix up PAM configuration for login so that invalid users are prompted
	# for password
	#sed -i 's/requisite[ \t][ \t]*pam_securetty.so/required pam_securetty.so/' /etc/pam.d/login

	# TODO: OE currently uses busybox 'adduser' command
	# Change default shell for new accounts
	#sed -i -e ':^DSHELL:s:/bin/bash:/bin/vbash:' /etc/adduser.conf

	# Do not allow users to change full name field (controlled by Vyatta config)
	sed -i -e 's/^CHFN_RESTRICT/#&/' /etc/login.defs

	# TODO: OE has no /etc/pam.d/passwd command
	# Only allow root to use passwd command
	#if ! grep -q 'pam_succeed_if.so' /etc/pam.d/passwd ; then
	#    sed -i -e '/^@include/i \
	#password	requisite pam_succeed_if.so user = root
	#' /etc/pam.d/passwd
	#fi

	# remove unnecessary ddclient script in /etc/ppp/ip-up.d/
	# this logs unnecessary messages trying to start ddclient
	rm -f /etc/ppp/ip-up.d/ddclient
}
