package event.training.combat.internal;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.event.Event;

import event.combat.BuryBonesEvent;
import event.combat.KillTargetEvent;
import event.combat.LootCloseEvent;

public class KillChickenEvent extends Event {
	private Area LUMBRIDGE_CHICKENS = new Area(3235, 3288, 3231, 3300);
	
	private NPC findChicken() {
		return getNpcs().closest(new Filter<NPC>() {
			@Override public boolean match(NPC npc) {
				return getMap().canReach(npc) && !npc.isUnderAttack();
			}
		});
	}
	
	@Override public int execute() throws InterruptedException {
		if (!LUMBRIDGE_CHICKENS.contains(myPlayer())) {
			getWalking().webWalk(LUMBRIDGE_CHICKENS);
		}
		
		NPC target = findChicken();
		if (target == null) return 1_000;
		
		KillTargetEvent killEvent = new KillTargetEvent(target);
		execute(killEvent);
		
		if (killEvent.hasFailed()) {
			setFailed();
			return 100;
		}
		
		execute(new LootCloseEvent(killEvent.getDeathPosition(), 5_000,
				new Filter<GroundItem>() {
					@Override public boolean match(GroundItem gi) {
						return gi.getName().equals("Feather") ||
							   gi.getName().equals("Bones");
					}
			
		}));
		
		execute(new BuryBonesEvent());
		
		setFinished();
		return 100;
	}

}
