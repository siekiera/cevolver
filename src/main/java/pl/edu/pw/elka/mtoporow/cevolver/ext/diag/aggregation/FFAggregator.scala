package pl.edu.pw.elka.mtoporow.cevolver.ext.diag.aggregation

import java.io.File

import org.apache.commons.math3.linear.MatrixUtils
import pl.edu.pw.elka.mtoporow.cevolver.cli.export.{FileRowWriter, RowWriter}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps
import pl.edu.pw.elka.mtoporow.cevolver.util.GeneralConstants

/**
 * Agregator plików ze statystykami funkcji celu.
 *
 * Założenie: struktura analogiczna, jak dla klasy StatsAggregator.
 *
 * Z plików o nazwie funkcja_celu.csv zgarniamy wartości funkcji celu w kolejnych pokoleniach.
 * Z plików czasy.csv zgarniamy czas i tworzymy ogólny plik fc_w_czasie.csv z zależnością FC od czasu
 *
 * Parametr: katalog
 *
 * Data utworzenia: 15.05.16, 17:28
 * @author Michał Toporowski
 * @see pl.edu.pw.elka.mtoporow.cevolver.ext.diag.StatsAggregator
 */
object FFAggregator {

  def main(args: Array[String]): Unit = {
    if (args.length == 1) {
      processTestSetDir(new File(args(0)))
    } else {
      throw new IllegalArgumentException("|args| != 1")
    }
  }

  private def processTestSetDir(rootDir: File): Unit = {
    val totalAgrFile = new File(rootDir, "fc_w_czasie.csv")
    val totalWriter = new FileRowWriter(totalAgrFile)
    rootDir.listFiles().filter(_.isDirectory).foreach(f => processTestCaseSubdir(f, totalWriter))
    totalWriter.finish()
    println(s"Zapisano ${totalAgrFile.getPath}")
  }

  private def processTestCaseSubdir(dir: File, totalWriter: RowWriter): Unit = {
    val ffFile = new File(dir, GeneralConstants.FF_OUTPUT_FILENAME)
    val timesFile = new File(dir, GeneralConstants.TIMES_FILENAME)

    // Odczytanie danych z CSV-ki funkcji celu do macierzy
    val matrix = csvToMatrix(ffFile)
    // Wyliczenie średniej
    val aggregates = (0 until matrix.getColumnDimension).map(matrix.getColumn).map(c => Array(MatrixOps.avg(c))).toArray
    val aggregatedData = MatrixUtils.createRealMatrix(aggregates).transpose().getData

    // Z CSV-ki dot. czasu odczytujemy czas i liczymy jego średnią
    val times = csvToArray(timesFile).head
    val avgTime = MatrixOps.avg(times)

    // Tworzymy wiersz zawierający czas kolejnych pokoleń
    val generationCount = matrix.getColumnDimension
    val avgGenerationTime = avgTime / generationCount
    val generationTimes = (0 until generationCount).map(_ * avgGenerationTime)

    // Zapis do pliku z całością - czasy
    totalWriter.yieldRow(Array(dir.getName + " - czasy") ++ generationTimes)

    // Zapis do pliku z całością - wartości
    val f = aggregatedData.flatten
    totalWriter.yieldRow(Array(dir.getName + " - wartości") ++ f)
  }

}
