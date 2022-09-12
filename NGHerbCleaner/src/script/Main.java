package script;

import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import constants.Herb;

@ScriptManifest(
author = "LiamN", 
info = "Cleans herbs", 
logo = "", 
name = "NGHerbCleaner", 
version = 0)
public class Main extends Script {

	private enum State {
		CLEAN, SWAP
	}
	
	private long startTime;
	int currentSlot = 0;
	
	public static String formatTime(long ms) {
		long s = (ms / 1000L) % 60L;
		long m = (ms / (1000L * 60L)) % 60L;
		long h = (ms / (1000L * 60L * 60L));
		return String.format("%02d:%02d:%02d",h,m,s);
	}
	
	private State getCurrentState(Herb herb) {
		if (getInventory().contains(herb.grimyName))
			return State.CLEAN;
		return State.SWAP;
	}
	
	private int herbloreLevel() {
		return getSkills().getStatic(Skill.HERBLORE);
	}
	
	/* TODO: Make it so that this actually fixes our shit */
	private Herb getCurrentHerb() {
		return Herb.GUAM;
	}
	
	private void doClean(Herb herb) {
		Item it = getInventory().getItemInSlot(currentSlot % 28);
		if (it.getName().equals(herb.grimyName)) {
			getInventory().hover(currentSlot % 28);
			getMouse().click(false);
		}
		currentSlot++;
	}
	
	private void doSwap(Herb herb) {
		if (getBank().isOpen()) {
			getBank().depositAll();
			getBank().withdrawAll(herb.grimyName);
			getBank().close();
			new ConditionalSleep(5_000) {
				@Override
				public boolean condition() throws InterruptedException {
					return !getBank().isOpen();
				}
				
			}.sleep();
			currentSlot = 0;
		} else {
			NPC banker = getNpcs().closest("Banker");
			if (banker != null) {
				banker.interact("Bank");
				new ConditionalSleep(5_000) {
					@Override
					public boolean condition() throws InterruptedException {
						return getBank().isOpen();
					}
					
				}.sleep();
			}
		}
	}
	
	@Override
	public void onStart() {
		startTime = System.currentTimeMillis();
		
		if (herbloreLevel() < 3) {
			log("Herblore level must be 3 or above");
			stop(false);
		}
	}
	
	
	@Override
	public int onLoop() throws InterruptedException {
		if (getInventory().isItemSelected())
			getInventory().deselectItem();
		Herb herb = getCurrentHerb();
		// TODO Auto-generated method stub
		switch(getCurrentState(herb)) {
		case CLEAN:
			doClean(herb);
			break;
		case SWAP:
			doSwap(herb);
			break;
		default:
			break;
		}
		
		return random(50,100);
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
		g.drawString(String.format("NGMotherLode: %s", formatTime(ms)), 5, 20);
	}
}
