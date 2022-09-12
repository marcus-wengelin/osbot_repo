package event.combat;

import org.osbot.rs07.api.filter.ActionFilter;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.event.Event;

public class EatFoodEvent extends Event {

	
	private int minHealthPercent;
	
	public EatFoodEvent(int minHealthPercent) {
		this.minHealthPercent = minHealthPercent;
		this.setAsync();
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (!getInventory().contains(new ActionFilter<Item>("Eat"))) {
			log("[EatFoodEvent][-] No edibles left");
			setFailed();
			return 100;
			//throw new NoFoodException();
		}
		
		if (myPlayer().getHealthPercent() < minHealthPercent) {
			Item edible = getInventory().getItem(new ActionFilter<Item>("Eat"));
			if (edible != null) {
				edible.interact("Eat");
			}
		}
			
		
		return random(800, 1_200);
	}
	
	@Override
	public void onEnd() {
		log("[EatFoodEvent][-] onEnd()");
	}
	
	@Override
	public void onStart() {
		log("[EatFoodEvent][+] onStart()");
	}

}
