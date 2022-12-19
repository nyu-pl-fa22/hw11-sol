package hw11

import scala.collection.immutable.AbstractMap
import scala.language.implicitConversions

object hw11:
  sealed abstract class TreeMap[+A] extends AbstractMap[Int, A]:
    def updated[B >: A](key: Int, value: B): TreeMap[B] =
      this match
        case Leaf() => Node(key, value, Leaf(), Leaf())
        case Node (key1, value1, left, right) =>
          if key == key1 then
            Node (key1, value, left, right)
          else if key < key1 then
            Node (key1, value1, left.updated(key, value), right)
          else
            Node (key1, value1, left, right.updated(key, value))
      end match
    

    def deleteMin(): ((Int, A), TreeMap[A]) =
      require(this.nonEmpty, "deleteMin on empty map")

      (this: @unchecked) match
        case Node(key, value, Leaf(), right) =>
          ((key, value), right)
        case Node(key, value, left, right) =>
          val (kv, newLeft) = left.deleteMin()
          (kv, Node(key, value, newLeft, right))
      end match
    
    def removed(key: Int): TreeMap[A] = this match
      case Leaf() => Leaf()
      case Node(key1, value1, left, right) =>
        if key == key1 then
          if right.isEmpty then left 
          else
            val ((minKey, minValue), newRight) = right.deleteMin()
            Node(minKey, minValue, left, newRight)
          else if key < key1 then
            Node (key1, value1, left.removed(key), right)
          else
            Node(key1, value1, left, right.removed(key))
  
    def get(key: Int): Option[A] = this match
      case Leaf() => None
      case Node(key1, value, left, right) =>
        if key == key1 then Some(value)
        else if key < key1 then left.get(key)
        else right.get(key)
    
    override def toList: List[(Int, A)] = 
      this match
      case Leaf() => Nil
      case Node(key, value, left, right) => 
        left.toList ::: (key, value) :: right.toList
  
    def iterator: Iterator[(Int, A)] = toList.iterator
  
  case class Leaf() extends TreeMap[Nothing]
  case class Node[+A](key: Int, value: A, 
                      left: TreeMap[A], right: TreeMap[A]) extends TreeMap[A]

  object TreeMap:
    def empty[A]: TreeMap[A] = Leaf()
    
    def apply[A](kvs: (Int, A)*): Map[Int, A] = 
      kvs.toSeq.foldLeft(empty[A]: Map[Int, A])(_ + _)
      
