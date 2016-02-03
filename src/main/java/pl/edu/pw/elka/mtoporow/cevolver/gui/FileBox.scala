package pl.edu.pw.elka.mtoporow.cevolver.gui

import java.io.File

import scalafx.Includes._
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.HBox
import scalafx.stage.{FileChooser, Window}

/**
 * Kontrolka pozwalająca na wczytanie pliku
 * Data utworzenia: 05.11.15, 12:42
 * @author Michał Toporowski
 */
class FileBox(val name: String, val ownerWindow: Window) extends HBox {
  private var _dataFile: File = null

  def dataFile = _dataFile

  private val fileField = new TextField()
  private val fileButton = new Button("Wybierz... ") {
    onAction = handle {
      val fileChooser = new FileChooser() {
        title = "Wybierz plik"
      }
      _dataFile = fileChooser.showOpenDialog(ownerWindow)
      fileField.text = if (_dataFile != null) _dataFile.getAbsolutePath else ""
    }
  }
  children += new Label(name)
  children += fileField
  children += fileButton
}
