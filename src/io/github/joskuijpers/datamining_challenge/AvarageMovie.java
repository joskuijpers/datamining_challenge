package io.github.joskuijpers.datamining_challenge;

/**
 * Voegd een gemiddelde score per film aan een Movie toe.
 * @author Jan
 *
 */
public class AvarageMovie extends Movie{
	public double average;
	public int count = 0;

	public AvarageMovie(int _index, int _year, String _title){
		super(_index,_year,_title);
		average = 0;
	}

	
	public int getCount() {
		return count;
	}
	
	public double getAverage() {
		return average;
	}

	public void setAverage(double avarage) {
		this.average = avarage;
	}
	
	public void updateAverage(double update){
		if(this.average==0){
			average = update;
			count++;
		}
		else{
			average += ((update - average) / ++count);
		}

		
	}
	
	
	
		
		
}
