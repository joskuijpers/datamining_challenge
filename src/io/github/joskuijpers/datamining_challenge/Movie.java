package io.github.joskuijpers.datamining_challenge;

public class Movie {
	private int index, year;
	private String title;
	private double averageRating = 0.0;
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

	public void updateAverageRating(double update) {
		averageRating += ((update - averageRating) / ++numberOfRatings);
	}
}
