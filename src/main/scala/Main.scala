import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import scala.io.Source
import scala.util.Try

/**
  * * Created by Carsten Ihlemann on 13.03.2018.     
  **/
object Main extends App {

  def writeToFile(path: String, strings: Seq[String]): Unit = {
    val out = new PrintWriter(path)
    //noinspection JavaMutatorMethodAccessedAsParameterless
    strings.foreach(out.println)
    out.close()
  }

  def parseChrome(dt: String): LocalDateTime = {
    val formatter = DateTimeFormatter.ofPattern("dd.M.yyyy, HH:mm:ss") // chrome format
    LocalDateTime.parse(dt trim, formatter)
  }

  def parseFirefox(dt: String): LocalDateTime = {
    val formatter = DateTimeFormatter.ofPattern("M/dd/yyyy, h:mm:ss a") // firefox format
    LocalDateTime.parse(dt trim, formatter)
  }

  def transformDate(dt: String): String = {
    val date = Try(parseChrome(dt)) getOrElse parseFirefox(dt)
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    date.format(formatter)
  }

  def now: String = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dt = LocalDateTime.now()
    dt.format(formatter)
  }

  def prettify(s: String): String = {
    val doublequote = """\\"""".r
    val t1 = doublequote.replaceAllIn(s, "\"")
    val doublespace = """  +"""".r
    val t2 = doublespace.replaceAllIn(t1, "\"")
    t2
  }

  val test2 = "3/15/2018, 5:54:46 PM"
  val test3 = transformDate(test2)
  val path = "res/"
  val name = "siemensDownloadsTS.txt"
  val source = Source.fromFile(path + name, "UTF-8")
  val raw = source.getLines().toList.filterNot(_.trim.startsWith(";")).filterNot(_.trim.isEmpty) // lines starting with ';' are comments
  val rawMap = raw map { _.split(";", 3) } map {
    case Array(s, t, u) => (transformDate(t trim), s toInt, u trim)
  }
  val values = rawMap filterNot { _._3 contains "No data" } sortBy(_._2) map {case(s,t,u) => (s,t, prettify(u))}

  val test = prettify(values.head._3)

  writeToFile(path + "siemensValues.txt", values map { a => a.productIterator.mkString(";") })

  println(now)
  println("hello")
}
