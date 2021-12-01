package com.example;

import java.time.Instant;
import java.util.ArrayList;

/**
 * Stores all the state for an orderbook, as described in <a href= "https://docs.coinapi.io/#order-book">https://docs.coinapi.io/#order-book</a>.
 * <p>
 * This class is multithread safe: it is <a href="http://www.ibm.com/developerworks/java/library/j-jtp02183.html">immutable</a>.
 * In particular, it is always <a href="http://www.ibm.com/developerworks/java/library/j-jtp0618.html">properly constructed</a>,
 * all of its fields are final,
 * and none of their state can be changed after construction.
 * See p. 53 of <a href="http://www.javaconcurrencyinpractice.com">Java Concurrency In Practice</a> for more discussion.
 */
public class Orderbook {
	
	/** Our symbol identifier */
	private final String symbol_id;
	
	/** Exchange time of orderbook */
	private final Instant time_exchange;
	
	/** CoinAPI time when orderbook received from exchange */
	private final Instant time_coinapi;
	
	/** Best 20 bid levels in order from best to worst */
	private final Level[] bids;
	
	/** Best 20 ask levels in order from best to worst */
	private final Level[] asks;
	
	public Orderbook(String symbol_id, Instant time_exchange, Instant time_coinapi, ArrayList<Level> bids, ArrayList<Level> asks) {
		this.symbol_id = symbol_id;
		this.time_exchange = time_exchange;
		this.time_coinapi = time_coinapi;
		this.asks = new Level[asks.size()];
		this.bids = new Level[bids.size()];
		for (int i = 0; i < asks.size(); i++) {
			this.asks[i] = asks.get(i);
		}
		for (int i = 0; i < bids.size(); i++) {
			this.bids[i] = bids.get(i);
		}
	}
	
	public String get_symbol_id() {
		return symbol_id;
	}
	
	public Instant get_time_exchange() {
		return time_exchange;
	}
	
	public Instant get_time_coinapi() {
		return time_coinapi;
	}
	
	public int get_bids_count() {
		return bids.length;
	}
	
	public int get_asks_count() {
		return asks.length;
	}
	
	public Level get_bid(int index) {
		return bids[index];
	}
	
	public Level get_ask(int index) {
		return asks[index];
	}
	
}
