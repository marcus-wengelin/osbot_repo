package util;

/**
 * A class that represents constraints that need
 * to be met for certain actions to be completed 
 * 
 * @author Marcus
 */
public interface Constraint {
	public abstract boolean solveable();
	public abstract boolean solved();
	public abstract boolean solve();
}
