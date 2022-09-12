package script;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import events.AfkEvent;


@ScriptManifest(
		author="LiamN",
		info = "Splashes curse on the Lesser Demon in Wizard's Tower",
		logo = "",
		name = "DemonSplasher",
		version = 0.1)

public class Main extends Script {
	
	private BufferedImage status = null;
	private static final int STATUS_X = 0;
	private static final int STATUS_Y = 340;
	
	private long startTime = -1;
	
	private boolean shouldAfk = false;
	
	private static final Area WIZARDS_TOWER 
		= new Area(3109, 3162, 3111, 3159).setPlane(2);

	private void fatal(String message) {
		log(message);
		stop(false);
	}
	
	private void doSplash() {
		NPC demon = npcs.closest("Lesser demon");
		if (demon != null && myPlayer().getAnimation() == -1) {
			magic.castSpellOnEntity(Spells.NormalSpells.CURSE, demon);
		} else if (demon == null) {
			mouse.moveOutsideScreen();
			new ConditionalSleep(10_000) {
				@Override public boolean condition() throws InterruptedException {
					return npcs.closest("Lesser demon") != null;
				}
			}.sleep();
		}
	}
	
	@Override
	public void onStart() {
		startTime = System.currentTimeMillis();
		experienceTracker.start(Skill.MAGIC);
		try {
			status = ImageIO.read(new File("Data/status.png"));
		} catch (IOException e) {
			fatal("Unable to find status image, stopping...");
		}
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		if (!magic.canCast(Spells.NormalSpells.CURSE)) {
			fatal("Unable to cast Curse, stopping...");
		}
		
		if (shouldAfk) {
			log("Cast failed, executing AFK event...");
			execute(new AfkEvent((long)random(10_000, 20_000)));
			shouldAfk = false;
		}
		
		if (!WIZARDS_TOWER.contains(myPlayer())) {
			walking.webWalk(WIZARDS_TOWER);
		} else {
			log("Casting splash...");
			doSplash();
		}
		
		return random(1_000, 3_000);
	}
	
	@Override
	public void onMessage(Message message) throws InterruptedException {
		if (message.getType() == Message.MessageType.GAME &&
			message.getMessage().equals("Your foe's defence has already been weakened.")) {
			shouldAfk = true;
		}
	}
	
	@Override
	public void onPaint(Graphics2D g) {
		util.Graphics.renderCursor(this, g, Color.RED);
		if (status != null) {
			g.drawImage(status, STATUS_X, STATUS_Y, null);
			g.setColor(Color.YELLOW);
			
			g.drawString(
				String.format("Runtime: %s", startTime == -1 ? "-" : util.Graphics.formatTime(System.currentTimeMillis() - startTime)),
			STATUS_X+25, STATUS_Y+35);
			
			g.drawString(
				String.format("Level: %d (+%d)", skills.getStatic(Skill.MAGIC), experienceTracker.getGainedLevels(Skill.MAGIC)),
			STATUS_X+25, STATUS_Y+75);
			
			g.drawString(
					String.format("Status: %s", shouldAfk ? "Waiting" : "Splashing"),
			STATUS_X+25, STATUS_Y+115);
		}
	}	
}
