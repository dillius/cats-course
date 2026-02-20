package part2abstractMath

object Monoids {

  import cats.Semigroup
  import cats.instances.int._
  import cats.syntax.semigroup._ // import the |+| extension method

  val numbers = (1 to 1000).toList
  // |+| is always associative
  val sumLeft = numbers.foldLeft(0)(_ |+| _)
  val sumRight = numbers.foldRight(0)(_ |+| _)

  // define a general API
//  def combineFold[T](list: List[T])(implicit semigroup: Semigroup[T]): T =
//    list.foldLeft(/* WHAT? */)(_ |+| _)

  // MONOIDS
  import cats.Monoid
  val intMonoid = Monoid[Int]
  val combineInt = intMonoid.combine(25, 999)
  val zero = intMonoid.empty // 0

  import cats.instances.string._
  val emptyString = Monoid[String].empty // ""
  val combineString = Monoid[String].combine("I understand ", "Monoids!")

  import cats.instances.option._
  val emptyOption = Monoid[Option[Int]].empty // None
  val combineOption =
    Monoid[Option[Int]].combine(Option(2), Option.empty[Int]) // Some(2)
  val combineOption2 =
    Monoid[Option[Int]].combine(Option(3), Option(6)) // Some(9)

  // extension methods for Monoids
//  import cats.syntax.monoid._ // But it's also already available from semigroup
  val combineOptionFancy = Option(3) |+| Option(7)

  // TODO 1 - implement combineFold
  def combineFold[T](list: List[T])(implicit monoid: Monoid[T]): T =
    list.foldLeft(monoid.empty)(_ |+| _)

  // TODO 2 - combine a list of phonebooks as Maps[String, Int]
  val phonebooks = List(
    Map("Alice" -> 34, "Bob" -> 56),
    Map("Daniel" -> 23, "Charlie" -> 45),
    Map("Tina" -> 897)
  )

  import cats.instances.map._
  val combinedPhonebooks = combineFold(phonebooks)

  // TODO 3 - shopping cart and online store with monoids
  case class ShoppingCart(items: List[String], total: Double)
  implicit val shoppingCartMonoid: Monoid[ShoppingCart] =
    Monoid.instance[ShoppingCart](
      ShoppingCart(List.empty[String], 0.0),
      (cart1, cart2) =>
        ShoppingCart(cart1.items ++ cart2.items, cart1.total + cart2.total)
    )
  def checkout(shoppingCarts: List[ShoppingCart]): ShoppingCart =
    combineFold(shoppingCarts)

  val shoppingCarts = List(ShoppingCart(List("Apple", "Banana"), 1.23), ShoppingCart(List("Orange", "Pear"), 2.34))

  def main(args: Array[String]): Unit = {
    println(sumLeft)
    println(sumRight)
    println(sumLeft == sumRight)
    println(combineOption)
    println(combineOption2)
    println(combineFold(numbers))
    println(combineFold(List("I ", "like ", "Monoids!")))
    println(combinedPhonebooks)
    println(checkout(shoppingCarts))
  }
}
