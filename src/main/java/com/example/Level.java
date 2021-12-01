package com.example;

/**
 * Stores all the state for an orderbook level, as described in <a href= "https://docs.coinapi.io/#order-book">https://docs.coinapi.io/#order-book</a>.
 * <p>
 * This class is multithread safe: it is <a href="http://www.ibm.com/developerworks/java/library/j-jtp02183.html">immutable</a>.
 * In particular, it is always <a href="http://www.ibm.com/developerworks/java/library/j-jtp0618.html">properly constructed</a>,
 * all of its fields are final,
 * and none of their state can be changed after construction.
 * See p. 53 of <a href="http://www.javaconcurrencyinpractice.com">Java Concurrency In Practice</a> for more discussion.
 */
public class Level {
	
	/** Price of bid/ask */
	private final double price;
	
	/** Volume resting on bid/ask level in base amount */
	private final double size;
	
	public Level(double price, double size) {
		this.price = price;
		this.size = size;
	}
	
	public double get_price() {
		return price;
	}
	
	public double get_size() {
		return size;
	}
	
}
