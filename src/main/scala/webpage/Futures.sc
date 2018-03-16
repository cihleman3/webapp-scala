object Test {
  import scala.concurrent._
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global

  val f1 = Future(Thread.sleep(1000))
  val f2 = Future(Thread.sleep(2000))
  val all = Future.sequence(Seq(f1, f2))
  all onComplete (_ => println("All done"))

  f1.foreach(_ => println("Future 1 done"))
  f2.foreach(_ => println("Future 2 done"))

  // Wait for the futures to complete before shutting down
  Await.result(all, 5.seconds)
}