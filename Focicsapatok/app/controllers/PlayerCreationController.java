package controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.List;

import models.Club;
import models.Player;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.mvc.Before;
import play.mvc.Controller;

public class PlayerCreationController extends Controller{	
	
	// A "Before" annótáció azt jelenti, hogy minden Controllerbeli metódus előtt lefut.
	@Before
	public static void preparePlayerCreationPage() {
		List<Club> clubs = Club.findAll();
		// Átadom a listát, hogy a template kódban is tudjam használni a template kigenerálásakor.
		renderArgs.put("clubs", clubs);
	}

	public static void createPlayerForm(long clubId) {
		List<Club> clubs = (List<Club>) renderArgs.get("clubs");
		if (clubs.size() == 0) {
			// Itt hiba van, nincs klub.
			flash.put("errorMessage", "Nincsenek klubbok!");
			ClubController.index(null);
		} else {
			// Minden rendben, van klub, tudunk létrehozni játékost.
			renderArgs.put("clubId", clubId);
			render("@Application.Club.createPlayer");
		}
	}
	
	// Megfeleltettük a HTML form inputjait a java-s metódus bemenő paramétereinek.  
	public static void createPlayer( 
			 @Required(message="NAGY A BAJ! Ha ezt az üzenetet látja, szóljon a rendszergazdának, ugyanis csak ő tudja orvosolni a problémát.") Long clubId, 
			 @Required(message="A játékos nevének kitöltése kötelező!") String playerName, 
			 @Required(message="A játékengedély számának kitöltése kötelező!") String playerLicense, 
			 @Required(message="A játékos fizetésének kitöltése kötelező!") Integer playerSalary, 
			 @Required(message="A játékos szerződésének kezdeti dátumának kitöltése kötelező!") String playerContractStart, 
			 @Required(message="A játékos szerződésének lejárati dátumának kitöltése kötelező!") String playerContractExpire, 
			 @Required(message="Az játékos pozíciójának kitöltése kötelező!") String playerPosition ) {
		
		playerContractStart = playerContractStart.trim();
		playerContractExpire = playerContractExpire.trim();
		playerPosition = playerPosition.trim(); // Ezt még akkor használtam amikor nem sikerült a lenyíló listát működésre bírjam.
		
		boolean isInvalid = isInvalidCreatePlayerRequest( validation,
														  clubId,
														  playerLicense,
														  playerSalary,
														  playerContractStart,
														  playerContractExpire,
														  playerPosition);
		
		if (isInvalid) {
			// Hiba volt validálás során.
			params.flash(); 
			renderArgs.put("clubId", clubId); // Ez azért kell, hogy a hidden input újra megkapja az értékét ha a validálás során valamelyik másik input elhasal.
			render("@Application.Club.createPlayer");
		} else {
			// Létrehozzuk a játékost.
			Player player = new Player();

			player.owningClub = Club.findById(clubId);
			player.playerName = playerName;
			player.playerLicense = playerLicense;
			player.playerSalary = playerSalary;
			player.playerContractStart = playerContractStart;
			player.playerContractExpire = playerContractExpire;	
			player.playerPosition = playerPosition;
			player.save();
			
			ClubDetailsController.clubDetails(clubId);
		}
	}
	
	public static boolean isInvalidCreatePlayerRequest( Validation validation,
													    Long clubId,
													    String playerLicense,
													    Integer playerSalary,
													    String playerContractStart,
													    String playerContractExpire,
													    String playerPosition) {
		
		// A kapott clubIdhoz tartozó Club valóban létezik
		Club club = Club.findById(clubId);
		if (club == null) {
			// Ha ez igaz, akkor nem létezik 
			validation.addError("clubId", "A kiválasztott klub már nem létezik!");
		}
		
		// Játékengedély validálása
		Player player = Player.find(" playerLicense = ? ", playerLicense).first();
		if (player != null) { //akkor lesz != null, ha már létezik!
			validation.addError("playerLicense", "Ilyen játékengedéllyel már létezik játékos.");
		}
		
		// Fizetés validálása.
		if(playerSalary != null && playerSalary < 150000) {
			validation.addError("playerSalary", "Az NLSZ-ben szerződött labdarúgók minimálbére 150.000 Ft, kérlek ennél többet adj meg.");
		}
		
		// A szerződés kezdeti dátumának validálása.
		try {
		LocalDate.parse(                   					 // Represent a date-only value, without time-of-day and without time zone.
				playerContractStart,                 		 // Input string.
			    DateTimeFormatter              				 // Define a formatting pattern to match your input string.
			    .ofPattern ( "uuuu-MM-dd" )
			    .withResolverStyle ( ResolverStyle.STRICT )  // Specify leniency in tolerating questionable inputs.
			);
		} catch (Exception e) {
		    validation.addError("playerContractStart", "Kérlek a dátumot az ÉÉÉÉ-HH-NN formátumban add meg.");
		}
		
		// A szerződés lejárati dátumának validálása.
		try {
			LocalDate.parse(                   				 	 // Represent a date-only value, without time-of-day and without time zone.
					playerContractExpire,                 	 	 // Input string.
					DateTimeFormatter              		     	 // Define a formatting pattern to match your input string.
					.ofPattern ( "uuuu-MM-dd" )
					.withResolverStyle ( ResolverStyle.STRICT )  // Specify leniency in tolerating questionable inputs.
					);
		} catch (Exception e) {
			validation.addError("playerContractExpire", "Kérlek a dátumot az ÉÉÉÉ-HH-NN formátumban add meg.");
		}
		
		// Extra validálás, amely során kiszűrjük azt, ha a lejárati dátum korábban van, mint a szerződés kezdete.
		if(playerContractStart.compareTo(playerContractExpire) > 0) {
			validation.addError("playerContractExpire", "A lejárati dátum nem lehet korábban, mint a szerződés kezdete.");
		}
		
		// Poszt validálása. // Ezt még akkor használtam amikor nem sikerült a lenyíló listát működésre bírjam.
		playerPosition = playerPosition.toLowerCase();
		if(!(playerPosition.equals("kapus") || playerPosition.equals("védő") || playerPosition.equals("középpályás") || playerPosition.equals("csatár"))) {
			validation.addError("playerPosition", "Kérlek csakis a megadott pozíciókból válassz.");
		}
		
		// Ha történt addError hívás, akkor ez TRUE lesz, ha nem, akkor FALSE.
		return validation.hasErrors();
	}
}