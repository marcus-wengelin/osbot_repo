package event.banking.internal;

import java.util.HashMap;

import org.osbot.rs07.event.Event;

public class BankWithdrawEvent extends Event {
	
	private HashMap<String, Integer> withdrawItems;
	public BankWithdrawEvent(HashMap<String, Integer> withdrawItems) {
		this.withdrawItems = withdrawItems;
	}
	
	@Override public int execute() {
		if (!getBank().isOpen()) {
			log("[BankWithdrawEvent] Bank is not open");
			setFailed();
			return 100;
		}
		
		
		boolean hasAllItems = true;
		for (String itemName : withdrawItems.keySet()) {
			if (getInventory().getAmount(itemName) < withdrawItems.get(itemName))
				hasAllItems = false;
		}
		if (hasAllItems) {
			log("[BankWithdrawEvent][+] We have all items");
			setFinished();
			return 100;
		}
		
		getBank().withdraw(withdrawItems);
		return random(500, 1_000);
	}
}
