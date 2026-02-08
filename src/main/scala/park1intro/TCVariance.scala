package park1intro

object TCVariance {

  import cats.Eq
  import cats.instances.int._ // Eq[Int] in scope
  import cats.instances.option._ // Eq[Option[Int]] in scope
  import cats.syntax.eq._

  val aComparison = Option(2) === Option(3)
//  val anInvalidComparison = Some(2) === None // Eq[Some[Int]] not found

  // variance
  class Animal
  class Cat extends Animal

  // covariant type: subtyping is propagated to the generic type
  class Cage[+T]
  val cage: Cage[Animal] =
    new Cage[Cat] // Cat <: Animal, so Cage[Cat] <: Cage[Animal]

  // contravariant type: subtyping is propagated BACKWARDS to the generic type
  class Vet[-T]
  val vet: Vet[Cat] = new Vet[
    Animal
  ] // Cat <: Animal, so Vet[Animal] <: Vet[Cat] - counterintuitive as hell
  // I want a Vet[Cat], but I get a Vet[Animal], because they can work on ANY Animal, not just cats

  // Rule of Thumb:
  // "HAS a T" or "CONTAINS a T" = covariant
  // "ACTS on T" = contravariant
  // variance affect how type class instances are being fetched

  // contravariant type class
  trait SoundMaker[-T]
  implicit object AnimalSoundMaker extends SoundMaker[Animal]
  def makeSound[T](implicit soundMaker: SoundMaker[T]): Unit = println(
    "SOUND!"
  ) // implementation not important
  makeSound[Animal] // ok - type class defined above
  makeSound[
    Cat
  ] // ok - type class instance for Animal is also applicable to Cats
  // rule 1: contravariant type classes can use the superclass instances if nothing is available strictly for the type

  // has implications for subtypes
  implicit object OptionSoundMaker extends SoundMaker[Option[Int]]
  makeSound[Option[Int]]
  makeSound[Some[Int]]

  // covarient type class
  trait AnimalShow[+T] {
    def show: String
  }

  implicit object GeneralAnimalShow extends AnimalShow[Animal] {
    override def show: String = "Animals everywhere!"
  }

  implicit object CatsShow extends AnimalShow[Cat] {
    override def show: String = "Meow!"
  }

  def organizeShow[T](implicit event: AnimalShow[T]): String = event.show
  // rule 2: covariant type classes will always use the more specific type class instance for that type
  // but may confuse the compiler if the general type class is also present

  // rule 3: you can't have both benefits
  // Cats uses INVARIANT type classes
  Option(2) === Option.empty[Int]

  def main(args: Array[String]): Unit = {
    println(
      organizeShow[Cat]
    ) // ok - the compiler will inject CatsShow as implicit

//    println(organizeShow[Animal]) // will not compile, ambiguous, use GeneralAnimalShow or CatsShow?

  }

}
