SUMMARY = "An IPv4 and IPv6 validation utility for use in scripts"
HOMEPAGE = "https://github.com/vyos/ipaddrcheck "
SECTION = "vyos/support"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/vyos/ipaddrcheck.git;branch=master;protocol=https \
	  "

# snapshot from July 25, 2018:
SRCREV = "89bc3caa89c28d819af2ddde6df9570c01c96e94"

PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = " \
	libcheck \
	libpcre \
	libcidr0 \
	"

RDEPENDS_${PN} = " \
	libpcre \
	libcidr0 \
	"

inherit  autotools
