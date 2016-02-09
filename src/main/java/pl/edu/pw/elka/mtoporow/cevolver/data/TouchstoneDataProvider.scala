package pl.edu.pw.elka.mtoporow.cevolver.data

import java.io.File
import java.net.URL

import org.apache.commons.math3.linear.{Array2DRowRealMatrix, RealVector}
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.maths.Units
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

import scala.io.Source

/**
 * Klasa dostarczająca dane z pliku Touchstone
 * (format: częstotliwość w Ghz, Re S11, Im S11, Re S12, Im S12, ...; komentarze od #, dodatkowe informacje od !)
 * Data utworzenia: 05.06.15, 13:33
 * @author Michał Toporowski
 */
class TouchstoneDataProvider(private val source: Source) extends DataProvider {
  private val SEPARATOR = "\\s+"
  private var _frequencies: RealVector = null

  def this(fileUrl: URL) = this(Source.fromURL(fileUrl))

  def this(file: File) = this(Source.fromFile(file))

  def frequencies() = _frequencies

  override def provide: CanalResponse = {
    // Odczytanie linii danych (z pominięciem komentarzy) + konwersja na Double
    val data = source.getLines().filter(isValidData)
    val dataArray = data.map(_.split(SEPARATOR)).map(_.map(_.toDouble)).toArray
    val matrix = new Array2DRowRealMatrix(dataArray)
    // Pierwsza kolumna: częstotliwości - ustawienia globalne + konwersja do Hz
    _frequencies = matrix.getColumnVector(0).mapMultiply(Units.GIGA.valueInSI)
    // Odczytujemy dwie pierwsze wartości - S11
    new CanalResponse(MatrixOps.createComplexVector(matrix.getColumnVector(1), matrix.getColumnVector(2)))
    //    new CanalResponse(MatrixOps.createComplexVector(matrix.getColumnVector(5), matrix.getColumnVector(6)))
  }

  private def isValidData(s: String): Boolean = {
    val c = s.charAt(0)
    c != '!' && c != '#'
  }
}