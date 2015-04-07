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
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.model.Rating;

/**
 * A tier utilizing latent factor models to make predictions
 * 
 * @author joskuijpers
 */
public class LatentFactorModelTier extends Tier {
	// Parameters
	private final static int MAX_FEATURES = 20;
	private final static int MIN_EPOCHS = 40;
	private final static double LRATE = 0.0035; //learning rate 		0.001
	private final static double K = 0.02; // regularization 		0.015
	private final static double MIN_IMPROV = 0.0005; // minimal improvement to continue	0.0001
	private final static double FINIT = 0.1; // initial feature value
	
	// Best till now: 20, 40, 0.0035, 0.02, 0.0005, 0.1
	
	// chart
	private static JFreeChart rmseChart;
	private static ChartFrame rmseFrame;
	private static XYSeries rmseSerie;
	
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
		/*System.out.println("Do you want to load the movie and user feature matrix from file?");
		try {
			question = Boolean.parseBoolean(console.nextLine());
		} catch(InputMismatchException e) {
			System.out.println("Did not understand your answer. Assuming NO.");
			question = false;
		}*/
		
		if(question) {
			System.out.println("Trying to load files form current working directory...");
			
			// Load from file
			movieFeatureMatrix = readMatrix("MoviesFeaturesMatrix.mat");
			featureUserMatrix = readMatrix("FeaturesUsersMatrix.mat");
			
			System.out.println("Loaded matrices from files.");
		} else {
			double rmse = 1.0, rmseLast = 0.0;
			int counter = 0;
			
			// Create matrices
			movieFeatureMatrix = new BlockRealMatrix(data.getMovieList().size(),MAX_FEATURES); // [m][f]
			featureUserMatrix = new BlockRealMatrix(MAX_FEATURES,data.getUserList().size()); // [f][u]
			
			// Initialize by adding intial value to all entries (are 0)
			movieFeatureMatrix = movieFeatureMatrix.scalarAdd(FINIT);
			featureUserMatrix = featureUserMatrix.scalarAdd(FINIT);
			
			// Create plot to see what happens
			plotRMSE();
			rmseSerie.add(0,rmse);
			
			// For each feature
			System.out.println("Starting SVD at "+(new Date()));
			for(int f = 0; f < MAX_FEATURES; ++f) {
				System.out.println("Calculating feature "+f);
				
				// Run a minimal number of epochs. Stop when max number of epochs have ran
				// or no real progress is being made
				for (int epoch = 0; epoch < MIN_EPOCHS
						|| (rmse <= rmseLast - MIN_IMPROV); ++epoch) {
					double squareSum = 0.0;

					++counter;
					rmseLast = rmse; // for verify improvement

					//http://buzzard.ups.edu/courses/2014spring/420projects/math420-UPS-spring-2014-gower-netflix-SVD.pdf
					for (Rating rating : data.getRatingList()) {
						double prediction, error, predictedInteraction;
						double mf, uf, movieChange, userChange;
						int userId, movieId;
						
						// Skip ignored ratings
						if(rating.isIgnored())
							continue;

						movieId = rating.getMovie().getIndex() - 1;
						userId = rating.getUser().getIndex() - 1;

						// Calculate current predicted value
						predictedInteraction = movieFeatureMatrix.getRowVector(movieId)
								.dotProduct(featureUserMatrix.getColumnVector(userId));
						
						prediction = data.getMovieMean() + rating.getUser().getBias() + rating.getMovie().getBias() + predictedInteraction;
						
						if(prediction > 5.0)
							prediction = 5.0;
						else if(prediction < 1.0)
							prediction = 1.0;

						// MIN (r_xi - (u + b_x + b_i + q_i*p_x) + ()
						error = (rating.getRating() - prediction);
						squareSum += error * error;

						// Get current values
						mf = movieFeatureMatrix.getEntry(movieId, f);
						uf = featureUserMatrix.getEntry(f, userId);

						// Update the feature with regularization and cross training (SGD)
						movieChange = LRATE * (error * uf - K * mf);
						userChange = LRATE * (error * mf - K * uf);

						// Calculate new values
						movieFeatureMatrix.setEntry(movieId, f, mf + movieChange);
						featureUserMatrix.setEntry(f, userId, uf + userChange);
					}

					// Calculate the new rmse
					rmse = Math.sqrt(squareSum / (double) data.getRatingList().getSizeNonIgnored());
				}
				
				// Plot for a nice view of change
				rmseSerie.add(f+1,rmse);
			}
			System.out.println("Finished SVD at "+(new Date()));

			// Ask to save to file
			System.out.println("Do you want to save the movie and user feature matrix to file?");
			
			/*try {
				question = Boolean.parseBoolean(console.nextLine());
			} catch(InputMismatchException e) {
				System.out.println("Did not understand your answer. Assuming NO.");
				question = false;
			}*/
			
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
	
	private static JFrame plotRMSE() {
		if(rmseFrame == null) {
			XYSeriesCollection collection = new XYSeriesCollection();
			rmseSerie = new XYSeries("svd");
				
			collection.addSeries(rmseSerie);
			
			// Create chart
			rmseChart = ChartFactory.createScatterPlot("LFM RMSE", "Features", "RMSE", collection);
			
			// Into a frame
			rmseFrame = new ChartFrame("Singular Values", rmseChart);
			rmseFrame.pack();
			rmseFrame.setLocationRelativeTo(null);
			rmseFrame.setVisible(true);
		}
		
		return rmseFrame;
	}
}
