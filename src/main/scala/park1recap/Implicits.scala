package park1recap

object Implicits {

  case class Person(name: String) {
    def greet: String = s"Hi, my name is $name!"
  }

  implicit class ImpersonableString(name: String) {
    def greet: String = Person(name).greet
  }

//  val impoersonableString = new ImpersonableString("Peter")
//  impoersonableString.greet

  val greeting = "Peter".greet

  import scala.concurrent.duration._
  val oneSec = 1.second

  def increment(x: Int)(implicit amount: Int) = x + amount

  implicit val defaultAmount = 10
  val incremented2 = increment(2)

  trait JSONSerializer[T] {
    def toJson(value:T): String
  }

  def listToJson[T](list: List[T])(implicit serializer: JSONSerializer[T]): String =
    list.map(serializer.toJson).mkString("[", ",", "]")

  implicit val personSerializer: JSONSerializer[Person] = new JSONSerializer[Person] {
    override def toJson(person: Person) =
      s"""
         |{ "name" : "${person.name}" }
         |""".stripMargin
  }

  val personsJson = listToJson(List(Person("Alice"), Person("Bob")))

  implicit def oneArgCaseClassSerializer[T <: Product]: JSONSerializer[T] = new JSONSerializer[T] {
    override def toJson(value: T): String =
      s"""
         |{ "${value.productElementName(0)}" : "${value.productElement(0)}" }
         |""".stripMargin.trim
  }

  case class Cat(name: String)

  val catsToJson = listToJson(List(Cat("Furry"), Cat("Fluffy")))

  //implicit methods are used to PROVE THE EXISTENCE of a type

  def main(args: Array[String]): Unit = {
    println(oneArgCaseClassSerializer[Cat].toJson(Cat("Furry")))
    println(oneArgCaseClassSerializer[Person].toJson(Person("Bill")))
    println(catsToJson)
  }

}
