package part2abstractMath

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Monads {

  // lists
  val numbersList = List(1, 2, 3)
  var charsList = List('a', 'b', 'c')

  // TODO 1.1 - How would you create all combinations of (number, char)
  val combos1 =
    numbersList.flatMap(number => charsList.map(char => (number, char)))
  val combos2 = for {
    number <- numbersList
    char <- charsList
  } yield (number, char)

  // options
  val numbersOption = Option(2)
  val charOption = Option('d')

  // TODO 1.2 - How would you create all combinations of (number, char)
  val combosOption1 =
    numbersOption.flatMap(number => charOption.map(char => (number, char)))
  val combosOption2 = for {
    number <- numbersOption
    char <- charOption
  } yield (number, char)

  // futures
  implicit val ec: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val numbersFuture = Future { 42 }
  val charFuture = Future { 'Z' }

  // TODO 1.3 - How would you create all combinations of (number, char)
  val combosFuture1 =
    numbersFuture.flatMap(number => charFuture.map(char => (number, char)))
  val combosFuture2 = for {
    number <- numbersFuture
    char <- charFuture
  } yield (number, char)

  /*
    Pattern
    - wrapping a value into an M value
    - flatMap mechanism
   */
  trait MyMonad[M[_]] {
    def pure[A](value: A): M[A]
    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]
  }

  // Cats Monad
  import cats.Monad

  import cats.instances.option._
  val optionMonad = Monad[Option]
  val anOption = optionMonad.pure(4)
  val aTransformedOption =
    optionMonad.flatMap(anOption)(x => if (x % 3 == 0) Some(x + 1) else None)

  import cats.instances.list._
  val listMonad = Monad[List]
  val aList = listMonad.pure(3)
  val aTransformedList =
    listMonad.flatMap(aList)(x => List(x, x + 1))

  // TODO 2: use a Monad[Future]
  import cats.instances.future._
  val futureMonad = Monad[Future] // requires an implicit execution context
  val aFuture = futureMonad.pure(42)
  val aTransformedFuture =
    futureMonad.flatMap(aFuture)(x => Future { x + 1 })

  // Specialized API
  def getPairsList(numbers: List[Int], chars: List[Char]): List[(Int, Char)] =
    numbers.flatMap(number => chars.map(char => (number, char)))

  def getPairsOption(
      numbers: Option[Int],
      chars: Option[Char]
  ): Option[(Int, Char)] =
    numbers.flatMap(number => chars.map(char => (number, char)))

  def getPairsFuture(
      numbers: Future[Int],
      chars: Future[Char]
  ): Future[(Int, Char)] =
    numbers.flatMap(number => chars.map(char => (number, char)))

  // generalize
  def getPairs[M[_], A, B](ma: M[A], mb: M[B])(implicit
      monad: Monad[M]
  ): M[(A, B)] =
    monad.flatMap(ma)(a => monad.map(mb)(b => (a, b)))

  def main(args: Array[String]): Unit = {
    println(getPairs(numbersList, charsList))
    println(getPairs(numbersOption, charOption))
    getPairs(numbersFuture, charFuture).foreach(println)
  }

}
