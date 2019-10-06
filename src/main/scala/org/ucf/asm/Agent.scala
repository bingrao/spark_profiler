package org.ucf.asm


import java.lang.instrument.{ClassFileTransformer, Instrumentation}
import java.security.ProtectionDomain

import java.io.{File, FileOutputStream}
import scala.tools.asm.{ClassReader,ClassWriter}

object Agent {
  def premain(args: String, inst: Instrumentation): Unit = {

    inst.addTransformer(new ClassFileTransformer {
      override def transform(loader: ClassLoader,
                             className: String,
                             classBeingRedefined: Class[_],
                             protectionDomain: ProtectionDomain,
                             classfileBuffer: Array[Byte]): Array[Byte] = {

        if (instr_asm_class.equals(className)) {
          try {

            //https://stackoverflow.com/questions/23416536/main-method-in-scala
            if (debug) {
              val oldFile = new File("old.class")
              val oldfout = new FileOutputStream(oldFile)
              oldfout.write(classfileBuffer)
              oldfout.close()
            }

            val reader = new ClassReader(classfileBuffer)
            val writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
            val visitor = new SparkClassAdapter(writer)
            reader.accept(visitor, ClassReader.SKIP_DEBUG)
            val data = writer.toByteArray

            if (debug) {
              val newFile = new File("new.class")
              val newfout = new FileOutputStream(newFile)
              newfout.write(data)
              newfout.close()
            }

            return data
          } catch {
            case ex:Exception =>
              ex.printStackTrace()
          }
        }

        return null
      }
    })
  }
}
