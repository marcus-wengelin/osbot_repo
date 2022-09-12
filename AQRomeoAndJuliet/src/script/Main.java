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
info = "Solves the Romeo & Juliet quest", 
logo = "", 
name = "AQRomeoAndJuliet", 
version = 0.01)
public class Main extends Script {
	
	private static final int CONFIG_ID = 144;
	
	private static final Area ROMEO_AREA =
			new Area(3206, 3435, 3218, 3416);
	private static final Area JULIET_AREA =
			new Area(3149, 3425, 3166, 3441).setPlane(1);
	private static final Area LAWRENCE_AREA =
			new Area(3258, 3483, 3253, 3473);
	private static final Area APOTHECARY_AREA =
			new Area(3192, 3406, 3198, 3402);
	private static final Area CADAVA_AREA =
			new Area(3278, 3374, 3272, 3370);
	
	private long startTime = 0;
	
	public int getProgress() {
		return this.configs.get(CONFIG_ID);
	}
	
	/* Stage 0 */
	public void startQuest() {
		talkToRomeo(3,1,3);
	}
	
	public void talkToRomeo(int... options) {
		util.Quests.talkToNPC(this, ROMEO_AREA, "Romeo", options);
	}
	
	/* Stage 10 */
	public void talkToJuliet(int... options) {
		util.Quests.talkToNPC(this, JULIET_AREA, "Juliet", options);
	}
	
	/* Stage 30 */
	public void talkToLawrence(int... options) {
		util.Quests.talkToNPC(this, LAWRENCE_AREA, "Father Lawrence", options);
	}
	
	/* Stage 40 */
	public void talkToApothecary(int... options) {
		if (!inventory.contains("Cadava berries")) {
			if (!CADAVA_AREA.contains(myPlayer())) {
				walking.webWalk(CADAVA_AREA);
			} else {
				objects.closest("Cadava bush").interact("Pick-from");
				new ConditionalSleep(5000) {
					@Override public boolean condition() throws InterruptedException {
						return inventory.contains("Cadava berries");
					}
				}.sleep();
			}
		} else {
			util.Quests.talkToNPC(this, APOTHECARY_AREA, "Apothecary", options);
		}
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		switch (getProgress()) {
		case 0:
			startQuest();
			break;
		case 10:
			talkToJuliet(3);
			break;
		case 20:
			talkToRomeo(4);
			break;
		case 30:
			talkToLawrence();
			break;
		case 40:
			talkToApothecary(2,1);
			break;
		case 50:
			if (!getInventory().contains("Cadava Potion")) {
				talkToApothecary(2,1);
			} else {
				talkToJuliet();
			}
			break;
		case 60:
			talkToRomeo();
			break;
		case 70:
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

