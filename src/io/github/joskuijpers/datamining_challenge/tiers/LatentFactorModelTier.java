package io.github.joskuijpers.datamining_challenge.tiers;

import org.apache.commons.math3.linear.*;

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
		SingularValueDecomposition svd;
		
		svd = new SingularValueDecomposition(data.getInputMatrix());
		
		System.out.println("Built 3 matrices. Num features: " + svd.getS().getColumnDimension());
		
		// Calculate predictions
		// MIN SUM (r_xi -(mean + movieBias + userBias + q_i * p_x))^2
		// + (l1*SUM(q_i)^2 + l2*SUM(p_x)^2 + l3*SUM(b_x)^2 + l4*SUM(b_i)^2)
		
		return data;
	}
	
	public static double getPrediction(TierData data, int movie, int user) {
		
		
		return 0.0;
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
