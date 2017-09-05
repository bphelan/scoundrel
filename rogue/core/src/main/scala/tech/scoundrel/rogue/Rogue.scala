// Copyright 2011 Foursquare Labs Inc. All Rights Reserved.

package tech.scoundrel.rogue

import java.util.Date

import com.mongodb.DBObject
import org.bson.types.ObjectId
import org.joda.time.DateTime
import tech.scoundrel.field._

/**
 * A utility trait containing typing shorthands, and a collection of implicit conversions that make query
 * syntax much simpler.
 *
 * @see AbstractQuery for an example of the use of implicit conversions.
 */
trait Rogue {

  // QueryField implicits
  implicit def rbooleanFieldtoQueryField[M](f: Field[Boolean, M]): QueryField[Boolean, M] = new QueryField(f)
  implicit def rcharFieldtoQueryField[M](f: Field[Char, M]): QueryField[Char, M] = new QueryField(f)

  implicit def rbyteFieldtoNumericQueryField[M](f: Field[Byte, M]): NumericQueryField[Byte, M] = new NumericQueryField(f)
  implicit def rshortFieldtoNumericQueryField[M](f: Field[Short, M]): NumericQueryField[Short, M] = new NumericQueryField(f)
  implicit def rintFieldtoNumericQueryField[M](f: Field[Int, M]): NumericQueryField[Int, M] = new NumericQueryField(f)
  implicit def rlongFieldtoNumericQueryField[F <: Long, M](f: Field[F, M]): NumericQueryField[F, M] = new NumericQueryField(f)
  implicit def rjlongFieldtoNumericQueryField[F <: java.lang.Long, M](f: Field[F, M]): NumericQueryField[F, M] = new NumericQueryField(f)
  implicit def rfloatFieldtoNumericQueryField[M](f: Field[Float, M]): NumericQueryField[Float, M] = new NumericQueryField(f)
  implicit def rdoubleFieldtoNumericQueryField[M](f: Field[Double, M]): NumericQueryField[Double, M] = new NumericQueryField(f)

  implicit def rstringFieldToStringQueryField[F <: String, M](f: Field[F, M]): StringQueryField[F, M] = new StringQueryField(f)
  implicit def robjectIdFieldToObjectIdQueryField[F <: ObjectId, M](f: Field[F, M]): ObjectIdQueryField[F, M] = new ObjectIdQueryField[F, M](f)
  implicit def rdateFieldToDateQueryField[M](f: Field[Date, M]): DateQueryField[M] = new DateQueryField(f)
  implicit def rdatetimeFieldToDateQueryField[M](f: Field[DateTime, M]): DateTimeQueryField[M] = new DateTimeQueryField(f)
  implicit def rdbobjectFieldToQueryField[M](f: Field[DBObject, M]): QueryField[DBObject, M] = new QueryField(f)

  implicit def renumNameFieldToEnumNameQueryField[M, F <: Enumeration#Value](f: Field[F, M]): EnumNameQueryField[M, F] = new EnumNameQueryField(f)
  implicit def renumerationListFieldToEnumerationListQueryField[M, F <: Enumeration#Value](f: Field[List[F], M]): EnumerationListQueryField[F, M] = new EnumerationListQueryField[F, M](f)
  implicit def rlatLongFieldToGeoQueryField[M](f: Field[LatLong, M]): GeoQueryField[M] = new GeoQueryField(f)
  implicit def rStringsListFieldToStringsListQueryField[M](f: Field[List[String], M]): StringsListQueryField[M] = new StringsListQueryField[M](f)
  implicit def rlistFieldToListQueryField[M, F: BSONType](f: Field[List[F], M]): ListQueryField[F, M] = new ListQueryField[F, M](f)
  implicit def rseqFieldToSeqQueryField[M, F: BSONType](f: Field[Seq[F], M]): SeqQueryField[F, M] = new SeqQueryField[F, M](f)
  implicit def rmapFieldToMapQueryField[M, F](f: Field[Map[String, F], M]): MapQueryField[F, M] = new MapQueryField[F, M](f)

  /**
   * ModifyField implicits
   *
   * These are dangerous in the general case, unless the field type can be safely serialized
   * or the field class handles necessary serialization. We specialize some safe cases.
   */
  implicit def rfieldToSafeModifyField[M, F](f: Field[F, M]): SafeModifyField[F, M] = new SafeModifyField(f)
  implicit def booleanRFieldToModifyField[M](f: Field[Boolean, M]): ModifyField[Boolean, M] = new ModifyField(f)
  implicit def charRFieldToModifyField[M](f: Field[Char, M]): ModifyField[Char, M] = new ModifyField(f)

  implicit def byteRFieldToModifyField[M](f: Field[Byte, M]): NumericModifyField[Byte, M] = new NumericModifyField(f)
  implicit def shortRFieldToModifyField[M](f: Field[Short, M]): NumericModifyField[Short, M] = new NumericModifyField(f)
  implicit def intRFieldToModifyField[M](f: Field[Int, M]): NumericModifyField[Int, M] = new NumericModifyField(f)
  implicit def longRFieldToModifyField[M, F <: Long](f: Field[F, M]): NumericModifyField[F, M] = new NumericModifyField(f)
  implicit def floatRFieldToModifyField[M](f: Field[Float, M]): NumericModifyField[Float, M] = new NumericModifyField(f)
  implicit def doubleRFieldToModifyField[M](f: Field[Double, M]): NumericModifyField[Double, M] = new NumericModifyField(f)

  implicit def stringRFieldToModifyField[M, F <: String](f: Field[F, M]): ModifyField[F, M] = new ModifyField(f)
  implicit def objectidRFieldToModifyField[M, F <: ObjectId](f: Field[F, M]): ModifyField[F, M] = new ModifyField(f)
  implicit def dateRFieldToDateModifyField[M](f: Field[Date, M]): DateModifyField[M] = new DateModifyField(f)
  implicit def datetimeRFieldToDateModifyField[M](f: Field[DateTime, M]): DateTimeModifyField[M] = new DateTimeModifyField(f)

  implicit def renumerationFieldToEnumerationModifyField[M, F <: Enumeration#Value](f: Field[F, M]): EnumerationModifyField[M, F] =
    new EnumerationModifyField(f)

  implicit def renumerationListFieldToEnumerationListModifyField[M, F <: Enumeration#Value](f: Field[List[F], M]): EnumerationListModifyField[F, M] =
    new EnumerationListModifyField[F, M](f)

  implicit def rlatLongFieldToGeoQueryModifyField[M](f: Field[LatLong, M]): GeoModifyField[M] =
    new GeoModifyField(f)

  implicit def rlistFieldToListModifyField[M, F: BSONType](f: Field[List[F], M]): ListModifyField[F, M] =
    new ListModifyField[F, M](f)

  implicit def rSeqFieldToSeqModifyField[M, F: BSONType](f: Field[Seq[F], M]): SeqModifyField[F, M] =
    new SeqModifyField[F, M](f)

  implicit def rArrayFieldToArrayModifyField[M, F: BSONType](f: Field[Array[F], M]): ArrayModifyField[F, M] =
    new ArrayModifyField[F, M](f)

  implicit def rmapFieldToMapModifyField[M, F](f: Field[Map[String, F], M]): MapModifyField[F, M] =
    new MapModifyField[F, M](f)

  // SelectField implicits
  implicit def roptionalFieldToSelectField[M, V](
    f: OptionalField[V, M]
  ): SelectField[Option[V], M] = new OptionalSelectField(f)

  implicit def rrequiredFieldToSelectField[M, V](
    f: RequiredField[V, M]
  ): SelectField[V, M] = new MandatorySelectField(f)

  class Flattened[A, B]
  implicit def anyValIsFlattened[A <: AnyVal]: Flattened[A, A] = new Flattened[A, A]
  implicit def enumIsFlattened[A <: Enumeration#Value]: Flattened[A, A] = new Flattened[A, A]
  implicit val stringIsFlattened = new Flattened[String, String]
  implicit val objectIdIsFlattened = new Flattened[ObjectId, ObjectId]
  implicit val dateIsFlattened = new Flattened[java.util.Date, java.util.Date]
  implicit def recursiveFlattenList[A, B](implicit ev: Flattened[A, B]) = new Flattened[List[A], B]
  implicit def recursiveFlattenSeq[A, B](implicit ev: Flattened[A, B]) = new Flattened[Seq[A], B]
  implicit def recursiveFlattenArray[A, B](implicit ev: Flattened[A, B]) = new Flattened[Array[A], B]

}

object Rogue extends Rogue
