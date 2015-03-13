package io.github.joskuijpers.datamining_challenge;

import io.github.joskuijpers.datamining_challenge.math.Matrix;
import io.github.joskuijpers.datamining_challenge.model.*;

/**
 * A simple POJO for storing the intermediate tier data.
 * 
 * @author joskuijpers
 */
public class TierData {
	// Storage
	private UserList userList;
	private MovieList movieList;
	private RatingList ratingList, predRatings;
	
	// MovieMeanTier
	private float movieMean;
	
	// LatentFactorModel
	private Matrix movieFactorMatrix, factorUserMatrix;
	
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

	public float getMovieMean() {
		return movieMean;
	}

	public void setMovieMean(float movieMean) {
		this.movieMean = movieMean;
	}

	public Matrix getMovieFactorMatrix() {
		return movieFactorMatrix;
	}

	public void setMovieFactorMatrix(Matrix movieFactorMatrix) {
		this.movieFactorMatrix = movieFactorMatrix;
	}

	public Matrix getFactorUserMatrix() {
		return factorUserMatrix;
	}

	public void setFactorUserMatrix(Matrix factorUserMatrix) {
		this.factorUserMatrix = factorUserMatrix;
	}
}
