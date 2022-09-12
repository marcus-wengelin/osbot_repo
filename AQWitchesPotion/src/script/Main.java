package script;

import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import util.Graphics;

@ScriptManifest(
author = "LiamN", 
info = "Solves the Witch's Potion quest", 
logo = "", 
name = "AQWitchesPotion",
version = 0.00)
public class Main extends Script {
	
	private static final Area HETTY_AREA = 
			new Area(2965, 3208, 2970, 3203);
	
	private static final int CONFIG_ID = 67;
	
	private long startTime = 0;
	
	public int getProgress() {
		return this.configs.get(CONFIG_ID);
	}
	
	/* Stage 0 */
	public void startQuest() {
		util.Quests.talkToNPC(this, HETTY_AREA, "Hetty", 1, 1);
	}
	
	/* Stage 1 */
	public void gatherItems() {
		if (!inventory.contains("Eye of Newt")) {
			log("No Eye of Newt! 1x required");
			stop(false);
		} else if (!inventory.contains("Burnt meat")) {
			log("No Burnt Meat! 1x required");
			stop(false);
		} else if (!inventory.contains("Onion")) {
			log("No Onion! 1x required");
			stop(false);
		}
	}
	
	/* Stage 2 */
	public void drinkFromCauldron() {
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		switch (getProgress()) {
		case 0:
			startQuest();
			break;
		case 1:
			gatherItems();
			break;
		case 2:
			drinkFromCauldron();
		default:
			stop(false);
			break; 
		}
		return 100;
	}
	
	@Override
	public void onStart() {
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void onPaint(Graphics2D g) {
		Graphics.renderCursor(this, g, Color.RED);
		long ms = startTime == 0 ? 0 : System.currentTimeMillis() - startTime;
		g.setColor(Color.GREEN);
		g.drawString(String.format("AQ Romeo & Juliet: %s", Graphics.formatTime(ms)), 5, 20);
	}

}

