package script;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

@ScriptManifest(
author = "LiamN", 
info = "Solves the Restless Ghost quest", 
logo = "", 
name = "AQRestlessGhost", 
version = 0)
public class Main extends Script {
	
	private static final int CONFIG_ID = 107;
	
	private static final Area CHURCH_AREA = 
			new Area(3240, 3204, 3247, 3215);
	private static final Area GRAVEYARD_AREA = 
			new Area(3247, 3195, 3252, 3190);
	private static final Area HUT_AREA =
			new Area(3144, 3177, 3151, 3173);
	private static final Area CELLAR_AREA =
			new Area(3121, 9569, 3116, 9564);

	public int getProgress() {
		return this.configs.get(CONFIG_ID);
	}
	
	/* Stage 2 */
	public void talkToGhost() {
		if (!equipment.isWearingItem(EquipmentSlot.AMULET, "Ghostspeak amulet")) {
			equipment.equip(EquipmentSlot.AMULET, "Ghostspeak amulet");
		}
		
		if (!GRAVEYARD_AREA.contains(myPlayer())) {
			walking.webWalk(GRAVEYARD_AREA);
		} else {
			if (npcs.closest("Restless ghost") == null) {
				RS2Object coffin = objects.closest("Coffin");
				if (coffin.hasAction("Open")) {
					coffin.interact("Open");
				} else {
					coffin.interact("Close");
				}
			} else {
				if (dialogues.inDialogue()) {
					util.Quests.solveDialogue(this, 1);
				} else {
					npcs.closest("Restless ghost").interact("Talk-to");
					new ConditionalSleep(1000) {
						@Override public boolean condition() throws InterruptedException {
							return dialogues.inDialogue();
						}
					}.sleep();
				}
			}
		}
	}
	/* Stage 3 */
	public void getSkull() {
		if (!CELLAR_AREA.contains(myPlayer())) {
			walking.webWalk(CELLAR_AREA);
		} else {
			objects.closest("Altar").interact("Search");
			new ConditionalSleep(1000) {
				@Override public boolean condition() throws InterruptedException {
					return inventory.contains("Ghost's skull");
				}
			}.sleep();
		}
	}
	/* Stage 4 */
	public void returnSkull() {
		if (!GRAVEYARD_AREA.contains(myPlayer())) {
			walking.webWalk(GRAVEYARD_AREA);
		} else {
			RS2Object coffin = objects.closest("Coffin");
			if (coffin.hasAction("Open")) {
				coffin.interact("Open");
				new ConditionalSleep(1000) {
					@Override public boolean condition() throws InterruptedException {
						return coffin.hasAction("Close");
					}
					
				}.sleep();
			} else {
				Item skull = inventory.getItem("Ghost's skull");
				skull.interact("Use");
				new ConditionalSleep(1_000) {
					@Override public boolean condition() throws InterruptedException {
						return inventory.isItemSelected();
					}
				}.sleep();
				
				coffin.interact("Use");
				new ConditionalSleep(5_000) {
					@Override public boolean condition() throws InterruptedException {
						return inventory.isItemSelected();
					}
				}.sleep();
			}
		}
	}

	
	@Override
	public int onLoop() throws InterruptedException {
		switch (getProgress()) {
		case 0:
			util.Quests.talkToNPC(this, CHURCH_AREA, "Father Aereck", 3,1);
			break;
		case 1:
			util.Quests.talkToNPC(this, HUT_AREA, "Father Urhney", 2,1);
			break;
		case 2:
			talkToGhost();
			break;
		case 3:
			getSkull();
			break;
		case 4:
			returnSkull();
			break;
		case 5: // quest completed
		default:
			stop(false);
			break; 
		}
		return 100;
	}

}
