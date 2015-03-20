package io.github.joskuijpers.datamining_challenge.tiers;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.math.Vector;
import io.github.joskuijpers.datamining_challenge.model.Rating;

/**
 * A tier calculating making the neccesary predictions.
 *
 * @author joskuijpers
 */
@SuppressWarnings("unused")
public class PredictTier extends Tier {

	public static TierData run(TierData data) {
		return runLFMBiased(data);
//		return runBiases(data);
	}

	/**
	 * Calculate the predictions.
	 *
	 * Formula: Rating_mu = MovieMean + MoviaBias_m + UserBias_u
 	 *
	 * @param data
	 * @return
	 */
	private static TierData runBiases(TierData data) {
		for (Rating predRating : data.getPredRatings()) {
			float rating;

			// Calculate the rating
			rating = //predRating.getMovie().getAverageRating()
					data.getMovieMean()
					+ predRating.getMovie().getBias()
					+ predRating.getUser().getBias();

			// Clip the rating to 0.0 - 5.0
			rating = Math.max(Math.min(5.0f, rating), 0.0f);

			predRating.setRating(rating);
		}

		return data;
	}

	private static TierData runLFMBiased(TierData data) {
		for (Rating predRating : data.getPredRatings()) {
			float rating;

			// Calculate the rating
			int userIndex = predRating.getUser().getIndex() - 1;
			int movieIndex = predRating.getMovie().getIndex() - 1;

			Vector user = data.getFactorUserMatrix().getColumn(userIndex);
			Vector movie = data.getMovieFactorMatrix().getRow(movieIndex);

			// Calculate the rating
			rating = movie.dotproduct(user);

			// Clip the rating to 0.0 - 5.0
			rating = Math.max(Math.min(5.0f, rating), 0.0f);

			predRating.setRating(rating);
		}

		return data;
	}
}
