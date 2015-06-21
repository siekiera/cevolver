package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.MatrixUtils
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams
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
    for (dist <- distances.distances.toArray) {
      val length = dist - prev // długość paska
      prev = dist //poprzednia odległość
      // TODO:: do zastanowienia, czy to wystarczy - co ze skokiem imp.?
      val microstrip = new Microstrip(params.w, length, params.t, params.h, params.epsr)
      val tmatrix = microstrip.tMatrix(frequency, new Complex(0, 0)) //FIXME - skąd Z01
      resultTMatrix *= tmatrix
    }
    val s11 = resultTMatrix.toSMatrix.s11
    s11
  }

}