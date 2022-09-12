package event.banking.internal;

import org.osbot.rs07.api.filter.ActionFilter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.event.WebWalkEvent;



public class WalkToClosestBankEvent extends Event {

	private static final Area[] BANK_AREA_LIST = {
		Banks.AL_KHARID,
		Banks.ARCEUUS_HOUSE,
		Banks.ARDOUGNE_NORTH,
		Banks.ARDOUGNE_SOUTH,
		Banks.CAMELOT,
		Banks.CANIFIS,
		Banks.CASTLE_WARS,
		Banks.CATHERBY,
		Banks.DRAYNOR,
		Banks.DUEL_ARENA,
		Banks.EDGEVILLE,
		Banks.FALADOR_EAST,
		Banks.FALADOR_WEST,
		Banks.GNOME_STRONGHOLD,
		Banks.GRAND_EXCHANGE,
		Banks.HOSIDIUS_HOUSE,
		Banks.LOVAKENGJ_HOUSE,
		Banks.LOVAKITE_MINE,
		Banks.LUMBRIDGE_LOWER,
		Banks.LUMBRIDGE_UPPER,
		Banks.PEST_CONTROL,
		Banks.PISCARILIUS_HOUSE,
		Banks.SHAYZIEN_HOUSE,
		Banks.TZHAAR,
		Banks.VARROCK_EAST,
		Banks.VARROCK_WEST,
		Banks.YANILLE
	};
	
	public boolean isBankBoothReachable() {
		RS2Object bankBooth = getObjects().closest(new ActionFilter<RS2Object>("Bank"));
		return bankBooth != null && getMap().canReach(bankBooth);
	}
	
	public boolean playerInsideBank() {
		for (Area bank : BANK_AREA_LIST) {
			if (bank.contains(myPlayer()))
				return true;
		}
		return false;
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (playerInsideBank() && isBankBoothReachable()) {
			log("[WalkToClosestBankEvent][+] Completed walk to bank");
			setFinished();
			return 100;
		}
		
		Event e = new WebWalkEvent(BANK_AREA_LIST);
		
		execute(e);
		
		return 100;
	}

}
