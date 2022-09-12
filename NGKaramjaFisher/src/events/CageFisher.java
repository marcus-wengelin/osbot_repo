package events;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.ConditionalSleep;

public class CageFisher extends Event {
	private NPC fishingSpot;
	
	public void findSpot() {
		NPC npc = getNpcs().closest(new Filter<NPC>() {
			@Override 
			public boolean match(NPC npc) {
				return (npc.getName().equals("Fishing spot") && npc.hasAction("Cage"));
			}
		});
		
		if (npc == null) setFailed();
		else			 fishingSpot = npc;
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (getInventory().contains("Raw tuna")) getInventory().dropAll("Raw tuna");
		
		if (!getInventory().contains("Lobster pot")) {
			log("[ERROR] No lobster pot found");
		} else if (getSkills().getStatic(Skill.FISHING) < 40) {
			log("[ERROR] Too low fishing level");
		} else {
			if (!getInventory().isFull()) {
				if (!myPlayer().isAnimating() && !myPlayer().isMoving()) {
					findSpot();
					fishingSpot.interact("Cage");
					getMouse().moveOutsideScreen();
					new ConditionalSleep(random(5_000, 10_000)) {
						@Override public boolean condition() throws InterruptedException {
							return !myPlayer().isAnimating();
						}
					}.sleep();
				}
			} else setFinished();
		}
		return random(1_000, 5_000);
	}
}
