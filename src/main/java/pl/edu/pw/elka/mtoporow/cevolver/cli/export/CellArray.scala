package pl.edu.pw.elka.mtoporow.cevolver.cli.export

import java.io.IOException
import java.lang.reflect.{AnnotatedElement, Field}

import scala.collection.immutable.ListMap

/**
 * Tablica pól przeznaczonych do wyeksportowania
 * Data utworzenia: 04.04.16, 15:14
 *
 * @author Michał Toporowski
 */
class CellArray(cls: Class[_]) {
  private val methods = annotationsMap(cls.getDeclaredMethods)
  private val fields = annotationsMap(cls.getDeclaredFields)

  private def annotationsMap[T <: AnnotatedElement](els: Array[T]) = ListMap(els.filter(_.isAnnotationPresent(classOf[Cell])).map(m => (m.getAnnotation(classOf[Cell]).value(), m)): _*)

  def names = fields.keySet ++ methods.keySet
}

object CellArray {
  /**
   * Eksportuje pola oznaczone adnotacją @Cell z obiektu cellObject do Writera
   *
   * @param cellObjects obiekt
   * @param writer klasa zapisująca
   * @throws java.io.IOException io
   */
  @throws(classOf[IOException])
  def writeValues(cellObjects: Iterable[AnyRef], writer: RowWriter) = {
    var ca: CellArray = null
    for (cellObject <- cellObjects) {
      if (ca == null) {
        ca = new CellArray(cellObject.getClass)
        writer.yieldRow(ca.names)
      }
      val fieldVals = ca.fields.values.map(f => fieldValue(f, cellObject))
      val methodVals = ca.methods.values.map(m => m.invoke(cellObject))
      writer.yieldRow(fieldVals ++ methodVals)
    }
  }

  /**
   * Eksportuje pola oznaczone adnotacją @Cell z obiektu cellObject do Writera
   *
   * @param cellObject obiekt
   * @param writer klasa zapisująca
   * @throws java.io.IOException io
   */
  def writeValue(cellObject: AnyRef, writer: RowWriter): Unit = {
    writeValues(Iterable(cellObject), writer)
  }

  /**
   * Pobiera wartość pola przy pomocy refleksji
   *
   * @param field pole
   * @param obj obiekt
   * @return wartość
   */
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