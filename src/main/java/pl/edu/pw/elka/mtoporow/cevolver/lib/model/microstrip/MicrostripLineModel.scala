package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.MatrixUtils
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix.TMatrix
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{AbstractCanalModel, CanalResponse, Distances}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Model kanału w postaci linii mikropaskowej
 * Data utworzenia: 06.05.15, 20:24
 *
 * @author Michał Toporowski
 */
class MicrostripLineModel(val distances: Distances, val params: MicrostripParams)
  extends AbstractCanalModel {

  private val frequences = MeasurementParams.getFrequencies

  /**
   * Zwraca odpowiedź modelu
   *
   * @return odpowiedź modelu
   */
  override def response(): CanalResponse = {
    // Liczymy odpowiedź dla każdej częstotliwości
    val responseArray = frequences.toArray.map(f => response(f)).array
    new CanalResponse(MatrixUtils.createFieldVector(responseArray))
  }

  /**
   * Liczy odpowiedź dla danej częstotliwości
   *
   * @param frequency częstotliwość fali
   * @return
   */
  private def response(frequency: Double): Complex = {
    var resultTMatrix = new TMatrix(Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ONE)
    var prev = 0.0
    val (dists, last) = MatrixOps.extractLast(distances.distances)
    val z01 = MeasurementParams.getImpedance
    for (dist <- dists.toArray) {
      val length = dist - prev // długość paska
      prev = dist //poprzednia odległość
      // TODO:: do zastanowienia, czy to wystarczy - co ze skokiem imp.?
      // TODO:: Z01 wszędzie to samo - upewnić się, czy na pewno
      // TODO:: Do zastanowienia czy na pewno jako length - params.discL to modelować
      // Właściwy mikropasek
      resultTMatrix *= calculateTMatrix(params.w, length - params.discL, frequency, z01)
      // Przerwanie - modelujemy jako mikropasek o większym W
      resultTMatrix *= calculateTMatrix(params.discW, params.discL, frequency, z01)
    }
    // Ostatni element mikropaska
    resultTMatrix *= calculateTMatrix(params.w, last - prev, frequency, z01)
    // Obliczenie odpowiedzi całego kanału
    val s11 = resultTMatrix.toSMatrix.s11
    s11
  }

  /**
   * Oblicza macierz T zadanego mikropaska
   *
   * @param w
   * @param l
   * @param frequency
   * @param z01
   */
  private def calculateTMatrix(w: Double, l: Double, frequency: Double, z01: Complex): TMatrix = {
    val microstrip = new Microstrip(w, l, params.t, params.h, params.epsr)
    microstrip.tMatrix(frequency, z01)
  }

  override def toString: String = {
    val sb = StringBuilder.newBuilder
    sb ++= "Microstrip Line Model: "
    sb ++= "distances: ["
    sb ++= distances.distances.toArray.map(_.toString).mkString(", ")
    sb ++= "]; response: " + response()
    sb.toString()
  }
}