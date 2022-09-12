package util;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;

/**
 * Static utility functions for testing.
 * 
 * @author Marcus
 *
 */
public class Quests {
	
	// Private methods
	public static void solveDialogue(MethodProvider api, int... options) {
		long lastDialogue = System.currentTimeMillis();
		int i = 0;
		while (System.currentTimeMillis() - lastDialogue < 10_000) {
			if (api.dialogues.inDialogue())
				lastDialogue = System.currentTimeMillis();
			
			if (api.dialogues.isPendingContinuation()) {
				api.dialogues.clickContinue();
			} else if (api.dialogues.isPendingOption()) {
				if (i < options.length) {
					api.dialogues.selectOption(options[i]);
					i++;
				} else {
					api.dialogues.selectOption(1);
				}
			}
			
			new ConditionalSleep(1_000) {
				@Override public boolean condition() throws InterruptedException {
					return api.dialogues.inDialogue();
	   			}
			}.sleep();
		}
	}
	
	public static void talkToNPC(MethodProvider api, Area area, String name, int... options) {
		NPC npc = api.getNpcs().closest(name);
		if ( npc == null ||  !api.getMap().canReach(npc)) {
			api.getWalking().webWalk(area.getRandomPosition());
			return;
		}
		
		if ( !api.getDialogues().inDialogue() ) {
			if ( npc != null ) {
				api.getWalking().webWalk(npc.getPosition());
				npc.interact("Talk-to");
				new ConditionalSleep(10_000) {
					@Override public boolean condition() throws InterruptedException {
						return api.getDialogues().inDialogue();
		   			}
				}.sleep();
			}
			return;
		}
		
		
		solveDialogue(api, options);
	}
}
