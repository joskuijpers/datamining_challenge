package io.github.joskuijpers.datamining_challenge.tiers;

import io.github.joskuijpers.datamining_challenge.TierData;
import io.github.joskuijpers.datamining_challenge.model.Movie;
import io.github.joskuijpers.datamining_challenge.model.User;

/**
 * Een klasse om wat data uit de gegevens te halen.
 * 
 * @author Jan
 */
public class StatData extends Tier {
	
	public static TierData run(TierData data) {
		// uitrekenen gegevens userlist.
		int aantalUsers = 0;
		int aantalMannen = 0;
		int somLeeftijd = 0;
		int somLeeftijdMannen = 0;
		int somNumberRatings = 0;
		
		for (User user : data.getUserList()) {
			aantalUsers++;
			if (user.isMale()) {
				aantalMannen++;
				somLeeftijdMannen += user.getAge();
			}
			somLeeftijd += user.getAge();
			somNumberRatings += user.getNumberOfRatings();
		}
		
		float gemLeeftijd = (float) somLeeftijd / (float) aantalUsers;
		float gemLeeftijdMannen = (float) somLeeftijdMannen / (float) aantalMannen;
		float gemAantalRatings = (float) somNumberRatings / (float) aantalUsers;
		
		System.out.println("Aantal users is: " + aantalUsers);
		System.out.println("Daarvan zijn mannen: " + aantalMannen);
		System.out.println("Daarvan zijn vrouwen: " + (aantalUsers - aantalMannen));
		System.out.println("De gemiddelde leeftijd is: " + gemLeeftijd);
		System.out.println("De gemiddelde leeftijd van de mannen is: " + gemLeeftijdMannen);
		System.out.println("De gemiddelde aantal ratings is: " + gemAantalRatings);
		
		int totalmovies = 0;
		int sumNumberofRatings = 0;
		for (Movie movie : data.getMovieList()) {
			totalmovies++;
			sumNumberofRatings += movie.getNumberOfRatings();
		}
		float genNumRat = (float) sumNumberofRatings / (float) totalmovies;
		System.out.println(genNumRat);
		return data;
	}
}
