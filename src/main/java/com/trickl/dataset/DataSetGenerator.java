package com.trickl.dataset;

import cern.colt.matrix.DoubleMatrix2D;

public interface DataSetGenerator {

   DoubleMatrix2D generate(int n);
}
