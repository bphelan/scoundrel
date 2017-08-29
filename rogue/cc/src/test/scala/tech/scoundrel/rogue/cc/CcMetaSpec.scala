package tech.scoundrel.rogue.cc

import org.bson.types.ObjectId
import org.scalatest.{ FlatSpec, MustMatchers }
import shapeless.tag._

case class IdOneEnum(_id: ObjectId, one: String, en: VenueStatus.Value)

class CcMetaSpec extends FlatSpec with MustMatchers {

  "Meta R" should "work as expected" in {
    //reguired implicits for implicit call of BsonFormat[IdOneEnum] inside constructor of RCcMeta[IdOneEnum]
    implicit val ev = VenueStatus
    import tech.scoundrel.rogue.BsonFormats._
    import tech.scoundrel.rogue.EnumNameFormats._
    object IdOneEnumR extends RCcMeta[IdOneEnum]("idoneenum")
    val elem = IdOneEnum(new ObjectId(), "One", VenueStatus.closed)
    val bson = IdOneEnumR.write(elem)
    IdOneEnumR.read(bson) mustBe elem

    //println(classOf[IdOneEnumR.R])
    classOf[IdOneEnumR.R] mustBe classOf[IdOneEnum]
  }

  it should "find implicit format fo tagged ObjectId" in {

    import tech.scoundrel.rogue.BsonFormats._

    trait Tag

    type TaggedObjectId = ObjectId @@ Tag

    case class A(
      id: TaggedObjectId,
      name: String @@ Tag,
      age: Long @@ Tag
    )

    val _ = new RCcMeta[A]

    succeed
  }

}
