package pl.edu.pw.elka.mtoporow.cevolver.gui

import java.io.FileInputStream
import javafx.concurrent

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.RegisteredParams.ParamDef
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param._
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{AlgorithmParameters, AlgorithmPartParams, EvolutionaryAlgorithm}
import pl.edu.pw.elka.mtoporow.cevolver.cli.EnvPropertiesReader
import pl.edu.pw.elka.mtoporow.cevolver.data.TouchstoneDataProvider
import pl.edu.pw.elka.mtoporow.cevolver.engine.Solver
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripLineModel

import scala.collection.mutable
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.concurrent.Task
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.VBox

/**
 * Główna klasa interfejsu użytkownika
 *
 * Data utworzenia: 23.10.15, 17:48
 * @author Michał Toporowski
 */
object CevolverUI extends JFXApp {
  val additionalParams: mutable.Map[ParamDef, Spinner[_ <: AnyVal]] = new mutable.HashMap[ParamDef, Spinner[_ <: AnyVal]]()
  val populationSizeCtrl = new Spinner[Int](10, 10000, 100, 1)
  val eliteCountCtrl = new Spinner[Int](10, 10000, 10, 1)
  val cfCtrl = createPartComboBox(CFType.values())
  val ssCtrl = createPartComboBox(SSType.values())
  val eoCtrl = createPartComboBox(EOType.values())
  val tcCtrl = createPartComboBox(TCType.values())
  val feCtrl = createPartComboBox(FEType.values())
  val dataFileBox = new FileBox("Plik danych", stage)
  val metaFileBox = new FileBox("Plik metadanych", stage)
  val expectedResLabel = new Label()
  val resultLabel = new Label()

  stage = new JFXApp.PrimaryStage {
    title.value = "Cevolver"
    width = 600
    height = 450
    scene = new Scene {
      content = new VBox() {
        children += dataFileBox
        children += metaFileBox
        children += createConfigPane()
        children += loadAdditionalParamsPane()
        children += createRunButton()
        children += expectedResLabel
        children += resultLabel
      }
    }
  }

  private def createConfigPane() = {
    val builder = new GridPaneBuilder(2)
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

      }
    }
  }

  private def loadAdditionalParamsPane() = {
    additionalParams.clear()
    val builder = new GridPaneBuilder(2)
    RegisteredParams.partsParamDefs.foreach {
      case (partType, paramDefs) => {
        // TODO:: dodać dynamiczne zmienianie tego
        for (paramDef <- paramDefs) {
          builder += new Label(paramDef.name)
          val spinner: Spinner[_ <: AnyVal] = if (paramDef.cls == classOf[Integer])
            new Spinner[Int](0, 1000, 0)
          else
            new Spinner[Double](0, 1000, 0, 0.5)
          spinner.editable = true
          builder += spinner
          additionalParams.put(paramDef, spinner)
        }
      }
    }
    builder.gridPane
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
      partParams.setParamValue(paramDef, additionalParams.get(paramDef).get.value.value)
    })
    partParams
  }

  private object RunAlgorithmTask extends Task(new concurrent.Task[Void]() {
    private var result: EvolutionaryAlgorithm.C = _
    private var expResult: EvolutionaryAlgorithm.C = _

    override def call(): Void = {
      val data = new TouchstoneDataProvider(dataFileBox.dataFile).provide
      val expectedDists = new EnvPropertiesReader(new FileInputStream(metaFileBox.dataFile)).getExpectedDistances
      expResult = new MicrostripLineModel(expectedDists, DataHolder.getCurrent.measurementParams.getMicrostripParams)
      result = new Solver().solve(paramValues(), data)
      null
    }

    // TODO
    override def succeeded(): Unit = {
      expectedResLabel.text = "Oczekiwany wynik: " + expResult
      resultLabel.text = "Wynik" + result.toString
    }
  })

}
