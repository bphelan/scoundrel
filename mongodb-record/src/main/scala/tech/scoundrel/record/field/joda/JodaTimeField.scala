package tech.scoundrel
package record
package field
package joda

import scala.xml.NodeSeq

import net.liftweb.common._
import net.liftweb.http.S
import net.liftweb.http.S.SFuncHolder
import net.liftweb.http.js.JE.{ JsNull, Num }
import net.liftweb.json._
import net.liftweb.util.Helpers._
import net.liftweb.util.JodaHelpers

import org.joda.time.DateTime

trait JodaTimeTypedField extends TypedField[DateTime] with JodaHelpers {

  def setFromAny(in: Any): Box[DateTime] = toDateTime(in).flatMap(d => setBox(Full(d))) or genericSetFromAny(in)

  def setFromString(s: String): Box[DateTime] = s match {
    case null | "" if optional_? => setBox(Empty)
    case null | "" => setBox(Failure(notOptionalErrorMessage))
    case other => setBox(toDateTime(other))
  }

  private def elem =
    S.fmapFunc(SFuncHolder(this.setFromAny(_))) { funcName =>
      <input type={ formInputType } name={ funcName } value={ valueBox.map(v => dateTimeFormatter.print(v)) openOr "" } tabindex={ tabIndex.toString }/>
    }

  def toForm: Box[NodeSeq] =
    uniqueFieldId match {
      case Full(id) => Full(elem % ("id" -> id))
      case _ => Full(elem)
    }

  def asJs = valueBox.map(v => Num(v.getMillis)) openOr JsNull

  protected def asJInt(encode: MyType => BigInt): JValue =
    valueBox.map(v => JInt(encode(v))) openOr (JNothing: JValue)

  def asJValue: JValue = asJInt(v => v.getMillis)
  def setFromJValue(jvalue: JValue) = setFromJInt(jvalue) {
    v => toDateTime(v)
  }

  protected def setFromJInt(jvalue: JValue)(decode: BigInt => Box[MyType]): Box[MyType] = jvalue match {
    case JNothing | JNull if optional_? => setBox(Empty)
    case JInt(n) => setBox(decode(n))
    case other => setBox(FieldHelpers.expectedA("JInt", other))
  }
}

class JodaTimeField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[DateTime, OwnerType] with MandatoryTypedField[DateTime] with JodaTimeTypedField {

  def owner = rec

  def this(rec: OwnerType, value: DateTime) = {
    this(rec)
    setBox(Full(value))
  }

  def defaultValue = DateTime.now
}

class OptionalJodaTimeField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[DateTime, OwnerType] with OptionalTypedField[DateTime] with JodaTimeTypedField {

  def owner = rec

  def this(rec: OwnerType, value: Box[DateTime]) = {
    this(rec)
    setBox(value)
  }
}
