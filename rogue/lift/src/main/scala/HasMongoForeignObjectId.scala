// Copyright 2012 Foursquare Labs Inc. All Rights Reserved.

package tech.scoundrel.rogue.lift

import tech.scoundrel.mongodb.record.MongoRecord

trait HasMongoForeignObjectId[RefType <: MongoRecord[RefType] with ObjectIdKey[RefType]]
