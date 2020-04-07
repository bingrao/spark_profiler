package ucf.spark.rdd

import ucf.spark.utils.Common

object SparkInterceptor extends Common{
  def beforeInvoke() = {
    logger.info("################## Before RDD compute: " + System.currentTimeMillis())
  }
  def afterInvoke() = {
    logger.info("################## After RDD compute: " + System.currentTimeMillis())
  }
}
