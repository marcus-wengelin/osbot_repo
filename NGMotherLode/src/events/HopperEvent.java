package events;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.filter.NameFilter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.utility.ConditionalSleep;

public class HopperEvent extends Event {
	/*private static final Area MIDDLE_AREA = new Area(
		    new int[][]{
		        { 3744, 5655 },
		        { 3750, 5655 },
		        { 3752, 5657 },
		        { 3752, 5658 },
		        { 3753, 5659 },
		        { 3754, 5659 },
		        { 3755, 5660 },
		        { 3757, 5661 },
		        { 3760, 5661 },
		        { 3761, 5662 },
		        { 3760, 5663 },
		        { 3760, 5664 },
		        { 3762, 5666 },
		        { 3762, 5667 },
		        { 3760, 5669 },
		        { 3760, 5671 },
		        { 3759, 5672 },
		        { 3758, 5672 },
		        { 3756, 5674 },
		        { 3755, 5674 },
		        { 3754, 5675 },
		        { 3753, 5675 },
		        { 3751, 5677 },
		        { 3746, 5677 },
		        { 3742, 5676 },
		        { 3740, 5673 },
		        { 3741, 5672 },
		        { 3741, 5671 },
		        { 3740, 5670 },
		        { 3740, 5668 },
		        { 3741, 5667 },
		        { 3741, 5665 },
		        { 3740, 5664 },
		        { 3740, 5661 },
		        { 3741, 5660 },
		        { 3741, 5659 },
		        { 3742, 5658 },
		        { 3742, 5657 }
		    }
		);*/
	private static final Area HOPPER_AREA = new Area(3749, 5673, 3750, 5672);
	private static final Area SACK_AREA   = new Area(3749, 5659, 3748, 5658);
	private static final Area STRUT_AREA  = new Area(3741, 5671, 3741, 5661);
	
	private boolean hasPaydirt() {
		return getInventory().contains("Pay-dirt");
	}
	
	private int getSackAmount() {
		RS2Widget w = getWidgets().get(382, 1, 2);
		if (w != null) {
			return Integer.parseInt(w.getMessage());
		}
		return 0;
	}
	
	private boolean hasOre() {
		return getInventory().contains(new Filter<Item>() {
			@Override public boolean match(Item i) {
				return (i.getName().contains("ore") || i.getName().contains("nugget"));
			}
		});
	}
	
	private boolean needRepairs() {
		return getObjects().filter(new NameFilter<RS2Object>("Broken strut")).size() > 1;
	}
	
	private void repairStrut(final RS2Object strut) {
		if (strut == null) return;
		strut.interact("Hammer");
		new ConditionalSleep(30_000) {
			@Override public boolean condition() throws InterruptedException {
				return !strut.exists() || !strut.hasAction("Hammer");
			}
		}.sleep();
	}
	
	private void fixStruts() {
		if (!STRUT_AREA.contains(myPlayer())) {
			getWalking().webWalk(STRUT_AREA);
		} else {
			RS2Object strut = getObjects().closest("Broken strut");
			repairStrut(strut);
		}
	}
	
	private void depoPaydirt() {
		if (!HOPPER_AREA.contains(myPlayer())) {
			getWalking().webWalk(HOPPER_AREA);
		} else {
			RS2Object hopper = getObjects().closest("Hopper");
			if (hopper != null) {
				hopper.interact("Deposit");
				new ConditionalSleep(5_000) {
					@Override public boolean condition() throws InterruptedException {
						return !hasPaydirt();
					}
				}.sleep();
			}
		}
	}
	
	private void getOre() {
		if (!SACK_AREA.contains(myPlayer())) {
			getWalking().webWalk(SACK_AREA);
		} else {
			RS2Object sack = getObjects().closest("Sack");
			if (sack != null) {
				sack.interact("Search");
				new ConditionalSleep(5_000) {
					@Override public boolean condition() throws InterruptedException {
						return hasOre();
					}
				}.sleep();
				setFinished();
			}
		}
	}	
	
	@Override public int execute() throws InterruptedException {
		if (needRepairs() && getSackAmount()<=0)
			fixStruts();
		else if (hasPaydirt())
			depoPaydirt();
		else if (getSackAmount()>0)
			getOre();
		else if (!SACK_AREA.contains(myPlayer()))
			getWalking().webWalk(SACK_AREA);
		return 300;
	}
}
