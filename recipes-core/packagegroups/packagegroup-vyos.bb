DESCRIPTION = "Packages used by VyOS"
LICENSE = "MIT"
PR = "r1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

# core packages required for minimal functionality
VYOS_CORE = " \
    vyos-bash \
    "

VYOS_CONFIG = " \
    vyos-cfg \
    vyos-cfg-system \
    vyos-op \
    vyos-config-migrate \
    vyos-config-mgmt \
    vyos-util \
    vyos-cron \
    "

VYOS_NET = " \
    vyos-conntrack \
    vyos-conntrack-sync \
    vyos-nat \
    vyos-wanloadbalance \
    vyos-lldp \
    vyos-igmpproxy \
    vyos-zone \
    vyos-watchdog \
    "

VYOS_WLAN = " \
    vyos-wireless \
    "

VYOS_ROUTING = " \
    vyos-cfg-quagga \
    vyos-op-quagga \
    vyos-ipv6-rtradv \
    "

VYOS_WWAN = " \
    virtual/vyos-wwan \
    "

VYOS_VPN = " \
    vyos-cfg-vpn \
    vyos-op-vpn \
    vyos-openvpn \
    vyos-ravpn \
    vyos-nhrp \
    "

VYOS_QOS = "\
    vyos-cfg-qos \
    vyos-op-qos \
    "

VYOS_PPPOE = " \
    vyos-cfg-op-pppoe \
    vyos-pppoe-server \
    "

VYOS_DHCP = " \
    vyos-op-dhcp-server \
    vyos-cfg-dhcp-server \
    vyos-cfg-dhcp-relay \
    "

VYOS_MONITOR = " \
    vyos-netflow \
    vyos-eventwatch \
    "

VYOS_FIREWALL = " \
    vyos-cfg-firewall \
    vyos-op-firewall \
    "

VYOS_REDUNDANCY = " \
    vyos-vrrp \
    "

VYOS_PROXY = " \
    vyos-webproxy \
    "

VYOS_SWUPDATE = " \
    swupdate \
    swupdate-www \
    "

VYOS_DEBUG = " \
    strace \
    "

VYOS_EMBEDDED = " \
    "

VYOS_NON_EMBEDDED = " \
    vyos-op-non-embedded \
    "

RDEPENDS_${PN} = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-core', '${VYOS_CORE}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-config', '${VYOS_CONFIG}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-net', '${VYOS_NET}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-wlan', '${VYOS_WLAN}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-routing', '${VYOS_ROUTING}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-wwan', '${VYOS_WWAN}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-vpn', '${VYOS_VPN}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-qos', '${VYOS_QOS}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-pppoe', '${VYOS_PPPOE}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-dhcp', '${VYOS_DHCP}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-monitor', '${VYOS_MONITOR}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-firewall', '${VYOS_FIREWALL}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-redundancy', '${VYOS_REDUNDANCY}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-proxy', '${VYOS_PROXY}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-swupdate', '${VYOS_SWUPDATE}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-debug', '${VYOS_DEBUG}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-embedded', '${VYOS_EMBEDDED}', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vyos-non-embedded', '${VYOS_NON_EMBEDDED}', '',d)} \
    "