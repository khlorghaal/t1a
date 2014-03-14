package com.typ1a.common.AmmoStuff;

import net.minecraft.item.Item;

import com.typ1a.common.SmallArms.ItemM2;


public class ItemMagazineBMG extends ItemMagazine {

	public static ItemMagazineBMG instance;
	public ItemMagazineBMG() {
		super("BMG Ammo Belt", ItemM2.instance.getInternalMagSize());
		instance=this;
	}
	@Override
	public Item getAmmoItem() {
		return ItemAmmo.ItemRoundBMG.instance;
	}

}
