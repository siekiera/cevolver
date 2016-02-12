package pl.edu.pw.elka.mtoporow.cevolver.util

import java.io.File

/**
 * Globalne definicje i narzędzia
 * Data utworzenia: 12.02.16, 12:02
 * @author Michał Toporowski
 */
object GeneralConstants {

  /**
   * Domyślny katalog wyjściowy programu, czyli ${USER_HOME}/cevolver_out
   */
  lazy val OUTPUT_DIR = {
    val home = System.getProperty("user.home")
    val dir = new File(home, "cevolver_out")
    dir.mkdirs()
    dir
  }
}
