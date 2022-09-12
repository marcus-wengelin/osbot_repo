package event.training.combat.internal;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.event.Event;

import event.combat.KillTargetEvent;
import event.combat.LootCloseEvent;
import event.cooking.CookEvent;

/**
 * Goes to Lumbridge and cooks chicken
 * 
 * @author user
 *
 */
public class GetChickenEvent extends Event {
	
	private Area LUMBRIDGE_RANGE = new Area(3225, 3292, 3228, 3289);
	private Area LUMBRIDGE_CHICKENS = new Area(3235, 3288, 3231, 3300);
	
	private int amount;
	
	public GetChickenEvent(int amount) {
		this.amount = amount;
	}
	
	private void getRawChicken() {
		if (!LUMBRIDGE_CHICKENS.contains(myPlayer()))
			getWalking().webWalk(LUMBRIDGE_CHICKENS);
		
		NPC target = getNpcs().closest(new Filter<NPC>() {
			@Override public boolean match(NPC npc) {
				return npc.getName().equals("Chicken") && getMap().canReach(npc) && !npc.isUnderAttack();
			}
		});
		
		if (target == null) {
			log("[GetChickenEvent][-] No chickens found");
			return;
		}
		
		KillTargetEvent killEvent = new KillTargetEvent(target);
		execute(killEvent);
			
		if (killEvent.hasFailed())
			return;
			
		LootCloseEvent lootEvent = new LootCloseEvent(killEvent.getDeathPosition(), 10_000, new Filter<GroundItem>() {
			@Override public boolean match(GroundItem gi) {
					return gi.getName().equals("Raw chicken");
			}
		});
		execute(lootEvent);
	}
	
	private void cookChicken() {
		if (!LUMBRIDGE_RANGE.contains(myPlayer()))
			getWalking().webWalk(LUMBRIDGE_RANGE);
		
		execute(new CookEvent(LUMBRIDGE_RANGE, "Raw chicken"));
		getInventory().dropAll("Burnt chicken");
	}

	@Override public int execute() throws InterruptedException {
		// Finish condition
		if (getInventory().getAmount("Cooked chicken") >= amount) {
			log(String.format("[GetChickenEvent][+] %d amount of chicken acquired", amount));
			setFinished();
			return 100;
		}
		
		// Fail condition
		if (!getInventory().contains("Raw chicken") && getInventory().getEmptySlots() < amount) {
			log("[GetChickenEvent][-] Not enough inventory space");
			setFailed();
			return 100;
		}
		
		if (getInventory().getEmptySlots() > 4) {
			getRawChicken();
		} else {
			cookChicken();
		}
		
		return 100;
	}

}
