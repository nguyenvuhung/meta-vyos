SUMMARY = "VyOS operational command completion script"
HOMEPAGE = "https://github.com/vyos/vyatta-op"
SECTION = "vyos/core"


LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = "git://github.com/vyos/vyatta-op.git;branch=current;protocol=https \
	  "

# snapshot from Jul 13, 2017:
SRCREV = "76822101f04681402df67fcb194e8c0fdd71c96d"

PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "vyos-cfg vyos-cfg-system"

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
	--sysconfdir=/opt/vyatta/etc \	
	"
