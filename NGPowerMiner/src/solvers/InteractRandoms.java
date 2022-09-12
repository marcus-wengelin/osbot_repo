package solvers;
import org.osbot.rs07.api.filter.ActionFilter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.RandomEvent;
import org.osbot.rs07.script.RandomSolver;
import org.osbot.rs07.utility.ConditionalSleep;

public class InteractRandoms extends RandomSolver {
	private NPC randomEncounter;
	
	public InteractRandoms() {
		super(RandomEvent.DISMISS_RANDOM);
	}
	
	@SuppressWarnings("unchecked")
	private boolean findRandomEncounter() {
		for (NPC npc : getNpcs().filter(new ActionFilter<NPC>("Dismiss"))) {
			if (npc.getInteracting() == myPlayer()) {
				randomEncounter = npc;
				return true;
			}
		}
		randomEncounter = null;
		return false;
	}
	
	private void dismissRandom() {
		randomEncounter.interact("Dismiss");
		new ConditionalSleep(5_000) {
			@Override
			public boolean condition() throws InterruptedException {
				return !findRandomEncounter();
			}
		}.sleep();
	}
	
	private void interactGenie() {
		if (getInventory().isFull()) {
			log("[RNDSLV] Inventory is full");
			dismissRandom();
			return;
		}
		randomEncounter.interact("Talk-to");
		if (new ConditionalSleep(5_000) {
			@Override
			public boolean condition() throws InterruptedException {
				return getDialogues().inDialogue() && getDialogues().isPendingContinuation();
			}
		}.sleep()) {
			getDialogues().clickContinue();
		}
	}
	
	private void interactRandom() {
		log("[RNDSLV] Interacting with random: "+randomEncounter.getName());
		switch (randomEncounter.getName()) {
		case "Genie":
			interactGenie();
			break;
		default:
			dismissRandom();
			break;
		}
	}
	
	@Override
	public boolean shouldActivate() {
		return findRandomEncounter();
	}

	@Override
	public int onLoop() throws InterruptedException {
		/* Get random encounter */
		if (randomEncounter == null)
			findRandomEncounter();
		/* Interact with random encounter */
		interactRandom();		
		return 300;
	}
}
