package ucf.spark
package rdd

import org.objectweb.asm.ClassVisitor
import ucf.spark.GenericVisitor

class RDDClassAdapter(writer:ClassVisitor, trace:Boolean)
  extends GenericVisitor(writer, trace){

  private var class_name = ""
  private var class_super_name = ""
  private var isProfiling = false

  override def visit(version: Int,
                     access: Int,
                     name: _root_.java.lang.String,
                     signature: _root_.java.lang.String,
                     superName: _root_.java.lang.String,
                     interfaces: Array[_root_.java.lang.String]): Unit ={
    class_name = name
    class_super_name = superName
    if (class_super_name.equals("org/apache/spark/rdd/RDD")) isProfiling = true
    logger.info(s"[Visit] - Class ${class_name}, supper Name ${class_super_name}")
    getCV.visit(version, access, name, signature, superName, interfaces)
  }

  override def visitMethod(access: Int,
                           name: _root_.java.lang.String,
                           descriptor: _root_.java.lang.String,
                           signature: _root_.java.lang.String,
                           exceptions: Array[_root_.java.lang.String]): _root_.org.objectweb.asm.MethodVisitor = {
    val mv = getCV.visitMethod(access, name, descriptor, signature, exceptions)
    if (isProfiling && (mv != null) && (name.equals(instr_method))) {
      logger.info(s"[visitMethod] - Class ${class_name}, supper Name ${class_super_name}, method ${instr_method}")
      new ComputeMethodAdapter(this.api, mv)
    } else mv
  }
}
