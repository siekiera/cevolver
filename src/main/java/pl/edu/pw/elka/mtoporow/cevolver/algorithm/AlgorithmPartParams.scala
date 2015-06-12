package pl.edu.pw.elka.mtoporow.cevolver.algorithm

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.{AlgorithmPartType, RegisteredParams}

/**
 * Klasa opisująća parametry części algorytmu
 *
 * @tparam T
 */
class AlgorithmPartParams[T <: AlgorithmPartType](val partType: T) {
  private val params: Array[Any] = new Array[Any](partsParamsDefs.length)

  def paramValue(paramDef: RegisteredParams.ParamDef): Any = {
    val index = partsParamsDefs.indexOf(paramDef)
    if (index >= 0) params(index) else null
  }

  def paramValueCasted[V](paramDef: RegisteredParams.ParamDef): V = paramValue(paramDef).asInstanceOf[V]

  def setParamValue(paramDef: RegisteredParams.ParamDef, value: Any): Unit = {
    // TODO:: dodać obsługę typów, żeby nie można było czegokolwiek ustawić
    // TODO 2:: dodać sensowną obsługę błędnych/brakujących parametrów
    val index = partsParamsDefs.indexOf(paramDef)
    if (index >= 0) {
      params(index) = value
    }
  }

  def setParamValAsString(paramDef: RegisteredParams.ParamDef, strValue: String): Unit = {
    val value = paramDef.cls match {
      case q if q == classOf[Integer] => strValue.toInt
      case q if q == classOf[java.lang.Double] => strValue.toDouble
      case _ => strValue
    }
    setParamValue(paramDef, value)
  }

  def partsParamsDefs = Option(RegisteredParams.partsParamDefs.get(partType)).getOrElse(Array[RegisteredParams.ParamDef]())
}
