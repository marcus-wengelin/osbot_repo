package events;

import java.awt.event.KeyEvent;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.Condition;
import org.osbot.rs07.utility.ConditionalSleep;

public class AlchingEvent extends Event {
	public String[] itemNames;
	
	public AlchingEvent(String[] itemNames) {
		this.itemNames = itemNames;
	}
	
	@Override public int execute() throws InterruptedException {
		if (!canAlch()) {
			log("[NGAutoAlcher] Unable to alch");
			setFinished();
		} else alch();
		return 300;
	}
	
	private void clickThroughPrompt() throws InterruptedException {
		RS2Widget contWidget = getWidgets().getWidgetContainingText("Click here to continue");
		if (contWidget != null) {
			log("[NGAutoAlcher] Continuing prompt");
			getKeyboard().pressKey(KeyEvent.VK_SPACE);
			getKeyboard().releaseKey(KeyEvent.VK_SPACE);
			new ConditionalSleep(5_000) {
				@Override public boolean condition() throws InterruptedException {
					return getWidgets().getWidgetContainingText("Alch it") != null;
				}
			}.sleep();
			sleep(1_000);
		}
		RS2Widget alchWidget = getWidgets().getWidgetContainingText("Alch it");
		if (alchWidget != null) {
			log("[NGAutoAlcher] Confirming prompt");
			getDialogues().selectOption(1);
			new ConditionalSleep(5_000) {
				@Override public boolean condition() throws InterruptedException {
					return getWidgets().getWidgetContainingText("Alch it") == null;
				}
			}.sleep();
		}
	}
	
	private void moveSlightly() {
		int x = getMouse().getPosition().x;
		int y = getMouse().getPosition().y;
		int nx = x+random(-3, 3);
		int ny = y+random(-3, 3);
		getMouse().move(nx, ny);
	}
	
	private boolean arrContains(String[] arr, String e) {
		for (String s : arr)
			if (s.equals(e)) return true;
		return false;
	}
	
	private void switchSlots(final int i, final int j) throws InterruptedException {
		getTabs().open(Tab.INVENTORY);
		if (getInventory().isItemSelected())
			getInventory().deselectItem();
        getMouse().continualClick(getInventory().getMouseDestination(j),
        	new Condition() {
            	@Override public boolean evaluate() {
            		return getMouse().move(getInventory().getMouseDestination(i), true);
            	}
        	});
	}
	
	private boolean canAlch() {
		if (!getInventory().contains("Nature rune")) return false;
		if (!getEquipment().isWieldingWeapon("Staff of fire") &&
			!getInventory().contains("Fire rune")) return false;
		if (!getInventory().contains(itemNames)) return false;
		return true;
	}
	
	private void alch() throws InterruptedException {
		Item bestSlot = getInventory().getItemInSlot(15);
		if (bestSlot != null && arrContains(itemNames, bestSlot.getName())) {
			long num = getInventory().getAmount(bestSlot.getName());
			getMagic().castSpell(Spells.NormalSpells.HIGH_LEVEL_ALCHEMY);
			bestSlot.interact();
			moveSlightly();
			new ConditionalSleep(5_000) {
				@Override public boolean condition() throws InterruptedException {
					if (getWidgets().getWidgetContainingText("Click here to continue") != null)
						clickThroughPrompt();
					return getInventory().getAmount(bestSlot.getName()) < num;
				}
			}.sleep();
		} else {
			int i = 0;
			for (; i < 28; i++) {
				Item slot = getInventory().getItemInSlot(i);
				if (slot != null && arrContains(itemNames, slot.getName()))
					break;
			}
			switchSlots(15, i);
		}
		
	}

}
