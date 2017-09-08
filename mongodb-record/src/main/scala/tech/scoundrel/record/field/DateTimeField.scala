package tech.scoundrel
package record
package field

import java.util.{ Calendar, Date }
import scala.xml.NodeSeq

import net.liftweb.common._
import net.liftweb.http.S
import net.liftweb.http.S.SFuncHolder
import net.liftweb.http.js.JE.{ JsNull, Str }
import net.liftweb.json.{ DefaultFormats, JValue }
import net.liftweb.util.Helpers._

trait DateTimeTypedField extends TypedField[Calendar] {
  private final def dateToCal(d: Date): Calendar = {
    val cal = Calendar.getInstance()
    cal.setTime(d)
    cal
  }

  val formats = new DefaultFormats {
    override def dateFormatter = internetDateFormatter
  }

  def setFromAny(in: Any): Box[Calendar] = toDate(in).flatMap(d => setBox(Full(dateToCal(d)))) or genericSetFromAny(in)

  def setFromString(s: String): Box[Calendar] = s match {
    case null | "" if optional_? => setBox(Empty)
    case null | "" => setBox(Failure(notOptionalErrorMessage))
    case other => setBox(tryo(dateToCal(parseInternetDate(s))))
  }

  private def elem =
    S.fmapFunc(SFuncHolder(this.setFromAny(_))) { funcName =>
      <input type={ formInputType } name={ funcName } value={ valueBox.map(s => toInternetDate(s.getTime)) openOr "" } tabindex={ tabIndex.toString }/>
    }

  def toForm: Box[NodeSeq] =
    uniqueFieldId match {
      case Full(id) => Full(elem % ("id" -> id))
      case _ => Full(elem)
    }

  def asJs = valueBox.map(v => Str(formats.dateFormat.format(v.getTime))) openOr JsNull

  def asJValue: JValue = asJString(v => formats.dateFormat.format(v.getTime))
  def setFromJValue(jvalue: JValue) = setFromJString(jvalue) {
    v =>
      formats.dateFormat.parse(v).map(d => {
        val cal = Calendar.getInstance
        cal.setTime(d)
        cal
      })
  }
}

class DateTimeField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[Calendar, OwnerType] with MandatoryTypedField[Calendar] with DateTimeTypedField {

  def owner = rec

  def this(rec: OwnerType, value: Calendar) = {
    this(rec)
    setBox(Full(value))
  }

  def defaultValue = Calendar.getInstance
}

class OptionalDateTimeField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[Calendar, OwnerType] with OptionalTypedField[Calendar] with DateTimeTypedField {

  def owner = rec

  def this(rec: OwnerType, value: Box[Calendar]) = {
    this(rec)
    setBox(value)
  }
}
