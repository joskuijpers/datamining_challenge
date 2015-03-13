package io.github.joskuijpers.datamining_challenge.tiers;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.math.*;
import io.github.joskuijpers.datamining_challenge.model.*;

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
		Matrix inputMatrix, movieMatrix, userMatrix;
		int numFactors = 10;

		// Create matrix for input data
		inputMatrix = new Matrix(data.getMovieList().size(), data.getUserList()
				.size());

		// For every rating in the rating list, set value in matrix
		for (Rating rating : data.getRatingList()) {
			inputMatrix.set(rating.getMovie().getIndex() - 1, rating.getUser()
					.getIndex() - 1, rating.getRating());
		}

		// Factorize into two matrices
		movieMatrix = new Matrix(inputMatrix.getNumberOfRows(),numFactors);
		userMatrix = new Matrix(numFactors,inputMatrix.getNumberOfColumns());

		// Store in tier data
		data.setFactorUserMatrix(userMatrix);
		data.setMovieFactorMatrix(movieMatrix);

		// Initialize matrices using the average of all the ratings ('non blanks')

		/////// Singular Value Decomposition => Alternating least squares

		movieMatrix.init(data.getMovieMean());
		userMatrix.init(data.getMovieMean());
		// this.svd(inputMatrix, movieMatrix, userMatrix, numFactors);


		// Calculate predictions
		// MIN SUM  (r_xi -(mean + movieBias + userBias + q_i * p_x))^2
		// + (l1*SUM(q_i)^2 + l2*SUM(p_x)^2 + l3*SUM(b_x)^2 + l4*SUM(b_i)^2)

		return data;
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
	 * @param original the original matrix to compare to
	 * @return the sum
	 */
	public static double rmse(Matrix original, Matrix toTest, TierData data) {
		if (original == null || toTest == null
				|| original.n != toTest.n || original.m != toTest.m)
			throw new IllegalArgumentException();

		double sse = 0.0;
		int numFilled = 0;

		for (int i = 0; i < original.m; ++i) {
			for (int j = 0; j < original.n; ++j) {
				// Skip where in original matrix there is no rating
				if(original.elements[i][j] == 0)
					continue;

				float x = original.elements[i][j] - toTest.elements[i][j];

				sse += x*x;
				++numFilled;
			}
		}

		return Math.sqrt(sse/(double)numFilled);
	}
}
