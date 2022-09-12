package script;

import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import app.App;
import events.CageFisher;
import events.DepositEvent;
import events.HarpoonFisher;
import util.Counter;

@ScriptManifest(
		author = "LiamN",
		info = "Fishing at the Karamja Island",
		logo = "",
		name = "NGKaramjaFisher", 
		version = 0
)
public class Main extends Script {
	private final Area DEPOSIT_BOX  = new Area(
			3043, 3237, 3052, 3234);
	private final Area FISHING_SPOT = new Area(
			new int[][]{
		        { 2921, 3178 },
		        { 2921, 3172 },
		        { 2929, 3172 },
		        { 2927, 3175 },
		        { 2926, 3176 },
		        { 2926, 3181 },
		        { 2924, 3181 },
		        { 2924, 3177 }
		    }
		);
	
	private App app;
	private long startTime;
	private Counter fishCounter;
	
	public static String formatTime(long ms) {
		long s = (ms / 1000L) % 60L;
		long m = (ms / (1000L * 60L)) % 60L;
		long h = (ms / (1000L * 60L * 60L));
		return String.format("%02d:%02d:%02d",h,m,s);
	}
	
	@Override
	public void onStart() {
		log("Starting NGKaramjaFisher");
		app = App.startApp();
		
		startTime = System.currentTimeMillis();
		getExperienceTracker().start(Skill.FISHING);
		
		fishCounter = new Counter();
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		if (getSkills().getStatic(Skill.FISHING) < 35) {
			log("[ERROR] Must be lvl 40 fishing or higher");
			stop(false);
		}
		if (!getInventory().isFull()) {
			if (FISHING_SPOT.contains(myPlayer())) {
				if (app.getFishingMethod().equals("Cage"))
					execute(new CageFisher());
				else if (app.getFishingMethod().equals("Harpoon"))
					execute(new HarpoonFisher());
			} else getWalking().webWalk(FISHING_SPOT);
		} else if (!app.getPowerFishing()){
			if (DEPOSIT_BOX.contains(myPlayer())) execute(new DepositEvent(fishCounter, "Raw lobster", "Raw swordfish"));
			else 								  getWalking().webWalk(DEPOSIT_BOX);
		} else {
			getInventory().dropAll("Raw tuna", "Raw swordfish", "Raw lobster");
		}
		return 300;
	}
	
	@Override
	public void onExit() {
		app.dispose();
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
		
		app.setStatistics(
				formatTime(ms),
				String.format("%d", getExperienceTracker().getGainedXP(Skill.FISHING)),
				String.format("%d/h", getExperienceTracker().getGainedXPPerHour(Skill.FISHING)),
				String.format("%d (+%d)", 
						getSkills().getStatic(Skill.FISHING), 
						getExperienceTracker().getGainedLevels(Skill.FISHING)
				), 
				String.format("%d", fishCounter.get())
		);
	}
}
