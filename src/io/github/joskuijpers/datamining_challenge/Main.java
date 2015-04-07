package io.github.joskuijpers.datamining_challenge;

import io.github.joskuijpers.datamining_challenge.model.*;
import io.github.joskuijpers.datamining_challenge.tiers.*;

import java.util.Locale;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		// Create user list, movie list, and list of ratings
		UserList userList = new UserList();
		userList.readFile("data/users.csv");

		MovieList movieList = new MovieList();
		movieList.readFile("data/movies.csv");

		// Either make a submission, or test the algorithm
		if (false) {
			// Read all the ratings
			RatingList ratings = new RatingList();
			ratings.readFile("data/ratings.csv", userList, movieList);
			
			// Read list of ratings we need to predict
			RatingList predRatings = new RatingList();
			predRatings.readFile("data/predictions.csv", userList, movieList);

			// Perform rating predictions
			predictRatings(userList, movieList, ratings, predRatings);

			// Write result file
			predRatings.writeResultsFile("submission.csv");
		} else {
			// Read only the training ratings. This list contains 3/4th of the actual ratings
			RatingList ratings = new RatingList();
			ratings.readFile("data/localtest/ratings.csv", userList, movieList);
			
			// Read list of prediction to make. This list only contains the
			// user/movie combo's we removed from the ratings_train set
			RatingList predictions = new RatingList();
			predictions.readFile("data/localtest/predictions.csv", userList, movieList);

			// Perform rating predictions
			predictRatings(userList, movieList, ratings, predictions);
			
			// Read list of actual rating. This list contains 1/4th of the actual ratings
			RatingList verification = new RatingList();
			verification.readFile("data/localtest/verification.csv", userList, movieList);
			
			// Compare and calculate RMSE
			if(predictions.size() != verification.size()) {
				System.out.println("Size of predictions and verification is not the same.");
				return;
			}
			
			double squaredSum = 0.0;
			for(int i = 0; i < predictions.size(); ++i) {
				Rating prediction = predictions.get(i);
				Rating actual = verification.get(i);
				double error;
				
				assert(prediction.getMovie() == actual.getMovie());
				assert(prediction.getUser() == actual.getUser());
				
				error = actual.getRating() - prediction.getRating();
				squaredSum += error * error;
			}
			
			double rmse = Math.sqrt(squaredSum / (double)predictions.size());
			System.out.println("RMSE: "+rmse);
		}
		
		System.out.println("Finished running. Close all windows to quit.");
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
		
		// Filter spam users
		tierData = UserFilterTier.run(tierData);
		
		// Construct the input matrix, used by multiple tiers
		tierData = InputMatrixTier.run(tierData);
		
		//Het runnen van de Statdata
		tierData = StatData.run(tierData);

		// Compute the LFM matrices
		tierData = LatentFactorModelTier.run(tierData);
		
		// Compute the Collaborative Filtering matrices
//		tierData = CollaborativeFilteringTier.run(tierData);
//		System.out.println("CF afgerond");

		// Predict with average per movie and bias per user.
		tierData = PredictTier.run(tierData);

		// Return predictions
		return tierData.getPredRatings();
	}

}
