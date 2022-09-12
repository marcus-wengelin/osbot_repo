package script;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(
author = "LiamN", 
info = "Solves the Rune Mysteries quest", 
logo = "", 
name = "AQRuneMysteries", 
version = 0)
public class Main extends Script {
	
	private static final int CONFIG_ID = 63;
	
	private static final Area CASTLE_AREA = 
			new Area(3213, 3218, 3208, 3223).setPlane(1);
	private static final Area CELLAR_AREA =
			new Area(3106, 9573, 3099, 9569);
	private static final Area MERCHANT_AREA =
			new Area(3251, 3402, 3254, 3400);
			

	public int getProgress() {
		return this.configs.get(CONFIG_ID);
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		switch (getProgress()) {
		case 0:
			util.Quests.talkToNPC(this, CASTLE_AREA, "Duke Horacio", 1,1);
			break;
		case 1:
		case 2:
			util.Quests.talkToNPC(this, CELLAR_AREA, "Archmage Sedridor", 3,1,1);
			break;
		case 3:
		case 4:
			util.Quests.talkToNPC(this, MERCHANT_AREA, "Aubury", 2);
			break;
		case 5:
			util.Quests.talkToNPC(this, CELLAR_AREA, "Archmage Sedridor");
			break;
		case 6: // quest completed
		default:
			stop(false);
			break; 
		}
		return 100;
	}
}
