package events;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.utility.Condition;
import org.osbot.rs07.utility.ConditionalSleep;

public class PayDirtEvent extends Event {
	private static final Position[] LOOP = {
		    new Position(3728, 5687, 0),
		    new Position(3727, 5683, 0),
		    new Position(3727, 5677, 0),
		    new Position(3726, 5671, 0),
		    new Position(3725, 5663, 0),
		    new Position(3725, 5660, 0),
		    new Position(3727, 5658, 0),
		    new Position(3724, 5652, 0),
		    new Position(3720, 5647, 0),
		    new Position(3723, 5641, 0),
		    new Position(3722, 5637, 0),
		    new Position(3716, 5637, 0),
		    new Position(3716, 5642, 0),
		    new Position(3717, 5644, 0),
		    new Position(3716, 5654, 0),
		    new Position(3716, 5654, 0),
		    new Position(3720, 5658, 0),
		    new Position(3719, 5667, 0),
		    new Position(3720, 5672, 0),
		    new Position(3721, 5681, 0),
		    new Position(3724, 5685, 0)
		};
	private static final Position[] UPSTAIRS_LOOP = {
		    new Position(3755, 5675, 0),
		    new Position(3757, 5678, 0),
		    new Position(3756, 5682, 0),
		    new Position(3760, 5684, 0),
		    new Position(3762, 5685, 0),
		    new Position(3765, 5682, 0),
		    new Position(3764, 5679, 0),
		    new Position(3761, 5677, 0)
		};
	private static final boolean ENABLE_UPSTAIRS = true;
	private static final Area UPSTAIRS_AREA  = new Area(
		    new int[][]{
		        { 3734, 5687 },
		        { 3747, 5687 },
		        { 3749, 5685 },
		        { 3750, 5685 },
		        { 3751, 5686 },
		        { 3760, 5686 },
		        { 3761, 5685 },
		        { 3763, 5685 },
		        { 3764, 5684 },
		        { 3764, 5679 },
		        { 3765, 5679 },
		        { 3767, 5677 },
		        { 3767, 5676 },
		        { 3764, 5673 },
		        { 3764, 5667 },
		        { 3765, 5665 },
		        { 3765, 5662 },
		        { 3764, 5660 },
		        { 3766, 5658 },
		        { 3766, 5655 },
		        { 3761, 5655 },
		        { 3761, 5673 },
		        { 3758, 5673 },
		        { 3752, 5678 },
		        { 3741, 5682 },
		        { 3735, 5682 }
		    }
		);
	
	private static final long ANIMATION_DELAY = 5_000;
	private static final int VEIN_ANIMATION   = 6752;
	
	private long animationTimer = 0;
	private int loopIndex 		= 0;
	private Position[] loop;
	
	public PayDirtEvent() {
		if (ENABLE_UPSTAIRS)
			loop = UPSTAIRS_LOOP;
		else
			loop = LOOP;
	}
	
	private void mineVein() {
		RS2Object vein = getVein();
		if (vein.interact("Mine")) {
			getMouse().moveOutsideScreen();
			new ConditionalSleep(10_000) {
				@Override public boolean condition() throws InterruptedException {
					return myPlayer().isAnimating();
				}
			}.sleep();
		} else {
			getWalking().webWalk(vein.getArea(1));
		}
	}
	
	private RS2Object getVein() {
		if (ENABLE_UPSTAIRS) {
			return getObjects().closest(true, new Filter<RS2Object>() {
				@Override public boolean match(RS2Object o) {
					return ("Ore vein".equals(o.getName()) &&
					    UPSTAIRS_AREA.contains(o));
				}
			});
		} else {
			return getObjects().closest(true, new Filter<RS2Object>() {
				@Override public boolean match(RS2Object o) {
					return ("Ore vein".equals(o.getName()) &&
					    !UPSTAIRS_AREA.contains(o) &&
						getMap().canReach(o) &&
						!hasCompetition(o));
				}
			});
		}
	}
	
	private boolean hasCompetition(RS2Object o) {
		return getPlayers().closest(
					new Filter<Player>() {
						@Override public boolean match(Player p) {
							return (o.getArea(1).contains(p) && !p.getName().equals(myPlayer().getName()));
						}
					}
			   ) != null;
	}
	
	private void walkNode() throws InterruptedException {
		log("[PayDirtEvent] Walking node");
		WebWalkEvent wwe = new WebWalkEvent(
				loop[loopIndex % loop.length]
		);
		wwe.setBreakCondition(new Condition() {
			@Override public boolean evaluate() {
				return getVein() != null;
			}
		});
		execute(wwe);
		if (!wwe.hasFailed())
			loopIndex++;
	}
	
	@Override public int execute() throws InterruptedException {
		if (myPlayer().getAnimation() == VEIN_ANIMATION) 
			animationTimer = System.currentTimeMillis();
		
		if (!getInventory().isFull()) {
			if (!myPlayer().isAnimating()) {
				if (getVein() == null)
					walkNode();
				if (System.currentTimeMillis() - animationTimer > ANIMATION_DELAY+random(0, 3_000))
					mineVein();
			}
		} else setFinished();
		return random(1_000, 3_000);
	}

}
