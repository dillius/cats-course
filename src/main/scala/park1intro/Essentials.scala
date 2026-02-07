package park1intro

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Essentials {

  // Scala 2.13 changed the behavior of ExecutionContext.global, be aware before every using it again
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val aFuture = Future { 42 }


  aFuture.onComplete {
    case Success(value) => println(value)
    case Failure(exception) => println(exception)
  }

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 43
    case 8 => 56
    case 100 => 999
  }

  trait HigherKindedType[F[_]]
  trait SequenceChecker[F[_]] {
    def isSequential: Boolean
  }

  val listChecker = new SequenceChecker[List] {
    override def isSequential: Boolean = true
  }

  def main(args: Array[String]): Unit = {

  }

}
