package org.theflies

import org.theflies.svm.*
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
        "d" ->
          param.copy(degree = it.second.toInt())
        "g" ->
          param.copy(gamma = it.second.toDouble())
        "r" ->
          param.copy(coef0 = it.second.toDouble())
        "n" ->
          param.copy(nu = it.second.toDouble())
        "m" ->
          param.copy(cacheSize = it.second.toDouble())
        "c" ->
          param.copy(c = it.second.toDouble())
        "e" ->
          param.copy(eps = it.second.toDouble())
        "p" ->
          param.copy(p = it.second.toDouble())
        "h" ->
          param.copy(shrinking = it.second.toInt())
        "b" ->
          param.copy(probability = it.second.toInt())
        "v" -> {
          crossValidation = 1;
          nrFold = it.second.toInt()
          if (nrFold < 2) {
            System.err.print("n-fold cross validation: n must >= 2\n")
            exitWithHelp()
          }
        }
        "w" -> {
          val p = param.copy(nrWeight = param.nrWeight++)
          p.weightLabel[p.nrWeight - 1] = it.first.substring(2).toInt()
          p.weight[p.nrWeight - 1] = it.second.toDouble()
        }
        else -> {
          print("Unknown option -${it.first} ${it.second}\n")
          exitProcess(1)
        }
      }
    }
  }

  private fun exitWithHelp() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}