package io.github.joskuijpers.datamining_challenge;

import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * 
 * @author Jan
 *
 */
public class MovieList extends ArrayList<Movie> {
	private static final long serialVersionUID = 1L;

	// Reads in a file with movies data
	public void readFile(String filename) {
		BufferedReader br = null;
		String line;
		try {
			br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				String[] movieData = line.split(";");
				add(Integer.parseInt(movieData[0]) - 1,
						new Movie(Integer.parseInt(movieData[0]),
								Integer.parseInt(movieData[1]), movieData[2]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
