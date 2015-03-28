package io.github.joskuijpers.datamining_challenge;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

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
	private double movieMean;

	// Multi Tier
	private RealMatrix inputMatrix;
	
	// LatentFactorModel
	private SingularValueDecomposition svd;
	
	//Collaborative Filtering
	private RealMatrix diffMatrix, countMatrix;

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
	
	public RealMatrix getInputMatrix() {
		return inputMatrix;
	}

	public void setInputMatrix(RealMatrix inputMatrix) {
		this.inputMatrix = inputMatrix;
	}

	public SingularValueDecomposition getSvd() {
		return svd;
	}

	public void setSvd(SingularValueDecomposition svd) {
		this.svd = svd;
	}
	
}
