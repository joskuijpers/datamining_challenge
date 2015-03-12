package io.github.joskuijpers.datamining_challenge.model;

public class User {
	private int index;
	
	// Factors
	private int age, profession;
	private boolean male;
	
	// Computed
	private double bias = 0.0, averageRating = 0.0;
	private int numberOfRatings = 0;

	public User(int _index, boolean _male, int _age, int _profession) {
		this.index = _index;
		this.male = _male;
		this.age = _age;
		this.profession = _profession;
	}

	public int getIndex() {
		return index;
	}

	public boolean isMale() {
		return male;
	}

	public int getAge() {
		return age;
	}

	public int getProfession() {
		return profession;
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

	public void updateBiasAndAverage(double update, double movieAverage) {
		++numberOfRatings;
		
		averageRating += ((update - averageRating) / numberOfRatings);
		bias += ((update - movieAverage) - bias) / numberOfRatings;
	}
}
