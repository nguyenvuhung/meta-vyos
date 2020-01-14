DESCRIPTION = "Squid URL redirector"
HOMEPAGE = "http://www.squidguard.org/"
SECTION = "network"
DEPENDS = "gettext-native autogen-native autoconf-native libtool-native \
		db openldap mysql5 zlib"
RDEPENDS_${PN} += "squid"
LICENSE = "GPLv2"
PR = "r2"

#TODO: does not include VyOS patches! This is supposed to cause an error.

SRC_URI = " \
	https://fossies.org/linux/www/squidGuard-${PV}.tar.gz;name=tar \
	file://squidGuard.conf \
	"
SRC_URI[tar.md5sum] = "85216992d14acb29d6f345608f21f268"
SRC_URI[tar.sha256sum] = "3dd40e2a94231cc85707094b8088534f4dde5c17ba94da836d1c66d50a04e580"

LIC_FILES_CHKSUM = "file://COPYING;md5=17cccb55725bad30d60ee344fa9561e6"

S = "${WORKDIR}/squidGuard-${PV}"

EXTRA_OECONF += " \
	--with-squiduser=nobody \
	--with-db=${STAGING_INCDIR}/.. \
	--with-sg-config=${sysconfdir}/squid/squidGuard.conf \
	--with-sg-logdir=${localstatedir}/log/squid \
	--with-sg-dbhome=${localstatedir}/lib/squidguard/db \
	--with-ldap=yes \
	--with-ldap-inc=${STAGING_INCDIR} \
	--with-ldap-lib=${STAGING_LIBDIR} \
	--with-mysql=${STAGING_INCDIR}/.. \
	"

inherit autotools-brokensep

do_configure_prepend() {
	export ac_cv_header_db_h=yes
	export db_ok_version=yes
	export dbg3_ok_version=yes
	export dbg2_ok_version=yes
	cp src/config.h.in src/config.h.in.original
}

do_configure_append() {
	mv src/config.h.in.original src/config.h.in
	./config.status
}

do_install() {
	install -d ${D}${bindir}
	install -d ${D}${sysconfdir}/squid
	install -d ${D}${localstatedir}/log/squid
	install -d ${D}${localstatedir}/lib/squidguard/db
	install -m 0755 src/squidGuard ${D}${bindir}
	install -m 0644 ${WORKDIR}/squidGuard.conf ${D}${sysconfdir}/squid/squidGuard.conf
}
