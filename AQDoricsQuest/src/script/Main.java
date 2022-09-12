package script;

import org.osbot.rs07.api.Quests.Quest;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

/**
 * Autosolver for Doric's Quest, requires the player having the
 * quest items in inventory.
 * 
 * Items:
 *  2x iron ore, 
 *  4x copper ore, 
 *  6 clay un-noted
 * 
 * @author user
 *
 */

@ScriptManifest(
author = "LiamN", 
info = "Solves the Doric's Quest",
logo = "", 
name = "AQDoricsQuest",
version = 0)
public class Main extends Script {
	private static final Area DORIC_AREA = new Area(2950, 3452, 2953, 3449);
	
	public boolean questComplete() {
		return getQuests().isComplete(Quest.DORICS_QUEST);
	}
	
	@Override public void onStart() {
		if (getInventory().getAmount(constants.Ore.COPPER.getName()) < 4) {
			log("Not enough copper ore! 4x required");
			stop(false);
		} else if (getInventory().getAmount(constants.Ore.IRON.getName()) < 2) {
			log("Not enough iron ore! 2x required");
			stop(false);
		} else if (getInventory().getAmount(constants.Ore.CLAY.getName()) < 6) {
			log("Not enough clay! 6x required");
			stop(false);
		}
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		
		if (!questComplete()) {
			util.Quests.talkToNPC(this, DORIC_AREA, "Doric", 1, 1);
			new ConditionalSleep(10_000) {
				@Override public boolean condition() throws InterruptedException {
					return questComplete();
				}
			}.sleep();
		} else {
			stop(false);
		}
		
		return 100;
	}
}
