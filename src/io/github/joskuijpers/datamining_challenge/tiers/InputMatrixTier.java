package io.github.joskuijpers.datamining_challenge.tiers;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.model.Rating;

public class InputMatrixTier extends Tier {
	public static TierData run(TierData data) {
		RealMatrix inputMatrix;
		
		// Create matrix for input data
		inputMatrix = new BlockRealMatrix(data.getMovieList().size(), data.getUserList().size());
		
		// For every rating in the rating list, set value in matrix
		for (Rating rating : data.getRatingList()) {
			if(rating.isIgnored())
				continue;
			
			inputMatrix.setEntry(rating.getMovie().getIndex() - 1, rating.getUser().getIndex() - 1, rating.getRating());
		}
		
		data.setInputMatrix(inputMatrix);
		
		return data;
	}
}
