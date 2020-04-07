package ucf.spark

import java.lang.instrument.ClassFileTransformer
import java.io.{File, FileOutputStream}
import org.objectweb.asm.{ClassReader, ClassVisitor, ClassWriter}
import ucf.spark.utils.Common
/**
 *
 * @param newVisitor
 */
class GenericTransformer(newVisitor:ClassVisitor => GenericVisitor) extends ClassFileTransformer with Common {
  override def transform(loader: _root_.java.lang.ClassLoader,
                         className: _root_.java.lang.String,
                         classBeingRedefined: _root_.java.lang.Class[_],
                         protectionDomain: _root_.java.security.ProtectionDomain,
                         classfileBuffer: Array[Byte]): Array[Byte] = {

    try {
      if (className == null || className.isEmpty) {
        logger.info(s"Hit null or empty class name ${className}")
        classfileBuffer
      } else if(className.contains("RDD")){
        logger.info(s"Transforming class [${className}] using ASM tool")
        if (logger.isDebugEnabled) {
          val oldfout = new FileOutputStream(new File(s"/home/bing/tmp/profiler/${className}.old.class"))
          oldfout.write(classfileBuffer)
          oldfout.close()
        }
        /**
         *
         * *
         * *            ##########       ###########      ###########
         * *            #        #       #         #      #         #
         * *  bytes --> # Reader # -->   # Adapter # -->  # Writer  #
         * *            #        #       #         #      #         #
         * *            ##########       ###########      ###########
         *
         *
         */
        val reader = new ClassReader(classfileBuffer)
        val writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS)

        // Create a new visitor bonding with existing writer
        val visitor = newVisitor(writer)

        reader.accept(visitor, ClassReader.SKIP_DEBUG)

        val reg = writer.toByteArray
        if (logger.isDebugEnabled) {
          val newfout = new FileOutputStream(new File(s"/home/bing/tmp/profiler/${className}.new.class"))
          newfout.write(reg)
          newfout.close()
        }
        reg
      } else{
        classfileBuffer
      }
    } catch {
      case ex: Throwable =>
        logger.warn("Failed to transform class " + className)
        logger.warn(ex.getMessage)
        ex.printStackTrace()
        classfileBuffer
    }
  }

  def transform(classfileBuffer:String = "java.lang.Runnable"):Array[Byte] = {
    val reader = new ClassReader(classfileBuffer)
    val writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
    // Create a new visitor bonding with existing writer
    val visitor = newVisitor(writer)
    reader.accept(visitor, ClassReader.SKIP_DEBUG)
    writer.toByteArray
  }

}
