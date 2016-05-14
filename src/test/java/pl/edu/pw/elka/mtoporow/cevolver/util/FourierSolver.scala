package pl.edu.pw.elka.mtoporow.cevolver.util

import java.io.File

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.{DftNormalization, FastFourierTransformer, TransformType}
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.cli.export.Exporter
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.ModelChecker
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Klasa obliczająca IDFT dla odpowiedzi częstotliwościowej
 * Data utworzenia: 15.02.16, 14:14
 * @author Michał Toporowski
 */
object FourierSolver {
  private val fft = new FastFourierTransformer(DftNormalization.STANDARD)

  def calculate() = {
    val vals = freqResp()
    val respImp = fft.transform(vals, TransformType.INVERSE)
    val resAbs = respImp.map(c => new java.lang.Double(c.abs())).array
    val cl = java.util.Arrays.asList[java.lang.Double](resAbs: _*)
    Exporter.serialize(new File(GeneralConstants.OUTPUT_DIR, s"idft_${DataHolder.getCurrentId}.csv"), cl)
  }

  private def freqResp(): Array[Complex] = {
    val measuredValue = DataHolder.getCurrent.canalResponse.value
    val freqs = DataHolder.getCurrent.measurementParams.getFrequencies
    val freqStep = freqs.getEntry(1) - freqs.getEntry(0)
    println(s"Częstotliwości: ${freqs.getEntry(0)}:${freqs.getEntry(freqs.getDimension - 1)}:$freqStep")
//    val initialPaddingSize = (freqs.getEntry(0) / freqStep).toInt
    val initialPaddingSize = 0
    val initialPadding = MatrixOps.zeroComplexArray(initialPaddingSize)
    val len = initialPaddingSize + measuredValue.getDimension
    val padding = MatrixOps.zeroComplexArray(findNext2p(len) - len)
    println(s"Dopełniono tablicę. Wyp. początkowe: $initialPaddingSize, dane: ${measuredValue.getDimension}, wyp. końcowe: ${padding.length}")
    initialPadding ++ measuredValue.toArray ++ padding
  }

  private def findNext2p(n: Int) = 1 << (32 - Integer.numberOfLeadingZeros(n - 1))

  def main(args: Array[String]) {
    val checker = new ModelChecker()
    while(checker.loadNext()) {
      println(s"Obliczanie IDFT dla zestawu: ${DataHolder.getCurrentId}...")
      calculate()
      println("OK.")
    }
    println("Zakończono.")
  }

}
