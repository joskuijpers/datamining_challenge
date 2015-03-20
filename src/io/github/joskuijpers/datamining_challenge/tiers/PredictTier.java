package io.github.joskuijpers.datamining_challenge.tiers;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.math.Vector;
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
			float rating;

			// Calculate the rating
			rating = //predRating.getMovie().getAverageRating()
					data.getMovieMean()
					+ predRating.getMovie().getBias()
					+ predRating.getUser().getBias();

			// Clip the rating to 0.0 - 5.0
			rating = Math.max(Math.min(5.0f, rating), 0.0f);

					
			
			/////De tweede methode, met CF
			Float rating2 = 0.0f;
			
			//Hierna moet de predictratings matrix gevuld worden. 
			//De formule die hier voor gebruikt wordt is,
			//per door de user gerate film, nemen we de rating van deze film+ plus de diff van deze film.
			//We vermenigvuldigen het met count, om een waarde aan te geven. Dit tellen we allemaal bij elkaar op
			//Dan delen we het door de som van counts van alle meegenomen waarden
			//  sum(count*(userrating+diffwaard) )/ sum counts
			
			// Calculate the rating
			int userIndex = predRating.getUser().getIndex() - 1;
			//System.out.println("userIndex: " + userIndex);
			int movieIndex = predRating.getMovie().getIndex() - 1;
			
			Vector userColumn = data.getImputMatrix().getColumn(userIndex);
			
			float teller = 0.0f;
			float noemer = 0.0f;
			for(int i=0; i<userColumn.size();++i){
				if(userColumn.get(i)>0.0){
					if(!data.getDiffMatrix().get(movieIndex, i).isNaN()){
					teller += (userColumn.get(i)+data.getDiffMatrix().get(i,movieIndex))*data.getCountMatrix().get(i,movieIndex);
					noemer += data.getCountMatrix().get(movieIndex, i);
					}
				}
			}
			
			// Calculate the rating
			rating2 = teller/noemer;
			//NaN eruit halen. en dan maar de bias nemen.
			if(rating2.isNaN()){			
				rating2 = rating;
			}
			// Clip the rating to 0.0 - 5.0
			rating2 = Math.max(Math.min(5.0f, rating2), 0.0f);
			
			if(data.getUserList().get(userIndex).getNumberOfRatings()>30){
				predRating.setRating(rating2);
			}
			else{
				predRating.setRating(rating);
			}
			
		}

		return data;
	}

	private static TierData runLFMBiased(TierData data) {
		for (Rating predRating : data.getPredRatings()) {
			float rating;

			// Calculate the rating
			int userIndex = predRating.getUser().getIndex() - 1;
			int movieIndex = predRating.getMovie().getIndex() - 1;

			Vector user = data.getFactorUserMatrix().getColumn(userIndex);
			Vector movie = data.getMovieFactorMatrix().getRow(movieIndex);

			// Calculate the rating
			rating = movie.dotproduct(user);

			// Clip the rating to 0.0 - 5.0
			rating = Math.max(Math.min(5.0f, rating), 0.0f);

			predRating.setRating(rating);
		}

		return data;
	}
	
	public static TierData runCF(TierData data){
		for(Rating predRating : data.getPredRatings()){
			Float rating2 = 0.0f;
			
			//Hierna moet de predictratings matrix gevuld worden. 
			//De formule die hier voor gebruikt wordt is,
			//per door de user gerate film, nemen we de rating van deze film+ plus de diff van deze film.
			//We vermenigvuldigen het met count, om een waarde aan te geven. Dit tellen we allemaal bij elkaar op
			//Dan delen we het door de som van counts van alle meegenomen waarden
			//  sum(count*(userrating+diffwaard) )/ sum counts
			
			// Calculate the rating
			int userIndex = predRating.getUser().getIndex() - 1;
			//System.out.println("userIndex: " + userIndex);
			int movieIndex = predRating.getMovie().getIndex() - 1;
			
			Vector userColumn = data.getImputMatrix().getColumn(userIndex);
			
			float teller = 0.0f;
			float noemer = 0.0f;
			for(int i=0; i<userColumn.size();++i){
				if(userColumn.get(i)>0.0){
					if(!data.getDiffMatrix().get(movieIndex, i).isNaN()){
					teller += (userColumn.get(i)+data.getDiffMatrix().get(i,movieIndex))*data.getCountMatrix().get(i,movieIndex);
					noemer += data.getCountMatrix().get(movieIndex, i);
					}
				}
			}
			
			// Calculate the rating
			rating2 = teller/noemer;
			//NaN eruit halen.
			if(rating2.isNaN()){			
				rating2 = data.getMovieMean();
			}
			// Clip the rating to 0.0 - 5.0
			rating2 = Math.max(Math.min(5.0f, rating2), 0.0f);
			predRating.setRating(rating2);
					}
		return data;
	}
}
