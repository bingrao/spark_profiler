package org.ucf.asm

object SparkInterceptor {
  def beforeInvoke() = {
    println("Before: " + System.currentTimeMillis())
  }
  def afterInvoke() = {
    println("After: " + System.currentTimeMillis())
  }
}
