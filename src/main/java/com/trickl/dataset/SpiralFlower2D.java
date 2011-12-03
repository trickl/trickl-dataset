/*
 * This file is part of the Trickl Open Source Libraries.
 *
 * Trickl Open Source Libraries - http://open.trickl.com/
 *
 * Copyright (C) 2011 Tim Gee.
 *
 * Trickl Open Source Libraries are free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Trickl Open Source Libraries are distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this project.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.trickl.dataset;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.Normal;
import cern.jet.random.engine.MersenneTwister;

public class SpiralFlower2D implements DataSetGenerator {

   private RandomEngine randomEngine;

   private double noiseStd = 0.05;

   private int arms = 5;

   private double radius = 1;

   private double armRotation = Math.PI * 0.75;

   public SpiralFlower2D()
   {
      this.randomEngine = new MersenneTwister();
   }

   // Gaussian circular 2D distribution, with random cluster centres and std dev
   @Override
   public DoubleMatrix2D generate(int n) {
      DoubleMatrix2D data = new DenseDoubleMatrix2D(n, 2);
     
      Normal noiseDistribution = new Normal(0., noiseStd, randomEngine);

      for (int i = 0; i < data.rows(); i++) {
         int arm = i % arms;

         double angle = (2 * Math.PI * (double) arm / (double) arms)
                 + armRotation * (double) i / (double) data.rows();

         double r = radius * (double) i / (double) data.rows();

         data.setQuick(i, 0, r * Math.cos(angle) + noiseDistribution.nextDouble());
         data.setQuick(i, 1, r * Math.sin(angle) + noiseDistribution.nextDouble());
      }

      return data;
   }

   public RandomEngine getRandomEngine() {
      return randomEngine;
   }

   public void setRandomEngine(RandomEngine randomEngine) {
      this.randomEngine = randomEngine;
   }

   public double getRadiusStd() {
      return noiseStd;
   }

   public void setRadiusStd(double radiusStd) {
      this.noiseStd = radiusStd;
   }

   public double getNoiseStd() {
      return noiseStd;
   }

   public void setNoiseStd(double noiseStd) {
      this.noiseStd = noiseStd;
   }

   public int getArms() {
      return arms;
   }

   public void setArms(int arms) {
      this.arms = arms;
   }

   public double getRadius() {
      return radius;
   }

   public void setRadius(double radius) {
      this.radius = radius;
   }

   public double getArmRotation() {
      return armRotation;
   }

   public void setArmRotation(double armRotation) {
      this.armRotation = armRotation;
   }
}
