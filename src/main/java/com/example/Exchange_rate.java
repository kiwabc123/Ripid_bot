package com.example;


import java.time.Instant;

/**
 * Stores all the state for a current exchange rate, as described in <a href= "https://docs.coinapi.io/#get-all-current-rates">https://docs.coinapi.io/#get-all-current-rates</a>.
 * <p>
 * This class is multithread safe: it is <a href="http://www.ibm.com/developerworks/java/library/j-jtp02183.html">immutable</a>.
 * In particular, it is always <a href="http://www.ibm.com/developerworks/java/library/j-jtp0618.html">properly constructed</a>,
 * all of its fields are final,
 * and none of their state can be changed after construction.
 * See p. 53 of <a href="http://www.javaconcurrencyinpractice.com">Java Concurrency In Practice</a> for more discussion.
 */
public class Exchange_rate {
	
	/** Time of the market data used to calculate exchange rate */
	private final Instant time;
	
	/** Exchange rate base asset identifier */
	private final String asset_id_base;
	
	/** Exchange rate quote asset identifier */
	private final String asset_id_quote;
	
	/** Exchange rate between assets */
	private final double rate;
	
	public Exchange_rate(Instant time, String asset_id_base, String asset_id_quote, double rate) {
		this.time = time;
		this.asset_id_base = asset_id_base;
		this.asset_id_quote = asset_id_quote;
		this.rate = rate;
	}
	
	public Instant get_time() {
		return time;
	}
	
	public String get_asset_id_base() {
		return asset_id_base;
	}
	
	public String get_asset_id_quote() {
		return asset_id_quote;
	}
	
	public double get_rate() {
		return rate;
	}
	
}
