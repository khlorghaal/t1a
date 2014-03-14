package com.typ1a.common.AmmoStuff;

import com.typ1a.common.BlocksItems.T1AItem;

public class ItemCasing extends T1AItem {
	public static ItemCasing instance;
	
	public ItemCasing() {
		super("Brass Ammo Casing", 64, -1);
		instance= this;
	}
}
