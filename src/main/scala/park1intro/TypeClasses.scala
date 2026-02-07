package park1intro

object TypeClasses {

  case class Person(name: String, age: Int)

  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  implicit object StrngSerializer extends JSONSerializer[String] {
    override def toJson(value: String): String = s""""$value""""
  }

  implicit object IntSerializer extends JSONSerializer[Int] {
    override def toJson(value: Int): String = value.toString
  }

  implicit object PersonSerializer extends JSONSerializer[Person] {
    override def toJson(value: Person): String = {
      s"""
         |{ "name" : ${value.name}, "age" : ${value.age} }
         |""".stripMargin.trim
    }
  }

  def convertListToJSON[T](list: List[T])(implicit
      serializer: JSONSerializer[T]
  ): String =
    list.map(serializer.toJson).mkString("[", ",", "]")

  object JSONSyntax {
    implicit class JSONSerializable[T](value: T)(implicit
        serializer: JSONSerializer[T]
    ) {
      def toJson: String = serializer.toJson(value)
    }
  }

  def main(args: Array[String]): Unit = {
    println(convertListToJSON(List(Person("Alice", 20), Person("Bob", 30))))
    import JSONSyntax._
    println(Person("John", 33).toJson)
  }
}
