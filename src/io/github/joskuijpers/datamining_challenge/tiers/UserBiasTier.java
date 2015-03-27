package io.github.joskuijpers.datamining_challenge.tiers;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.model.*;

/**
 * A tier calculating the user bias.
 * 
 * @author joskuijpers
 */
public class UserBiasTier extends Tier {

	/**
	 * Calculate the bias for every user.
	 * 
	 * This is done by taking, for every rating made, the average of the movie
	 * the rating is for, and taking the diff with the user's rating. Using that
	 * difference, the average bias is updated in the user model.
	 * 
	 * @param data
	 *            the tier data
	 * @return the tier data
	 */
	public static TierData run(TierData data) {
		for (Rating rating : data.getRatingList()) {
			rating.getUser().updateBiasAndAverage(rating.getRating(),
					rating.getMovie().getAverageRating());
		}

		// For users with no rating, set the mean
		for(User user : data.getUserList()) {
			if(user.getAverageRating() == 0.0f)
				user.setAverageRating(data.getMovieMean());
		}

		return data;
	}
}
