package tech.scoundrel
package record
package field

import java.util.Locale
import scala.xml.NodeSeq

import net.liftweb.common.{ Box, Full }
import net.liftweb.http.SHtml
import net.liftweb.util.Helpers._

object LocaleField {
  lazy val localeList = Locale
    .getAvailableLocales.toList
    .sortWith(_.getDisplayName < _.getDisplayName)
    .map(lo => (lo.toString, lo.getDisplayName))
}

trait LocaleTypedField extends TypedField[String] {
  /** Build a list of string pairs for a select list. */
  def buildDisplayList: List[(String, String)]

  private def elem = SHtml.select(buildDisplayList, Full(valueBox.map(_.toString) openOr ""),
    locale => setBox(Full(locale))) % ("tabindex" -> tabIndex.toString)

  override def toForm: Box[NodeSeq] =
    uniqueFieldId match {
      case Full(id) => Full(elem % ("id" -> id))
      case _ => Full(elem)
    }
}

class LocaleField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends StringField(rec, 16) with LocaleTypedField {

  override def defaultValue = Locale.getDefault.toString

  def isAsLocale: Locale = Locale.getAvailableLocales.filter(_.toString == value).toList match {
    case Nil => Locale.getDefault
    case x :: xs => x
  }

  def buildDisplayList: List[(String, String)] = LocaleField.localeList

}

class OptionalLocaleField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends OptionalStringField(rec, 16) with LocaleTypedField {

  /** Label for the selection item representing Empty, show when this field is optional. Defaults to the empty string. */
  def emptyOptionLabel: String = ""

  def buildDisplayList: List[(String, String)] = ("", emptyOptionLabel) :: LocaleField.localeList
}
