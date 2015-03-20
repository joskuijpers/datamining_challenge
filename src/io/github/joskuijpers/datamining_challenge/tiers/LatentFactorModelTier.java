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
		float learningRate = 0.001f;

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

		/// Initialize matrices using the average of all the ratings ('non blanks')
		movieMatrix.init((float)(Math.sqrt(data.getMovieMean()) / (double)movieMatrix.getNumberOfRows()));	// X
		userMatrix.init((float)(Math.sqrt(data.getMovieMean()) / (double)userMatrix.getNumberOfColumns()));	// Y

		// Calculate predictions
		// MIN SUM  (r_xi -(mean + movieBias + userBias + q_i * p_x))^2
		// + (l1*SUM(q_i)^2 + l2*SUM(p_x)^2 + l3*SUM(b_x)^2 + l4*SUM(b_i)^2)

		
		/*
		 
		  Two matrices with all initialized to MU
		  Take 1 element from 1 matrix, call it X
		  Algebra the product of the matrices with the unknown
		  
		  
		 */
		
		for (int iter = 0; iter < 10; ++iter) {
			for (int f = 0; f < numFactors; ++f) {
				for (Rating rating : data.getRatingList()) {
					int i = rating.getMovie().getIndex() - 1; // movie
					int x = rating.getUser().getIndex() - 1; // user

					// Calculate the current rating in the matrices
					// r_xi - q_i * p_x
					float predictedRating = movieMatrix.getRow(i).dotproduct(
							userMatrix.getColumn(x));

					// TODO: add bias
					float error = rating.getRating() - predictedRating;
					if(!Float.isNaN(error))
						System.out.println("error " + error);

					// Update values
					float uv = userMatrix.elements[f][x];
					userMatrix.elements[f][x] += error
							* movieMatrix.elements[i][f];
					movieMatrix.elements[i][f] += error * uv;
				}

				// for(movie)
				// for user
				// real err = lrate * (rating - predictRating(movie, user)); =>
				// r_xi - q_i * p_x
				// userValue[user] += err * movieValue[movie]
				// movieValue[movie] ++ err * userValue[user]

			}
//			double error = rmse(inputMatrix, movieMatrix, userMatrix, data);
//			System.out.println("Error for iter " + iter + ": " + error);
		}


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
	private static double rmse(Matrix original, Matrix movieMatrix, Matrix userMatrix, TierData data) {
		if (original == null || movieMatrix == null || userMatrix == null)
			throw new IllegalArgumentException();

		// Create the test matrix
		Matrix testMatrix = movieMatrix.multiply(userMatrix);

		return rmse(original, testMatrix, data);
	}


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
