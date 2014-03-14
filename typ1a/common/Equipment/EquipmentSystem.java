package com.typ1a.common.Equipment;

import java.util.List;
import java.util.Vector;

import net.minecraft.item.ItemStack;


/**The idea is to break up each type of equipment into different systems.
 * Each ItemStack adds an arbitrary subsystem.*/
public abstract class EquipmentSystem{

	protected List<Subunit> subunitList= new Vector<Subunit>();

	protected EquipmentFacade eqf;
	
	public EquipmentSystem(EquipmentFacade eqf){
		this.eqf= eqf;
	}
	public EquipmentSystem(){}
	
	public void activate(boolean state){}
	
	public void update(){
		for(Subunit s : this.subunitList)
			eqf.addToHeat( s.update() );
	}

	
	/**MUST CHECK- if( item instanceof appropriateItemEquipmentType)
	 * @param stack only used for subunit ops. May be null*/
	public void addItem(ItemEquipment item, ItemStack stack){
		//TODO
		subunitList.add(item.makeSubunit(stack));
		System.out.println("added "+subunitList.get(subunitList.size()-1));
	}
	public void removeItem(ItemEquipment item){
		System.out.println("removing "+subunitList.get(subunitList.size()-1));
		//TODO
	}

	public static interface Subunit{
		/**@return energy use*/
		public int update();
	}
}
