package script;

import org.osbot.rs07.api.Quests.Quest;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

/**
 * Autosolver for Sheep Shearer
 * Requires "Ball of Wool" x20
 * 
 * @author user
 *
 */

@ScriptManifest(
author = "LiamN", 
info = "Solves the Sheep Shearer Quest",
logo = "", 
name = "AQSheepShearer",
version = 0)
public class Main extends Script {
	private static final Area FRED_AREA = new Area(3191, 3274, 3184, 3270);
			

	public boolean questComplete() {
		return getQuests().isComplete(Quest.SHEEP_SHEARER);
	}
	
	@Override public void onStart() {
		if (getInventory().getAmount("Ball of Wool") < 20) {
			log("Not enough Balls of Wool! 20x required");
			stop(false);
		}
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		if (!questComplete()) {
			util.Quests.talkToNPC(this, FRED_AREA, "Fred the Farmer", 1, 1);
		} else {
			stop(false);
		}
		
		return 100;
	}
}
