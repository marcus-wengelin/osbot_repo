package event.crafting;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.event.Event;

import event.crafting.internal.CloseTannerEvent;
import event.crafting.internal.OpenTannerEvent;
import event.crafting.internal.TanHidesUIEvent;

public class TanHidesEvent extends Event {
	
	private static final Area TAN_AREA = new Area(3270, 3194, 3277, 3189);

	@Override public int execute() throws InterruptedException {
		if (!TAN_AREA.contains(myPlayer())) {
			log("[TanHidesEvent] Walking to tanner");
			getWalking().webWalk(TAN_AREA);
			return random(1_000, 5_000);
		} else {
			log("[TanHidesEvent] Using tanner");
			OpenTannerEvent openTanner = new OpenTannerEvent("Ellis");
			execute(openTanner);
			if (openTanner.hasFailed()) {
				log("[TanHidesEvent] openTanner failed");
				setFailed();
				return 100;
			}

			TanHidesUIEvent tanUiEvent = new TanHidesUIEvent("Soft leather");
			execute(tanUiEvent);
			if (tanUiEvent.hasFailed()) {
				log("[TanHidesEvent] tanUiEvent failed");
				setFailed();
				return 100;
			}

			CloseTannerEvent closeTanner = new CloseTannerEvent();
			execute(closeTanner);
			if (closeTanner.hasFailed()) {
				log("[TanHidesEvent] closeTanner failed");
				setFailed();
				return 100;
			}

			setFinished();
			return 0;
		}
	}

}
