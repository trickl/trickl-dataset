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
