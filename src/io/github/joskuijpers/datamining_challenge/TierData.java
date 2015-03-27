package io.github.joskuijpers.datamining_challenge;

import org.apache.commons.math3.linear.RealMatrix;

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
	private RealMatrix movieFactorMatrix, factorUserMatrix;
	
	//Collaborative Filtering
	private RealMatrix diffMatrix, countMatrix, imputMatrix;

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

	public RealMatrix getMovieFactorMatrix() {
		return movieFactorMatrix;
	}

	public void setMovieFactorMatrix(RealMatrix movieFactorMatrix) {
		this.movieFactorMatrix = movieFactorMatrix;
	}
	
	public RealMatrix getFactorUserMatrix() {
		return factorUserMatrix;
	}

	public void setFactorUserMatrix(RealMatrix factorUserMatrix) {
		this.factorUserMatrix = factorUserMatrix;
	}

	public RealMatrix getCountMatrix() {
		return countMatrix;
	}

	public void setCountMatrix(RealMatrix countMatrix) {
		this.countMatrix = countMatrix;
	}
	
	public RealMatrix getDiffMatrix() {
		return diffMatrix;
	}

	public void setDiffMatrix(RealMatrix diffMatrix) {
		this.diffMatrix = diffMatrix;
	}
	
	public RealMatrix getImputMatrix() {
		return imputMatrix;
	}

	public void setImputMatrix(RealMatrix imputMatrix) {
		this.imputMatrix = imputMatrix;
	}
	
}
