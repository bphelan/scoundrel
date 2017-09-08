package tech.scoundrel
package record
package field

import scala.reflect.Manifest
import scala.xml.NodeSeq

import net.liftweb.common.Box.option2Box
import net.liftweb.common._
import net.liftweb.http.SHtml
import net.liftweb.http.js.JE.{ JsNull, Str }
import net.liftweb.json._
import net.liftweb.util.Helpers._

trait EnumNameTypedField[EnumType <: Enumeration] extends TypedField[EnumType#Value] {
  protected val enum: EnumType
  protected val valueManifest: Manifest[EnumType#Value]

  def setFromAny(in: Any): Box[EnumType#Value] = genericSetFromAny(in)(valueManifest)

  def setFromString(s: String): Box[EnumType#Value] = s match {
    case null | "" if optional_? => setBox(Empty)
    case null | "" => setBox(Failure(notOptionalErrorMessage))
    case _ => setBox(enum.values.find(_.toString == s))
  }

  /** Label for the selection item representing Empty, show when this field is optional. Defaults to the empty string. */
  def emptyOptionLabel: String = ""

  /**
   * Build a list of (value, label) options for a select list.  Return a tuple of (Box[String], String) where the first string
   * is the value of the field and the second string is the Text name of the Value.
   */
  def buildDisplayList: List[(Box[EnumType#Value], String)] = {
    val options = enum.values.toList.map(a => (Full(a), a.toString))
    if (optional_?) (Empty, emptyOptionLabel) :: options else options
  }

  private def elem = SHtml.selectObj[Box[EnumType#Value]](buildDisplayList, Full(valueBox), setBox(_)) % ("tabindex" -> tabIndex.toString)

  def toForm: Box[NodeSeq] =
    uniqueFieldId match {
      case Full(id) => Full(elem % ("id" -> id))
      case _ => Full(elem)
    }

  def defaultValue: EnumType#Value = enum.values.iterator.next

  def asJs = valueBox.map(_ => Str(toString)) openOr JsNull

  def asJStringName: JValue = valueBox.map(v => JString(v.toString)) openOr (JNothing: JValue)
  def setFromJStringName(jvalue: JValue): Box[EnumType#Value] = jvalue match {
    case JNothing | JNull if optional_? => setBox(Empty)
    case JString(s) => setBox(enum.values.find(_.toString == s) ?~ ("Unknown value \"" + s + "\""))
    case other => setBox(FieldHelpers.expectedA("JString", other))
  }

  def asJValue: JValue = asJStringName
  def setFromJValue(jvalue: JValue): Box[EnumType#Value] = setFromJStringName(jvalue)
}

class EnumNameField[OwnerType <: Record[OwnerType], EnumType <: Enumeration](rec: OwnerType, protected val enum: EnumType)(implicit m: Manifest[EnumType#Value])
    extends Field[EnumType#Value, OwnerType] with MandatoryTypedField[EnumType#Value] with EnumNameTypedField[EnumType] {
  def this(rec: OwnerType, enum: EnumType, value: EnumType#Value)(implicit m: Manifest[EnumType#Value]) = {
    this(rec, enum)
    set(value)
  }

  def owner = rec
  protected val valueManifest = m
}

class OptionalEnumNameField[OwnerType <: Record[OwnerType], EnumType <: Enumeration](rec: OwnerType, protected val enum: EnumType)(implicit m: Manifest[EnumType#Value])
    extends Field[EnumType#Value, OwnerType] with OptionalTypedField[EnumType#Value] with EnumNameTypedField[EnumType] {
  def this(rec: OwnerType, enum: EnumType, value: Box[EnumType#Value])(implicit m: Manifest[EnumType#Value]) = {
    this(rec, enum)
    setBox(value)
  }

  def owner = rec
  protected val valueManifest = m
}
