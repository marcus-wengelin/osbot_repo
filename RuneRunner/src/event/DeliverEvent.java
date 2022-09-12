package event;

import java.util.List;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.ConditionalSleep;

public class DeliverEvent extends Event {
	
	private static final Area RUNE_AREA =
			new Area(3049, 3445, 3057, 3437);
	
	private static final String MULE_NAME = "AmirTheBank";
	
	private static final int TRADE_ROOT_ID = 335;
	
	private long numEssence = Long.MAX_VALUE;
	
	private int getFreeTradeSlots() {
		if (!trade.isCurrentlyTrading()) return -1;
		
		List<RS2Widget> slotsWidgets = widgets.containingText(TRADE_ROOT_ID, "inventory slots");
		if (slotsWidgets.isEmpty()) return -1;
		
		RS2Widget slotsWidget = slotsWidgets.get(0);
		return Integer.parseInt(slotsWidget.getMessage().replaceAll("[^\\d+]", ""));
	}
	
	private void deliverRuneEssence() {
		if (trade.isCurrentlyTrading()) {
			if (trade.isFirstInterfaceOpen()) {
				logger.info("Number of empty slots: "+getFreeTradeSlots());
				trade.offer("Rune essence", getFreeTradeSlots());
				
				trade.acceptTrade();
				new ConditionalSleep(10_000) {
					@Override public boolean condition() throws InterruptedException {
						return trade.isSecondInterfaceOpen();
					}
				}.sleep();
			} else if (trade.isSecondInterfaceOpen() && trade.didOtherAcceptTrade()) {
				trade.acceptTrade();
				new ConditionalSleep(30_000) {
					@Override public boolean condition() throws InterruptedException {
						return !trade.isCurrentlyTrading() && inventory.getAmount("Rune essence") < numEssence;
					}
				}.sleep();
			}
		} else if (players.closest(MULE_NAME) != null) {
			players.closest(MULE_NAME).interact("Trade with");
			new ConditionalSleep(30_000) {
				@Override public boolean condition() throws InterruptedException {
					return trade.isCurrentlyTrading();
				}
			}.sleep();
		}
	}
	
	@Override
	public void onStart() {
		numEssence = inventory.getAmount("Rune essence");
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (!RUNE_AREA.contains(myPlayer()) && !trade.isCurrentlyTrading())
			walking.webWalk(RUNE_AREA);
		else
			deliverRuneEssence();
		
		if (!trade.isCurrentlyTrading() && inventory.getAmount("Rune essence") < numEssence) setFinished();
		return 300;
	}

}