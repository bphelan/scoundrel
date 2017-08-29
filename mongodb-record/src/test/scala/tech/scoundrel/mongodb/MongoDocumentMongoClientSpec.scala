/*
 * Copyright 2010-2017 WorldWide Conferencing, LLC
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
package mongodb

import org.specs2.mutable.Specification

import com.mongodb._
import org.bson.types.ObjectId
import tech.scoundrel.mongodb.BsonDSL._

package mongoclienttestdocs {
  case class SessCollection(_id: ObjectId, name: String, dbtype: String, count: Int)
    extends MongoDocument[SessCollection] {

    def meta = SessCollection
  }

  object SessCollection extends MongoDocumentMeta[SessCollection] {
    override def formats = super.formats + new ObjectIdSerializer
    // create a unique index on name
    createIndex(("name" -> 1), true)
  }
}

/**
 * Systems under specification for MongoDocumentMongoClient.
 */
class MongoDocumentMongoClientSpec extends Specification with MongoTestKit {
  "MongoDocumentMongoClient Specification".title

  import mongoclienttestdocs._

  "MongoClient example" in {

    checkMongoIsRunning

    val tc = SessCollection(ObjectId.get, "MongoSession", "db", 1)
    val tc2 = SessCollection(ObjectId.get, "MongoSession", "db", 1)
    val tc3 = SessCollection(ObjectId.get, "MongoDB", "db", 1)

    // save to db
    SessCollection.save(tc)
    SessCollection.save(tc2) must throwA[MongoException]
    SessCollection.save(tc3)

    success
  }

}
