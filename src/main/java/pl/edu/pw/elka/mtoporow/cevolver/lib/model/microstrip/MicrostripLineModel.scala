package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.MatrixUtils
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix.TMatrix
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.util.Units
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{AbstractCanalModel, CanalResponse, Distances}

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
   * Tworzy nowy model tego samego typu z innymi odległościami
   *
   * @param distances odległości
   * @return
   */
  override def createNew(distances: Distances) = new MicrostripLineModel(distances, params)

  /**
   * Liczy odpowiedź dla danej częstotliwości
   *
   * @param frequency częstotliwość fali
   * @return
   */
  private def response(frequency: Double): Complex = {
    var resultTMatrix = new TMatrix(Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ONE)
    val z01 = MeasurementParams.getImpedance
    var thick = false // Cienki, czy gruby element paska - na zmianę
    for (dist <- distances.distances.toArray) {
      // TODO:: do zastanowienia, czy to wystarczy - co ze skokiem imp.?
      // TODO:: Z01 wszędzie to samo - upewnić się, czy na pewno
      // Właściwy mikropasek
      // Przerwanie - modelowane na styku pasków o mniejszym i większym W
      val w = if (thick) params.biggerW else params.w
      resultTMatrix *= calculateTMatrix(w, dist, frequency, z01)
      thick = !thick
    }
    // Ostatni element mikropaska
    resultTMatrix *= calculateTMatrix(params.w, distances.last, frequency, z01)
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
    sb ++= "distances (m): ["
    sb ++= distances.distances.toArray.map(_.toString).mkString(", ")
    sb ++= "]; distances (mils): ["
    sb ++= distances.distances.toArray.map(Units.MIL.fromSI(_).toString).mkString(", ")
    sb ++= "]; response: " + response()
    sb.toString()
  }
}