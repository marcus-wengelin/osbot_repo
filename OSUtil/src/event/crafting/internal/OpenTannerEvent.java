package event.crafting.internal;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.event.Event;

public class OpenTannerEvent extends Event {

	
	private String tannerName;
	public OpenTannerEvent(String tannerName) {
		this.tannerName = tannerName;
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (myPlayer().isMoving()) {
			log("[OpenTannerEvent][-] Player is moving, waiting");
			return random(1_000, 4_000);
		}
		
		if (util.Misc.isTannerOpen(this)) {
			log("[OpenTannerEvent][+] Tanning window open");
			setFinished();
			return 100;
		}
		
		NPC tanner = getNpcs().closest(tannerName);
		log("[OpenTannerEvent][*] Getting closest tanner");
		if (tanner != null && getMap().canReach(tanner)) {
			tanner.interact("Trade");
			return random(1_000, 4_000);
		} else {
			log("[OpenTannerEvent][-] Event failed");
			setFailed();
		}
		return 100;
	}

}
