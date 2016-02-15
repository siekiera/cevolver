package pl.edu.pw.elka.mtoporow.cevolver.ext.fft

import java.io.File

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.{TransformType, DftNormalization, FastFourierTransformer}
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.cli.export.Exporter
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps
import pl.edu.pw.elka.mtoporow.cevolver.util.GeneralConstants

/**
 * Klasa X
 * Data utworzenia: 15.02.16, 14:14
 * @author Michał Toporowski
 */
object FourierSolver {
  private val fft = new FastFourierTransformer(DftNormalization.STANDARD)

  def calculate() = {
    val vals = freqResp()
    val respImp = fft.transform(vals, TransformType.INVERSE)
    val resAbs = respImp.map(c => new java.lang.Double(c.abs())).array
    val cl = java.util.Arrays.asList[java.lang.Double](resAbs:_*)
    Exporter.serialize(new File(GeneralConstants.OUTPUT_DIR, s"idft_${DataHolder.getCurrentId}.csv"), cl)
  }

  private def freqResp(): Array[Complex] = {
    val measuredValue = DataHolder.getCurrent.canalResponse.value
//    val freqs = DataHolder.getCurrent.measurementParams.getFrequencies
//    val freqStep = freqs.getEntry(1) - freqs.getEntry(0)
    val initialP = MatrixOps.zeroComplexArray(50)
    val len = initialP.length + measuredValue.getDimension
    val padding = MatrixOps.zeroComplexArray(findNext2p(len) - len)
    initialP ++ measuredValue.toArray ++ padding
  }

  private def findNext2p(n: Int) = 1 << (32 - Integer.numberOfLeadingZeros(n - 1))

  def main(args: Array[String]) {
    DataHolder.load("20")
    calculate()
  }

}
