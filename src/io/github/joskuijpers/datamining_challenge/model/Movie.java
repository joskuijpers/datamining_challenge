package io.github.joskuijpers.datamining_challenge.model;

public class Movie {
	private int index;
	
	// Factors
	private int year;
	private String title;
	
	// Computed
	private double bias = 0.0, averageRating = 0.0;
	private int numberOfRatings = 0;
	
	public Movie(int _index, int _year, String _title) {
		this.index = _index;
		this.year = _year;
		this.title = _title;
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
	
	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	public void updateBiasAndAverage(double update, double movieMean) {
		++numberOfRatings;
		
		averageRating += ((update - averageRating) / numberOfRatings);
		bias += ((update - movieMean) - bias) / numberOfRatings;
	}
}
