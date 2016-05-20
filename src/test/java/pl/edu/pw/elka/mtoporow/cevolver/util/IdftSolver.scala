package pl.edu.pw.elka.mtoporow.cevolver.util

import java.io.File

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.cli.export.Exporter
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.ModelChecker
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Klasa obliczająca IDFT dla odpowiedzi częstotliwościowej przy użyciu własnej implementacji IDFT (nie-FFT)
 * Data utworzenia: 20.05.16, 11:31
 * @author Michał Toporowski
 */
object IdftSolver {
  private val idft = new IDFT()

  def calculate() = {
    val vals = freqResp()
    val respImp = idft.transform(vals)
    val resAbs = MatrixOps.asIterable(respImp).map(c => c.abs()).toArray
    val cl = Array(resAbs)
    Exporter.serialize(new File(GeneralConstants.OUTPUT_DIR, s"slow_idft_${DataHolder.getCurrentId}.csv"), cl)
  }

  private def freqResp(): MatrixOps.ComplexVector = DataHolder.getCurrent.canalResponse.value


  def main(args: Array[String]) {
    val checker = new ModelChecker()
    while (checker.loadNext()) {
      println(s"Obliczanie IDFT dla zestawu: ${DataHolder.getCurrentId}...")
      calculate()
      println("OK.")
    }
    println("Zakończono.")
  }
}
