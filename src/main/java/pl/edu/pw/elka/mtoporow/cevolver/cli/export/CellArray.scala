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

  private def names = fields.keySet ++ methods.keySet
}

object CellArray {
  /**
   * Eksportuje pola oznaczone adnotacją @Cell z obiektu cellObject do Writera
   *
   * @param cellObjects obiekt
   * @param writer klasa zapisująca
   * @param ca opcjonalny obiekt CA
   * @throws java.io.IOException io
   * @return obiekt CellArray do reużycia
   */
  @throws(classOf[IOException])
  def writeValues(cellObjects: Iterable[AnyRef], writer: RowWriter, cellArray: CellArray = null): CellArray = {
    var ca: CellArray = cellArray
    for (cellObject <- cellObjects) {
      if (ca == null) {
        ca = new CellArray(cellObject.getClass)
        writer.yieldRow(ca.names)
      }
      val fieldVals = ca.fields.values.map(f => fieldValue(f, cellObject))
      val methodVals = ca.methods.values.map(m => m.invoke(cellObject))
      writer.yieldRow(fieldVals ++ methodVals)
    }
    ca
  }

  /**
   * Eksportuje pola oznaczone adnotacją @Cell z obiektu cellObject do Writera
   *
   * @param cellObject obiekt
   * @param writer klasa zapisująca
   * @param ca opcjonalny obiekt CA
   * @throws java.io.IOException io
   * @return obiekt CellArray do reużycia
   */
  def writeValue(cellObject: AnyRef, writer: RowWriter, ca: CellArray = null): CellArray = {
    writeValues(Iterable(cellObject), writer, ca)
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