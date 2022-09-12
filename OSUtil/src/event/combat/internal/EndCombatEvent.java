package event.combat.internal;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.event.Event;

/**
 * Waits for combat with a given target to finish.
 * 
 * @finish if target dies
 * @fail if player is not under attack.
 * 
 * 
 * @author user
 *
 */
public class EndCombatEvent extends Event {

	private NPC target;
	private Position targetPosition;
	private long lastUnderAttack;
	
	public EndCombatEvent(NPC target) {
		this.target = target;
		lastUnderAttack = System.currentTimeMillis();
	}
	
	public Position getTargetPosition() {
		return targetPosition;
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (myPlayer().isUnderAttack())
			lastUnderAttack = System.currentTimeMillis();
		
		
		if (target.getHealthPercent() == 0 || !target.exists()) {
			log("[EndCombat][+] `target` is dead");
			setFinished();
		} else if (System.currentTimeMillis() - lastUnderAttack > 3_000) {
			log("[EndCombat][-] player is not under attack");
			setFailed();
		} else {
			targetPosition = new Position(target.getPosition());
		}
		
		return 100;
	}
}
