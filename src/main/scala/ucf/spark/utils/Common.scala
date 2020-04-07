package ucf.spark.utils

trait Common {
  val logger = new Logging(this.getClass.getName)
}
