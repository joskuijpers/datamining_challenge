package io.github.joskuijpers.datamining_challenge.tiers;

import org.apache.commons.math3.linear.*;

import io.github.joskuijpers.datamining_challenge.TierData;

/**
 * A tier using collaborative filtering, implementing the slope one method
 * @author Jan
 *
 */
public class CollaborativeFilteringTier extends Tier{

	/**
	 * creating the difference and count matrix.
	 * 
	 * @param data 
	 * 				the tier data
	 * @return the tier data
	 */
	public static TierData run(TierData data) {
		RealMatrix diffMatrix, countMatrix, inputMatrix;
		long tijdnu = System.currentTimeMillis();
		long tijdtoen = System.currentTimeMillis();
		
		inputMatrix = data.getInputMatrix();
		
		// Create matrix for calculated data
		diffMatrix = new Array2DRowRealMatrix(data.getMovieList().size(), data.getMovieList().size());
		countMatrix = new Array2DRowRealMatrix(data.getMovieList().size(), data.getMovieList().size());
		
		// Calculate the values for calculated data
		for (int i = 0; i < data.getMovieList().size(); ++i) {
			for (int j = 0; j < data.getMovieList().size(); ++j) {
				if(i == j) {
					diffMatrix.setEntry(i, j, 0.0);
					countMatrix.setEntry(i, j, 0.0);
				}
				
				double diff = 0.0;
				double count = 0.0;
				
				//en nu moeten we er door heen gaan lopen
				//dit moet sneller kunnen
				for (int k = 0; k < data.getUserList().size(); ++k) {
					if(inputMatrix.getEntry(j, k) > 0 && inputMatrix.getEntry(i, k) > 0){
						diff += inputMatrix.getEntry(j, k) - inputMatrix.getEntry(i, k);
						++count;
					}
				}
				
				// Sla het gemiddelde verschil op tussen twee films
				diffMatrix.setEntry(i, j, diff / count);
				
				// Houdt de count <van?> bij
				countMatrix.setEntry(i, j, count);
			}
			if(i%100==0){
				tijdnu = System.currentTimeMillis();
				System.out.println(i + "de kolum diffmatrix, deze honderd duurden: "+ (tijdnu-tijdtoen) + " milliseconden");
				tijdtoen = tijdnu;
			}
		}
				
        data.setDiffMatrix(diffMatrix);
		data.setCountMatrix(countMatrix);
		
		return data;
	}
}