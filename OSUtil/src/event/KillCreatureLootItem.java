package event;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.ConditionalSleep;

public class KillCreatureLootItem extends Event {
	/**
	 * webWalk to a given location, kill a creature and loot an item
	 */
	private Area area;
	private String creatureName;
	private int creatureMaxLevel;
	private String itemName;
	private int itemAmount;
	
	public KillCreatureLootItem(Area area, String creatureName, int creatureMaxLevel, String itemName, int itemAmount, boolean itemStackable) {
		this.area = area;
		this.creatureName = creatureName;
		this.creatureMaxLevel = creatureMaxLevel;
		this.itemName = itemName;
		this.itemAmount = itemAmount;
		
		if (itemStackable) {
			// 1 slot needed for stackable items
			if (!inventory.contains(itemName) && inventory.getEmptySlots() < 1) {
				debugLog("Not enough inventory space");
				setFailed();
			}
		} else {
			// X slots needed for non-stackable items
			if (inventory.getEmptySlots() < inventory.getAmount(itemName) - itemAmount) {
				debugLog("Not enough inventory space");
				setFailed();
			}
		}
	}
	
	private void debugLog(String msg) {
		log("[DEBUG][KillCreatureLootItem]: "+msg);
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (inventory.getAmount(itemName) >= itemAmount) setFinished();
		
		if (!area.contains(myPlayer())) {
			getWalking().webWalk(area);
		} else {			
			if (!myPlayer().isUnderAttack()) {
				NPC target = npcs.closest(new Filter<NPC> () {
					@Override public boolean match(NPC npc) {
						return npc.getName().equals(creatureName) && npc.getLevel() <= creatureMaxLevel;
					}
				});
				
				if (target != null) {
					target.interact("Attack");
					
					new ConditionalSleep(10_000) {
						@Override public boolean condition() throws InterruptedException {
							return myPlayer().isUnderAttack();
						}
						
					}.sleep();
					
					
				}
			}
		}
		
		
		return 100;
	}

}
