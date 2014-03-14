package com.typ1a.common.Equipment;

import net.minecraft.item.ItemStack;

import com.typ1a.common.BlocksItems.T1AItem;
import com.typ1a.common.Equipment.EquipmentSystem.Subunit;

public class ItemHeatSynch extends T1AItem implements ItemEquipment {

	public ItemHeatSynch(String name, String tex) {
		super(name, tex, 1, 100);
	}

	protected int coolAmount = -5;

	@Override
	public Subunit makeSubunit(ItemStack stack) {
		return null;
	}
	
	

}
