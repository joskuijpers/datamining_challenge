package io.github.joskuijpers.datamining_challenge.model;

public class Movie {
	private int index;

	// Factors
	private int year;
	private String title;

	// Computed
	private float bias = 0.0f, averageRating = 0.0f;
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

	public float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	public float getBias() {
		return bias;
	}
	
	public void updateBiasAndAverage(float update, float movieMean) {
		++numberOfRatings;

		averageRating += ((update - averageRating) / numberOfRatings);
		bias += ((update - movieMean) - bias) / numberOfRatings;
	}
}
