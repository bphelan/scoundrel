package tech.scoundrel.rogue.cc

trait BsonExecutors[MB] {
  def async: AsyncBsonQueryExecutor[MB]
  def sync: BsonQueryExecutor[MB]
}

object CcBsonExecutors extends BsonExecutors[CcMeta[_]] {
  override val async = CcAsyncQueryExecutor
  override val sync = CcQueryExecutor
}