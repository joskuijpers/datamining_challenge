package io.github.joskuijpers.datamining_challenge;

import java.util.Comparator;
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
		predRatings.writeResultsFile("submission2.csv");	
	}
	
	public static RatingList predictRatings(UserList userList,
			MovieList movieList, RatingList ratingList, RatingList predRatings) {

		// Compute mean of ratings
		double mean = ratingList.get(0).getRating();
		for (int i = 1; i < ratingList.size(); i++) {
			mean = ((double) i / ((double) i + 1.0)) * mean
					+ (1.0 / ((double) i + 1.0))
					* ratingList.get(i).getRating();
		}


		
		//Het uitrekenen van de gemiddelde movierating
		for (int i = 1; i < ratingList.size(); i++) {
			ratingList.get(i).getMovie().updateAverage(ratingList.get(i).getRating());
		}
		
		//Het uitrekenen van de gemiddelde bias per user
		for (int i = 1; i < ratingList.size(); i++) {
			ratingList.get(i).getUser().updateBias(ratingList.get(i).getRating(), ratingList.get(i).getMovie().getAverage());
		}
		

		
		// Predict with average per movie and bias per user.
		for (int i = 0; i < predRatings.size(); i++) {
			Double temp = predRatings.get(i).getMovie().getAverage() + predRatings.get(i).getUser().getBias();	
			if(temp>5.0)temp=5.0;
			if(temp<0.0)temp=0.0;
			predRatings.get(i).setRating(temp);
		}
			

		// Return predictions
		return predRatings;
	}


}
