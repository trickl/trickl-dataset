package com.trickl.dataset;

import com.trickl.dataset.SwissRoll3D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

public class SwissRollTest {

   public SwissRollTest() {
   }

   @Test
   public void testGetData() throws IOException {

      DoubleMatrix1D normal = new DenseDoubleMatrix1D(3);
      normal.assign(new double[]{1.0, 1.0, 1.0});

      SwissRoll3D swissRoll = new SwissRoll3D();
      DoubleMatrix2D data = swissRoll.generate(1000);

      // Output to file (can be read by GNU Plot)      
      String fileName = "swiss-roll.dat";
      String packagePath = this.getClass().getPackage().getName().replaceAll("\\.", "/");
      File outputFile = new File("src/test/resources/"
              + packagePath
              + "/" + fileName);
      PrintWriter writer = new PrintWriter(outputFile);
      writer.write(data.toString());
      writer.close();
   }
}
