SUMMARY = "VyOS operational command completion script"
HOMEPAGE = "https://github.com/vyos/vyatta-op"
SECTION = "vyos/config"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = " \
	git://github.com/vyos/vyatta-op.git;branch=current;protocol=https \
	file://git/vyatta-tmpfs \
	"

# snapshot from Jul 13, 2017:
SRCREV = "76822101f04681402df67fcb194e8c0fdd71c96d"

PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "vyos-cfg vyos-cfg-system"
RDEPENDS_${PN} = " \
	vyos-bash \
	perl \
	pciutils \
	usbutils \
	mmc-utils \
	util-linux-lscpu \
	bind-utils \
	iperf2 \
	iperf3 \
	python \
	python-json \
	python-subprocess \
	python-math \
	python-pystache \
	${@bb.utils.contains('DISTRO_FEATURES', 'vyos-non-embedded', 'lsscsi', '',d)} \
	"

PACKAGES = "${PN}-non-embedded ${PN}"

FILES_${PN} = "/etc /opt"
FILES_${PN}-non-embedded = " \
	/opt/vyatta/share/vyatta-op/templates/show/hardware/scsi \
	/opt/vyatta/share/vyatta-op/templates/show/raid \
"

# NOTE: this software seems not capable of being built in a separate build directory
# from the source, therefore using 'autotools-brokensep' instead of 'autotools'
inherit autotools-brokensep 
#update-rc.d

# additional options to be passed to the configure script:
EXTRA_OECONF = "\
	--prefix=/opt/vyatta \
	--exec_prefix=/opt/vyatta \
	--sbindir=/opt/vyatta/sbin \
	--bindir=/opt/vyatta/bin \
	--datadir=/opt/vyatta/share \
	--sysconfdir=/opt/vyatta/etc \
	"

do_install_append () {
#	install -d ${D}${sysconfdir}/init.d
#	install vyatta-tmpfs ${D}${sysconfdir}/init.d

	# TODO: this needs to get cleaned up upstream: this package provides templates
	# for vxlan which conflict with files from the vyos-vxlan package. For now we
	# delete them here
	rm -rf ${D}/opt/vyatta/share/vyatta-op/templates/show/interfaces/vxlan
}

#INITSCRIPT_PACKAGES = "${PN}"
#INITSCRIPT_NAME_${PN} = "vyatta-tmpfs"
#INITSCRIPT_PARAMS_${PN} = "start 00 1 2 3 4 5 6 ."
