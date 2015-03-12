package io.github.joskuijpers.datamining_challenge;

import io.github.joskuijpers.datamining_challenge.model.*;

/**
 * A simple POJO for storing the intermediate tier data.
 * 
 * @author joskuijpers
 */
public class TierData {

	private UserList userList;
	private MovieList movieList;
	private RatingList ratingList, predRatings;
	
	private double movieMean;
	
	public TierData(UserList userList, MovieList movieList,
			RatingList ratingList, RatingList predRatings) {
		this.setUserList(userList);
		this.setMovieList(movieList);
		this.setRatingList(ratingList);
		this.setPredRatings(predRatings);
	}

	public UserList getUserList() {
		return userList;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}

	public MovieList getMovieList() {
		return movieList;
	}

	public void setMovieList(MovieList movieList) {
		this.movieList = movieList;
	}

	public RatingList getPredRatings() {
		return predRatings;
	}

	public void setPredRatings(RatingList predRatings) {
		this.predRatings = predRatings;
	}

	public RatingList getRatingList() {
		return ratingList;
	}

	public void setRatingList(RatingList ratingList) {
		this.ratingList = ratingList;
	}

	public double getMovieMean() {
		return movieMean;
	}

	public void setMovieMean(double movieMean) {
		this.movieMean = movieMean;
	}
}
