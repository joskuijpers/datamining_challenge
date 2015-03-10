package io.github.joskuijpers.datamining_challenge;

public class Rating {
    private BiasedUser user;
    private AvarageMovie movie;
    private double rating;
    
	public Rating(BiasedUser _user, AvarageMovie _movie, int _rating) {
        this.user      = _user;
        this.movie     = _movie;
        this.rating    = (double) _rating;
    }
    
    public Rating(BiasedUser _user, AvarageMovie _movie, double _rating) {
        this.user      = _user;
        this.movie     = _movie;
        this.rating    = _rating;
    }

    
    public BiasedUser getUser() {
        return user;
    }
    
    public AvarageMovie getMovie() {
        return movie;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double _rating) {
        this.rating = _rating;
    }
}
