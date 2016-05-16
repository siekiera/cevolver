package pl.edu.pw.elka.mtoporow.cevolver.ext.diag

import java.io.File

import org.apache.commons.math3.linear.RealMatrix
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.JavaVectorOps

import scala.io.Source

/**
 * Obiekt dla pakietu agregacji
 * Data utworzenia: 15.05.16, 17:46
 * @author Michał Toporowski
 */
package object aggregation {

  /**
   * Wczytuje plik CSV do tablicy dwuwymiarowej
   * (założenie: wszystkie pola są typu liczbowego)
   *
   * @param file plik
   * @return tablica 2D
   */
  def csvToArray(file: File): Array[Array[Double]] = Source.fromFile(file).getLines().map(s => s.split(";").map(_.replace("\"", "").toDouble)).toArray

  /**
   * Wczytuje plik CSV do macierzy
   *
   * @param file plik
   * @return macierz
   * @see csvToArray
   */
  def csvToMatrix(file: File): RealMatrix = JavaVectorOps.createMatrix(csvToArray(file))

}
