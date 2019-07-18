package com.xiumuzjq.shadowsockslogparser

/**
 * @log eg "2019-02-09 23:12:22 INFO     connecting www.google.com:443 from 183.154.148.216:53890"
 */
case class ShadowsocksLogRecord (
    accessTime: String,
    destHost: String,
    destPort: String,
    srcHost: String,
    srcPort: String
)















