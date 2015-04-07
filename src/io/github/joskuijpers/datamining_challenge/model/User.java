package io.github.joskuijpers.datamining_challenge.model;

public class User {
	private int index;

	// Factors
	private int age, profession;
	private boolean male;

	// Computed
	private double bias = 0.0f, averageRating = 0.0f;
	private int numberOfRatings = 0;
	private boolean ignored = false;

	// testing
	private int biasedNum = 0, cfNum = 0, lfmNum = 0;
	private double biasedSum = 0.0, cfSum = 0.0, lfmSum = 0.0;
	
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

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
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

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	public void addBiasedError(double error) {
		++biasedNum;
		biasedSum += error * error;
	}

	public void addCFError(double error) {
		++cfNum;
		cfSum += error * error;
	}

	public void addLFMError(double error) {
		++lfmNum;
		lfmSum += error * error;
	}

	public double getBiasedRMSE() {
		return Math.sqrt(biasedSum / (double)biasedNum);
	}

	public double getCFRMSE() {
		return Math.sqrt(cfSum / (double)cfNum);
	}

	public double getLFMRMSE() {
		return Math.sqrt(lfmSum / (double)lfmNum);
	}

}
