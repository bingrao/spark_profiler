package org.ucf.asm

import scala.tools.asm.{MethodVisitor, Opcodes}

class SparkMethod(api:Int, cv: MethodVisitor)  extends MethodVisitor(api,cv){

  override def visitCode(): Unit = {
    super.visitCode()
    this.visitMethodInsn(Opcodes.INVOKESTATIC, "org/ucf/asm/SparkInterceptor", "beforeInvoke", "()V",false)

  }

  override def visitInsn(opcode: Int): Unit = {
    if(opcode == Opcodes.RETURN) {
      this.visitMethodInsn(Opcodes.INVOKESTATIC, "org/ucf/asm/SparkInterceptor", "afterInvoke", "()V", false)
    }
    super.visitInsn(opcode)
  }
}
