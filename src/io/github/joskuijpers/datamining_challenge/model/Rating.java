package io.github.joskuijpers.datamining_challenge.model;

public class Rating {
	private User user;
	private Movie movie;
	private double rating;
	private boolean ignored = false;

	public Rating(User user, Movie movie, int rating) {
		this.user = user;
		this.movie = movie;
		this.rating = (double)rating;
	}

	public Rating(User user, Movie movie, double rating) {
		this.user = user;
		this.movie = movie;
		this.rating = rating;
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

	public void setRating(double rating) {
		this.rating = rating;
	}

	public boolean isIgnored() {
		return ignored || user.isIgnored();
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}
}
