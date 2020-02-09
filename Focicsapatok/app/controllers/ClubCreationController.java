package controllers;

import models.Club;
import play.data.validation.Required;
import play.mvc.Controller;

public class ClubCreationController extends Controller {

	public static void createClubForm() {
		render("@Application.Club.createClub");
	}
	
	public static void createClub(
			@Required(message = "A klub nevének megadása kötelező!") String clubName,
			@Required(message = "Az alapítás évének megadása kötelező!") Integer clubEstablished,
			@Required(message = "Az ország megadása kötelező!") String clubCountry,
			@Required(message = "A megye megadása kötelező!") String clubRegion,
			@Required(message = "A város megadása kötelező!") String clubCity) {
		
		
		if (clubEstablished == null || clubEstablished < 1862 || clubEstablished > 2019) {
			validation.addError("clubEstablished", "Az alapítás éve 1862 és 2019 közötti kell legyen.");
		}
		
		
		// Futtatunk egy selectet név alapján
		Club club = Club.find(" clubName = ? ", clubName).first();
		if (club != null) { //akkor lesz != null, ha már létezik!
			validation.addError("clubName", "Ilyen nevű klub már létezik.");
		}
		
		
		// Nézzük meg, vannak-e errorok. Ha igen, visszadobjuk a usert a formra.
		if (validation.hasErrors()) {
			params.flash(); // Átmásoljuk a paramétereket. Flash - az előző request paraméterei
			render("@Application.Club.createClub");
		} else {
			// Mentés az adatbázisba.
			club = new Club();
			club.clubName = clubName;
			club.clubEstablished = clubEstablished;
			club.clubCountry = clubCountry;
			club.clubRegion = clubRegion;
			club.clubCity = clubCity;
			
			club.save(); // Ekkor fut le az INSERT gyakorlatilag.
			
			/**
			 * Átirányítás a könyvtárakat listázó oldalra
			 */
			ClubController.index(null);
		}
	}
}
