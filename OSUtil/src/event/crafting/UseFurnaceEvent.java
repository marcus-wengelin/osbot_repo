package event.crafting;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.event.Event;

import event.crafting.internal.FurnaceUIEvent;
import event.crafting.internal.OpenFurnaceEvent;
import event.misc.AnimationFilterEvent;

public class UseFurnaceEvent extends Event {
	private Area area;
	
	public UseFurnaceEvent(Area area) {
		this.area = area;
	}

	@Override public int execute() throws InterruptedException {	
		if (!area.contains(myPlayer())) getWalking().webWalk(area);
		
		OpenFurnaceEvent openEvent = new OpenFurnaceEvent();
		execute(openEvent);
		
		if (openEvent.hasFailed()) {
			log("[UseFurnaceEvent][-] openEvent failed");
			setFailed();
			return 100;
		}
		
		FurnaceUIEvent uiEvent = new FurnaceUIEvent("Iron bar");
		execute(uiEvent);
		if (uiEvent.hasFailed()) {
			log("[UseFurnaceEvent][-] uiEvent failed");
			setFailed();
			return 100;
		}
		
		AnimationFilterEvent animationFilter = new AnimationFilterEvent(3_000);
		execute(animationFilter);
		
		
		/*CloseFurnaceEvent closeEvent = new CloseFurnaceEvent();
		execute(closeEvent);
		if (closeEvent.hasFailed()) {
			log("[UseFurnaceEvent][-] closeEvent failed");
			setFailed();
			return 100;
		}*/
		
		log("[UseFurnaceEvent][+] Event finished!");
		setFinished();
		return 100;
	}
}
