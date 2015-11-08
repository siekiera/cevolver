package pl.edu.pw.elka.mtoporow.cevolver.algorithm

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.{AlgorithmPartType, RegisteredParams}

/**
 * Klasa opisująca parametry części algorytmu
 *
 * @param partType typ częśći algorytmu
 * @tparam T
 */
class AlgorithmPartParams[T <: AlgorithmPartType](val partType: T) {
  private val params: Array[Any] = new Array[Any](partsParamsDefs.length)

  /**
   * Zwraca wartość danego parametru
   *
   * @param paramDef
   * @return
   */
  def paramValue(paramDef: RegisteredParams.ParamDef): Any = {
    val index = partsParamsDefs.indexOf(paramDef)
    if (index >= 0) params(index) else null
  }

  /**
   * Zwraca wartość parametru zrzutowaną na typ V
   *
   * @param paramDef
   * @tparam V
   * @return
   */
  def paramValueCasted[V](paramDef: RegisteredParams.ParamDef): V = paramValue(paramDef).asInstanceOf[V]

  /**
   * Ustawia wartość parametru
   * @param paramDef
   * @param value
   */
  def setParamValue(paramDef: RegisteredParams.ParamDef, value: Any): Unit = {
    // TODO:: dodać obsługę typów, żeby nie można było czegokolwiek ustawić
    // TODO 2:: dodać sensowną obsługę błędnych/brakujących parametrów
    val index = partsParamsDefs.indexOf(paramDef)
    if (index >= 0) {
      params(index) = value
    }
  }

  /**
   * Ustawia wartość parametru.
   * Wartość typu String jest konwertowana do liczbowej, jeśli zdefiniowano sposób konwersji
   *
   * @param paramDef
   * @param strValue
   */
  def setStringAsParamVal(paramDef: RegisteredParams.ParamDef, strValue: String): Unit = {
    val value = paramDef.cls match {
      case q if q == classOf[Integer] => strValue.toInt
      case q if q == classOf[java.lang.Double] => strValue.toDouble
      case _ => strValue
    }
    setParamValue(paramDef, value)
  }

  /**
   * Pobiera definicje parametrów dla danego typu części algorytmu
   *
   * @return
   */
  def partsParamsDefs = RegisteredParams.partsParamDefs.getOrElse(partType, Array[RegisteredParams.ParamDef]())
}
