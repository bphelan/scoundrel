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

trait EnumTypedField[EnumType <: Enumeration] extends TypedField[EnumType#Value] {
  protected val enum: EnumType
  protected val valueManifest: Manifest[EnumType#Value]

  def toInt: Box[Int] = valueBox.map(_.id)

  def fromInt(in: Int): Box[EnumType#Value] = tryo(enum(in))

  def setFromAny(in: Any): Box[EnumType#Value] = in match {
    case (value: Int) => setBox(fromInt(value))
    case Some(value: Int) => setBox(fromInt(value))
    case Full(value: Int) => setBox(fromInt(value))
    case (value: Int) :: _ => setBox(fromInt(value))
    case (value: Number) => setBox(fromInt(value.intValue))
    case Some(value: Number) => setBox(fromInt(value.intValue))
    case Full(value: Number) => setBox(fromInt(value.intValue))
    case (value: Number) :: _ => setBox(fromInt(value.intValue))
    case _ => genericSetFromAny(in)(valueManifest)
  }

  def setFromString(s: String): Box[EnumType#Value] =
    if (s == null || s.isEmpty) {
      if (optional_?)
        setBox(Empty)
      else
        setBox(Failure(notOptionalErrorMessage))
    } else {
      setBox(asInt(s).flatMap(fromInt))
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

  def asJIntOrdinal: JValue = toInt.map(i => JInt(BigInt(i))) openOr (JNothing: JValue)
  def setFromJIntOrdinal(jvalue: JValue): Box[EnumType#Value] = jvalue match {
    case JNothing | JNull if optional_? => setBox(Empty)
    case JInt(i) => setBox(fromInt(i.intValue))
    case other => setBox(FieldHelpers.expectedA("JInt", other))
  }

  def asJStringName: JValue = valueBox.map(v => JString(v.toString)) openOr (JNothing: JValue)
  def setFromJStringName(jvalue: JValue): Box[EnumType#Value] = jvalue match {
    case JNothing | JNull if optional_? => setBox(Empty)
    case JString(s) => setBox(Option(enum.withName(s)) ?~ ("Unknown value \"" + s + "\""))
    case other => setBox(FieldHelpers.expectedA("JString", other))
  }

  def asJValue: JValue = asJIntOrdinal
  def setFromJValue(jvalue: JValue): Box[EnumType#Value] = setFromJIntOrdinal(jvalue)
}

class EnumField[OwnerType <: Record[OwnerType], EnumType <: Enumeration](rec: OwnerType, protected val enum: EnumType)(implicit m: Manifest[EnumType#Value])
    extends Field[EnumType#Value, OwnerType] with MandatoryTypedField[EnumType#Value] with EnumTypedField[EnumType] {
  def this(rec: OwnerType, enum: EnumType, value: EnumType#Value)(implicit m: Manifest[EnumType#Value]) = {
    this(rec, enum)
    set(value)
  }

  def owner = rec
  protected val valueManifest = m
}

class OptionalEnumField[OwnerType <: Record[OwnerType], EnumType <: Enumeration](rec: OwnerType, protected val enum: EnumType)(implicit m: Manifest[EnumType#Value])
    extends Field[EnumType#Value, OwnerType] with OptionalTypedField[EnumType#Value] with EnumTypedField[EnumType] {
  def this(rec: OwnerType, enum: EnumType, value: Box[EnumType#Value])(implicit m: Manifest[EnumType#Value]) = {
    this(rec, enum)
    setBox(value)
  }

  def owner = rec
  protected val valueManifest = m
}
