package pl.edu.pw.elka.mtoporow.cevolver.data

import java.net.URL

import org.apache.commons.math3.linear.Array2DRowRealMatrix
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

import scala.io.Source

/**
 * Klasa dostarczająca dane z pliku CSV (format: częstotliwość, Re Gamma, Im Gamma)
 * Data utworzenia: 05.06.15, 13:33
 * @author Michał Toporowski
 */
class CsvDataProvider(val fileUrl: URL) extends DataProvider {
  private val SEPARATOR = ","

  override def provide: CanalResponse = {
    val source = Source.fromURL(fileUrl)
    val dataArray = source.getLines().map(_.split(SEPARATOR)).map(_.map(_.toDouble)).toArray
    val matrix = new Array2DRowRealMatrix(dataArray)
    // Pierwsza kolumna: częstotliwości - ustawienia globalne
    DataHolder.getCurrent.measurementParams.setFrequencies(matrix.getColumnVector(0))
    new CanalResponse(MatrixOps.createComplexVector(matrix.getColumnVector(1), matrix.getColumnVector(2)))
  }
}
