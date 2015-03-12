package io.github.joskuijpers.datamining_challenge.tiers;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.model.Rating;

/**
 * A tier calculating the movie bias.
 * 
 * @author joskuijpers
 */
public class MovieBiasTier extends Tier {

	/**
	 * Calculate the movie bias and average.
	 * The bias is calculated using the mean.
	 * 
	 * @param data the tier data
	 * @return the tier data
	 */
	public static TierData run(TierData data) {

		for (Rating rating : data.getRatingList()) {
			rating.getMovie().updateBiasAndAverage(rating.getRating(),
					data.getMovieMean());
		}

		return data;
	}
}
