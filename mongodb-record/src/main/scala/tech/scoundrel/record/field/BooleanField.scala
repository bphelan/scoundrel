package tech.scoundrel
package record
package field

import scala.xml.NodeSeq

import net.liftweb.common._
import net.liftweb.http.SHtml
import net.liftweb.http.js.JE.{ JsNull, boolToJsExp }
import net.liftweb.http.js.JsExp
import net.liftweb.json.JsonAST._
import net.liftweb.util.Helpers.{ toBoolean, tryo }

trait BooleanTypedField extends TypedField[Boolean] {

  def setFromAny(in: Any): Box[Boolean] = in match {
    case b: java.lang.Boolean => setBox(Full(b.booleanValue))
    case Full(b: java.lang.Boolean) => setBox(Full(b.booleanValue))
    case Some(b: java.lang.Boolean) => setBox(Full(b.booleanValue))
    case (b: java.lang.Boolean) :: _ => setBox(Full(b.booleanValue))
    case _ => genericSetFromAny(in)
  }

  def setFromString(s: String): Box[Boolean] =
    if (s == null || s.isEmpty) {
      if (optional_?)
        setBox(Empty)
      else
        setBox(Failure(notOptionalErrorMessage))
    } else {
      setBox(tryo(toBoolean(s)))
    }

  private def elem(attrs: SHtml.ElemAttr*) =
    SHtml.checkbox(valueBox openOr false, (b: Boolean) => this.setBox(Full(b)), (("tabindex" -> tabIndex.toString): SHtml.ElemAttr) :: attrs.toList: _*)

  def toForm: Box[NodeSeq] =
    // FIXME? no support for optional_?
    uniqueFieldId match {
      case Full(id) => Full(elem("id" -> id))
      case _ => Full(elem())
    }

  def asJs: JsExp = valueBox.map(boolToJsExp) openOr JsNull

  def asJValue: JValue = valueBox.map(JBool) openOr (JNothing: JValue)
  def setFromJValue(jvalue: JValue) = jvalue match {
    case JNothing | JNull if optional_? => setBox(Empty)
    case JBool(b) => setBox(Full(b))
    case other => setBox(FieldHelpers.expectedA("JBool", other))
  }
}

class BooleanField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[Boolean, OwnerType] with MandatoryTypedField[Boolean] with BooleanTypedField {

  def owner = rec

  def this(rec: OwnerType, value: Boolean) = {
    this(rec)
    set(value)
  }

  def defaultValue = false
}

class OptionalBooleanField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[Boolean, OwnerType] with OptionalTypedField[Boolean] with BooleanTypedField {

  def owner = rec

  def this(rec: OwnerType, value: Box[Boolean]) = {
    this(rec)
    setBox(value)
  }
}
