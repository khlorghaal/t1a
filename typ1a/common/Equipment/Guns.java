package com.typ1a.common.Equipment;

import net.minecraft.item.ItemStack;



public class Guns extends EquipmentSystem{
	
	public boolean[] isGunSideMount= new boolean[5];
	
	public Guns(){
		super();
		this.subunitList.add(null);
		this.subunitList.add(null);
		this.subunitList.add(null);
		this.subunitList.add(null);
		this.subunitList.add(null);
	}
	
	@Override
	@Deprecated
	public void addItem(ItemEquipment item, ItemStack stack){}
	public void addItem(int index, ItemEquipment item, ItemStack stack){
		subunitList.set(index, item.makeSubunit(stack));
	}
	

	@Override
	@Deprecated
	public void removeItem(ItemEquipment item){}
	public void removeItem(int index, ItemEquipment item){
		this.subunitList.set(index, null);
	}
	
	public void setFiring(boolean[] triggers){
		assert(triggers.length==this.subunitList.size());
		for(int i=0; i<triggers.length; i++){
			((Gun)this.subunitList.get(i)).setFiring(triggers[i]);
		}
	}
	
	public Gun[] getGuns(){
		return (Gun[]) this.subunitList.toArray();
	}
}
