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
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;

import java.awt.Rectangle;

public class GaussianCircles2D implements DataSetGenerator {

   private RandomEngine randomEngine;

   private Rectangle prototypeBounds;

   private int clusters = 3;

   private double radiusStd = 0.2;

   public GaussianCircles2D()
   {
      this.randomEngine = new MersenneTwister();
      this.prototypeBounds = new Rectangle(-1, -1, 2, 2);
   }

   // Gaussian circular 2D distribution, with random cluster centres and std dev
   @Override
   public DoubleMatrix2D generate(int n) {
      DoubleMatrix2D data = new DenseDoubleMatrix2D(n, 2);

      DoubleMatrix2D prototypes = new DenseDoubleMatrix2D(clusters, 2);
      Uniform xDistribution = new Uniform(prototypeBounds.x, prototypeBounds.x + prototypeBounds.width, randomEngine);
      Uniform yDistribution = new Uniform(prototypeBounds.y, prototypeBounds.y + prototypeBounds.height, randomEngine);
      for (int i = 0; i < clusters; i++) {
         double x = xDistribution.nextDouble();
         double y = yDistribution.nextDouble();
         prototypes.setQuick(i, 0, x);
         prototypes.setQuick(i, 1, y);
      }

      Normal radialDistribution = new Normal(0., radiusStd, randomEngine);
      Uniform angleDistribution = new Uniform(0, 2 * Math.PI, randomEngine);
      Uniform clusterDistribution = new Uniform(randomEngine);

      for (int i = 0; i < data.rows(); i++) {
         // Choose a cluster randomly
         int j = clusterDistribution.nextIntFromTo(0, clusters - 1);
         double radius = radialDistribution.nextDouble();
         double angle = angleDistribution.nextDouble();

         data.setQuick(i, 0, prototypes.getQuick(j, 0) + radius * Math.cos(angle));
         data.setQuick(i, 1, prototypes.getQuick(j, 1) + radius * Math.sin(angle));
      }

      return data;
   }

   public RandomEngine getRandomEngine() {
      return randomEngine;
   }

   public void setRandomEngine(RandomEngine randomEngine) {
      this.randomEngine = randomEngine;
   }

   public Rectangle getPrototypeBounds() {
      return prototypeBounds;
   }

   public void setPrototypeBounds(Rectangle prototypeBounds) {
      this.prototypeBounds = prototypeBounds;
   }

   public int getClusters() {
      return clusters;
   }

   public void setClusters(int clusters) {
      this.clusters = clusters;
   }

   public double getRadiusStd() {
      return radiusStd;
   }

   public void setRadiusStd(double radiusStd) {
      this.radiusStd = radiusStd;
   }
}
