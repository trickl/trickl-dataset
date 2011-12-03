/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trickl.dataset;

import com.trickl.dataset.SpiralFlower2D;
import cern.colt.matrix.DoubleMatrix2D;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

public class SpiralFlowerTest {

   public SpiralFlowerTest() {
   }

   @Test
   public void testGetData() throws IOException {

      SpiralFlower2D spiralFlower = new SpiralFlower2D();
      DoubleMatrix2D data = spiralFlower.generate(1000);

      // Output to file (can be read by GNU Plot)
      String fileName = "spiral-flower.dat";
      String packagePath = this.getClass().getPackage().getName().replaceAll("\\.", "/");
      File outputFile = new File("src/test/resources/"
              + packagePath
              + "/" + fileName);
      PrintWriter writer = new PrintWriter(outputFile);

      writer.write(data.toString());
      writer.close();
   }
}
