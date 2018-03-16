package webpage

import org.scalajs.dom
import org.scalajs.dom.{Node, Element}
import scalajs.js
import scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import dom.html
@JSExport
object Weather0 extends{
  @JSExport
  def main(target: html.Div): Unit = {
    val xhr = new dom.XMLHttpRequest()
    val key = "2baefae30a432af9d16411b00f981d8c"
    val url = "http://api.openweathermap.org/" +
      "data/2.5/weather?q=Singapore" +
      s"&appid=$key"

    xhr.open("GET", url)

    println(url)

    xhr.onload = (e: dom.Event) => {
      if (xhr.status == 200) {
        target.appendChild(
          pre(xhr.responseText).render
        )
      }
    }
    xhr.send()
  }
}