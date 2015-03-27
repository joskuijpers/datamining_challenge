package io.github.joskuijpers.datamining_challenge.model;

public class Movie {
	private int index;

	// Factors
	private int year;
	private String title;

	// Computed
	private double bias = 0.0f, averageRating = 0.0f;
	private int numberOfRatings = 0;
	
	public Movie(int index, int year, String title) {
		this.index = index;
		this.year = year;
		this.title = title;
	}

	public int getIndex() {
		return index;
	}

	public int getYear() {
		return year;
	}

	public String getTitle() {
		return title;
	}

	public int getNumberOfRatings() {
		return numberOfRatings;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	public double getBias() {
		return bias;
	}
	
	public void updateBiasAndAverage(double update, double movieMean) {
		++numberOfRatings;

		averageRating += ((update - averageRating) / numberOfRatings);
		bias += ((update - movieMean) - bias) / numberOfRatings;
	}
}
