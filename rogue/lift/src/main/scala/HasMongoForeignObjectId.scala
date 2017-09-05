// Copyright 2012 Foursquare Labs Inc. All Rights Reserved.

package io.fsq.rogue.lift

import tech.scoundrel.mongodb.record.MongoRecord

trait HasMongoForeignObjectId[RefType <: MongoRecord[RefType] with ObjectIdKey[RefType]]
