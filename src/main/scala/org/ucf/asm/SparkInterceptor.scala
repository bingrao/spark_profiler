package org.ucf.asm

object SparkInterceptor {
  def beforeInvoke() = {
    println("##################Before RDD compute: " + System.currentTimeMillis())
  }
  def afterInvoke() = {
    println("##################After RDD compute: " + System.currentTimeMillis())
  }
}
