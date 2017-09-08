package tech.scoundrel
package record
package field

import net.liftweb.common._
import net.liftweb.json._
import net.liftweb.util.Helpers.tryo

trait IntTypedField extends NumericTypedField[Int] {

  def setFromAny(in: Any): Box[Int] = setNumericFromAny(in, _.intValue)

  def setFromString(s: String): Box[Int] = s match {
    case null | "" if optional_? => setBox(Empty)
    case null | "" => setBox(Failure(notOptionalErrorMessage))
    case _ => setBox(tryo(java.lang.Integer.parseInt(s)))
  }

  def defaultValue = 0

  def asJValue: JValue = valueBox.map(i => JInt(BigInt(i))) openOr (JNothing: JValue)

  def setFromJValue(jvalue: JValue): Box[Int] = jvalue match {
    case JNothing | JNull if optional_? => setBox(Empty)
    case JInt(i) => setBox(Full(i.intValue))
    case JDouble(d) => setBox(Full(d.toInt))
    case other => setBox(FieldHelpers.expectedA("JInt", other))
  }
}

class IntField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[Int, OwnerType] with MandatoryTypedField[Int] with IntTypedField {

  def owner = rec

  def this(rec: OwnerType, value: Int) = {
    this(rec)
    set(value)
  }
}

class OptionalIntField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[Int, OwnerType] with OptionalTypedField[Int] with IntTypedField {

  def owner = rec

  def this(rec: OwnerType, value: Box[Int]) = {
    this(rec)
    setBox(value)
  }
}
