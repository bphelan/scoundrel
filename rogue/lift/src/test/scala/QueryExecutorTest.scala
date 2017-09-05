// Copyright 2011 Foursquare Labs Inc. All Rights Reserved.

package tech.scoundrel.rogue.lift.test

import tech.scoundrel.rogue.InitialState
import tech.scoundrel.rogue.MongoHelpers.AndCondition
import tech.scoundrel.rogue.lift.{ LiftAdapter, ObjectIdKey }
import tech.scoundrel.mongodb.record.{ MongoMetaRecord, MongoRecord }
import org.junit.{ Ignore, Test }
import org.specs2.matcher.JUnitMustMatchers

import tech.scoundrel.rogue.{ Query, RogueException }

class LegacyQueryExecutorTest extends JUnitMustMatchers {

  class Dummy extends MongoRecord[Dummy] with ObjectIdKey[Dummy] {
    def meta = Dummy
  }

  object Dummy extends Dummy with MongoMetaRecord[Dummy] {
  }

  @Test @Ignore("TODO(mateo): The public version of lift 2.6.2 causes an infinite recursion. FIXME") // Test ignored because it is broken when using OSS version of lift.
  def testExceptionInRunCommandIsDecorated {
    val query = Query[Dummy.type, Dummy, InitialState](
      Dummy, "Dummy", None, None, None, None, None, AndCondition(Nil, None, None), None, None, None
    )
    (LiftAdapter.runCommand(() => "hello", query) {
      throw new RuntimeException("bang")
      "hi"
    }) must throwA[RogueException]
  }

}
