package tech.scoundrel
package record
package field

import net.liftweb.common._
import net.liftweb.json._
import net.liftweb.util.Helpers.tryo

trait DoubleTypedField extends NumericTypedField[Double] {

  def setFromAny(in: Any): Box[Double] = setNumericFromAny(in, _.doubleValue)

  def setFromString(s: String): Box[Double] =
    if (s == null || s.isEmpty) {
      if (optional_?)
        setBox(Empty)
      else
        setBox(Failure(notOptionalErrorMessage))
    } else {
      setBox(tryo(java.lang.Double.parseDouble(s)))
    }

  def defaultValue = 0.0

  def asJValue: JValue = valueBox.map(JDouble) openOr (JNothing: JValue)

  def setFromJValue(jvalue: JValue) = jvalue match {
    case JNothing | JNull if optional_? => setBox(Empty)
    case JDouble(d) => setBox(Full(d))
    case JInt(i) => setBox(Full(i.toDouble))
    case other => setBox(FieldHelpers.expectedA("JDouble", other))
  }
}

class DoubleField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[Double, OwnerType] with MandatoryTypedField[Double] with DoubleTypedField {

  def this(rec: OwnerType, value: Double) = {
    this(rec)
    set(value)
  }

  def owner = rec
}

class OptionalDoubleField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[Double, OwnerType] with OptionalTypedField[Double] with DoubleTypedField {

  def this(rec: OwnerType, value: Box[Double]) = {
    this(rec)
    setBox(value)
  }

  def owner = rec
}
