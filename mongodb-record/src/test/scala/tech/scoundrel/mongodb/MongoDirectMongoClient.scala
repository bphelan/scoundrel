/*
 * Copyright 2014-2017 WorldWide Conferencing, LLC
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

/**
 * System under specification for MongoDirectMonoClient.
 */
class MongoDirectMongoClientSpec extends Specification with MongoTestKit {
  "MongoDirectMongoClient Specification".title

  "MongoClient example" in {

    checkMongoIsRunning

    // use a Mongo instance directly
    MongoDB.use(db => {
      val coll = db.getCollection("testCollection")

      // create a unique index on name
      coll.createIndex(new BasicDBObject("name", 1), new BasicDBObject("unique", true))

      // build the DBObjects
      val doc = new BasicDBObject
      val doc2 = new BasicDBObject
      val doc3 = new BasicDBObject

      doc.put("name", "MongoSession")
      doc.put("type", "db")
      doc.put("count", 1: java.lang.Integer)

      doc2.put("name", "MongoSession")
      doc2.put("type", "db")
      doc2.put("count", 1: java.lang.Integer)

      doc3.put("name", "MongoDB")
      doc3.put("type", "db")
      doc3.put("count", 1: java.lang.Integer)

      // save the docs to the db
      coll.save(doc)
      coll.save(doc2) must throwA[MongoException]
      coll.save(doc3)
    })
    success
  }
}

