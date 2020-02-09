package controllers;

import models.Club;
import models.Player;
import play.mvc.Controller;

public class ClubDeleteController extends Controller{
	
	public static void deleteClub(Long clubId) {
		
		Club clubToDelete = Club.findById(clubId); // elsődleges kulcs alapján keresünk. Nem kódban, db szinten megy a matek!
		if (clubToDelete != null) { // Ha null NEM maradt, akkor van találat!
			for (Player player : clubToDelete.players) {
				player.delete();
			}
			clubToDelete.delete();
		} else {
			// nincs találat, nem adunk át semmit.
		}
		
		ClubController.index(null);
	}
	
}
