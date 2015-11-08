package pl.edu.pw.elka.mtoporow.cevolver.gui

import scalafx.scene.Node
import scalafx.scene.layout.GridPane

/**
 * Klasa X
 * Data utworzenia: 01.11.15, 11:32
 * @author Michał Toporowski
 */
class GridPaneBuilder(val columns: Int) {
  val gridPane = new GridPane()
  private var row = 0
  private var col = 0


  def +=(child: Node): this.type = {
    gridPane.add(child, col, row)
    increment()
    this
  }

  private def increment() = {
    col += 1
    if (col >= columns) {
      col = 0
      row += 1
    }
  }

}
