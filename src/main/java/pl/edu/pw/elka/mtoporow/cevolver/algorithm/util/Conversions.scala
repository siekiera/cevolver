package pl.edu.pw.elka.mtoporow.cevolver.algorithm.util

import java.util

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
 * Obiekt zawierający metody konwersji pomiędzy kolekcjami Javy i Scali
 * Data utworzenia: 29.05.15, 19:17
 * @author Michał Toporowski
 */
object Conversions {

  /**
   * Konwertuje listę Scali do Javy
   *
   * @param list
   * @tparam T
   * @return
   */
  def scalaToJavaList[T](list: List[T]): util.List[T] = ListBuffer(list: _*)

  /**
   * Konwertuje listę Javy do Scali
   *
   * @param list
   * @tparam T
   * @return
   */
  def javaToScalaList[T](list: util.List[T]): List[T] = list.toList

  /**
   * Konwertuje tablicę do listy scalowej
   *
   * @param objects
   * @tparam T
   * @return
   */
  def arrayToScalaList[T](objects: Array[T]): List[T] = objects.toList

  /**
   * Tworzy listę scali złożoną z 1 obiektu
   *
   * @param obj
   * @tparam T
   * @return
   */
  def objectToScalaList[T](obj: T): List[T] = List(obj)
}
