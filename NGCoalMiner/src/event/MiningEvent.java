package event;

import java.util.Comparator;
import java.util.List;

import org.osbot.rs07.api.filter.AreaFilter;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;

import constants.Ore;

public class MiningEvent extends Event {

	private static final long MAX_IDLE = 10_000;
	private static final Area MINING_GUILD = new Area(
		    new int[][]{
		        { 3016, 9741 },
		        { 3018, 9743 },
		        { 3020, 9743 },
		        { 3021, 9744 },
		        { 3024, 9744 },
		        { 3026, 9742 },
		        { 3029, 9742 },
		        { 3031, 9744 },
		        { 3033, 9744 },
		        { 3034, 9743 },
		        { 3035, 9743 },
		        { 3037, 9745 },
		        { 3037, 9748 },
		        { 3040, 9751 },
		        { 3041, 9751 },
		        { 3043, 9753 },
		        { 3043, 9754 },
		        { 3045, 9756 },
		        { 3045, 9757 },
		        { 3048, 9757 },
		        { 3048, 9756 },
		        { 3049, 9755 },
		        { 3049, 9753 },
		        { 3051, 9751 },
		        { 3052, 9751 },
		        { 3055, 9748 },
		        { 3055, 9742 },
		        { 3056, 9741 },
		        { 3056, 9739 },
		        { 3050, 9733 },
		        { 3049, 9733 },
		        { 3048, 9732 },
		        { 3034, 9732 },
		        { 3033, 9733 },
		        { 3027, 9733 },
		        { 3025, 9735 },
		        { 3021, 9735 },
		        { 3020, 9736 },
		        { 3018, 9736 },
		        { 3016, 9738 }
		    }
		);
	
	private int[] targetOres = Ore.COAL.getIds();
	private long idleTimer;
	
	private RS2Object currentOre;
	
	private boolean othersClose(RS2Object o) {
		List<Player> plist = getPlayers().filter(new AreaFilter<Player>(o.getArea(1)));
		for (Player p : plist)
			if (!myPlayer().getName().equals(p.getName())) return true;
		return false;
	}
	
	private boolean arrayContains(int[] arr, int x) {
		for (int n : arr)
			if (x == n) return true;
		return false;
	}
	
	private double preciseDistance(Position p1, Position p2) {
		return Math.sqrt((p1.getX()-p2.getX())*(p1.getX()-p2.getX()) +
						 (p1.getY()-p2.getY())*(p1.getY()-p2.getY()));
	}
	
	private boolean hasOre(RS2Object ore) {
		Position pos = ore.getPosition();
		for (RS2Object o : getObjects().get(pos.getX(), pos.getY())) {
			if (o.exists() && o.isVisible() && arrayContains(Ore.EMPTY.getIds(), o.getId()))
				return false;
			if (o.exists() && o.isVisible() && arrayContains(targetOres, o.getId()))
				return true;
		}
		return false;
	}
	
	private void mineOre() {
		List<RS2Object> ores = getObjects().filter(new Filter<RS2Object>() {
			@Override public boolean match(RS2Object o) {
				return MINING_GUILD.contains(o) &&
					   arrayContains(targetOres, o.getId()) &&
					   !othersClose(o);
			}
		});

		if (ores.isEmpty()) return;
		
		ores.sort(new Comparator<RS2Object>() {
			@Override public int compare(RS2Object a, RS2Object b) {
				return Double.compare(preciseDistance(a.getPosition(), myPosition()),
									  preciseDistance(b.getPosition(), myPosition()));
			}
		});
		
		currentOre = ores.get(0);
		currentOre.interact("Mine");
	}
	
	private boolean isIdle() {
		return !myPlayer().isMoving() && !myPlayer().isAnimating();
	}
	
	@Override public int execute() throws InterruptedException {
		if (getInventory().isFull()) setFinished();
		if (!isIdle()) idleTimer = System.currentTimeMillis();
		
		if (!MINING_GUILD.contains(myPlayer())) {
			getWalking().webWalk(MINING_GUILD);
		} else if (currentOre == null || !hasOre(currentOre)) {
			mineOre();
		} else if (System.currentTimeMillis() - idleTimer > MAX_IDLE) {
			currentOre = null;
		}
		return random(500, 1_000);
	}
}
