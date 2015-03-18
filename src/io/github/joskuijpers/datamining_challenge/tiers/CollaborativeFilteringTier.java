package io.github.joskuijpers.datamining_challenge.tiers;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.math.Matrix;
import io.github.joskuijpers.datamining_challenge.math.Vector;
import io.github.joskuijpers.datamining_challenge.model.Rating;
import io.github.joskuijpers.datamining_challenge.model.User;

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
		Matrix imputMatrix, diffMatrix, countMatrix;


		// Create matrix for input data
		imputMatrix = new Matrix(data.getMovieList().size(), data.getUserList()
				.size());
		System.out.println("UserListsize "+data.getUserList().size());
		System.out.println("MovieListsize "+data.getMovieList().size());
		

		// For every rating in the rating list, set value in matrix
		for (Rating rating : data.getRatingList()) {
			imputMatrix.set(rating.getMovie().getIndex() - 1, rating.getUser()
					.getIndex() - 1, rating.getRating());
		}
		
		data.setImputMatrix(imputMatrix);
		
		
		// Create matrix for calculated data
		diffMatrix = new Matrix(data.getMovieList().size(), data.getMovieList()
				.size());
		
		countMatrix = new Matrix(data.getMovieList().size(), data.getMovieList()
				.size());
		
		// Calculate the values for calculated data
		for (int i = 0; i < data.getMovieList().size(); i++) {
			for (int j = 0; j < data.getMovieList().size(); j++) {
				if(i==j){
					diffMatrix.set(i,j,0.0f);
					countMatrix.set(i, j, 0.0f);
				}
				Float diff = 0.0f;
				Float count = 0.0f;
				//en nu moeten we er door heen gaan lopen
				//dit moet sneller kunnen
				for (int k = 0; k < data.getUserList().size(); k++) {
					if(imputMatrix.get(j, k)>0&&imputMatrix.get(i, k)>0){
						diff += imputMatrix.get(j, k)-imputMatrix.get(i, k);
						count++;
					}
				}
				
				
				diffMatrix.set(i,j,diff/count);//het vullen van de matrix met het gemiddelde verschil tussen twee films.
				countMatrix.set(i, j, count);//het bijhouden van de count.
			}
			
		}
		System.out.println(diffMatrix.get(4, 5));
		System.out.println(countMatrix.get(4, 5));
		
		System.out.println(diffMatrix.get(100, 130));
		System.out.println(countMatrix.get(100, 130));
		
        data.setDiffMatrix(diffMatrix);
		data.setCountMatrix(countMatrix);
		
		return data;
	}
}