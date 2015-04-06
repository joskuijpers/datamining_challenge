package io.github.joskuijpers.datamining_challenge.tiers;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.model.User;

public class UserFilterTier extends Tier {
	// number of ratings a user must have for filters to be applied
	private final static int NUM_THRESHOLD = 10;
	// upper bound of the average rating of a low spammer
	private final static double LOW_SPAMMER_THRESHOLD = 1.4;
	
	/**
	 * Filter the users.
	 * 
	 * Set the ignore flag on users with any of the following properties:
	 * 	- has >= NUM_THRESHOLD ratings but average <= LOW_SPAMMER_THRESHOLD, removing users who spam the 1 star
	 * @param data
	 * @return
	 */
	public static TierData run(TierData data) {
		int numLowSpam = 0;
		
		for(User user : data.getUserList()) {
			if(user.getNumberOfRatings() >= NUM_THRESHOLD) {

				// filter lowspammers
				if(user.getAverageRating() <= LOW_SPAMMER_THRESHOLD) {
					user.setIgnored(true);
					++numLowSpam;
				}
			}
		}
		
		System.out.println("Finished ignoring users. Marked "+numLowSpam+" lowspammers.");
		
		return data;
	}
}
