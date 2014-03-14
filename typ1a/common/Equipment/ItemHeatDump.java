package com.typ1a.common.Equipment;

import net.minecraft.item.ItemStack;

import com.typ1a.common.BlocksItems.T1AItem;
import com.typ1a.common.Equipment.EquipmentSystem.Subunit;

public class ItemHeatDump extends T1AItem implements ItemEquipment {

	public ItemHeatDump(String name, String tex) {
		super("Heat Discharger", tex, 1, 100);
	}
	
	int coolAmount=-50;




	@Override
	public Subunit makeSubunit(ItemStack stack) {
		return null;
	}
}
