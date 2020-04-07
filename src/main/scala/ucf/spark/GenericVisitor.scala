package ucf.spark

import java.io.{File, PrintWriter}
import utils.Common
import org.objectweb.asm.util.TraceClassVisitor
import org.objectweb.asm.{ClassVisitor, Opcodes}

abstract class GenericVisitor(classVisitor:ClassVisitor, trace:Boolean)
  extends ClassVisitor(Opcodes.ASM5, classVisitor)
    with Common {

  /**
   * you expect, the byte array returned by a ClassWriter is not really helpful
   * because it is unreadable by humans. A textual representation would be much
   * easier to use. This is what the TraceClassVisitor class provides. This class,
   * as its name implies, extends the ClassVisitor class, and builds a textual
   * representation of the visited class. So, instead of using a ClassWriter to
   * generate your classes, you can use a TraceClassVisitor, in order to get a
   * readable trace of what is actually generated. Or, even better, you can use
   * both at the same time.
   */
  val _cv = if (trace) new TraceClassVisitor(classVisitor,
    new PrintWriter(new File("/home/bing/tmp/profiler/trace.java")))
  else
    null

  def getCV = if (trace) this._cv else this.cv

}
