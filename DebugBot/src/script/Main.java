package script;

import java.awt.Graphics2D;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import event.training.combat.TrainMeleeEvent;

@ScriptManifest(
		author="LiamN",
		info = "Outputs debug information",
		logo = "",
		name = "DebugBot",
		version = 0.1)
public class Main extends Script {

	@Override public int onLoop() throws InterruptedException {
		/* Go train melee */
		execute(new TrainMeleeEvent());
		return 500;
	}
	
	@Override public void onPaint(Graphics2D g) {
		g.drawString(String.format("IsMinimapLocked: %s", ""+map.isMinimapLocked()), 5, 20);
		g.drawString(String.format("InDialogue: %s", ""+dialogues.inDialogue()), 5, 40);
		g.drawString(String.format("IsPendingContinue: %s", ""+dialogues.isPendingContinuation()), 5, 60);
	}
}
