package pl.edu.pw.elka.mtoporow.cevolver.gui

import javafx.concurrent

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.RegisteredParams.ParamDef
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param._
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.Conversions
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{AlgorithmParameters, AlgorithmPartParams, EvolutionaryAlgorithm}
import pl.edu.pw.elka.mtoporow.cevolver.engine.Solver
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripLineModel

import scala.collection.mutable
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.concurrent.Task
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

/**
 * Główna klasa interfejsu użytkownika
 *
 * Data utworzenia: 23.10.15, 17:48
 * @author Michał Toporowski
 */
object CevolverUI extends JFXApp {
  private val additionalParamsMap = new AdditionalParamsMap
  private val populationSizeCtrl = new Spinner[Int](10, 10000, 100, 1)
  private val eliteCountCtrl = new Spinner[Int](10, 10000, 10, 1)
  private val additionalParamsPane = loadAdditionalParamsPane()
  private val cfCtrl = createPartComboBox(CFType.values())
  private val ssCtrl = createPartComboBox(SSType.values())
  private val eoCtrl = createPartComboBox(EOType.values())
  private val tcCtrl = createPartComboBox(TCType.values())
  private val feCtrl = createPartComboBox(FEType.values())
  private val dataSetBox = new ComboBox[String](Conversions.javaToScalaList(DataHolder.getAvailableDataSets))
  private val expectedResLabel = new Label()
  private val resultLabel = new Label()

  stage = new JFXApp.PrimaryStage {
    title.value = "Cevolver"
    //    width = 600
    //    height = 450
    scene = new Scene {
      content = new VBox() {
        children += createConfigPane()
        children += additionalParamsPane
        children += createRunButton()
        children += expectedResLabel
        children += resultLabel
      }
    }
  }

  private def createConfigPane() = {
    val builder = new GridPaneBuilder(2)
    builder += new Label("Zestaw danych")
    builder += dataSetBox
    builder += new Label("Wielkość populacji")
    builder += populationSizeCtrl
    builder += new Label("Wielkość elity")
    builder += eliteCountCtrl
    builder += new Label("Metoda inicjacji")
    builder += cfCtrl
    builder += new Label("Strategia selekcji")
    builder += ssCtrl
    builder += new Label("Operator ewolucyjny")
    builder += eoCtrl
    builder += new Label("Kryterium zakończenia")
    builder += tcCtrl
    builder += new Label("Funkcja oceny jakości")
    builder += feCtrl
    builder.gridPane
  }

  private def createPartComboBox[T](values: Seq[T]) = {
    new ComboBox[T](values) {
      value = values.head
      onAction = handle {
        refreshAdditionalParams(this)
      }
      refreshAdditionalParams(this)
    }
  }

  private def loadAdditionalParamsPane() = {
    additionalParamsMap.clear()
    val builder = new GridPaneBuilder(1)
    RegisteredParams.partsParamDefs.foreach {
      case (partType, paramDefs) => {
        // TODO:: dodać dynamiczne zmienianie tego
        for (paramDef <- paramDefs) {
          val vb = additionalParamsMap.put(paramDef)
          builder += vb
        }
      }
    }
    builder.gridPane
  }

  private def refreshAdditionalParams[T](comboBox: ComboBox[T]) = {
    RegisteredParams.partsParamDefs.foreach {
      case (partType, paramDefs) => {
        if (comboBox.value.value.getClass == partType.getClass) {
          val active = comboBox.value.value.equals(partType)
          paramDefs.foreach {
            d => additionalParamsMap.setVisible(d, active)
          }
        }
      }
    }
  }

  private def createRunButton() = {
    new Button("Uruchom...") {
      onAction = handle {
        new Thread(RunAlgorithmTask).start()
      }
    }
  }

  private def paramValues(): AlgorithmParameters = {
    val p = new AlgorithmParameters
    p.populationSize = populationSizeCtrl.value.value
    p.eliteCount = eliteCountCtrl.value.value
    p.candidateFactory = getPartParams(cfCtrl)
    p.selectionStrategy = getPartParams(ssCtrl)
    p.operators = List(getPartParams(eoCtrl))
    p.terminationCondition = getPartParams(tcCtrl)
    p.fitnessEvaluator = getPartParams(feCtrl)
    p
  }

  private def getPartParams[T <: AlgorithmPartType](ctrl: ComboBox[T]) = {
    val partType = ctrl.value.value
    val partParams = new AlgorithmPartParams[T](partType)
    partParams.partsParamsDefs.foreach(paramDef => {
      partParams.setParamValue(paramDef, additionalParamsMap.getValue(paramDef))
    })
    partParams
  }

  private object RunAlgorithmTask extends Task(new concurrent.Task[Void]() {
    private var result: EvolutionaryAlgorithm.C = _
    private var expResult: EvolutionaryAlgorithm.C = _

    override def call(): Void = {
      DataHolder.load(dataSetBox.value.value)
      expResult = new MicrostripLineModel(DataHolder.getCurrent.expectedDistances, DataHolder.getCurrent.measurementParams.getMicrostripParams)
      result = new Solver().solve(paramValues(), DataHolder.getCurrent.canalResponse)
      null
    }

    // TODO
    override def succeeded(): Unit = {
      expectedResLabel.text = "Oczekiwany wynik: " + expResult
      resultLabel.text = "Wynik" + result.toString
    }
  })

  /**
   * Klasa przechowująca kontrolki oraz informację nt. parametrów dodatkowych
   */
  private class AdditionalParamsMap {
    private val map: mutable.Map[ParamDef, ValueBox[_ <: AnyVal]] = new mutable.HashMap[ParamDef, ValueBox[_ <: AnyVal]]()

    /**
     * Pobiera wartość dla danej definicji parametru
     *
     * @param paramDef definicja parametru
     * @return wartość (Int lub Double)
     */
    def getValue(paramDef: ParamDef) = map.get(paramDef).get.spinner.value.value

    /**
     * Czyści mapę
     */
    def clear() = map.clear()

    /**
     * Umieszcza nową definicję parametru.
     * Utworzona zostaje odpowiednia kontrolka (tekst + spinner)
     *
     * @param paramDef definicja parametru
     * @return wynikowa kontrolka
     */
    def put(paramDef: ParamDef) = {
      val spinner: Spinner[_ <: AnyVal] = if (paramDef.cls == classOf[Integer])
        new Spinner[Int](0, 1000, 0)
      else
        new Spinner[Double](0, 1000, 0, 0.5)
      spinner.editable = true
      val vb = new ValueBox(paramDef.name, spinner)
      map.put(paramDef, vb)
      vb
    }

    /**
     * Ustawia widoczność danej kontrolki
     *
     * @param paramDef definicja parametru
     * @param visible wartość logiczna
     */
    def setVisible(paramDef: ParamDef, visible: Boolean) = map.get(paramDef).get.visible = visible

  }

  /**
   * Kontrolka zawierająca tekst oraz Spinner
   *
   * @param caption tekst
   * @param spinner spinner
   * @tparam T typ wartości w spinnerze
   */
  private class ValueBox[T <: AnyVal](val caption: String, val spinner: Spinner[T]) extends HBox {
    children += new Label(caption)
    children += spinner
  }

}
