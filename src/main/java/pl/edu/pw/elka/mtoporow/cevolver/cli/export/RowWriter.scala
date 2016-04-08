package pl.edu.pw.elka.mtoporow.cevolver.cli.export

import java.io.{File, FileWriter, OutputStreamWriter, Writer}

/**
 * Klasa wypisująca wiersze
 *
 * Data utworzenia: 08.04.16 12:31
 * @author Michał Toporowski
 */
abstract class RowWriter protected(protected val writer: Writer, separator: String) {
  /**
   * Przekazuje wiersz do wypisania
   *
   * @param row wiersz (kolekcja lub tablica)
   */
  def yieldRow(row: TraversableOnce[Any]): Unit = writer.append(row.mkString(separator)).append("\n")

  /**
   * Kończy zapisywanie (wykonanie flush lub close)
   */
  def finish()
}

/**
 * Wypisywacz do pliku
 *
 * @param file plik
 */
class FileRowWriter(file: File) extends RowWriter(new FileWriter(file), ";") {
  def finish() = writer.close()
}

/**
 * Wypisywacz do konsoli
 */
class ConsoleRowWriter() extends RowWriter(new OutputStreamWriter(System.out), "\t") {
  def finish() = writer.flush()
}