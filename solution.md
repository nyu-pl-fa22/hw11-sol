## Part 1.2:

The following example shows that making TreeMap contravariant would
not be type-safe:

```scala
val m: TreeMap[AnyRef] = TreeMap.empty[AnyRef].updated(1, new AnyRef)
val m1: TreeMap[String] = m // OK because String <: AnyRef and TreeMap is contravariant
val s = m1.get(1).get // Note that static type of s is String but dynamic type would be AnyRef
s.length // Would fail because AnyRef has no length field
```
