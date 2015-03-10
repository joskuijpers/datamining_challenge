package io.github.joskuijpers.datamining_challenge;

/**
 * Voegd een gemiddelde bias aan een user toe
 * @author Jan
 *
 */
public class BiasedUser extends User{

	public double bias;
	public int count = 0;
	
	public BiasedUser(int _index, boolean _male, int _age, int _profession) {
		super(_index, _male, _age, _profession);
		bias = 0.0;
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	public void updateBias(double update, double movieAverage){
		if(this.bias==0){
			bias = update - movieAverage;
			count++;
		}
		else{
			bias += ((update-movieAverage) - bias) / ++count;
		}
		
	}


	
	
}
