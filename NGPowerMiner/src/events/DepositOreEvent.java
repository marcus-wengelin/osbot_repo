package events;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.utility.ConditionalSleep;

import constants.DepoBox;

public class DepositOreEvent extends Event {

	private int[] targetOres;
	private Position returnPosition;
	
	public DepositOreEvent(int[] targetOres, Position returnPosition) {
		this.targetOres = targetOres;
		this.returnPosition = returnPosition;
	}
	public DepositOreEvent(int targetOre, Position returnPosition) {
		this.targetOres = new int[] {targetOre};
		this.returnPosition = returnPosition;
	}
	
	public boolean shouldFinish() {
		if (getInventory().contains(targetOres))
			return false;
		if (getDepositBox().isOpen())
			return false;
		if (!myPlayer().getPosition().equals(returnPosition))
			return false;
		
		return true;
	}
	
	
	@Override public int execute() throws InterruptedException {
		if (myPlayer().isMoving())
			return random(2_000, 4_000);
		
		/*if (shouldFinish()) {
			log("[DepositOreEvent] event is finished");
			setFinished();
			return 100;
		}*/
		
		execute(new WebWalkEvent(
				DepoBox.getClosest(getMap()).getArea()));
		
		execute(new OpenDepoBoxEvent());
		execute(new DepoBoxDepositAllEvent());
		execute(new CloseDepoBoxEvent());
		
		WebWalkEvent returnEvent = new WebWalkEvent(returnPosition);
		execute(returnEvent);
		new ConditionalSleep(10_000) {
			@Override public boolean condition() throws InterruptedException {
				return myPlayer().getPosition().equals(returnPosition) &&
						!myPlayer().isMoving();
			}
		}.sleep();
		
		setFinished();
		return 0;
	}

}
