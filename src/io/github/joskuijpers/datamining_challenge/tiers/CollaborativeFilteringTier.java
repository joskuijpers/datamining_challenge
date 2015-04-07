package io.github.joskuijpers.datamining_challenge.tiers;

import java.util.Date;

import org.apache.commons.math3.linear.*;

import io.github.joskuijpers.datamining_challenge.Main;
import io.github.joskuijpers.datamining_challenge.MatUtils;
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
		RealMatrix diffMatrix = null, countMatrix = null;

		// Ask if load from file
		if(Main.getBooleanInput("Do you want to load the diff and count matrices from file?",false)) {
			System.out.println("Trying to load files from current working directory...");
			
			// Load from file
			diffMatrix = MatUtils.readMatrix("DiffMatrix.mat");
			countMatrix = MatUtils.readMatrix("CountMatrix.mat");
			
			System.out.println("Loaded matrices from files.");
		} else {
			long tijdnu = System.currentTimeMillis();
			long tijdtoen = System.currentTimeMillis();
			int numberOfMovies = data.getMovieList().size();
			RealMatrix inputMatrix;

			inputMatrix = data.getInputMatrix();

			// Create matrix for calculated data
			diffMatrix = new BlockRealMatrix(numberOfMovies, numberOfMovies);
			countMatrix = new BlockRealMatrix(numberOfMovies, numberOfMovies);

			System.out.println("Starting CF at "+(new Date()));
			
			// Calculate the values for calculated data
			for (int i = 0; i < numberOfMovies; ++i) {
				for (int j = 0; j < numberOfMovies; ++j) {
					if(i == j) {
						// this is useless, default values are 0.0?
						diffMatrix.setEntry(i, j, 0.0);
						countMatrix.setEntry(i, j, 0.0);
						//continue; ???????????? maybe this should be here?
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

				if(i % 100 == 0){
					tijdnu = System.currentTimeMillis();
					System.out.println(i + "de kolum diffmatrix, deze honderd duurden: "+ (tijdnu-tijdtoen) + " milliseconden");
					tijdtoen = tijdnu;
				}
			}
			
			System.out.println("Finished CF at "+(new Date()));

			// Ask to save to file
			if(Main.getBooleanInput("Do you want to save the diff and count matrices to file?",false)) {
				System.out.println("Writing diffMatrix matrix to file...");
				MatUtils.writeMatrix(diffMatrix,"DiffMatrix.mat");

				System.out.println("Writing countMatrix matrix to file...");
				MatUtils.writeMatrix(countMatrix,"CountMatrix.mat");
			}
		}

		data.setDiffMatrix(diffMatrix);
		data.setCountMatrix(countMatrix);

		return data;
	}
}