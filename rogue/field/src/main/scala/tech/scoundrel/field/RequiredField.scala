package tech.scoundrel.field

trait RequiredField[V, R] extends Field[V, R] {
  def defaultValue: V
}
