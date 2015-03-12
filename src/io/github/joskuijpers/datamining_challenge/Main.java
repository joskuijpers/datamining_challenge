package io.github.joskuijpers.datamining_challenge;

import io.github.joskuijpers.datamining_challenge.model.MovieList;
import io.github.joskuijpers.datamining_challenge.model.Rating;
import io.github.joskuijpers.datamining_challenge.model.RatingList;
import io.github.joskuijpers.datamining_challenge.model.UserList;

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

	public static RatingList predictRatings(UserList userList,
			MovieList movieList, RatingList ratingList, RatingList predRatings) {

		// Compute mean of ratings
				double mean = ratingList.get(0).getRating();
				for (int i = 1; i < ratingList.size(); i++) {
					mean = ((double) i / ((double) i + 1.0)) * mean
							+ (1.0 / ((double) i + 1.0))
							* ratingList.get(i).getRating();
		
		
		// Bereken van elke film de gemiddelde rating
		for (Rating rating : ratingList) {
			rating.getMovie().updateAverageRating(rating.getRating());
		}

		// Bereken de bias voor elke gebruiker
		for (Rating rating : ratingList) {
			rating.getUser().updateBias(rating.getRating(),
					rating.getMovie().getAverageRating());
		}

		// Predict with average per movie and bias per user.
		for (Rating predRating : predRatings) {
			Double rating;

			rating = predRating.getMovie().getAverageRating()
					+ predRating.getUser().getBias();

			rating = Math.max(Math.min(5.0, rating), 0.0);

			predRating.setRating(rating);
		}

		// Return predictions
		return predRatings;
	}

}
