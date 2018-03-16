package webpage

import org.scalajs.dom
import org.scalajs.dom.{XMLHttpRequest, html}
import scalatags.JsDom.all._

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.{Failure, Success}

import dom.ext._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

@JSExport
object WebApp1 extends {

  @JSExport
  def main(target: html.Div): Unit = {

    def now: String = {
      val dt = new js.Date()
      dt.toLocaleString()
    }

    val mnd = 13
    val min = 111285
//        val max = 111288
    val max = 117180
    val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyNCIsInN1YiI6InNlcnZpY2UiLCJpc3MiOiJJbkxpbmVUb21jYXQiLCJleHAiOjE1MjEyMDk5MDgsImlhdCI6MTUyMTEyMzUwOCwicmVjaHRlIjpbImNvbmYubWFuZGFudCIsImNvbmYucGVybWlzc2lvbnMiLCJjb25mLnBlcm1pc3Npb25zLmVkaXQiLCJjb25mLnVzZXIiLCJvbmxpbmUiLCJvbmxpbmUuZWRpdCIsIm9ubGluZS52aWV3cy5lZGl0IiwicGxhbiIsInBsYW4uZGZpdGV4dC5lZGl0IiwicGxhbi5mcC5lZGl0IiwicGxhbi5mcHJlZi5lZGl0IiwicGxhbi5rYnRleHQuZWRpdCIsInN0YW1tLmJzdG9lciIsInN0YW1tLmJzdG9lci5lZGl0Iiwic3RhbW0uZnpnIiwic3RhbW0uZnpnLmVkaXQiLCJzdGFtbS5memcybW5kIiwic3RhbW0uZnpnemFlaGxlci5lZGl0Iiwic3RhbW0ubmV0eiIsInN0YW1tLm5ldHouZWRpdCIsInN0YW1tLnBlcnMua2IiLCJzdGFtbS5wZXJzLmtiLmVkaXQiLCJzdGFtbS5wZXJzLnRmIiwic3RhbW0ucGVycy50Zi5lZGl0Iiwic3lzdGVtLm51dHp2ZXJ3Iiwic3lzdGVtLm51dHp2ZXJ3LmVkaXQiLCJ0ZXN0Lm1yIiwidmVyd2FsdHVuZy5yZWNodGUiLCJ2ZXJ3YWx0dW5nLnJlY2h0ZS5lZGl0Il19.S2VfsyYXp8SG6tSMlB3urdODteIhk4gEkmgd6RAOsZk"
    val urlPattern = (id: Int) => s"http://192.168.1.246/InLine3Api/api.php?key=doSiemensCommand&format=json&mnd=$mnd&command=testDownload&fileId=$id" +
      s"&access_token=$token"

    println(token)

    def fut(i: Int): Future[XMLHttpRequest] = Ajax.get(urlPattern(i))

    val futures = (min to max) map (i => (i, fut(i)))
    val all = Future.sequence(futures.unzip._2)
    //    all.onSuccess{case i => target.appendChild(pre("doneX").render)}

    futures.foreach { case (i, future) =>
      future.onComplete {
        case Success(xhr) =>
          val outer: js.Dynamic = js.JSON.parse(xhr.responseText)
          val inner: js.Dynamic = outer.doSiemensCommand.result
          //      val inner2 = js.JSON.parse(inner.toString)
          //      println("i2" + inner2)

          target.appendChild(
            pre(s"$i; $now; "
              + js.JSON.stringify(inner, space = 1)
            ).render
          )
        case Failure(ex) => target.appendChild(
          pre(s"$i;" + now + " ; "
            + s"Could not retrieve value for $i due to " + ex.getMessage
          ).render
        )
      }
    } // loop
  }
}