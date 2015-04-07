package io.github.joskuijpers.datamining_challenge;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class MatUtils {
	/**
	 * Write a RealMatrix to file.
	 * 
	 * The file is in binary format. All entries are saved as a double in
	 * row-column order, preceded by two integers for the rowdimension and
	 * columndimension of the matrix.
	 * 
	 * @param mat The matrix to store.
	 * @param filename Name of the file to store in
	 */
 	public static void writeMatrix(RealMatrix mat, String filename) {
		DataOutputStream out;
		
		try {
			double data[][];
			int m, n;
			
			out = new DataOutputStream(new FileOutputStream(filename));
			
			data = mat.getData();
			
			// Add M and N
			m = data.length;
			n = data[0].length;
			
			out.writeInt(m);
			out.writeInt(n);
			
			// Add all data
			for(int i = 0; i < m; ++i) {
				for(int j = 0; j < n; ++j)
					out.writeDouble(data[i][j]);
			}
			
			out.close();
		} catch(IOException ioe) {
			System.out.println("Could not write matrix to file.");
			ioe.printStackTrace();
		}
		
		System.out.println("Wrote matrix, of "+mat.getRowDimension() + " x "+mat.getColumnDimension());
	}
	
	/**
	 * Read a RealMatrix from file.
	 * @param filename Name of the file to read from.
	 * @return
	 */
 	public static RealMatrix readMatrix(String filename) {
		DataInputStream in;
		RealMatrix mat = null;
		
		try {
			int m, n;
			double data[][];
			
			in = new DataInputStream(new FileInputStream(filename));
			
			// Read M and N
			m = in.readInt();
			n = in.readInt();
			
			data = new double[m][n];
			
			for(int i = 0; i < m; ++i) {
				for(int j = 0; j < n; ++j)
					data[i][j] = in.readDouble();
			}
			
			mat = new BlockRealMatrix(data);
			
			in.close();
		} catch(IOException ioe) {
			System.out.println("Could not read matrix from file.");
			ioe.printStackTrace();
		}
		
		System.out.println("Read matrix, of "+mat.getRowDimension() + " x "+mat.getColumnDimension());
		
		return mat;
	}
}
