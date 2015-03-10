package io.github.joskuijpers.datamining_challenge;

public class Rating {
	private User user;
	private Movie movie;
	private double rating;

	public Rating(User _user, Movie _movie, int _rating) {
		this.user = _user;
		this.movie = _movie;
		this.rating = (double) _rating;
	}

	public Rating(User _user, Movie _movie, double _rating) {
		this.user = _user;
		this.movie = _movie;
		this.rating = _rating;
	}

	public User getUser() {
		return user;
	}

	public Movie getMovie() {
		return movie;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double _rating) {
		this.rating = _rating;
	}
}
