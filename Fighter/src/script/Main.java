package script;

import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import event.combat.EatFoodEvent;
import event.combat.KillTargetEvent;

@ScriptManifest(
	author = "FatCuck",
	info = "NaN",
	logo = "",
	name = "Fighter",
	version = 0
)
public class Main extends Script {

	private static final String ENEMY_NAME = "Minotaur";
	private static final int ENEMY_LVL     = 27;
	
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
				log("[Fighter] FightEvent on `target` failed!");
			} else if (fightEvent.hasFinished()) {
				log("[Fighter] FightEvent on `target` finished!");
			}

			if (eatEvent.hasFailed()) {
				// We ran out of food
				log("[Fighter] EatFoodEvent failed, ran out of food?");
				stop(false);
			}
		
			eatEvent.setFinished();
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
