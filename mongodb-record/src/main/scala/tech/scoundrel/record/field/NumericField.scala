/*
 * Copyright 2007-2011 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.scoundrel
package record
package field

import scala.reflect.Manifest
import scala.xml.NodeSeq

import net.liftweb.common.{ Box, Full }
import net.liftweb.http.S
import net.liftweb.http.js.JE.{ JsNull, JsRaw }
import net.liftweb.util.Helpers._

trait NumericTypedField[ThisType] extends TypedField[ThisType] {

  /** Augments genericSetFromAny with support for values of type Number (optionally wrapped in any of the usual suspects) */
  protected final def setNumericFromAny(in: Any, f: Number => ThisType)(implicit m: Manifest[ThisType]): Box[ThisType] =
    in match {
      case (n: Number) => setBox(Full(f(n)))
      case Some(n: Number) => setBox(Full(f(n)))
      case Full(n: Number) => setBox(Full(f(n)))
      case (n: Number) :: _ => setBox(Full(f(n)))
      case _ => genericSetFromAny(in)
    }

  private def elem = S.fmapFunc((s: List[String]) => setFromAny(s)) {
    funcName => <input type={ formInputType } name={ funcName } value={ valueBox.map(_.toString) openOr "" } tabindex={ tabIndex.toString }/>
  }

  /**
   * Returns form input of this field
   */
  def toForm: Box[NodeSeq] =
    uniqueFieldId match {
      case Full(id) => Full(elem % ("id" -> id))
      case _ => Full(elem)
    }

  override def noValueErrorMessage = S.?("number.required")

  def asJs = valueBox.map(v => JsRaw(String.valueOf(v))) openOr JsNull

}

