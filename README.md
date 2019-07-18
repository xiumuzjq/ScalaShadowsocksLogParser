# 一个 shadowsocks 日志分析器

这个项目是一个 spark 实验外围项目，将 shadowsocks 的日志解析与常见函数封装成一个 jar 包供 spark 调用


## 使用

简单使用如下：

    import com.xiumuzjq.shadowsockslogparser._
    
    
    val rawRecord = """
    2019-02-09 23:12:32 INFO     connecting www.google.com:443 from 183.154.148.216:53971
    """

    val parser = new ShadowsocksLogParser
    val shadowsocksLogRecord = parser.parseRecord(rawRecord)    // an ShadowsocksLogRecord instance

The `ShadowsocksLogRecord` class definition looks like this:

    case class ShadowsocksLogRecord (
        accessTime: String,
        destHost: String,
        destPort: String,
        srcHost: String,
        srcPort: String
    )


If you don't like using the Option/Some/None pattern, I added a method named `parseRecordReturningNullObjectOnFailure`
that returns a "Null Object" version of an `ShadowsocksLogRecord` instead of an Option.


## 编译

This project is a typical Scala/SBT project, so just use commands like this:

    sbt package

If you don't like using the Option/Some/None pattern, I added a method named `parseRecordReturningNullObjectOnFailure`
that returns a "Null Object" version of an `ShadowsocksLogRecord` instead of an Option.

I also added some methods to parse the `date`, parseTime



