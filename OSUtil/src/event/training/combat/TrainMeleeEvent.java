package event.training.combat;

import org.osbot.rs07.api.filter.ActionFilter;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.event.Event;

import event.training.combat.internal.GetChickenEvent;
import event.training.combat.internal.KillChickenEvent;
import util.Combat;

public class TrainMeleeEvent extends Event {
	
	private long lastMethodSwitch;
	
	@Override public void onStart() {		
		if (getInventory().getEmptySlots() < 7) {
			log("Not enough empty slots");
			setFailed();
		}
		
		lastMethodSwitch = 0;
	}
	
	@Override public int execute() throws InterruptedException {
		if (!hasFood()) {
			execute(new GetChickenEvent(5));
		} else {
			execute(new KillChickenEvent());
		}
		
		return 100;
	}
	
	/** EVENT CHAINS **/
	
	/** HELPERS **/

	/**
	 * Selects attack method according to level
	 */
	private void selectAttackMethod() {
		// Fetch lowest level
		int lowest = Math.min(getDefenceLevel() ,
				 			  Math.min(getAttackLevel(), getStrengthLevel()));

		if (getStrengthLevel() == lowest)
			Combat.setStrengthStyle(this);
		else if (getAttackLevel() == lowest)
			Combat.setAttackStyle(this);
		else
			Combat.setDefenceStyle(this);
		
		lastMethodSwitch = System.currentTimeMillis();
	}
	
	private boolean hasFood() {
		return getInventory().contains(new ActionFilter<Item>("Eat"));
	}
	
	private boolean shouldSwitchAttackMethod() {
		return getAttackLevel() != getStrengthLevel() && getAttackLevel() != getDefenceLevel();
	}
	
	/** GETTERS **/
	
	private int getAttackLevel() {
		return getSkills().getStatic(Skill.ATTACK);
	}
	
	private int getStrengthLevel() {
		return getSkills().getStatic(Skill.STRENGTH);
	}
	
	private int getDefenceLevel() {
		return getSkills().getStatic(Skill.DEFENCE);
	}
}
