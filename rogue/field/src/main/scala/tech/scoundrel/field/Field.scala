package tech.scoundrel.field

trait Field[V, R] {
  def name: String
  def owner: R
}
