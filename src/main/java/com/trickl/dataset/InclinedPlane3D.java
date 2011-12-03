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

import java.awt.Rectangle;

public class InclinedPlane3D implements DataSetGenerator {
   
   private RandomEngine randomEngine;

   private DoubleMatrix1D normal;

   private Rectangle bounds;

   private double noiseStd = 0.1;

   public InclinedPlane3D()
   {
      this.randomEngine = new MersenneTwister();
      this.normal = new DenseDoubleMatrix1D(new double[] {0, 0, 1});
      this.bounds = new Rectangle(-1, -1, 2, 2);
   }

   @Override
   public DoubleMatrix2D generate(int n) {
      DoubleMatrix2D data = new DenseDoubleMatrix2D(n, 3);

      DoubleMatrix2D untransformedData = data.like(data.rows(), data.columns());

      // Planar random uniform distribution in 2D.
      Normal noiseDistribution = new Normal(0., noiseStd, randomEngine);
      Uniform xDistribution = new Uniform(bounds.x, bounds.x + bounds.width, randomEngine);
      Uniform yDistribution = new Uniform(bounds.y, bounds.y + bounds.height, randomEngine);

      for (int i = 0; i < untransformedData.rows(); i++)
      {
         untransformedData.setQuick(i, 0, xDistribution.nextDouble());
         untransformedData.setQuick(i, 1, yDistribution.nextDouble());
         untransformedData.setQuick(i, 2, noiseDistribution.nextDouble());
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

   public Rectangle getBounds() {
      return bounds;
   }

   public void setBounds(Rectangle bounds) {
      this.bounds = bounds;
   }

   public double getNoiseStd() {
      return noiseStd;
   }

   public void setNoiseStd(double noiseStd) {
      this.noiseStd = noiseStd;
   }
}
