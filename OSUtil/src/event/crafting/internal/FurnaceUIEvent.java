package event.crafting.internal;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.event.Event;

public class FurnaceUIEvent extends Event {
	
	private String target;
	
	public FurnaceUIEvent(String target) {
		this.target = target;
	}
	
	private boolean allSelected() {
		RS2Widget w = getWidgets().getWidgetContainingText("All");
		if (w != null) return w.getMessage().contains("col=ffffff");
		return false;
	}
	
	private void selectAll() {
		RS2Widget w = getWidgets().getWidgetContainingAction("All");
		if (w != null) w.interact("All");
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (!allSelected()) {
			log("[FurnaceUIEvent] Selecting all");
			selectAll();
			return random(500, 1_000);
		}
		
		RS2Widget w = getWidgets().singleFilter(new Filter<RS2Widget>() {
			@Override public boolean match(RS2Widget w) {
				return w.getSpellName().contains(target);
			}
		});
		
		if (w == null) {
			log("[FurnaceUIEvent] Did not find target bar");
			setFailed();
			return 100;
		}
		
		w.interact("Smelt");
		setFinished();

		return 100;
	}
}
