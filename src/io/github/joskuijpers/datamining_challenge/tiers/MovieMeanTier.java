package io.github.joskuijpers.datamining_challenge.tiers;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.model.Rating;

/**
 * Tier for calculating the mean of all the movies.
 * 
 * @author joskuijpers
 */
public class MovieMeanTier extends Tier {
	
	/**
	 * Calculate the mean of ALL the movies.
	 * 
	 * @param data the tier data
	 * @return the tier data
	 */
	public static TierData run(TierData data) {

		double movieMean = data.getRatingList().get(0).getRating();
		for (int i = 1; i < data.getRatingList().size(); i++) {
			movieMean = ((double) i / ((double) i + 1.0)) * movieMean
					+ (1.0 / ((double) i + 1.0))
					* data.getRatingList().get(i).getRating();
		}
		
		data.setMovieMean(movieMean);

		return data;
	}
}
