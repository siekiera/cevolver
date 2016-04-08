package pl.edu.pw.elka.mtoporow.cevolver.cli.export

import java.io.{IOException, Writer}
import java.lang.reflect.Field

import pl.edu.pw.elka.mtoporow.cevolver.ext.diag.PopulationStatistics

/**
 * Klasa X
 * Data utworzenia: 04.04.16, 15:14
 *
 * @author Michał Toporowski
 */
class CellArray(cls: Class[_]) {
  //  private final val fields: Map[String, Field] = new LinkedHashMap[String, Field]
  //  private final val methods: Map[String, Method] = new LinkedHashMap[String, Method]

  private val methods = cls.getDeclaredMethods.filter(_.isAnnotationPresent(classOf[Cell])).map(m => (m.getAnnotation(classOf[Cell]).value(), m)).toMap
  private val fields = cls.getDeclaredFields.filter(_.isAnnotationPresent(classOf[Cell])).map(f => (f.getAnnotation(classOf[Cell]).value(), f)).toMap

  def names = fields.keySet ++ methods.keySet


}

object CellArray {
  @throws(classOf[IOException])
  def writeValues(cellObjects: Iterable[AnyRef], writer: Writer) = {
    var ca: CellArray = null
    for (cellObject <- cellObjects) {
      if (ca == null) {
        ca = new CellArray(cellObject.getClass)
        writer.append(ca.names.mkString(";")).append("\n")
      }
      val fieldVals = ca.fields.values.map(f => fieldValue(f, cellObject))
      val methodVals = ca.methods.values.map(m => m.invoke(cellObject))
      writer.append((fieldVals ++ methodVals).mkString(";")).append("\n")
    }
    writer.flush()
    writer.close()
  }

  def writeValue(cellObject: AnyRef, writer: Writer): Unit = {
    writeValues(Iterable(cellObject), writer)
  }

  private def fieldValue(field: Field, obj: AnyRef): AnyRef = {
    var result: AnyRef = null
    if (!field.isAccessible) {
      field.setAccessible(true)
      result = field.get(obj)
      field.setAccessible(false)
    } else {
      result = field.get(obj)
    }
    result
  }

}