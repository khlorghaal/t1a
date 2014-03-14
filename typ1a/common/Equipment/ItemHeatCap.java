package com.typ1a.common.Equipment;

import net.minecraft.item.ItemStack;

import com.typ1a.common.BlocksItems.T1AItem;
import com.typ1a.common.Equipment.EquipmentSystem.Subunit;


public class ItemHeatCap extends T1AItem implements ItemEquipment {

	public ItemHeatCap(String name, String tex) {
		super(name, tex, 1, 100);
	}
	
	public class ItemHeavyHeatCap extends ItemHeatCap{
		public ItemHeavyHeatCap() {
			super("Pressurized H2 Heat Capacitor","equheath2");
		}
		
	}

	@Override
	public Subunit makeSubunit(ItemStack stack) {
		return null;
	}

}
