package com.typ1a.common.Equipment;

import net.minecraft.item.ItemStack;

import com.typ1a.common.BlocksItems.T1AItem;
import com.typ1a.common.Equipment.EquipmentSystem.Subunit;

public abstract class ItemArmor extends T1AItem implements ItemEquipment {

	public ItemArmor(String name, String texIndex,	int maxDmg) {
		super(name, texIndex, 1, maxDmg);
		this.canRepair=true;
	}


	public static class ItemArmorSteel extends ItemArmor{
		public ItemArmorSteel() {
			super("Steel Armor Plates", "armorsteel", 1000);}	
		@Override
		public Subunit makeSubunit(ItemStack stack) {
			return new Armor.SteelArmorPlate(stack);
		}
	}

	public static class ItemArmorCeramic extends ItemArmor{
		public ItemArmorCeramic() {
			super("Ceramic Armor Plates", "armorceramic", 300);}
		@Override
		public Subunit makeSubunit(ItemStack stack) {
			return new Armor.CeramicArmorPlate(stack);
		}
	}
	public static class ItemArmorKevlar extends ItemArmor{
		public ItemArmorKevlar() {
			super("Aluminum-Kevlar Armor Plates", "armorkevlar", 1000);}	
		@Override
		public Subunit makeSubunit(ItemStack stack) {
			return new Armor.KevlarArmorPlate(stack);
		}
	}
}
