package com.typ1a.common.AmmoStuff;

import net.minecraft.item.Item;

public class ItemMagazine5mm extends ItemMagazine {

	public static ItemMagazine instance;
	public ItemMagazine5mm() {
		super("5mm Magazine", 48);
		instance=this;
	}
	@Override
	public Item getAmmoItem() {
		return ItemAmmo.ItemRound5mm.instance;
	}
}
