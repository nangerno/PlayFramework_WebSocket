package models

import java.util.UUID
import play.api.libs.json._

case class AuthRequest(secretKey: String)
case class UserRequest(value: String)
case class Item(id: UUID, name: String, email: String)
case class Respond(items: Seq[Item])

object JsonFormats {
  implicit val authRequestFormat: OFormat[AuthRequest] = Json.format[AuthRequest]
  implicit val userRequestFormat: OFormat[UserRequest] = Json.format[UserRequest]
  implicit val itemFormat: OFormat[Item] = Json.format[Item]
  implicit val respondFormat: OFormat[Respond] = Json.format[Respond]
}