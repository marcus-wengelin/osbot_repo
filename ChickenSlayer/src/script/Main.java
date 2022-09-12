package script;

import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import event.combat.EatFoodEvent;
import event.combat.KillTargetEvent;
import event.combat.LootCloseEvent;
import event.prayer.BuryAllEvent;
@ScriptManifest(author = "FatCuck", info = "Blah", logo = "", name = "ChickenSlayer", version = 0.0)
public class Main extends Script {

	@Override public void onStart() {
		log("[ChickenSlayer] Starting script");
	}
	
	@Override public void onStop() {
		log("[ĆhickenSlayer] Stopping script");
	}
	
	private static final String ENEMY_NAME = "Chicken";
	private static final int ENEMY_LVL     = 1;
	
	private NPC getClosestTarget() {
		return getNpcs().closest(new Filter<NPC>() {
			@Override public boolean match(NPC npc) {
				return (npc.getName().equals(ENEMY_NAME) && // Enemy name is `ENEMY_NAME`
						npc.getLevel() <= ENEMY_LVL &&		// Enemy level is lower than or equal to `ENEMY_LVL`
						getMap().canReach(npc) &&			// Enemy is reachable
						!npc.isUnderAttack() &&				// Enemy is not under attack
						npc.getHealthPercent() == 100);		// Enemy is at 100% health
			}
		});
	}
	
	@Override public int onLoop() throws InterruptedException {
		// Not under attack, get new target
		NPC target = getClosestTarget();
		if (target != null) {
			// Fight target
			log("[Fighter] Starting a fight with `target`");
			EatFoodEvent eatEvent = new EatFoodEvent(random(50, 75));
			execute(eatEvent);
			KillTargetEvent fightEvent = new KillTargetEvent(target);
			execute(fightEvent);

			if (fightEvent.hasFailed()) {
				log("[Fighter] FightEvent on " + ENEMY_NAME + " failed!");
				
			} else if (fightEvent.hasFinished()) {
				log("[Fighter] FightEvent on " + ENEMY_NAME + " finished!");
				LootCloseEvent lootEvent = new LootCloseEvent(fightEvent.getDeathPosition(), 5_000, new Filter<GroundItem>() {
					@Override public boolean match(GroundItem gi) {
						return gi.getName().equals("Feather") || gi.getName().equals("Bones");
					}
				});
				execute(lootEvent);
				
				BuryAllEvent buryEvent = new BuryAllEvent();
				execute(buryEvent);
			}

			if (eatEvent.hasFailed()) {
				// We ran out of food
				log("[Fighter] EatFoodEvent failed");
				stop(false);
			}
		
			log("[Fighter][+] End sequence");
		} else {
			// No target found
			log("[Fighter][-] No targets found");
		}	
		
		return 1_000;
	}
	
	@Override public void onPaint(Graphics2D g) {
		g.setColor(Color.RED);
		int mx = (int) getMouse().getPosition().getX();
		int my = (int) getMouse().getPosition().getY();
		
		g.drawLine(mx-5, my, mx+5, my);
		g.drawLine(mx, my-5, mx, my+5);
	}

}
