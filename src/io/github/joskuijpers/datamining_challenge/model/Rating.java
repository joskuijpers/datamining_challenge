package io.github.joskuijpers.datamining_challenge.model;

public class Rating {
	private User user;
	private Movie movie;
	private float rating;

	public Rating(User _user, Movie _movie, int _rating) {
		this.user = _user;
		this.movie = _movie;
		this.rating = (float) _rating;
	}

	public Rating(User _user, Movie _movie, float _rating) {
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

	public float getRating() {
		return rating;
	}

	public void setRating(float _rating) {
		this.rating = _rating;
	}
}
