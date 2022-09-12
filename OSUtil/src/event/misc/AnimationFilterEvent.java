package event.misc;

import org.osbot.rs07.event.Event;

/**
 * Finished when we have not animated for a given
 * amount of time
 * 
 * @author user
 *
 */
public class AnimationFilterEvent extends Event {

	private long timeout;
	private long prevAnimation;
	public AnimationFilterEvent(long timeout) {
		this.timeout = timeout;
		this.prevAnimation = System.currentTimeMillis();
	}
	
	@Override
	public int execute() throws InterruptedException {
		if (getMouse().isOnScreen()) getMouse().moveOutsideScreen();
		
		if (myPlayer().isAnimating())
			prevAnimation = System.currentTimeMillis();
			
		if (System.currentTimeMillis() > prevAnimation + timeout)
			setFinished();

		return 100;
	}

}
