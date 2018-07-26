SUMMARY = "A library to work with IP addresses and netblocks"
HOMEPAGE = "https://www.over-yonder.net/~fullermd/projects/libcidr"
SECTION = "vyos/support"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9678ca24973564440bb17b30b6c13a0d"

SRC_URI = "https://www.over-yonder.net/~fullermd/projects/libcidr/libcidr-${PV}.tar.xz \
	  "

SRC_URI[md5sum] = "c5efcc7ae114fdaa5583f58dacecd9de"
SRC_URI[sha256sum] = "afbe266a9839775a21091b0e44daaf890a46ea4c2d3f5126b3048d82b9bfbbc4"

S = "${WORKDIR}/libcidr-${PV}"


DEPENDS = " \
    sed-native \
    coreutils-native \
    "

EXTRA_OEMAKE = " \
    'CC=${CC}' \
    'CFLAGS=${CFLAGS} -I${S}/include -I${S}/src/include' \
    "

do_install () {
	install -d ${D}/${libdir} ${D}/${includedir}
	oe_runmake 'DESTDIR=${D}' 'PREFIX=${prefix}'  install-lib install-dev
}