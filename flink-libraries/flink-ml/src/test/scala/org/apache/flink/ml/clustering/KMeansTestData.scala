package org.apache.flink.ml.clustering

import org.apache.flink.ml.common.LabeledVector
import org.apache.flink.ml.math.DenseVector


object KMeansTestData {

  val trainingData = Seq[LabeledVector](
    LabeledVector(1.0000, DenseVector(-4.14957419, -3.99819495)),
    LabeledVector(0.0000, DenseVector(-4.42723922, -3.67194584)),
    LabeledVector(1.0000, DenseVector(-3.88970919, -3.5415584)),
    LabeledVector(0.0000, DenseVector(-3.89218834, -4.11923194)),
    LabeledVector(1.0000, DenseVector(-4.0038261, -3.76499976)),
    LabeledVector(0.0000, DenseVector(-3.99169212, -4.22358509)),
    LabeledVector(1.0000, DenseVector(-4.21159044, -4.18180152)),
    LabeledVector(0.0000, DenseVector(-4.08335157, -4.01125337)),
    LabeledVector(1.0000, DenseVector(-4.35868712, -4.16834947)),
    LabeledVector(0.0000, DenseVector(-3.89942372, -4.24905762)),
    LabeledVector(1.0000, DenseVector(3.85042581, 4.00180505)),
    LabeledVector(0.0000, DenseVector(3.57276078, 4.32805416)),
    LabeledVector(1.0000, DenseVector(4.11029081, 4.4584416)),
    LabeledVector(0.0000, DenseVector(4.10781166, 3.88076806)),
    LabeledVector(1.0000, DenseVector(3.9961739, 4.23500024)),
    LabeledVector(0.0000, DenseVector(4.00830788, 3.77641491)),
    LabeledVector(1.0000, DenseVector(3.78840956, 3.81819848)),
    LabeledVector(0.0000, DenseVector(3.91664843, 3.98874663)),
    LabeledVector(1.0000, DenseVector(3.64131288, 3.83165053)),
    LabeledVector(0.0000, DenseVector(4.10057628, 3.75094238))
  )

  val expectedCentroids = Seq(DenseVector(4.0, 4.0), DenseVector(-4.0, -4.0))
}
