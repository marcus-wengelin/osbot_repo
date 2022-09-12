package event;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.Condition;
import org.osbot.rs07.utility.ConditionalSleep;

/**
 * An event for speaking to NPCs
 * It is probably smarter to use the util.Quests.talkToNPC function instead
 * 
 * @author Marcus
 *
 */
public class TalkToNPC extends Event {

	private Area area;	   // NPC area
	private String name;   // NPC name
	private int[] options; // List of dialogue options
	private int index;	   // Index of next option
	
	private Condition successCondition;
	
	private boolean dialogueStarted;
	
	public TalkToNPC(Area area, String name, Condition successCondition, int... options) {
		this.area = area;
		this.name = name;
		this.options = options;
		this.index 	 = 0;
		//this.successCondition = successCondition;
		
		this.dialogueStarted = false;
	}
	
	@Override public int execute() throws InterruptedException {
		// Do we need this? Can't we just run through the options?
		if (successCondition.evaluate()) setFinished();
		
		if (dialogueStarted) {
			if (dialogues.isPendingContinuation()) {
				dialogues.clickContinue();
			} else if (dialogues.isPendingOption()) {
				if (index < options.length) {
					dialogues.selectOption(options[index]);
					index++;
				}
			}
		}
		
		if (!canFindNpc()) {
			// We are not in vicinity of NPC
			walking.webWalk(area.getRandomPosition());
		} else if (canFindNpc() && !canReachNpc()) {
			// We are in the vicinity but the NPC is unreachable
			walking.walk(npcs.closest(name).getPosition());
		} else if (canFindNpc() && canReachNpc()) {
			// NPC is in the vicinity and reachable
			NPC npc = npcs.closest(name);
			if (npc != null) {
				npc.interact("Talk-to");
				if (new ConditionalSleep(10_000) {
					@Override public boolean condition() throws InterruptedException {
						return dialogues.inDialogue();
					}
				}.sleep()) {
					// Dialogue with NPC has started
					dialogueStarted = true;
				}
			}
		}
		
		return 500;
	}
	
	private boolean canFindNpc() {
		return npcs.closest(name) != null;
	}
	
	private boolean canReachNpc() {
		if (!canFindNpc()) return false;
		return map.canReach(npcs.closest(name));
	}

}
