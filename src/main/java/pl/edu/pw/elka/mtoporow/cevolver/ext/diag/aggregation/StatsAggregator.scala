package pl.edu.pw.elka.mtoporow.cevolver.ext.diag.aggregation

import java.io.File

import org.apache.commons.math3.linear.MatrixUtils
import pl.edu.pw.elka.mtoporow.cevolver.cli.export.{Exporter, FileRowWriter, RowWriter}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.{JavaVectorOps, MatrixOps}

import scala.io.Source

/**
 * Agregator plików ze statystykami
 *
 * Wywołanie: StatsAggregator &lt;katalog&gt; &lt;plik1&gt; [n] [avgOnly]
 *
 * Założenie: struktura jest następująca:
 * <ul><li> katalog <ul>
 * <li> podkatalog 1 <ul><li> plik1 </li><li> plik2 </li></ul></li>
 * <li> podkatalog 2 <ul><li> plik1 </li><li> plik2 </li></ul></li>
 * </ul></li></ul>
 *
 * n - liczba linii do pominięcia z początku pliku (domyślnie 0)
 * avgOnly - flaga określająca, czy wrzucać tylko średnią
 *
 * Przykładowe wywołania:
 * <ul>
 * <li>"katalog1" stats_total.csv 1</li>
 * <li>"katalog1" funkcja_celu.csv 0 t</li>
 * </ul>
 * Data utworzenia: 02.05.16, 12:08
 * @author Michał Toporowski
 */
object StatsAggregator {

  def main(args: Array[String]) {
    args.length match {
      case 2 => processDir(new File(args(0)), args(1))
      case 3 => processDir(new File(args(0)), args(1), args(2).toInt)
      case 4 => processDir(new File(args(0)), args(1), args(2).toInt, avgOnly = true)
      case _ => throw new IllegalArgumentException("Nieprawidłowa liczba argumentów!")
    }
  }

  private def processDir(rootDir: File, fileName: String, linesToDrop: Int = 0, avgOnly: Boolean = false): Unit = {
    println(s"Agregowanie danych z plików o nazwie $fileName z katalogu ${rootDir.getPath}; Liczba linii do pominięcia: $linesToDrop, avgOnly = $avgOnly...")
    val totalAgrFile = new File(rootDir, fileName + "_total_agr.csv")
    val totalWriter = new FileRowWriter(totalAgrFile)
    rootDir.listFiles().filter(_.isDirectory).foreach(f => processFile(f, fileName, totalWriter, linesToDrop, avgOnly))
    totalWriter.finish()
    println(s"Zapisano ${totalAgrFile.getPath}")
  }

  private def processFile(dir: File, filename: String, totalWriter: RowWriter, linesToDrop: Int, avgOnly: Boolean): Unit = {
    val file = new File(dir, filename)
    val src = Source.fromFile(file)
    // Odczytanie danych z CSV-ki do macierzy
    val data = src.getLines().drop(linesToDrop).map(s => s.split(";").map(_.replace("\"", "").toDouble)).toArray
    val matrix = JavaVectorOps.createMatrix(data)

    // Agregacja - do macierzy zawierającej średnią, max i min
    val aggFunction = if (avgOnly) {
      c: Array[Double] => Array(MatrixOps.avg(c))
    } else {
      c: Array[Double] => Array(MatrixOps.avg(c), c.max, c.min)
    }
    val aggregates = (0 until matrix.getColumnDimension).map(matrix.getColumn).map(aggFunction).toArray
    val aggregatedData = MatrixUtils.createRealMatrix(aggregates).transpose().getData

    // Zapis do pliku {nazwa}_agr.{roz}
    val outFile = new File(file.getParentFile, file.getName.replace(".", "_agr."))
    Exporter.serialize(outFile, aggregatedData)
    println(s"Zapisano ${outFile.getPath}")

    // Zapis do pliku z całością
    val f = aggregatedData.flatten
    totalWriter.yieldRow(Array(dir.getName) ++ f)
  }
}
