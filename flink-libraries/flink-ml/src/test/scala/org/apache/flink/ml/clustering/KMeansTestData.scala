package org.apache.flink.ml.clustering

import org.apache.flink.ml.common.LabeledVector
import org.apache.flink.ml.math.DenseVector


object KMeansTestData {

  val trainingData = Seq[LabeledVector](
    LabeledVector(0.0000, DenseVector(0.0,0.0)),
    LabeledVector(0.0000, DenseVector(1,0)),
    LabeledVector(0.0000, DenseVector(1,1)),
    LabeledVector(0.0000, DenseVector(0,1)),
    LabeledVector(1.0000, DenseVector(0.0,-4.0)),
    LabeledVector(1.0000, DenseVector(1,-4)),
    LabeledVector(1.0000, DenseVector(1,-3)),
    LabeledVector(1.0000, DenseVector(0,-3))
  )

  val expectedCentroids = Seq(DenseVector(0.5,0.5),DenseVector(0.5,-3.5))
}
