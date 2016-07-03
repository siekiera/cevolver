package pl.edu.pw.elka.mtoporow.cevolver.ext.diag

import org.apache.commons.math3.linear.ArrayRealVector
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts.FitnessFunction
import pl.edu.pw.elka.mtoporow.cevolver.ext.chart.ChartData
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.{FixedWidthLineModel, MicrostripLineModelFactory, MicrostripParams}
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{CanalResponse, Distances}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

import scala.collection.immutable.NumericRange

/**
 * Klasa badająca wartości f. celu
 * Data utworzenia: 13.02.16, 19:05
 * @author Michał Toporowski
 */
object FitnessProbe {
  private val STEP = 0.002
  private val MARGIN = 0.05

  /**
   * Zwraca wartości funkcji celu w postaci wykresu z dwiema seriami:
   * (1. odl stała + 2. ruchoma) i odwrotnie
   *
   * @return Dane wykresu
   */
  def asChartData(): ChartData = {
    val distances = DataHolder.getCurrent.expectedDistances
    if (distances.distances.getDimension != 2) {
      throw new IllegalArgumentException("Operacja zaimplementowana tylko dla 2 punktów nieciągłości!")
    }
    val dist1 = distances.distances.getEntry(0)
    val dist2 = distances.distances.getEntry(1)
    val realResp = DataHolder.getCurrent.canalResponse
    val params = DataHolder.getCurrent.measurementParams.getMicrostripParams
    val xRange = getXRange(dist1, dist2)
    val result = new ChartData("Wartości funkcji celu", "Odległość", xRange.toArray)
    // Pierwsza zafiksowana, druga ruchoma
    val vals1 = getValues(xRange, dist1, realResp, params, isFirstFixed = true)
    result.addSeries(s"Pierwsza odl. stała ($dist1), druga ruchoma", vals1)
    // Pierwsza ruchoma, druga zafiksowana
    val vals2 = getValues(xRange, dist2, realResp, params, isFirstFixed = false)
    result.addSeries(s"Pierwsza odl. ruchoma, druga stała ($dist2)", vals2)
    result
  }

  /**
   * Zwraca wartości funkcji celu w postaci macierzy 2d
   * Pierwsza kolumna i pierwszy wiersz zawierają wartości odległości
   * Pozostałe komórki - wartości f. celu
   *
   * @return Wartości macierzy
   */
  def as2dMatrix(): Array[Array[Double]] = {
    val distances = DataHolder.getCurrent.expectedDistances
    if (distances.distances.getDimension < 2) {
      throw new IllegalArgumentException("Operacja zaimplementowana dla co najmniej 2 punktów nieciągłości!")
    }
    val dist1 = distances.distances.getEntry(0)
    val dist2 = distances.distances.getEntry(1)
    // Jeśli odległości jest > 2 - pozostałe stałe
    val otherDists = MatrixOps.asIterable(distances.distances).slice(2, distances.distances.getDimension).toArray
    val realResp = DataHolder.getCurrent.canalResponse
    val range1 = 0.0.max(dist1 - MARGIN) to dist1 + MARGIN by STEP
    val range2 = 0.0.max(dist2 - MARGIN) to dist2 + MARGIN by STEP
    val data = range1.map(varDist1 => {
      Array(varDist1) ++ range2.map(varDist2 => {
        val dists = Array(varDist1, varDist2) ++ otherDists
        val model = MicrostripLineModelFactory.newModel(new Distances(new ArrayRealVector(dists)))
        FitnessFunction(model, realResp, 0)
      })
    })
    val firstRow = Array(Array(0.0) ++ range2)
    firstRow ++ data
  }

  /**
   * Zwraca wartości funkcji celu w postaci macierzy 2d
   * Format wierszy: [x = L1]; [y = L2]; [z = wartość f. celu]
   *
   * @return Wartości macierzy
   */
  def asXYZ(): Array[Array[Double]] = {
    val distances = DataHolder.getCurrent.expectedDistances
    if (distances.distances.getDimension < 2) {
      throw new IllegalArgumentException("Operacja zaimplementowana dla co najmniej 2 punktów nieciągłości!")
    }
    val dist1 = distances.distances.getEntry(0)
    val dist2 = distances.distances.getEntry(1)
    // Jeśli odległości jest > 2 - pozostałe stałe
    val otherDists = MatrixOps.asIterable(distances.distances).slice(2, distances.distances.getDimension).toArray
    val realResp = DataHolder.getCurrent.canalResponse
    val range1 = 0.0.max(dist1 - MARGIN) to dist1 + MARGIN by STEP
    val range2 = 0.0.max(dist2 - MARGIN) to dist2 + MARGIN by STEP

    var result: Array[Array[Double]] = Array(Array())

    for (varDist1 <- range1) {
      result = result ++ range2.map(varDist2 => {
        val dists = Array(varDist1, varDist2) ++ otherDists
        val model = MicrostripLineModelFactory.newModel(new Distances(new ArrayRealVector(dists)))
        val value = FitnessFunction(model, realResp, 0)
        Array(varDist1, varDist2, value)
      })
    }

    result
  }


  /**
   * Zwraca wartości funkcji celu dla kanału, w którym jedna odległość jest zafiksowana, a druga ruchoma
   *
   * @param xRange zakres wartości ruchomych
   * @param fixedDist wartość stała
   * @param realResp odpowiedź rzeczywistego kanału
   * @param params parametry kanału
   * @param isFirstFixed czy pierwsza wartość jest zafiksowana
   * @return
   */
  private def getValues(xRange: NumericRange[Double], fixedDist: Double, realResp: CanalResponse, params: MicrostripParams, isFirstFixed: Boolean) = {
    xRange.map(varDist => {
      val dists = if (isFirstFixed) Array(fixedDist, varDist) else Array(varDist, fixedDist)
      val model = new FixedWidthLineModel(new Distances(new ArrayRealVector(dists)), params)
      FitnessFunction(model, realResp, 0)
    }).toArray
  }

  /**
   * Zwraca zakres osi X
   *
   * @param dist1 odległość 1.
   * @param dist2 odległość 2.
   * @return zakres od mniejszej do większej odległości powiększony o margines
   */
  private def getXRange(dist1: Double, dist2: Double): NumericRange[Double] = {
    val (min, max) = if (dist1 < dist2) (dist1, dist2) else (dist2, dist1)
    0.0.min(min - MARGIN) to max + MARGIN by STEP
  }

}
