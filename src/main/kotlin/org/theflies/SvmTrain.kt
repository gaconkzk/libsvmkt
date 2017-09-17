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
            printHelp()
            exitProcess(1)
          }
        }
        "w" -> {
          val p = param.copy(nrWeight = param.nrWeight++)
          p.weightLabel[p.nrWeight - 1] = it.first.substring(2).toInt()
          p.weight[p.nrWeight - 1] = it.second.toDouble()
        }
        else -> {
          System.err.print("Unknown option -${it.first} ${it.second}\n")
          printHelp()
          exitProcess(1)
        }
      }
    }
  }

  private fun printHelp() {
    println("Usage: svm_train [options] training_set_file [model_file]\\n\"\n" +
        "\t\t+\"options:\\n\"\n" +
        "\t\t+\"-s svm_type : set type of SVM (default 0)\\n\"\n" +
        "\t\t+\"\t0 -- C-SVC\t\t(multi-class classification)\\n\"\n" +
        "\t\t+\"\t1 -- nu-SVC\t\t(multi-class classification)\\n\"\n" +
        "\t\t+\"\t2 -- one-class SVM\\n\"\n" +
        "\t\t+\"\t3 -- epsilon-SVR\t(regression)\\n\"\n" +
        "\t\t+\"\t4 -- nu-SVR\t\t(regression)\\n\"\n" +
        "\t\t+\"-t kernel_type : set type of kernel function (default 2)\\n\"\n" +
        "\t\t+\"\t0 -- linear: u'*v\\n\"\n" +
        "\t\t+\"\t1 -- polynomial: (gamma*u'*v + coef0)^degree\\n\"\n" +
        "\t\t+\"\t2 -- radial basis function: exp(-gamma*|u-v|^2)\\n\"\n" +
        "\t\t+\"\t3 -- sigmoid: tanh(gamma*u'*v + coef0)\\n\"\n" +
        "\t\t+\"\t4 -- precomputed kernel (kernel values in training_set_file)\\n\"\n" +
        "\t\t+\"-d degree : set degree in kernel function (default 3)\\n\"\n" +
        "\t\t+\"-g gamma : set gamma in kernel function (default 1/num_features)\\n\"\n" +
        "\t\t+\"-r coef0 : set coef0 in kernel function (default 0)\\n\"\n" +
        "\t\t+\"-c cost : set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)\\n\"\n" +
        "\t\t+\"-n nu : set the parameter nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)\\n\"\n" +
        "\t\t+\"-p epsilon : set the epsilon in loss function of epsilon-SVR (default 0.1)\\n\"\n" +
        "\t\t+\"-m cachesize : set cache memory size in MB (default 100)\\n\"\n" +
        "\t\t+\"-e epsilon : set tolerance of termination criterion (default 0.001)\\n\"\n" +
        "\t\t+\"-h shrinking : whether to use the shrinking heuristics, 0 or 1 (default 1)\\n\"\n" +
        "\t\t+\"-b probability_estimates : whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)\\n\"\n" +
        "\t\t+\"-wi weight : set the parameter C of class i to weight*C, for C-SVC (default 1)\\n\"\n" +
        "\t\t+\"-v n : n-fold cross validation mode\\n\"\n" +
        "\t\t+\"-q : quiet mode (no outputs)\\n")
  }
}