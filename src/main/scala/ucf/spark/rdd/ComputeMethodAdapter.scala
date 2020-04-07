package ucf.spark.rdd

import org.objectweb.asm.{MethodVisitor,Opcodes}

class ComputeMethodAdapter(api:Int, cv: MethodVisitor)
  extends MethodVisitor(api,cv){
  override def visitCode(): Unit = {
    super.visitCode()
    this.visitMethodInsn(Opcodes.INVOKESTATIC,
      "ucf/spark/rdd/SparkInterceptor",
      "beforeInvoke",
      "()V",
      false)
  }

  override def visitInsn(opcode: Int): Unit = {
    super.visitInsn(opcode)
    if(opcode == Opcodes.RETURN) {
      this.visitMethodInsn(Opcodes.INVOKESTATIC,
        "ucf/spark/rdd/SparkInterceptor",
        "afterInvoke",
        "()V",
        false)
    }
  }
}
