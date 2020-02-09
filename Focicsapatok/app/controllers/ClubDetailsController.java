package controllers;

import models.Club;
import models.Player;
import play.data.validation.Required;
import play.mvc.Controller;

public class ClubDetailsController extends Controller {

	public static void clubDetails(@Required(message="Azonosító szükséges") Long clubId){
		Club club = null;
		
		if (clubId != null){
			club = Club.findById(clubId);
		}
		else {
			ClubController.index(null);
		}
		
		// Warning üzenet ha valamelyik poszt betöltetlen.
		int kapus = 0;
		int vedo = 0;
		int kozeppalyas = 0;
		int csatar = 0;
		
		for (int i = 0; i < club.players.size(); i++) 
		{ 
			Player singleMember = club.players.get(i);
			
			if	   (singleMember.playerPosition.equals("Kapus")) kapus++; 
			else if(singleMember.playerPosition.equals("Védő")) vedo++; 
			else if(singleMember.playerPosition.equals("Középpályás")) kozeppalyas++; 
			else if(singleMember.playerPosition.equals("Csatár")) csatar++; 
		
		}
		if(kapus == 0) validation.addError("errorMessage1", "A csapat nem rendelkezik kapussal!");
		if(vedo == 0) validation.addError("errorMessage2", "A csapat nem rendelkezik védővel!");
		if(kozeppalyas == 0) validation.addError("errorMessage3", "A csapat nem rendelkezik középpályással!");
		if(csatar == 0) validation.addError("errorMessage4", "A csapat nem rendelkezik csatárral!");
		
		renderArgs.put("club", club);
		render("@Application.Club.clubDetails");
	}
}
