package io.github.joskuijpers.datamining_challenge.tiers;

import io.github.joskuijpers.datamining_challenge.TierData;

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
		for (int i = 1; i < data.getRatingList().size(); ++i) {
			double f = (double)i;
			
			movieMean = (f / (f + 1.0)) * movieMean
					+ (1.0 / (f + 1.0))
					* data.getRatingList().get(i).getRating();
		}

		data.setMovieMean(movieMean);

		return data;
	}
}
