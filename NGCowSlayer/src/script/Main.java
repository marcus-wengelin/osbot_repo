package script;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import event.banking.DepositEvent;
import event.combat.KillTargetEvent;
import event.combat.LootCloseEvent;
import event.crafting.TanHidesEvent;

@ScriptManifest(
		author = "FatCuck",
		info = "Slays cows",
		logo = "", 
		name = "NGCowSlayer",
		version = 0)
public class Main extends Script {

	private static final Area COWPEN_AREA = new Area(
		    new int[][]{
		        { 3253, 3255 },
		        { 3253, 3272 },
		        { 3251, 3276 },
		        { 3249, 3278 },
		        { 3242, 3287 },
		        { 3242, 3298 },
		        { 3264, 3298 },
		        { 3265, 3296 },
		        { 3265, 3255 }
		    }
		);
	
	private boolean inCowpen() {
		return COWPEN_AREA.contains(myPlayer());
	}
	
	private NPC getClosestCow() {
		return getNpcs().closest(new Filter<NPC>() {
			@Override public boolean match(NPC npc) {
				return npc.getName().equals("Cow") &&
					   getMap().canReach(npc) &&
					   !npc.isUnderAttack() &&
					   npc.getHealthPercent() == 100;
			}
		});
	}
	
	@Override public int onLoop() throws InterruptedException {
		// If inventory is full, either tan hides or depo leather
		if (getInventory().isFull()) {
			if (getInventory().contains("Cowhide")) {
				log("[NGCowSlayer] Tanning hides");
				execute(new TanHidesEvent());
			} else {
				log("[NGCowSlayer] Depositing leather");
				execute(new DepositEvent("Leather"));
			}
			
		// If inventory is not full, go to cowpen and kill/loot cow
		} else {
			if (!inCowpen()) getWalking().webWalk(COWPEN_AREA);
			
			NPC target = getClosestCow();
			if (target != null) {
				KillTargetEvent killTarget = new KillTargetEvent(target);
				execute(killTarget);
				
				if (killTarget.hasFinished()) {
					execute(new LootCloseEvent(killTarget.getDeathPosition(), 5_000, new Filter<GroundItem>() {
						@Override public boolean match(GroundItem gi) {
							return gi.getName().equals("Cowhide");
						}
					}));
				}
			}
		}
		
		return 100;
	}

}
