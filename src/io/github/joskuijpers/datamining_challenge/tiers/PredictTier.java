package io.github.joskuijpers.datamining_challenge.tiers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.model.Rating;
import io.github.joskuijpers.datamining_challenge.model.User;

/**
 * A tier calculating making the neccesary predictions.
 * 
 * @author joskuijpers
 */
@SuppressWarnings("unused")
public class PredictTier extends Tier {
	private final static int BIAS_THRESHOLD = 0; // these must be *4/3 when not running on verification data
	private final static int CF_THRESHOLD = 0;
	private final static int LFM_THRESHOLD = 100000;
	
	public static TierData run(TierData data) {
		int bias = 0, cf = 0, lfm = 0;

		// Draw a nice graph comparing the different models, if verification data is available
		if(data.getVerificationRatings() != null)
			draw(data);
		
		for (Rating predRating : data.getPredRatings()) {
			double rating = 0.0f;
			
			// Calculate the rating
			int userIndex = predRating.getUser().getIndex() - 1;
			int movieIndex = predRating.getMovie().getIndex() - 1;

			int numberOfUserRatings = data.getUserList().get(userIndex).getNumberOfRatings();
					
			if (numberOfUserRatings < BIAS_THRESHOLD) { // Use biases only
				rating = predictSimpleBiased(data,predRating,userIndex,movieIndex);
				++bias;
			} else if(numberOfUserRatings < CF_THRESHOLD) { // use CF, unless it fails
				rating = predictCF(data,predRating,userIndex,movieIndex);
				if(Double.isNaN(rating)) {
					rating = predictSimpleBiased(data,predRating,userIndex,movieIndex);
					++bias;
				} else
					++cf;
			} else { // with a large amount of ratings, use LFM
				rating = predictLFM(data,predRating,userIndex,movieIndex);
				++lfm;
			}
			
			// Clip the rating to 1.0 - 5.0
			rating = Math.max(Math.min(5.0, rating), 1.0);
			
			predRating.setRating(rating);
		}
		
		System.out.println("Predicted using Biased ("+bias+"), CF ("+cf+"), LFM ("+lfm+")");
		
		return data;
	}
	
	private static double predictSimpleBiased(TierData data, Rating predRating, int userIndex, int movieIndex) {
		return data.getMovieMean()
				+ predRating.getMovie().getBias()
				+ predRating.getUser().getBias();
	}
	
	private static double predictCF(TierData data, Rating predRating, int userIndex, int movieIndex) {
		double ratingcf = 0.0, rating = 0.0;
		
		// Hierna moet de predictratings matrix gevuld worden.
		// De formule die hier voor gebruikt wordt is,
		// per door de user gerate film, nemen we de rating van deze film+ plus de diff van deze film.
		// We vermenigvuldigen het met count, om een waarde aan te geven. Dit tellen we allemaal bij elkaar op
		// Dan delen we het door de som van counts van alle meegenomen waarden
		// sum(count*(userrating+diffwaard) )/ sum counts
		
		// Calculate the rating
		double[] userColumn = data.getInputMatrix().getColumn(userIndex);
		
		double teller = 0.0;
		double noemer = 0.0;
		for (int i = 0; i < userColumn.length; ++i) {
			if (userColumn[i] > 0.0) {
				double diffEntry = data.getDiffMatrix().getEntry(i, movieIndex);
				
				if (!Double.isNaN(diffEntry)) {
					//ik ga proberen eerst het gemiddelde van de film er af te trekken en dan heb bij de rating, 
					//dit bij de biased rating op te tellen.
					teller += (userColumn[i] + diffEntry) * data.getCountMatrix().getEntry(i, movieIndex); 
					noemer += data.getCountMatrix().getEntry(movieIndex,i);
				}
			}
		}
		
		// Calculate the rating
		return (teller / noemer);
	}
	
	private static double predictLFM(TierData data, Rating predRating, int userIndex, int movieIndex) {
		RealVector movieVector, userVector;
		
		movieVector = data.getMovieFeatureMatrix().getRowVector(movieIndex);
		userVector = data.getFeatureUserMatrix().getColumnVector(userIndex);
		
		// r_xi = u + b_x + b_i + q_i*p_x
		// rating = moviemean + userbias + moviebias + usermovieinteraction
		return data.getMovieMean() + predRating.getUser().getBias() + predRating.getMovie().getBias() + movieVector.dotProduct(userVector);
	}

	private static void draw(TierData data) {
		JFreeChart chart;
		ChartFrame frame;
		XYSeries biasedSerie, cfSerie, lfmSerie;

		XYSeriesCollection collection = new XYSeriesCollection();
		biasedSerie = new XYSeries("Biased");
		cfSerie = new XYSeries("CF");
		lfmSerie = new XYSeries("LFM");
		
		collection.addSeries(biasedSerie);
		collection.addSeries(cfSerie);
		collection.addSeries(lfmSerie);
		
		// Create chart
		chart = ChartFactory.createScatterPlot("RMSE per user, showing only model with lowest error", "Number of ratings", "RMSE", collection);
		
		// Into a frame
		frame = new ChartFrame("RMSE", chart);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		// Calculate the RMSE for each user for each model
		for(Rating rating : data.getVerificationRatings()) {
			int userIndex = rating.getUser().getIndex() - 1;
			int movieIndex = rating.getMovie().getIndex() - 1;
			
			// Get prediction for all 3 models
			double bp = predictSimpleBiased(data,rating,userIndex,movieIndex);
			double cp = predictCF(data,rating,userIndex,movieIndex);
			double lp = predictLFM(data,rating,userIndex,movieIndex);
			bp = Math.max(Math.min(5.0,bp),1.0);
			cp = Math.max(Math.min(5.0,cp),1.0);
			lp = Math.max(Math.min(5.0,lp),1.0);
			
			// Get actual rating
			double actual = rating.getRating();
			
			User user = rating.getUser();
			user.addBiasedError(actual - bp);
			user.addCFError(actual - cp);
			user.addLFMError(actual - lp);
		}
		
		// Add data points, 3 for each user
		for(User user : data.getUserList()) {
			// get number of ratings the user made
			int numberOfUserRatings = user.getNumberOfRatings();

			double a = user.getBiasedRMSE();
			double b = user.getCFRMSE();
			double c = user.getLFMRMSE();
			
			if(a < b && a < c) {
//				biasedSerie.add(numberOfUserRatings, a);
			} else if(b < c) {
				cfSerie.add(numberOfUserRatings, b);
			} else {
//				lfmSerie.add(numberOfUserRatings, c);
			}
		}
	}
}
