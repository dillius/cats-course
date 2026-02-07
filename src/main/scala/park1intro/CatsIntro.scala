package park1intro

object CatsIntro {

  // Eq
//  val aComparison = 2 == "a string" // wrong, will trigger a compiler warning, will always be false

  // part 1 - type class import
  import cats.Eq

  // part 2 - import type class instances for the types you need
  import cats.instances.int._

  // part 3 - use the type class API
  val intEquality = Eq[Int]
  val aTypeSafeComparison = intEquality.eqv(2, 3) // false
//  val anUnsafeComparison = intEquality.eqv(2, "a string") // doesn't compile

  // part 4 - use extension methods (if applicable)
  import cats.syntax.eq._
  val anotherTypeSafeComp = 2 === 3
  val neqComparison = 2 =!= 3
//  val invalidComparison = 2 === "a String" // doesn't compile
  // extension methods are only visible in the presence of the right type class instance

  // part 5 - extending the type class operations to composite types, e.g. lists
  import cats.instances.list._ // we bring Eq[List[Int]] into scope
  val aListComparison = List(2) === List(3)

  // part 6 - Create a type class instance for a custom type
  case class ToyCar(model: String, price: Double) //Never actually use Double for money, use BigDecimal
  implicit val toyCarEq: Eq[ToyCar] = Eq.instance[ToyCar] { (car1, car2) =>
    car1.price == car2.price
  }

  val compareTwoToyCars = ToyCar("Ferarri", 29.99) === ToyCar("Lamborghini", 29.99) // true

}
