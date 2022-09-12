package event.combat.internal;

import org.osbot.rs07.api.model.Character;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.event.Event;

/**
 * Starts combat with a given target.
 * 
 * @finish when player and enemy are fighting
 * @fail when `MAX_ATTEMPTS` reached or target fighting someone else
 * 
 * @author user
 *
 */
public class StartCombatEvent extends Event {
	private static final int MAX_ATTEMPTS = 3;
	
	private NPC target;
	private int attempts;
	
	public StartCombatEvent(NPC target) {
		this.target = target;
		this.attempts = 0;
		
		if (target == null) {
			log("[StartCombatEvent][-] `target` is null");
			setFailed();
		} else if (!target.hasAction("Attack")) {
			log("[StartCombatEvent][-] `target` does not have action 'Attack'");
			setFailed();
		}
	}
	
	/**
	 * Helper method to determine whether the player
	 * has initiated a fight with the given target.
	 * 
	 * @return true if player is fighting target
	 */
	private boolean playerFightingTarget() {
		return myPlayer().isInteracting(target) && target.isUnderAttack();
	}
	
	/**
	 * Helper method to determine whether the target
	 * has initiated a fight with the given player.
	 * 
	 * @return true if target is fighting player
	 */
	private boolean targetFightingPlayer() {
		return target.isInteracting(myPlayer()) && myPlayer().isUnderAttack();
	}
	
	/**
	 * Helper method to determine whether the target
	 * is fighting someone that is not the player.
	 * 
	 * @return true if target fighting other
	 */
	private boolean targetFightingOther() {
		Character<?> c = target.getInteracting();
		return c != null && !c.getName().equals(myPlayer().getName());
	}
	
	@Override public int execute() throws InterruptedException {
		if (attempts > MAX_ATTEMPTS) {
			log("[AttackEvent][-] `MAX_ATTEMPTS` exceeded");
			setFailed();
			return 100;
		}
		
		if (targetFightingOther()) {
			log("[AttackEvent][-] `target` is fighting someone else");
			setFailed();
			return 100;
		}
		
		if (playerFightingTarget() && targetFightingPlayer()) {
			log("[AttackEvent][+] `target` and player fighting");
			setFinished();
			return 100;
		}
		
		if (!playerFightingTarget()) {
			log("[AttackEvent][*] Attacking `target`");
			target.interact("Attack");
			getMouse().moveOutsideScreen();
			attempts += 1;
		}
		
		return random(2_000, 3_000);
	}
}
