package de.hsh.steam;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.hsh.steam.entities.Genre;
import de.hsh.steam.entities.Score;
import de.hsh.steam.entities.Series;
import de.hsh.steam.entities.Streamingprovider;
import de.hsh.steam.repositories.SerializedSeriesRepository;
import de.hsh.steam.services.SteamService;

public class SteamServiceTest {

	private final static Logger LOG = Logger.getLogger(SteamServiceTest.class.getName());
	
	static SteamService facade = SteamService.getInstance();

	static SerializedSeriesRepository repository = SerializedSeriesRepository.getInstance();

	@BeforeAll
	public static void initData() {
		// first clear all existing data to start fresh
		facade.clear();
		// init users
		facade.newUser("daisy",  "password1");
		facade.newUser("donald", "password2");
		facade.newUser("goofy",  "password3");
		
		// init series
		String username = "daisy";
		facade.addOrModifySeries("Dark", 3, Genre.Drama, Streamingprovider.Netflix, username, Score.good, "strange");	
		facade.addOrModifySeries("Breaking Bad", 7, Genre.Drama, Streamingprovider.Netflix, username, Score.very_good, "interesting");	
		facade.addOrModifySeries("Game Of Thrones", 50, Genre.ScienceFiction, Streamingprovider.Sky, username, Score.mediocre, "no  comment");		
		facade.addOrModifySeries("Narcos", 2, Genre.Thriller, Streamingprovider.Sky, username, Score.mediocre, "no  comment");		
		
		username = "donald";
		facade.addOrModifySeries("Dark", 3, Genre.ScienceFiction, Streamingprovider.Netflix, username, Score.mediocre, "strange");	
		facade.addOrModifySeries("Stranger Things", 4, Genre.ScienceFiction, Streamingprovider.AmazonPrime, username, Score.very_good, "interesting");	
	}

	@AfterAll
	public static void shutdown(){
		facade.clear();
	}

	@Test
	public void shouldLogin(){
		boolean actual = facade.login("daisy", "password1");
		assertEquals(true, actual);
	}

	@Test
	public void userstory() {
		
		String username = "daisy";
		facade.login(username, "password1");
		facade.addOrModifySeries("Casa del Papel", 4, Genre.Drama, Streamingprovider.Netflix, username, Score.good, "no comment");		
		
		//ändere bereits vorhandene Serie
        facade.addOrModifySeries("Narcos", 3, Genre.Comedy, Streamingprovider.AmazonPrime, username, Score.good, "strange");	
		
		LOG.info("Print all series for user "  + username +": \n" + repository.printAllSeries(username) );
		
		ArrayList<Series>searchResult;
		searchResult = facade.search(username, Genre.Drama, Streamingprovider.Netflix, Score.good);
		for (Series s: searchResult)
			LOG.info("Search Result for " + username + "Drama on Netflix and Score good: \n" + s + "  score:" + facade.getRating(s.getTitle(), username).getScore());
		
		username = "donald";
		facade.login(username, "password2");
		facade.addOrModifySeries("Casa del Papel", 4, Genre.Drama, Streamingprovider.Netflix, username, Score.mediocre, "no  comment");		
		facade.addOrModifySeries("Fleabag", 4, Genre.Drama, Streamingprovider.AmazonPrime, username, Score.good, "no  comment");	
		LOG.info("Print all series for user "  + username +": \n" + repository.printAllSeries(username) );
		
		// Suche für Donald im Genre Drama auf Netflix mit Mediocre Score
		searchResult = facade.search("donald", Genre.Drama, Streamingprovider.Netflix, Score.mediocre);
		for (Series s: searchResult)
			LOG.info("Search Result for " + username + " Drama on Netflix and Score mediocre: \n" + s + "  score:" + facade.getRating(s.getTitle(), username).getScore());	
		
		// Suche nach allen Serien mit 'ing' im Titel
		searchResult = facade.getAllSeriesWithTitle("ing");
		for (Series s: searchResult)
			LOG.info("Search Result for all Series with 'ing' in Title: \n" + s.toString());
	}
}
