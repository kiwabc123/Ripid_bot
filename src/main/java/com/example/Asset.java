package com.example;


/**
 * Stores all the state for an asset, as described in <a href= "https://docs.coinapi.io/#list-all-assets">https://docs.coinapi.io/#list-all-assets</a>.
 * <p>
 * This class is multithread safe: it is <a href="http://www.ibm.com/developerworks/java/library/j-jtp02183.html">immutable</a>.
 * In particular, it is always <a href="http://www.ibm.com/developerworks/java/library/j-jtp0618.html">properly constructed</a>,
 * all of its fields are final,
 * and none of their state can be changed after construction.
 * See p. 53 of <a href="http://www.javaconcurrencyinpractice.com">Java Concurrency In Practice</a> for more discussion.
 */
public class Asset {
	
	/** Our asset identifier. Superset of the ISO 4217 currency codes standard */
	private final String asset_id;                     // 
	
	/** Display name of the asset */
	private final String name;
	
	/** true for cryptocurrency assets, false otherwise */
	private final boolean type_is_crypto;
	
	public Asset(String asset_id, String name, boolean type_is_crypto) {
		this.asset_id = asset_id;
		this.name = name;
		this.type_is_crypto = type_is_crypto;
	}
	
	public String get_asset_id() {
		return asset_id;
	}
	
	public String get_name() {
		return name;
	}
	
	public boolean is_type_crypto() {
		return type_is_crypto;
	}
	
}
