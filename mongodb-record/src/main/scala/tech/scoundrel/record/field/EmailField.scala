package tech.scoundrel
package record
package field

import scala.xml.Text

import net.liftweb.common.Full
import net.liftweb.http.S
import net.liftweb.proto.ProtoRules
import net.liftweb.util.FieldError

object EmailField {
  def emailPattern = ProtoRules.emailRegexPattern.vend

  def validEmailAddr_?(email: String): Boolean = emailPattern.matcher(email).matches
}

trait EmailTypedField extends TypedField[String] {
  private def validateEmail(emailValue: ValueType): List[FieldError] = {
    toBoxMyType(emailValue) match {
      case Full(email) if (optional_? && email.isEmpty) => Nil
      case Full(email) if EmailField.validEmailAddr_?(email) => Nil
      case _ => Text(S.?("invalid.email.address"))
    }
  }

  override def validations = validateEmail _ :: Nil
}

class EmailField[OwnerType <: Record[OwnerType]](rec: OwnerType, maxLength: Int)
  extends StringField[OwnerType](rec, maxLength) with EmailTypedField

class OptionalEmailField[OwnerType <: Record[OwnerType]](rec: OwnerType, maxLength: Int)
  extends OptionalStringField[OwnerType](rec, maxLength) with EmailTypedField
