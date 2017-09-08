package tech.scoundrel
package record
package field

import java.util.TimeZone
import scala.xml.NodeSeq

import net.liftweb.common.{ Box, Full }
import net.liftweb.http.SHtml
import net.liftweb.util.Helpers._

object TimeZoneField {
  lazy val timeZoneList: List[(String, String)] = TimeZone.getAvailableIDs.toList.
    filter(!_.startsWith("SystemV/")).
    filter(!_.startsWith("Etc/")).filter(_.length > 3).
    sortWith(_ < _).map(tz => (tz, tz))
}

trait TimeZoneTypedField extends StringTypedField {
  /** Label for the selection item representing Empty, show when this field is optional. Defaults to the empty string. */
  def emptyOptionLabel: String = ""

  def buildDisplayList: List[(String, String)] =
    if (optional_?) ("", emptyOptionLabel) :: TimeZoneField.timeZoneList else TimeZoneField.timeZoneList

  private def elem = SHtml.select(buildDisplayList, Full(valueBox openOr ""),
    timezone => setBox(Full(timezone))) % ("tabindex" -> tabIndex.toString)

  override def toForm: Box[NodeSeq] =
    uniqueFieldId match {
      case Full(id) => Full(elem % ("id" -> id))
      case _ => Full(elem)
    }
}

class TimeZoneField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends StringField(rec, 32) with TimeZoneTypedField {

  override def defaultValue = TimeZone.getDefault.getID

  def isAsTimeZone: TimeZone = TimeZone.getTimeZone(value) match {
    case null => TimeZone.getDefault
    case x => x
  }
}

class OptionalTimeZoneField[OwnerType <: Record[OwnerType]](rec: OwnerType)
  extends OptionalStringField(rec, 32) with TimeZoneTypedField
