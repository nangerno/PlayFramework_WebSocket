package controllers

import org.apache.pekko.actor._
import org.apache.pekko.stream.Materializer
import javax.inject._
import models._
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import java.util.UUID
import java.io.{BufferedReader, FileReader, IOException}
import org.apache.commons.csv.{CSVFormat, CSVParser}
import scala.jdk.CollectionConverters._
import models.JsonFormats._

@Singleton
class WebSocketController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, materializer: Materializer) extends AbstractController(cc) {

  private val secretKey = "1234567890"
  private val items = loadItems()

  def index: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.index("view on ServerSide"))
  }

  // Define the WebSocket with both input and output types as String
  def socket: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      Props(new WebSocketActor(out, secretKey, items))
    }
  }

  private def loadItems(): Seq[Item] = {
    val filePath = "dataset.csv"
    val reader = new BufferedReader(new FileReader(filePath))

    try {
      val csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().build())
      
      val itemsList = csvParser.getRecords.asScala.toList.map { record =>
        val id = UUID.fromString(record.get("UUID"))
        val name = record.get("Name")
        val email = record.get("Email")
        
        Item(id, name, email)
      }
      itemsList
    } catch {
      case e: IOException =>
        println(s"Error reading CSV file: ${e.getMessage}")
        Seq.empty[Item]
    } finally {
      reader.close()
    }
  }
}

class WebSocketActor(out: ActorRef, secretKey: String, items: Seq[Item]) extends Actor {

  private var authenticated = false

  def receive: Receive = {
    case msg: String =>
      Json.parse(msg).validate[AuthRequest].fold(
        _ => handleRequest(msg),
        authRequest => handleAuth(authRequest)
      )
  }

  private def handleAuth(authRequest: AuthRequest): Unit = {
    if (authRequest.secretKey == secretKey) {
      authenticated = true
      out ! Json.toJson("Authenticated").toString()
    } else {
      out ! Json.toJson("Authentication failed").toString()
    }
  }

  private def handleRequest(msg: String): Unit = {
    if (!authenticated) return // Ignore messages until authenticated

    Json.parse(msg).validate[UserRequest].fold(
      _ => out ! Json.toJson("Invalid request format").toString(),
      request => {
        val matchedItems = items.filter(item => item.name == request.value || item.email == request.value).take(3)
        out ! Json.toJson(Respond(matchedItems)).toString()
      }
    )
  }
}