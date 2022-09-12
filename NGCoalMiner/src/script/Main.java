package script;

import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import event.BankingEvent;
import event.MiningEvent;

@ScriptManifest(
		author = "LiamN",
		info = "Mines coal in the mining guild",
		logo = "",
		name = "NGCoalMinerXXX", 
		version = 0
)
public class Main extends Script {
	private long startTime;
	
	public static String formatTime(long ms) {
		long s = (ms / 1000L) % 60L;
		long m = (ms / (1000L * 60L)) % 60L;
		long h = (ms / (1000L * 60L * 60L));
		return String.format("%02d:%02d:%02d",h,m,s);
	}
	
	@Override
	public void onStart() {
		log("Starting NGCoalMinerXXX");
		
		startTime = System.currentTimeMillis();
		getExperienceTracker().start(Skill.MINING);
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		if (getSkills().getStatic(Skill.MINING) < 60) {
			log("[ERROR] Must be lvl 60 mining or higher");
			stop(false);
		}
		
		if (!getInventory().isFull())
			execute(new MiningEvent());
		else
			execute(new BankingEvent());
		
		return 300;
	}
	
	@Override
	public void onExit() {
		log("Stopping NGCoalMinerXXX");
	}
	
	@Override
	public void onPaint(Graphics2D g) {
		g.setColor(Color.RED);
		/* Draw mouse */
		int mx = (int)getMouse().getPosition().getX();
		int my = (int)getMouse().getPosition().getY();
		g.drawLine(mx-5, my, mx+5, my);
		g.drawLine(mx, my-5, mx, my+5);
		
		long ms = startTime == 0 ? 0 : System.currentTimeMillis() - startTime;
		g.setColor(Color.GREEN);
		g.drawString(String.format("NGCoalMiner: %s", formatTime(ms)), 5, 20);
	}
}
