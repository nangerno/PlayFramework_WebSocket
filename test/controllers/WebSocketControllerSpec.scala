package controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatestplus.play._
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test._

class WebSocketControllerSpec extends PlaySpec with OneAppPerTest {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  "WebSocketController" should {

    "authenticate with valid secret key" in {

    }

    "return items on valid Request after authentication" in {

    }

    "ignore messages until authenticated" in {

    }
  }
}