package tech.scoundrel
package record
package field

import net.liftweb.common._
import net.liftweb.json._
import net.liftweb.util.Helpers.asLong

trait LongTypedField extends NumericTypedField[Long] {

  def setFromAny(in: Any): Box[Long] = setNumericFromAny(in, _.longValue)

  def setFromString(s: String): Box[Long] =
    if (s == null || s.isEmpty) {
      if (optional_?)
        setBox(Empty)
      else
        setBox(Failure(notOptionalErrorMessage))
    } else {
      setBox(asLong(s))
    }

  def defaultValue = 0L

  def asJValue: JValue = valueBox.map(l => JInt(BigInt(l))) openOr (JNothing: JValue)
  def setFromJValue(jvalue: JValue): Box[Long] = jvalue match {
    case JNothing | JNull if optional_? => setBox(Empty)
    case JInt(i) => setBox(Full(i.longValue))
    case JDouble(d) => setBox(Full(d.toLong))
    case other => setBox(FieldHelpers.expectedA("JLong", other))
  }
}

class LongField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[Long, OwnerType] with MandatoryTypedField[Long] with LongTypedField {

  def this(rec: OwnerType, value: Long) = {
    this(rec)
    set(value)
  }

  def owner = rec
}

class OptionalLongField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[Long, OwnerType] with OptionalTypedField[Long] with LongTypedField {

  def this(rec: OwnerType, value: Box[Long]) = {
    this(rec)
    setBox(value)
  }

  def owner = rec
}
