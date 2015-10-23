package pl.edu.pw.elka.mtoporow.cevolver.data

import java.net.URL

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.Array2DRowRealMatrix
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.util.Units
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

import scala.io.Source

/**
 * Klasa dostarczająca dane z pliku Touchstone
 * (format: częstotliwość w Ghz, Re S11, Im S11, Re S12, Im S12, ...; komentarze od #, dodatkowe informacje od !)
 * Data utworzenia: 05.06.15, 13:33
 * @author Michał Toporowski
 */
class TouchstoneDataProvider(val fileUrl: URL) extends DataProvider {
  private val SEPARATOR = " "

  override def provide: CanalResponse = {
    val source = Source.fromURL(fileUrl)
    // Odczytanie linii danych (z pominięciem komentarzy) + konwersja na Double
    val (data, comments) = source.getLines().partition(isValidData)
    val dataArray = data.map(_.split(SEPARATOR)).map(_.map(_.toDouble)).toArray
    val matrix = new Array2DRowRealMatrix(dataArray)
    // Z komentarzy !<P1 odczytujemy Z0, jeśli jest
    val p1Row = comments.filter(_.startsWith("!< P1")).toArray.head
    MeasurementParams.setImpedance(readZ0(p1Row))
    // Pierwsza kolumna: częstotliwości - ustawienia globalne + konwersja do Hz
    MeasurementParams.setFrequencies(matrix.getColumnVector(0).mapMultiply(Units.GIGA.valueInSI))
    // Odczytujemy dwie pierwsze wartości - S11
    new CanalResponse(MatrixOps.createComplexVector(matrix.getColumnVector(1), matrix.getColumnVector(2)))
  }

  private def isValidData(s: String): Boolean = {
    val c = s.charAt(0)
    c != '!' && c != '#'
  }

  /**
   * Odczytuje informację o impedancji z .s2p
   * Format przykładowy:
   * !< P1 F=4.0 Eeff=(6.1975274 -5.7859e-4) Z0=(67.8871587 0.00316838) R=0.08298377 C=0.04148466 
   *
   * @param p1Row
   */
  private def readZ0(p1Row: String): Complex = {
    if (p1Row != null) {
      val startId = p1Row.indexOf("Z0=(") + 4
      var s = p1Row.substring(startId)
      val endId = s.indexOf(")")
      s = s.substring(0, endId)
      val complexParts = s.split(" ").map(_.toDouble)
      return new Complex(complexParts(0), complexParts(1))
    }
    throw new IllegalArgumentException("No information on impedance (Z0)")
  }
}
