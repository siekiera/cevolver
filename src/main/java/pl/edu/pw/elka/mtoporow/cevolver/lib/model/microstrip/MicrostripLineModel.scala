package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.MatrixUtils
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.MeasurementParams
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix.TMatrix
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{AbstractCanalModel, CanalResponse, Distances}

/**
 * Model kanału w postaci linii mikropaskowej
 * Data utworzenia: 06.05.15, 20:24
 *
 * @author Michał Toporowski
 */
class MicrostripLineModel(val distances: Distances, val params: MicrostripParams)
  extends AbstractCanalModel {

  /**
   * Zwraca odpowiedź modelu
   *
   * @return odpowiedź modelu
   */
  override def calculateResponse(measurementParams: MeasurementParams): CanalResponse = {
    // Liczymy odpowiedź dla każdej częstotliwości
    val impedance = measurementParams.getImpedance
    val responseArray = measurementParams.getFrequencies.toArray.map(f => response(f, impedance)).array
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
   * @param z01 impedancja Z0 na złączach
   * @return
   */
  private def response(frequency: Double, z01: Complex): Complex = {
    var resultTMatrix = new TMatrix(Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ONE)
    var thick = false // Cienki, czy gruby element paska - na zmianę
    for (dist <- distances.distances.toArray) {
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
   * @param w szerokość
   * @param l długość
   * @param frequency częstotliwość
   * @param z01 impedancja
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
    sb ++= "]; distances (mm): ["
    sb ++= distances.toStringMM
    // FIXME:: zmienić to... liczy odpowiedź 2 razy?
    sb ++= "]; response: " + lastResponse()
    sb.toString()
  }
}