package event.combat;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.event.Event;

import event.combat.internal.StartCombatEvent;
import event.combat.internal.EndCombatEvent;


/**
 * Attack the given target and wait until the target dies
 * 
 * @finish startCombat and endCombat finished
 * @fail startCombat or endCombat failed
 * 
 * @author user
 *
 */
public class KillTargetEvent extends Event {
	
	private NPC target;
	private Position deathPosition;
	
	public KillTargetEvent(NPC target) {
		this.target = target;
		this.deathPosition = null;
	}
	
	public Position getDeathPosition() {
		return deathPosition;
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (myPlayer().isUnderAttack()) {
			log("[FightEvent][-] Player is still under attack, waiting...");
			return random(2_000, 4_000);
		}
		
		// Initiate fight with target
		StartCombatEvent startCombat = new StartCombatEvent(target);
		execute(startCombat);
		if (startCombat.hasFailed()) {
			setFailed();
			return 100;
		}
		
		// Wait for target to die
		EndCombatEvent endCombat = new EndCombatEvent(target);
		execute(endCombat);
		if (endCombat.hasFailed()) {
			setFailed();
			return 100;
		}
		
		deathPosition = endCombat.getTargetPosition();
		setFinished();
		return 100;
	}
}
