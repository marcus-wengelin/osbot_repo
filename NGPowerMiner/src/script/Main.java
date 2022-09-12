package script;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import events.DepositOreEvent;
import events.DropAllEvent;
import events.DropEvent;
import events.MiningEvent;
import events.PaintableEvent;
import events.SortEvent;
import gui.App;
import solvers.InteractRandoms;

@ScriptManifest(
		author = "LiamN",
		info = "Mines stuff",
		logo = "",
		name = "NGPowerMinerXXX", 
		version = 0
)
public class Main extends Script {
	private long startTime = 0;
	private App app;

	private PaintableEvent currentMiningEvent;
	
	public static String formatTime(long ms) {
		long s = (ms / 1000L) % 60L;
		long m = (ms / (1000L * 60L)) % 60L;
		long h = (ms / (1000L * 60L * 60L));
		return String.format("%02d:%02d:%02d",h,m,s);
	}

	@Override
	public void onStart() throws InterruptedException {
		app = App.startApp(logger);
		app.setTitle("NGPowerMiner: "+myPlayer().getName());
		startTime = System.currentTimeMillis();
		getExperienceTracker().start(Skill.MINING);
		getBot().getRandomExecutor().overrideOSBotRandom(new InteractRandoms());
		super.onStart();
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		if (!getTabs().isOpen(Tab.INVENTORY))
			getTabs().open(Tab.INVENTORY);
			
		if (app.getTargetOre() != null) {
			currentMiningEvent = new MiningEvent(app.getTargetOre().getIds(), app.getNoMove());
						
			execute((Event) currentMiningEvent); // BLOCK
			if (getInventory().isFull()) {
				if (app.getDropBehaviour() == App.DROP_ORE)
					execute(new DropEvent(app.getTargetOre().getName()));
				else if (app.getDropBehaviour() == App.DROP_ALL)
					execute(new DropAllEvent());
				else if (app.getDropBehaviour() == App.DEPO_ORE)
					execute(new DepositOreEvent(app.getTargetOre().getInvId(),
						myPlayer().getPosition()));
			}
			
			execute(new SortEvent(app.getTargetOre().getName()));
		}
		
		return 200;
	}
	
	@Override
	public void onPaint(Graphics2D g) {
		long ms = startTime == 0 ? 0 : System.currentTimeMillis() - startTime;
		g.setColor(Color.RED);
		/* Draw mouse */
		int mx = (int)getMouse().getPosition().getX();
		int my = (int)getMouse().getPosition().getY();
		g.drawLine(mx-5, my, mx+5, my);
		g.drawLine(mx, my-5, mx, my+5);
		/* Draw text */
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		g.setColor(Color.GREEN);
		
		app.setStatistics(formatTime(ms),
						  String.format("%d (+%d)", getSkills().getStatic(Skill.MINING),
								  getExperienceTracker().getGainedLevels(Skill.MINING)), 
						  String.format("%d", getExperienceTracker().getGainedXP(Skill.MINING)),
						  String.format("%d/h", getExperienceTracker().getGainedXPPerHour(Skill.MINING))
		);
		if (currentMiningEvent != null) currentMiningEvent.onPaint(g);
	}
	
	@Override
	public void onExit() {
		app.dispose();
	}
}
