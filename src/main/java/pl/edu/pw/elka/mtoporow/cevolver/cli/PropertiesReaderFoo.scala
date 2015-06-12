package pl.edu.pw.elka.mtoporow.cevolver.cli

import java.io.{IOException, InputStream}
import java.util.Properties

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param._
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{AlgorithmParameters, AlgorithmPartParams}

/**
 * Klasa odczytująca plik konfiguracji parametrów algorytmu
 * Data utworzenia: 31.05.15, 19:13
 * @author Michał Toporowski
 */
@Deprecated
class PropertiesReaderFoo(private val is: InputStream) {
//  private val properties = new Properties()
//  /**
//   * Odczytane parametry algorytmu
//   */
//  val parameters = new AlgorithmParameters
//
//  read()
//
//  /**
//   * Wczytuje plik
//   */
//  def read() = {
//    properties.load(is)
//    parameters.candidateFactory = readOne[CFType]("cf")
////    parameters.operators = List(readOne(classOf[EOType], "eo")) //FIXME:: chcemy obsługiwać > 1
////    parameters.fitnessEvaluator = readOne(classOf[FEType], "fe")
////    parameters.selectionStrategy = readOne(classOf[SSType], "ss")
////    parameters.terminationCondition = readOne(classOf[TCType], "tc")
//  }
//
//  private def readOne[T](propName: String): AlgorithmPartParams[T] = {
//    val propVal = getRequiredProperty(propName)
//    val partParams = new AlgorithmPartParams[T]
//    // Odczytujemy typ części algorytmu
//    partParams.partType = Enum.valueOf(classOf[T], propVal)
//    // Odczytujemy informację nt. wszystkich parametrów danej części
//    val partParamDefs = RegisteredParams.partsParamDefs.get(partParams.partType)
//    for (partParamDef <- partParamDefs) {
//      val parVal = getRequiredProperty(propName + "." + partParamDef.name)
//      partParams.setParamValAsString(partParamDef, parVal)
//    }
//    partParams
//
//  }
//
//  private def getRequiredProperty(propName: String) = {
//    val propVal = properties.getProperty(propName)
//    if (propVal == null) {
//      throw new IOException("Missing property: " + propName)
//    }
//    propVal
//  }
}
