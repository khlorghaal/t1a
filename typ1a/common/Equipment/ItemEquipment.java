package com.typ1a.common.Equipment;

import net.minecraft.item.ItemStack;

import com.typ1a.common.Equipment.EquipmentSystem.Subunit;

public interface ItemEquipment {
	
	public abstract Subunit makeSubunit(ItemStack stack);
}
