package script;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import event.DeliverEvent;
import event.FetchEvent;

@ScriptManifest(
author = "LiamN", 
info = "Running runes from bank", 
logo = "", 
name = "RuneRunner", 
version = 0)
public class Main extends Script {
	
	@Override
	public int onLoop() {

		if (inventory.isFull())
			execute(new DeliverEvent());
		else
			execute(new FetchEvent());
		
		
		return 300;
	}

}
