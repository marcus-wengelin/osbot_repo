package script;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import event.banking.DepositAllWithdrawEvent;
import event.crafting.UseFurnaceEvent;

@ScriptManifest(author = "FatCuck",
info = "Blah",
logo = "",
name = "EdgeVilleCrafter",
version = 0)
public class Main extends Script {
	
	private static final Area EDGEVILLE_FURNACE = new Area(3105, 3501, 3110, 3496);
	private static final String REQUIRED_TOOL = null;//"Amulet Mould";
	
	private static final String[] REQUIRED_ITEMS = {
			"Iron ore"
	};
	
	private static final String ITEM_NAME = "Iron bar";
	private static HashMap<String, Integer> WITHDRAW_ITEMS;
	private static HashMap<String, Integer> WITHDRAW_RING;
	
	@Override public void onStart() {
		WITHDRAW_ITEMS = new HashMap<String, Integer>(1);
		WITHDRAW_ITEMS.put("Iron ore", 28);
		
		WITHDRAW_RING  = new HashMap<String, Integer>(1);
		WITHDRAW_RING.put("Ring of forging", 1);
	}
	
	private boolean canCraftItem() {
		// TODO: Add feature for different amounts of ingredients
		boolean missingIngredient = false;
		for (String name : REQUIRED_ITEMS) {
			if (!getInventory().contains(name)) {
				missingIngredient = true;
			}
		}
		
		return !missingIngredient;
	}
	
	private boolean hasTool() {
		if (REQUIRED_TOOL == null) return true;

		return getInventory().contains(REQUIRED_TOOL);
	}
	
	@Override public int onLoop() throws InterruptedException {
		if (!hasTool()) {
			// If we don't have a tool, acquire one
			// Ignore this for now
			log("[EdgeVilleCrafter] No tool!");
			stop(false);
		} else if (canCraftItem()) {
			// If we have a tool and the ingredients, craft the item
			UseFurnaceEvent useFurnace = new UseFurnaceEvent(EDGEVILLE_FURNACE);
			execute(useFurnace);
		} else if (!getEquipment().isWearingItem(EquipmentSlot.RING, "Ring of forging")) {
			if (getInventory().contains("Ring of forging")) {
				getInventory().interact("Wear", "Ring of forging");
			} else {
				DepositAllWithdrawEvent withdrawRingEvent = new DepositAllWithdrawEvent(WITHDRAW_RING);
				execute(withdrawRingEvent);
			
				if (withdrawRingEvent.hasFailed()) {
					log("[EdgeVilleCrafter][-] Failed to withdraw 'Ring of forging'");
					stop(false);
				}
			}
		} else {
			DepositAllWithdrawEvent withdrawEvent = new DepositAllWithdrawEvent(WITHDRAW_ITEMS);
			execute(withdrawEvent);
			
			if (withdrawEvent.hasFailed()) {
				log("[EdgeVilleCrafter][-] Failed to withdraw ingredients");
				stop(false);
			}
		}
		
		return 100;
	}
	
	@Override public void onPaint(Graphics2D g) {
		g.setColor(Color.RED);
		int mx = (int) getMouse().getPosition().getX();
		int my = (int) getMouse().getPosition().getY();
		
		g.drawLine(mx-5, my, mx+5, my);
		g.drawLine(mx, my-5, mx, my+5);
	}
}
