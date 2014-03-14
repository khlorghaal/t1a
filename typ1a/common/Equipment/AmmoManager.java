package com.typ1a.common.Equipment;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AmmoManager{

	private ItemStack[] stacks;
	private Item ammoItem;

	public AmmoManager(){}

	/**@param stacks all slots which the weapon has access to, auto validates*/
	public AmmoManager(Item ammoItem, ItemStack... stacks){
		this.stacks= stacks;
		this.ammoItem= ammoItem;
	}
	
	/**@return the useable stacks out of the given*/
	public ItemStack[] getUseableStacks(ItemStack[] in){
		final ArrayList<ItemStack> newStacks= new ArrayList<ItemStack>(); 
		for(ItemStack stack : in){
			if(stack!=null && stack.getItem()==this.ammoItem)
				newStacks.add(stack);
		}
		return newStacks.toArray(new ItemStack[newStacks.size()]); 
	}
	
	/**@return bullet type fired, 0 if not typed*/
	public int popShot(){
		final ItemStack[] stacks= getUseableStacks(this.stacks);
		if(stacks!=null && stacks.length!=0){
			stacks[0].stackSize--;
		}
		return 0;
	}
	
	public int getShotsRemaining(){
//		if(magazines){
//			for(ItemStack stack : stacks){
//				this.ammoRemaining+= stack.getMaxDamage() - stack.getItemDamage();
//			}
//			this.ammoRemaining-= stacks.length;//mags have 1 extra maxdmg to stay alive
//		}
//		else{
//			for(ItemStack stack : stacks){
//				this.ammoRemaining+= stack.stackSize;
//			}
//		}
		return 1;
	}

	
////////////////////////////////
	public static class AmmoManagerHeat extends AmmoManager{
		private IEquipmentAcceptor heat;
		private int maxHeat, heatPerShot;

		public AmmoManagerHeat(IEquipmentAcceptor heat, int maxHeat, int heatPerShot){
			this.heat=heat;
			this.maxHeat=maxHeat;
			this.heatPerShot=heatPerShot;
		}
		@Override
		public int popShot(){
			//heat.addToHeat(heatPerShot);
			return 0;
		}
		@Override
		public int getShotsRemaining(){
			//return (maxHeat - heat.getHeat()) / heatPerShot ;
			return 1;
		}
	}

///////////////////////////////
//	public static class AmmoManagerSmallarm extends AmmoManager {
//		EntityPlayer player;
//		public AmmoManagerSmallarm(ItemStack gun, EntityPlayer player){
//			super(gun, gun.getItem());
//			this.player= player;
//		}
//	}
}
