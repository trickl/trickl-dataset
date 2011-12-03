/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trickl.dataset;

import com.trickl.dataset.GaussianCircles2D;
import cern.colt.matrix.DoubleMatrix2D;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

public class GaussianCirclesTest {

   public GaussianCirclesTest() {
   }

   @Test
   public void testGetData() throws IOException {

      GaussianCircles2D gaussianCircles = new GaussianCircles2D();
      gaussianCircles.setRadiusStd(0.20);
      DoubleMatrix2D data = gaussianCircles.generate(1000);

      // Output to file (can be read by GNU Plot)
      String fileName = "gaussian-circles.dat";
      String packagePath = this.getClass().getPackage().getName().replaceAll("\\.", "/");
      File outputFile = new File("src/test/resources/"
              + packagePath
              + "/" + fileName);
      PrintWriter writer = new PrintWriter(outputFile);

      writer.write(data.toString());
      writer.close();
   }
}
