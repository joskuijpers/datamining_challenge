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
		int numFactors = 3;

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
		movieMatrix.init(data.getMovieMean());
		userMatrix.init(data.getMovieMean());
		
		// Factors: age, profession, male/female (in Movie)
		// MIN SUM  (r_xi -(mean + movieBias + userBias + q_i * p_x))^2
		// + (l1*SUM(q_i)^2 + l2*SUM(p_x)^2 + l3*SUM(b_x)^2 + l4*SUM(b_i)^2)

		return data;
	}
}
