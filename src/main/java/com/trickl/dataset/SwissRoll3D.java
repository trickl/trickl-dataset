package com.trickl.dataset;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.Functions;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;

public class SwissRoll3D implements DataSetGenerator {
   
   private RandomEngine randomEngine;

   private DoubleMatrix1D normal;
   
   private double noiseStd = 0.1;

   private double decay = 1;

   private double radius = 1;

   private double revolutions = 2;

   private double depth = 2;

   public SwissRoll3D()
   {
      this.randomEngine = new MersenneTwister();
      this.normal = new DenseDoubleMatrix1D(new double[] {0, 0, 1});
   }

   @Override
   public DoubleMatrix2D generate(int n) {
      DoubleMatrix2D data = new DenseDoubleMatrix2D(n, 3);

      DoubleMatrix2D untransformedData = data.like(data.rows(), data.columns());

      // Planar random uniform distribution in 2D.
      Normal noiseDistribution = new Normal(0., noiseStd, randomEngine);
      Uniform depthDistribution = new Uniform(-getDepth() / 2, + getDepth() / 2, randomEngine);

      for (int i = 0; i < untransformedData.rows(); i++)
      {
         double l = ((double) i) / ((double) untransformedData.rows());
         double angle = 2.0 * Math.PI * getRevolutions() * l;
         double r = (getRadius() + noiseDistribution.nextDouble()) * Math.exp(-l * getDecay() * getRevolutions());


         untransformedData.setQuick(i, 0, r * Math.cos(angle));
         untransformedData.setQuick(i, 1, r * Math.sin(angle));
         untransformedData.setQuick(i, 2, getDepth() * depthDistribution.nextDouble());
      }

      // Normalise normal
      double normalSize = Math.sqrt(normal.aggregate(Functions.plus, Functions.square));
      normal.assign(Functions.div(normalSize));

      // Want to tranform system so that the z-axis becomes the specified normal.
      DoubleMatrix1D zAxis = new DenseDoubleMatrix1D(3);
      zAxis.setQuick(0, 0);
      zAxis.setQuick(1, 0);
      zAxis.setQuick(2, 1);

      // Find the rotation axis between z-axis and the normal and the angle of rotation
      DoubleMatrix1D axis = zAxis.copy();
      double angle = Math.acos(normal.zDotProduct(zAxis));
      double c = Math.cos(angle);
      double s = Math.sin(angle);

      if (angle > Double.MIN_VALUE)
      {
         // Use the cross product to calculate the rotation axis
         axis.setQuick(0, (normal.getQuick(1) * zAxis.getQuick(2)
                        - normal.getQuick(2) * zAxis.getQuick(1)) / s);
         axis.setQuick(1, (normal.getQuick(2) * zAxis.getQuick(0)
                        - normal.getQuick(0) * zAxis.getQuick(2)) / s);
         axis.setQuick(2, (normal.getQuick(0) * zAxis.getQuick(1)
                        - normal.getQuick(1) * zAxis.getQuick(0)) / s);
      }

      // Rotation about the axis
      DoubleMatrix2D transform = new DenseDoubleMatrix2D(3, 3);
      double nc = 1 - c;
      transform.setQuick(0, 0, nc * axis.getQuick(0) * axis.getQuick(0) + c);
      transform.setQuick(0, 1, nc * axis.getQuick(0) * axis.getQuick(1) - s * axis.getQuick(2));
      transform.setQuick(0, 2, nc * axis.getQuick(0) * axis.getQuick(2) + s * axis.getQuick(1));
      transform.setQuick(1, 0, nc * axis.getQuick(1) * axis.getQuick(0) + s * axis.getQuick(2));
      transform.setQuick(1, 1, nc * axis.getQuick(1) * axis.getQuick(1) + c);
      transform.setQuick(1, 2, nc * axis.getQuick(1) * axis.getQuick(2) - s * axis.getQuick(0));
      transform.setQuick(2, 0, nc * axis.getQuick(2) * axis.getQuick(0) - s * axis.getQuick(1));
      transform.setQuick(2, 1, nc * axis.getQuick(2) * axis.getQuick(1) + s * axis.getQuick(0));
      transform.setQuick(2, 2, nc * axis.getQuick(2) * axis.getQuick(2) + c);
      
      untransformedData.zMult(transform, data);

      return data;
   }

   public RandomEngine getRandomEngine() {
      return randomEngine;
   }

   public void setRandomEngine(RandomEngine randomEngine) {
      this.randomEngine = randomEngine;
   }

   public DoubleMatrix1D getNormal() {
      return normal;
   }

   public void setNormal(DoubleMatrix1D normal) {
      this.normal = normal;
   }

   public double getNoiseStd() {
      return noiseStd;
   }

   public void setNoiseStd(double noiseStd) {
      this.noiseStd = noiseStd;
   }

   public double getDecay() {
      return decay;
   }

   public void setDecay(double decay) {
      this.decay = decay;
   }

   public double getRadius() {
      return radius;
   }

   public void setRadius(double radius) {
      this.radius = radius;
   }

   public double getRevolutions() {
      return revolutions;
   }

   public void setRevolutions(double revolutions) {
      this.revolutions = revolutions;
   }

   public double getDepth() {
      return depth;
   }

   public void setDepth(double depth) {
      this.depth = depth;
   }
}
