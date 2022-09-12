package events;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.Condition;

public class SortEvent extends Event {
	private String[] ignoreNames;
	public SortEvent(String... ignoreNames) {
		this.ignoreNames = ignoreNames;
		setBlocking();
	}
	
	private boolean arrayContains(String[] arr, String x) {
		for (String s : arr) 
			if (x.equals(s)) return true;
		return false;
	}
	
	private int getUnsortedIndex() {
		for (int i = 0; i < 28; i++) {
			Item item = getInventory().getItemInSlot(i);
			if (item == null || arrayContains(ignoreNames, item.getName()))
				return i; 
		}
		return -1;
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
	
	private void sort() throws InterruptedException {
		int index = getUnsortedIndex();
		if (index == -1) return;
		for (int i = index; i < 28; i++) {
			Item item = getInventory().getItemInSlot(i);
			if (item != null && !arrayContains(ignoreNames, item.getName())) {
				switchSlots(index, i);
				index++;
			}
		}
	}
	
	@Override
	public int execute() throws InterruptedException {
		sort();
		setFinished();
		return 300;
	}
}
