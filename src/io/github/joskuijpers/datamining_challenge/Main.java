package io.github.joskuijpers.datamining_challenge;

import io.github.joskuijpers.datamining_challenge.model.*;
import io.github.joskuijpers.datamining_challenge.tiers.*;

import java.util.Locale;

public class Main {

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		// Create user list, movie list, and list of ratings
		UserList userList = new UserList();
		userList.readFile("data/users.csv");

		MovieList movieList = new MovieList();
		movieList.readFile("data/movies.csv");
		RatingList ratings = new RatingList();
		ratings.readFile("data/ratings.csv", userList, movieList);

		// Read list of ratings we need to predict
		RatingList predRatings = new RatingList();
		predRatings.readFile("data/predictions.csv", userList, movieList);

		// Perform rating predictions
		predictRatings(userList, movieList, ratings, predRatings);

		// Write result file
		predRatings.writeResultsFile("submission.csv");
	}

	/**
	 * Calculate the ratings.
	 *
	 * @param userList
	 * @param movieList
	 * @param ratingList
	 * @param predRatings
	 * @return
	 */
	public static RatingList predictRatings(UserList userList,
			MovieList movieList, RatingList ratingList, RatingList predRatings) {

		// Initial tier data
		TierData tierData = new TierData(userList, movieList, ratingList,
				predRatings);

		// Compute mean of ratings
		tierData = MovieMeanTier.run(tierData);

		// Calculate the bias and average for every movie
		tierData = MovieBiasTier.run(tierData);

		// Calculate the bias for every user
		tierData = UserBiasTier.run(tierData);
		
		// Construct the input matrix, used by multiple tiers
		tierData = InputMatrixTier.run(tierData);
		
		//Het runnen van de Statdata
		tierData = StatData.run(tierData);

		// Compute the LFM matrices
		tierData = LatentFactorModelTier.run(tierData);
		
		// Compute the Collaborative Filtering matrices
//		tierData = CollaborativeFilteringTier.run(tierData);

		// Predict with average per movie and bias per user.
//		tierData = PredictTier.run(tierData);

		// Return predictions
		return tierData.getPredRatings();
	}

}
