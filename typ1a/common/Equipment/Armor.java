package com.typ1a.common.Equipment;

import java.util.Random;

import net.minecraft.item.ItemStack;

public class Armor extends EquipmentSystem{

	int totalArmorVal=0, totalHpBonus=0, totalArmorDmg=0, armorMaxDmg=1, critProb=1;//init prob to 1 for no armor getting equiped
	//TODO a better damage distribution system

	static Random rand=new Random();
	
	/**Primary function*/
	public float hit(float f){
		if(rand.nextInt(critProb)==0){//crits dont damage armor
			return f;		//because a crit is a bypass of armor, duh 
		}

		float trueDmg=f-totalArmorVal;
		this.damageArmor(trueDmg);
		return trueDmg;
	}

	void damageArmor(float trueDmg){
		totalArmorDmg+= trueDmg;
		critProb=(totalArmorDmg/armorMaxDmg)/2;
		//TODO distribute to plates
	}
	/////////////////////////////////////
	public static abstract class ArmorPlate implements Subunit{
		int armorVal=1,hpBonus=100;
		ItemStack item;
		public ArmorPlate(ItemStack plate){
			this.item=plate;
		}
		public void damage(int dmg){
			item.damageItem(dmg, null);
		}
		public double getDmg(){
			return item.getItemDamage();
		}
		
		@Override
		public int update(){return 0;}
	}
	////

	public static class SteelArmorPlate extends ArmorPlate{
		public SteelArmorPlate(ItemStack plate){
			super(plate);
			this.armorVal=5;
			this.hpBonus=50;
		}
	}
	public static class CeramicArmorPlate extends ArmorPlate{
		public CeramicArmorPlate(ItemStack plate){
			super(plate);
			this.armorVal=5;
			this.hpBonus=50;
		}
	}

	public static class KevlarArmorPlate extends ArmorPlate{
		public KevlarArmorPlate(ItemStack plate){
			super(plate);
			this.armorVal=10;
			this.hpBonus=50;
		}
	}
	////////////////////////////////////
}
