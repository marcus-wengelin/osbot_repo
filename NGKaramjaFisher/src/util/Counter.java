package util;

public class Counter {
	private long n;
	public Counter() {}
	public void inc(long x) { n+=x; }
	public long  get() { return n; }
}
