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
		// return runLFMBiased(data);
		return runBiases(data);
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
			float ratingbias;
			
			// Calculate the rating
			ratingbias = // predRating.getMovie().getAverageRating()
			data.getMovieMean()
					+ predRating.getMovie().getBias()
					+ predRating.getUser().getBias();
			
			// Clip the rating to 0.0 - 5.0
			ratingbias = Math.max(Math.min(5.0f, ratingbias), 0.0f);
			
			// ///De tweede methode, met CF
			Float ratingcf = 0.0f;
			
			// Hierna moet de predictratings matrix gevuld worden.
			// De formule die hier voor gebruikt wordt is,
			// per door de user gerate film, nemen we de rating van deze film+ plus de diff van deze film.
			// We vermenigvuldigen het met count, om een waarde aan te geven. Dit tellen we allemaal bij elkaar op
			// Dan delen we het door de som van counts van alle meegenomen waarden
			// sum(count*(userrating+diffwaard) )/ sum counts
			
			// Calculate the rating
			int userIndex = predRating.getUser().getIndex() - 1;
			// System.out.println("userIndex: " + userIndex);
			int movieIndex = predRating.getMovie().getIndex() - 1;
			
			Vector userColumn = data.getImputMatrix().getColumn(userIndex);
			
			float teller = 0.0f;
			float noemer = 0.0f;
			for (int i = 0; i < userColumn.size(); ++i) {
				if (userColumn.get(i) > 0.0) {
					if (!data.getDiffMatrix().get(movieIndex, i).isNaN()) {
						teller += (userColumn.get(i) + data.getDiffMatrix().get(i, movieIndex)) * data.getCountMatrix().get(i, movieIndex);
						noemer += data.getCountMatrix().get(movieIndex, i);
					}
				}
			}
			
			// Calculate the rating
			ratingcf = teller / noemer;
			// NaN eruit halen. en dan maar de bias nemen.
			if (ratingcf.isNaN()) {
				ratingcf = ratingbias;
			}
			// Clip the rating to 0.0 - 5.0
			ratingcf = Math.max(Math.min(5.0f, ratingcf), 0.0f);
			
			// Dit is de tradeoff tussen de ratings. dit is een gokje, werkt nog niet beter dan cf alleen.
			if (data.getUserList().get(userIndex).getNumberOfRatings() > 10) {
				predRating.setRating(ratingcf);
			}
			else {
				predRating.setRating(ratingbias);
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
	
	public static TierData runCF(TierData data) {
		for (Rating predRating : data.getPredRatings()) {
			Float ratingcf = 0.0f;
			
			// Hierna moet de predictratings matrix gevuld worden.
			// De formule die hier voor gebruikt wordt is,
			// per door de user gerate film, nemen we de rating van deze film+ plus de diff van deze film.
			// We vermenigvuldigen het met count, om een waarde aan te geven. Dit tellen we allemaal bij elkaar op
			// Dan delen we het door de som van counts van alle meegenomen waarden
			// sum(count*(userrating+diffwaard) )/ sum counts
			
			// Calculate the rating
			int userIndex = predRating.getUser().getIndex() - 1;
			// System.out.println("userIndex: " + userIndex);
			int movieIndex = predRating.getMovie().getIndex() - 1;
			
			Vector userColumn = data.getImputMatrix().getColumn(userIndex);
			
			float teller = 0.0f;
			float noemer = 0.0f;
			for (int i = 0; i < userColumn.size(); ++i) {
				if (userColumn.get(i) > 0.0) {
					if (!data.getDiffMatrix().get(movieIndex, i).isNaN()) {
						teller += (userColumn.get(i) + data.getDiffMatrix().get(i, movieIndex)) * data.getCountMatrix().get(i, movieIndex);
						noemer += data.getCountMatrix().get(movieIndex, i);
					}
				}
			}
			
			// Calculate the rating
			ratingcf = teller / noemer;
			// NaN eruit halen.
			if (ratingcf.isNaN()) {
				ratingcf = data.getMovieMean();
			}
			// Clip the rating to 0.0 - 5.0
			ratingcf = Math.max(Math.min(5.0f, ratingcf), 0.0f);
			predRating.setRating(ratingcf);
		}
		return data;
	}
}
