# Homework 11 (20 Points)

The deadline for Homework 11 is Wednesday, Dec 14 at 10pm. The late
submission deadline is Sunday, Dec 18, 10pm.

The purpose of this homework assignment is to practice the use of generics.

## Getting the code template

Before you perform the next steps, you first need to create your own
private copy of this git repository. To do so, click on the link
provided in the announcement of this homework assignment on
Brightspace. After clicking on the link, you will receive an email from
GitHub, when your copy of the repository is ready. It will be
available at
`https://github.com/nyu-pl-fa22/hw11-<YOUR-GITHUB-USERNAME>`.  
Note that this may take a few minutes.

* Open a browser at `https://github.com/nyu-pl-fa22/hw11-<YOUR-GITHUB-USERNAME>` with your Github username inserted at the appropriate place in the URL.
* Choose a place on your computer for your homework assignments to reside and open a terminal to that location.
* Execute the following git command: <br/>
  ```bash
  git clone https://github.com/nyu-pl-fa22/hw11-<YOUR-GITHUB-USERNAME>.git
  cd hw11
  ```

The code template is provided in the file

```
src/main/scala/pl/hw11/hw11.scala
```

relative to the root directory of the repository. Follow the
[Scala setup](https://github.com/nyu-pl-fa22/scala-in-class-code) instructions to import the project into
InteliJ (or use your other favorite IDE or editor to work on the assignment).

The file

```
src/test/scala/pl/hw11/hw11Spec.scala
```

contains unit tests that help you to ensure that the code that you
write is correct. You can run the unit tests directly from within
InteliJ: right-click on the file in the "Project" overview and choose
"Run 'hw11Spec'". After running the test suite, the IDE will show you
a summary indicating which tests failed (if any).

Alternatively, you can run the tests by executing the command `test`
in the sbt shell. You can do the latter either from within the IDE or
on the command line: open a terminal in the project directory and
execute `sbt`. When the prompt of the sbt shell appears type `test`.

Feel free to add additional test cases to the file `hw11Spec.scala`.


## Submitting your solution

Once you have completed the assignment, you can submit your solution
by pushing the modified code template to GitHub. This can be done by
opening a terminal in the project's root directory and executing the
following commands:

```bash
git add .
git commit -m "solution"
git push
```

You can replace "solution" by a more meaningful commit message.

Refresh your browser window pointing at
```
https://github.com/nyu-pl-fa22/hw11-<YOUR-GITHUB-USERNAME>/
```
and double-check that your solution has been uploaded correctly.

You can resubmit an updated solution anytime by reexecuting the above
git commands. Though, please remember the rules for submitting
solutions after the homework deadline has passed.


## Problem 1: Generic Functional Maps (20 Points)

The goal of this homework is to implement a generic functional map
data structure `TreeMap` that stores key/value pairs using strictly
sorted binary search trees. We want our data structure to be
consistent with the interface of functional maps in Scala's standard
API. This interface is described by the trait `Map[K,+A]` whose
documentation you can find
[here](https://www.scala-lang.org/api/current/scala/collection/Map.html). The
trait takes two type parameters: `K` stands for the type of the keys
stored in the map and `A` stands for the type of the associated
values. Note that this trait is covariant in the value type `A`. To
simplify matters a bit, we fix the type of keys to be `Int` in our
tree map implementation. That is, we will implement a class
`TreeMap[+A]` that extends `Map[Int,A]`.

The `Map` trait provides a large number of methods to interact with
map data structures including complex higher-order methods to iterate
over maps such as `foldLeft` and `foldRight`. Rather than implementing
all of these methods from scratch, we build upon a *skeleton
implementation* of a map data structure that is already provided by
the Scala standard library. This skeleton implementation is given by
the abstract class `AbstractMap[K,+A]` whose documentation you can
find
[here](https://www.scala-lang.org/api/current/scala/collection/immutable/AbstractMap.html).

`AbstractMap` implements all of the methods of `Map` in terms of four
basic methods that every concrete map data structure needs to provide:

* `def updated[B >: A](key: Int, value: B): TreeMap[B]`

  Returns the new map that is like `this` but
  binds key `key` to value `value`. In particular, if `this` already
  contains a binding of `key` to some other value `value1`, then in the
  returned map `key` is now bound to `value` instead.
  
* `def removed(key: Int): TreeMap[A]`: 

  Returns the new map that is like `this` but has the binding for key
  `k` removed. If `this` has no binding for `key`, the returned map has
  exactly the same bindings as `this`.

* `def get(k: Int): Option[A]`: 

   Returns `Some(value)` if `this` binds key `key` to value
  `value`. If `this` contains no binding for `key`, then `get` returns
  `None`.

* `def iterator: Iterator[(Int, A)]`

  Returns an *iterator* object that abstractly describes a traversal
  over all key/value pairs stored in the map represented by `this`.

Your task will be to implement the methods `updated` and `removed`. The
other two are already implemented for you.

We implement the binary search trees that we use for our `TreeMap`
data structure as an algebraic data type. In Scala, algebraic data
types are implemented using so-called *case classes*. Here, is how the
case class definition of our binary search tree looks like:

```scala
case class Leaf() extends TreeMap[Nothing]
case class Node[+A](key: Int, value: A, 
                    left: TreeMap[A], right: TreeMap[A]) extends TreeMap[A]
```

The instances of the case class `Leaf` are the leaf nodes of the
tree. That is, these objects describe empty maps. The instances of the
case class `Node` are the inner nodes of the tree. They store the left
and right subtrees as well as a key/value pair. The type `TreeMap` is
similar to the OCaml ADT `'a tree` that we used in [Class
6](https://github.com/nyu-pl-fa22/class06#polymorphic-adts) and
[Homework 6](https://github.com/nyu-pl-fa22/hw06). The main difference
is that `TreeMap` is covariant in its value type `A`.

Note that `Leaf` nodes extend `TreeMap[Nothing]`. Recall that the type
`Nothing` is the smallest type in Scala's type hierarchy. That is, it
is a subtype of all other types. This means that `Leaf` instances are
also instances of `TreeMap[A]` for any type `A`.

Similar to ADTs in OCaml, we can pattern match on instances of case
classes using Scala's match expressions, similar to match expressions
in OCaml. The code template contains some examples (see methods
`TreeMap.get` and `TreeMap.toList`.

The actual implementations of the methods of the map data structure
are given in the abstract class `TreeMap` that looks like this:

```scala
  abstract class TreeMap[+A] extends AbstractMap[Int, A] {
  
    def updated[B >: A](key: Int, value: B): TreeMap[B] = ???

    def deleteMin(): ((Int, A), TreeMap[A]) = ???

    def removed(key: Int): TreeMap[A] = ???
    
    ...
  }

```

1. Complete the implementation of the three methods `updated`,
   `deleteMin`, and `removed`. The specification of method `deleteMin`
   is as follows: if the current instance `this` is not empty, then
   `deleteMind` should remove the binding `(key, value)` from `this`
   such that `key` is the smallest key among all the bindings in
   `this`. The method should return this pair as well as the new
   `TreeMap` obtained from `this` after removing the pair. If `this`
   is empty, then `deleteMin` should fail with an exception (this is
   already implemented for you using an appropriate `require`
   clause). The method `deleteMin` will come in handy in your
   implementation of `removed`.
   
   Make sure that your implementation maintains the invariant that the
   trees are strictly sorted according to the stored keys. **(15 Points)**

1. Provide a short code snippet that demonstrates why `TreeMap` cannot
   be contravariant in its type parameter `A`. That is, your code
   snippet should be well typed under the assumption that `TreeMap` is
   contravariant in `A` but executing it would lead to an error
   (e.g. because the code would try to call some method on an instance
   of a class that does not have that method). **(5 Points)**
