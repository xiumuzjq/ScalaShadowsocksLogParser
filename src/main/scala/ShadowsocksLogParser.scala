package com.xiumuzjq.shadowsockslogparser

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import scala.util.control.Exception._
import scala.util.matching.Regex.Match
import scala.util.{Try, Success, Failure}

/**
 * A sample record:
 * 94.102.63.11 - - [21/Jul/2009:02:48:13 -0700] "GET / HTTP/1.1" 200 18209 "http://acme.com/foo.php" "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)"
 * 
 * I put this code in the 'class' so (a) the pattern could be pre-compiled and (b) the user can create
 * multiple instances of this parser, in case they want to work in a multi-threaded way.
 * I don't know that this is necessary, but I think it is for this use case.
 * 
 */

@SerialVersionUID(100L)
class ShadowsocksLogParser extends Serializable {

    private val p = "(.*) INFO\\s+connecting (.*):([^ ]*) from (.*):(.*)".r
    
    private val timePattern = DateTimeFormatter.ofPattern("y-M-d H:m:s")

    /**
     * note: group(0) is the entire record that was matched (skip it)
     * @param record Assumed to be an Apache access log combined record.
     * @return An AccessLogRecord instance wrapped in an Option.
     */
    def parseRecord(record: String): Option[ShadowsocksLogRecord] = {
        p.findFirstMatchIn(record) match {
            case Some(matcher) => Some(buildShadowsocksLogRecord(matcher))
            case None => None 
        }
    }

    /**
     * Same as parseRecord, but returns a "Null Object" version of an AccessLogRecord
     * rather than an Option.
     * 
     * @param record Assumed to be an Apache access log combined record.
     * @return An AccessLogRecord instance. This will be a "Null Object" version of an
     * AccessLogRecord if the parsing process fails. All fields in the Null Object
     * will be empty strings.
     */
    def parseRecordReturningNullObjectOnFailure(record: String): ShadowsocksLogRecord = {
        p.findFirstMatchIn(record) match {
            case Some(matcher) => buildShadowsocksLogRecord(matcher)
            case None => ShadowsocksLogParser.nullObjectShadowsocksLogRecord
        }
    }
    
    /**
     * @param A string like "2019-07-18 10:28:23"
     *
     * return A LocalDateTime object
     */
    def parseTime(time: String): Option[LocalDateTime] = {
        Try(LocalDateTime.parse(time, timePattern)).toOption
    }
    
    /**
     * @param matcher
     *
     * return A ShadowsocksLogRecord instance
     */
    private def buildShadowsocksLogRecord(matcher: Match) = {
        ShadowsocksLogRecord(
            matcher.group(1),
            matcher.group(2),
            matcher.group(3),
            matcher.group(4),
            matcher.group(5)
        )
    }
}

/**
 * A sample record:
 * 94.102.63.11 - - [21/Jul/2009:02:48:13 -0700] "GET / HTTP/1.1" 200 18209 "http://acme.com/foo.php" "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)"
 */
object ShadowsocksLogParser {

    val nullObjectShadowsocksLogRecord = ShadowsocksLogRecord("", "", "", "", "")


    /**
     * @param A String like "GET /the-uri-here HTTP/1.1"
     * @return A Tuple3(requestType, uri, httpVersion). requestType is GET, POST, etc.
     * 
     * Returns a Tuple3 of three blank strings if the method fails.
     */
    def parseRequestField(request: String): Option[Tuple3[String, String, String]] = {
        val arr = request.split(" ")
        if (arr.size == 3) Some((arr(0), arr(1), arr(2))) else None
    }
    
    
}




