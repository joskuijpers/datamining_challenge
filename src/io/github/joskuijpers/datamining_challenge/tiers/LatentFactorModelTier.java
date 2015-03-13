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
		Matrix inputMatrix;

		// Create matrix for input data
		inputMatrix = new Matrix(data.getMovieList().size(), data.getUserList()
				.size());

		// For every rating in the rating list, set value in matrix
		for (Rating rating : data.getRatingList()) {
			inputMatrix.set(rating.getMovie().getIndex() - 1, rating.getUser()
					.getIndex() - 1, rating.getRating());
		}

		// Factorize into two matrices
		// Factors: age, profession, male/female (in Movie)

		return data;
	}

	/**
	 * Calculate the rating for a movie-user.
	 * 
	 * @param data
	 *            The tier data
	 * @param movieId
	 *            The movie ID
	 * @param userId
	 *            The user ID
	 * @return predicted rating
	 */
	public static float calculateRating(TierData data, int movieId, int userId) {
		Vector user = data.getFactorUserMatrix().getColumn(userId - 1);
		Vector movie = data.getMovieFactorMatrix().getRow(movieId - 1);

		return movie.dotproduct(user);
	}
}
