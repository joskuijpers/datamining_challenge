package io.github.joskuijpers.datamining_challenge.tiers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.JFrame;

import org.apache.commons.math3.linear.*;
import org.jfree.chart.*;
import org.jfree.data.category.DefaultCategoryDataset;

import io.github.joskuijpers.datamining_challenge.TierData;

/**
 * A tier utilizing latent factor models to make predictions
 * 
 * @author joskuijpers
 */
public class LatentFactorModelTier extends Tier {
	/**
	 * Create the factorized matrices
	 * 
	 * @param data
	 *            the tier data
	 * @return the tier data
	 */
	public static TierData run(TierData data) {
		Scanner console;
		boolean question = false;
		RealMatrix movieFeatureMatrix = null, featureUserMatrix = null;
		
		console = new Scanner(System.in);
		
		// Ask if load from file
		System.out.println("Do you want to load the movie and user feature matrix from file?");
		try {
			question = Boolean.parseBoolean(console.nextLine());
		} catch(InputMismatchException e) {
			System.out.println("Did not understand your answer. Assuming NO.");
			question = false;
		}
		
		if(question) {
			System.out.println("Trying to load files form current working directory...");
			
			// Load from file
			movieFeatureMatrix = readMatrix("MoviesFeaturesMatrix.mat");
			featureUserMatrix = readMatrix("FeaturesUsersMatrix.mat");
			
			System.out.println("Loaded matrices from files.");
		} else {
			SingularValueDecomposition svd;
			RealMatrix inputMatrix;
			RealMatrix subU, subS, subV;
			double singularValues[];
			int numFeatures = 0;
			
			// Clone the input matrix
			inputMatrix = data.getInputMatrix().copy();
			
			// Fill empty spots with guessed data
			inputMatrix.walkInColumnOrder(new RealMatrixChangingVisitor() {
				@Override
				public double end() { return 0; }

				@Override
				public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {}

				@Override
				public double visit(int row, int column, double value) {
					if(value != 0)
						return value;
					
					return data.getMovieMean();
				}
			});
			
			// Build the SVD
			System.out.println("Starting SVD at "+(new Date()));
			svd = new SingularValueDecomposition(data.getInputMatrix());
			System.out.println("Finished SVD at "+(new Date()));
			
			System.out.println("Built 3 matrices. Num features: " + svd.getS().getColumnDimension());
			System.out.println(
					  "U: "+svd.getU().getRowDimension()+"x"+svd.getU().getColumnDimension()
					+" S: "+svd.getS().getRowDimension()+"x"+svd.getS().getColumnDimension()
					+" V: "+svd.getV().getRowDimension()+"x"+svd.getV().getColumnDimension());
			
			System.out.println("First 10 values of U and V");
			for(int i = 0; i < 10; ++i)
				System.out.print(svd.getU().getEntry(0, i)+" ");
			System.out.println();
			
			for(int i = 0; i < 10; ++i)
				System.out.print(svd.getV().getEntry(0, i)+" ");
			System.out.println();
			
			// Get the Sigma diagonal values
			singularValues = svd.getSingularValues();
			
			// Plot values
			JFrame chart = plotSingularValues(singularValues);
			
			// Ask for number of features to keep
			System.out.println("How many features do you want to be kept?");
			
			try {
				numFeatures = Integer.parseInt(console.nextLine());
			} catch(InputMismatchException e) {
				System.out.println("Did not understand your answer. Assuming ALL.");
				numFeatures = singularValues.length;
			}
			chart.dispose();
			
			// A = m x n = U x S x VT
			// U = m x p
			// S = p x p
			// V = p x n (VT = n x p), p = min(m,n)

			System.out.println("Writing USV matrices to files, to be safe.");
			writeMatrix(svd.getU(), "u.mat");
			writeMatrix(svd.getS(), "s.mat");
			writeMatrix(svd.getV(), "v.mat");
			System.out.println("Done.");
			
			// Make subspaces, removing features
			subU = svd.getU().getSubMatrix(0, svd.getU().getRowDimension()-1, 0, numFeatures-1);
			subS = svd.getS().getSubMatrix(0, numFeatures-1, 0, numFeatures-1);
			subV = svd.getV().getSubMatrix(0, numFeatures-1, 0, svd.getV().getColumnDimension()-1);
			
			// Fold Sigma matrix into U matrix
			movieFeatureMatrix = subU.multiply(subS); // m x p * p x p = m * p
			featureUserMatrix = subV;
			
			// Ask to save to file
			System.out.println("Do you want to save the movie and user feature matrix to file?");
			
			try {
				question = Boolean.parseBoolean(console.nextLine());
			} catch(InputMismatchException e) {
				System.out.println("Did not understand your answer. Assuming NO.");
				question = false;
			}
			
			if(question) {
				System.out.println("Writing movieFeature matrix to file...");
				writeMatrix(movieFeatureMatrix,"MoviesFeaturesMatrix.mat");
				
				System.out.println("Writing featureUser matrix to file...");
				writeMatrix(featureUserMatrix,"FeaturesUsersMatrix.mat");
			}
		}		
		
		// Store in tierData
		data.setMovieFeatureMatrix(movieFeatureMatrix);
		data.setFeatureUserMatrix(featureUserMatrix);
		
		// Calculate predictions
		// MIN SUM (r_xi -(mean + movieBias + userBias + q_i * p_x))^2
		// + (l1*SUM(q_i)^2 + l2*SUM(p_x)^2 + l3*SUM(b_x)^2 + l4*SUM(b_i)^2)
		
		console.close();
		
		return data;
	}
	
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
	private static void writeMatrix(RealMatrix mat, String filename) {
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
	private static RealMatrix readMatrix(String filename) {
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
	
	/**
	 * Plot the singular values in order to find the knee.
	 * 
	 * @param singularValues array of singular values
	 * @return the frame so it can be disposed of.
	 */
	private static JFrame plotSingularValues(double[] singularValues) {
		JFreeChart chart;
		ChartFrame frame;
	
		// Create dataset
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(int i = 0; i < singularValues.length; ++i) {
			if(i % (singularValues.length / 25) == 0)
				dataset.addValue(singularValues[i], "sv", i+"");
		}
		
		// Create chart
		chart = ChartFactory.createLineChart("Singular Values", "Feature", "Influence", dataset);//, PlotOrientation.HORIZONTAL, true, false, false);
		
		// Into a frame
		frame = new ChartFrame("Singular Values", chart);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		return frame;
	}
	
	/**
	 * Computes the RMSE of this matrix, to the specified matrix,
	 * using the SSE and the number of filled values.
	 * 
	 * This assumes a matrix with original ratings from 1-5, where
	 * 0 dictates 'no rating'.
	 * 
	 * TODO: ADD BIASES
	 * 
	 * @param original
	 *            the original matrix to compare to
	 * @return the sum
	 */
	private static double rmse(RealMatrix original, RealMatrix movieMatrix, RealMatrix userMatrix, TierData data) {
		if (original == null || movieMatrix == null || userMatrix == null)
			throw new IllegalArgumentException();
		
		// Create the test matrix
		RealMatrix testMatrix = movieMatrix.multiply(userMatrix);
		
		return rmse(original, testMatrix, data);
	}
	
	public static double rmse(RealMatrix original, RealMatrix toTest, TierData data) {
		if (original == null || toTest == null
				|| original.getRowDimension() != toTest.getRowDimension()
				|| original.getColumnDimension() != toTest.getRowDimension())
			throw new IllegalArgumentException();
		
		double sse = 0.0;
		int numFilled = 0;
		
		for (int i = 0; i < original.getRowDimension(); ++i) {
			for (int j = 0; j < original.getColumnDimension(); ++j) {
				// Skip where in original matrix there is no rating
				if (original.getEntry(i,j) == 0)
					continue;
				
				double x = original.getEntry(i,j) - toTest.getEntry(i,j);
				
				sse += x * x;
				++numFilled;
			}
		}
		
		return Math.sqrt(sse / (double) numFilled);
	}
}
