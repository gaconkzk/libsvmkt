package org.theflies.svm

import java.io.File
import java.io.Serializable
import kotlin.system.exitProcess

fun main(args: Array<String>) {
  val t = SvmTrain()
  t.run(args)
}

class SvmTrain {
  lateinit var param : SvmParameter
  lateinit var prob: SvmProblem    // set by read_problem
  lateinit var model: SvmModel
  lateinit var inputFileName: String    // set by parse_command_line
  lateinit var modelFileName: String    // set by parse_command_line
  lateinit var errorMsg: String
  var crossValidation: Int = 0
  var nrFold: Int = 0

  fun run(args: Array<String>) {
    parseArgs(args)
  }

  private fun parseArgs(args: Array<String>) {
    val print_func = System.out
    param = SvmParameter()

    val cmds = (args.indices step 2)
        .map { Pair(args[it], args[it+1]) }
        .filter { it.first.startsWith('-') }
        .map { it.copy(first = it.first.substring(1)) }
        .toList()

    cmds.forEach {
      when (it.first) {
        "s" ->
            param.copy(type = SvmType.valueOf(it.second))
        "t" ->
            param.copy(kernelType = KernelType.valueOf(it.second))
        else -> {
          print("Unknown option -${it.first} ${it.second}\n")
          exitProcess(1)
        }
      }
    }
  }
}

class SvmModel {

}

class SvmProblem {

}

data class SvmParameter(
    var type: SvmType = SvmType.C_SVC,
    var kernelType: KernelType = KernelType.RBF,
    var degree: Int = 3,  // for poly
    var gamma: Double = 0.0,  // for poly/rbf/sigmod
    var coef0: Double = 0.0,  // for poly/sigmod
    var cacheSize: Double = 100.0,  // in MB
    var eps: Double = 1e-3,  // stopping criteria
    var c: Double = 0.0,  // for C_SVC, EPSILON_SVR and NU_SVR
    var nrWeight: Int = 0,  // for C_SVC
    var weightLabel: List<Int> = listOf(),  // for C_SVC
    var weight: List<Double> = listOf(),  // for C_SVC
    var nu: Double = 0.5,  // for NU_SVC, ONE_CLASS, and NU_SVR
    var p: Double =0.1,  // for EPSILON_SVR
    var shrinking: Int = 1,  // use the shrinking heuristics
    var probability: Int = 0  // do probability estimates
): Serializable

enum class SvmType(val type: Int) {
  C_SVC(0),
  NU_SVC(1),
  ONE_CLASS(2),
  EPSILON_SVR(3),
  NU_SVR(4)
}

enum class KernelType(val type: Int) {
  LINEAR(0),
  POLY(1),
  RBF(2),
  SIGMOID(3),
  PRECOMPUTED(4)
}