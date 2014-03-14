package com.typ1a.common.Equipment;

import net.minecraft.item.ItemStack;

import com.typ1a.common.BlocksItems.T1AItem;
import com.typ1a.common.Equipment.EquipmentSystem.Subunit;

public class ItemShield extends T1AItem implements ItemEquipment {

	public static ItemShield instance;
	public ItemShield(String name, String tex) {
		super(name, tex, 1, 100);
		instance=this;
	}


	@Override
	public Subunit makeSubunit(ItemStack stack) {
		return new Shields.Shield(stack);
	}

	public static class ItemShieldHeavy extends ItemShield{
		public ItemShieldHeavy() {
			super("Heavy Shield", "equipment/shieldheavy");
		}
		@Override
		public Subunit makeSubunit(ItemStack stack) {
			return new Shields.HeavyShield(stack);
		}
	}

	public static class ItemShieldFast extends ItemShield{
		public ItemShieldFast() {
			super("Fast Shield","equipment/shieldfast");
		}
		@Override
		public Subunit makeSubunit(ItemStack stack) {
			return new Shields.FastShield(stack);
		}
	}
}
