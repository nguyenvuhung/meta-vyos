SUMMARY = "VyOS config/op code for IPv6 router advertisements"
HOMEPAGE = "https://github.com/vyos/vyatta-ipv6-rtradv"
SECTION = "vyos/routing"


LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = "git://github.com/vyos/vyatta-ipv6-rtradv.git;branch=current;protocol=https \
	  "

# snapshot from Aug 10, 2017:
SRCREV = "97b54c3962e952a0bf975267a7f8b528c03434cd"

PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "vyos-bash"
RDEPENDS_${PN} = "sed perl procps vyos-bash  sudo vyos-cfg radvd"

FILES_${PN} += "/opt"


# NOTE: this software seems not capable of being built in a separate build directory
# from the source, therefore using 'autotools-brokensep' instead of 'autotools'
inherit autotools-brokensep

# additional options to be passed to the configure script:
EXTRA_OECONF = "\
    --prefix=/opt/vyatta \
    --exec_prefix=/opt/vyatta \
	--sbindir=/opt/vyatta/sbin \
	--bindir=/opt/vyatta/bin \
	--datadir=/opt/vyatta/share \
	--sysconfdir=/etc \
	"
	