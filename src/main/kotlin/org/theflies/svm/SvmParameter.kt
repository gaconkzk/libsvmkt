package org.theflies.svm

import java.io.Serializable

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
    var weightLabel: MutableList<Int> = mutableListOf(),  // for C_SVC
    var weight: MutableList<Double> = mutableListOf(),  // for C_SVC
    var nu: Double = 0.5,  // for NU_SVC, ONE_CLASS, and NU_SVR
    var p: Double = 0.1,  // for EPSILON_SVR
    var shrinking: Int = 1,  // use the shrinking heuristics
    var probability: Int = 0  // do probability estimates
) : Serializable

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