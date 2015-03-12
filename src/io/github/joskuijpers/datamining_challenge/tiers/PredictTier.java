package io.github.joskuijpers.datamining_challenge.tiers;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.model.Rating;

/**
 * A tier calculating making the neccesary predictions.
 * 
 * @author joskuijpers
 */
public class PredictTier extends Tier {

	/**
	 * Calculate the predictions.
	 * 
	 * Formula: Rating_mu = MovieMean + MoviaBias_m + UserBias_u
 	 *  
	 * @param data
	 * @return
	 */
	public static TierData run(TierData data) {

		for (Rating predRating : data.getPredRatings()) {
			Double rating;

			// Calculate the rating
			rating = //predRating.getMovie().getAverageRating()
					data.getMovieMean()
					+ predRating.getMovie().getBias()
					+ predRating.getUser().getBias();

			// Clip the rating to 0.0 - 5.0
			rating = Math.max(Math.min(5.0, rating), 0.0);

			predRating.setRating(rating);
		}

		return data;
	}
}
