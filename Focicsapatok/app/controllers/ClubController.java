package controllers;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import models.Club;
import play.mvc.Controller;

public class ClubController extends Controller{
	
	// private static final Logger LOGGER = Logger.getLogger(ClubController.class);

	public static void index(Long clubId) {
		/**
		 * Kell az összes könyvtár
		 * Kell az összes könyv belőlük
		 * 
		 * Át kell adni a html-be
		 */
		
		List<Club> clubs = Club.findAll();
		
		/**
		 * a clubId opcionális paraméter! Lehet üres!
		 */
		if (clubId == null) {
			for (Club club : clubs) {
				renderArgs.put("mainScreen"+club.clubId, club.players);
			}
			renderArgs.put("clubs", clubs);
		} 
		else {
			Club clubToShow = Club.findById(clubId); // elsődleges kulcs alapján keresünk. Nem kódban, db szinten megy a matek!
			if (clubToShow != null) { // Ha null NEM maradt, akkor van találat!
				renderArgs.put("clubs", Arrays.asList(clubToShow));
			} else {
				// nincs találat, nem adunk át semmit
			}
		}
		
		render("@Application.index");
	}
}
