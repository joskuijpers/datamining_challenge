package io.github.joskuijpers.datamining_challenge.tiers;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.model.Rating;

/**
 * A tier calculating making the neccesary predictions.
 * 
 * @author joskuijpers
 */
@SuppressWarnings("unused")
public class PredictTier extends Tier {
	
	public static TierData run(TierData data) {
		 return runLFMBiased(data);
//		return runBiases(data);
	}
	
	/**
	 * Calculate the predictions.
	 * 
	 * Formula: Rating_mu = MovieMean + MoviaBias_m + UserBias_u
	 * 
	 * @param data
	 * @return
	 */
	private static TierData runBiases(TierData data) {
		for (Rating predRating : data.getPredRatings()) {
			double ratingbias, ratingcf = 0.0, rating = 0.0;
			
			// Calculate the rating
			ratingbias = // predRating.getMovie().getAverageRating()
			data.getMovieMean()
					+ predRating.getMovie().getBias()
					+ predRating.getUser().getBias();
			
			// Hierna moet de predictratings matrix gevuld worden.
			// De formule die hier voor gebruikt wordt is,
			// per door de user gerate film, nemen we de rating van deze film+ plus de diff van deze film.
			// We vermenigvuldigen het met count, om een waarde aan te geven. Dit tellen we allemaal bij elkaar op
			// Dan delen we het door de som van counts van alle meegenomen waarden
			// sum(count*(userrating+diffwaard) )/ sum counts
			
			// Calculate the rating
			int userIndex = predRating.getUser().getIndex() - 1;
			int movieIndex = predRating.getMovie().getIndex() - 1;
			
			double[] userColumn = data.getInputMatrix().getColumn(userIndex);
			
			double teller = 0.0;
			double noemer = 0.0;
			for (int i = 0; i < userColumn.length; ++i) {
				if (userColumn[i] > 0.0) {
					double diffEntry = data.getDiffMatrix().getEntry(i, movieIndex);
					
					if (!Double.isNaN(diffEntry)) {
						//ik ga proberen eerst het gemiddelde van de film er af te trekken en dan heb bij de rating, 
						//dit bij de biased rating op te tellen.
						teller += (userColumn[i] +diffEntry) * data.getCountMatrix().getEntry(i, movieIndex); 
						noemer += data.getCountMatrix().getEntry(movieIndex,i);
					}
				}
			}
			
			// Calculate the rating
			ratingcf = (teller / noemer);
			
			// Gebruik de biased rating als er geen CF rating is.
			// Gebruik ook de biased rating als er minder dan 10 ratings zijn.
			if (Double.isNaN(ratingcf) || data.getUserList().get(userIndex).getNumberOfRatings() < 10) {
				rating = ratingbias;
			} else
				rating = ratingcf;
			
			// Clip the rating to 0.0 - 5.0
			ratingcf = Math.max(Math.min(5.0f, ratingcf), 0.0f);
			
			predRating.setRating(rating);
			
		}
		
		return data;
	}
	
	private static TierData runLFMBiased(TierData data) {
		RealMatrix movieFeatureMatrix, featureUserMatrix;
		RealVector movieVector, userVector;
		
		movieFeatureMatrix = data.getMovieFeatureMatrix();
		featureUserMatrix = data.getFeatureUserMatrix();
		
		for (Rating predRating : data.getPredRatings()) {
			double rating = 0.0f;
			
			// Calculate the rating
			int userIndex = predRating.getUser().getIndex() - 1;
			int movieIndex = predRating.getMovie().getIndex() - 1;
			
			movieVector = movieFeatureMatrix.getRowVector(movieIndex);
			userVector = featureUserMatrix.getColumnVector(userIndex);
			
			// r_xi = u + b_x + b_i + q_i*p_x
			// rating = moviemean + userbias + moviebias + usermovieinteraction
			rating = data.getMovieMean() + predRating.getUser().getBias() + predRating.getMovie().getBias() + movieVector.dotProduct(userVector);
			
			// Clip the rating to 0.0 - 5.0
			rating = Math.max(Math.min(5.0f, rating), 0.0f);
			
			predRating.setRating(rating);
		}
		
		return data;
	}
}
