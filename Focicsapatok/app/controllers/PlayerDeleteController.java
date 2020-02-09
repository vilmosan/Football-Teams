package controllers;

import models.Club;
import models.Player;
import play.mvc.Controller;

public class PlayerDeleteController extends Controller{

	public static void deletePlayer(Long playerId) {
			Player player = Player.findById(playerId);
			
			if (player != null) { // Ha nem NULL, akkor létezik
				Club club = player.owningClub;
				player.delete(); // DELETE
				ClubDetailsController.clubDetails(club.clubId);
			} else {
				flash.put("errorMessage", "Nem létező játékos.");
				ClubController.index(null);
			}
	}
}
