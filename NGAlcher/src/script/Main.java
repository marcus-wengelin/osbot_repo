package script;

import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import events.AlchingEvent;
import gui.App;

@ScriptManifest(
author="LiamN",
info = "Simple auto alcher",
logo = "",
name = "NGAutoAlcher",
version = 0

)
public class Main extends Script {
	private long startTime = 0;
	private App app;
	
	
	
	@Override public void onStart() {
		log("[NGAutoAlcher] Starting script");
		this.startTime = System.currentTimeMillis();
		app = App.startApp();
		
		if (getSkills().getStatic(Skill.MAGIC) < 55) {
			log("[NGAutoAlcher] Must be lvl 55 magic or above");
			stop(false);
		}
	}
	
	@Override public int onLoop() {
		if (app.hasApplied()) {
			if (getInventory().contains(app.getItemNames())) {
				execute(new AlchingEvent(app.getItemNames()));
			} else {
				log("[NGAutoAlcher] Out of items");
				stop(false);
			}
		}
		return 300;
	}
	
	@Override public void onPaint(Graphics2D g) {
		g.setColor(Color.RED);
		/* Draw mouse */
		int mx = (int)getMouse().getPosition().getX();
		int my = (int)getMouse().getPosition().getY();
		g.drawLine(mx-5, my, mx+5, my);
		g.drawLine(mx, my-5, mx, my+5);
		
		long ms = startTime == 0 ? 0 : System.currentTimeMillis() - startTime;
		app.setStatistics(formatTime(ms));
	}
	
	@Override public void onExit() {
		log("[NGAutoAlcher] Stopping script");
		app.dispose();
	}
	
	public static String formatTime(long ms) {
		long s = (ms / 1000L) % 60L;
		long m = (ms / (1000L * 60L)) % 60L;
		long h = (ms / (1000L * 60L * 60L));
		return String.format("%02d:%02d:%02d",h,m,s);
	}
	
}
