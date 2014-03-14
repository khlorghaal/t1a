package com.typ1a.common.Containers;
import net.minecraft.entity.player.InventoryPlayer;

import com.typ1a.common.Equipment.EquipmentFacade;

public class ContainerEquipment extends ContainerProper{
	public static final int[] basenrg={
		44,115,
		26,115,
		8,115,
	};
	public static final int[] baseammo={
		8,80,
		26,80,
		44,80,

		8,62,
		26,62,
		44,62,
		
		8,44,
		26,44,
		44,44,
		
		8,26,
		26,26,
		44,26,
		
		8,7,
		26,7,
		44,7,		
	};
	public static final int[] basewpn={
		63,79,
		63,61,
		63,43,
		63,25,
		63,7,
	};
	public static final int[] basearmr={
		147,79,
		147,61,
		147,43,		
		147,25,
	};

	public static final int[] baseequip={
		87,79,
		87,61,
		87,43,
		87,25,
		87,7,

		105,79,
		105,61,
		105,43,
		105,25,
		105,7,

		123,79,
		123,61,
		123,43,
		123,25,
		123,7,
	};
	

	/**populates from the base arrays based off numbers of slots*/
	public int[] slots;
	
	public ContainerEquipment (InventoryPlayer playerinv, EquipmentFacade eq){
		super(playerinv, eq, getSlots(eq), 72);
	}
	protected static int[] getSlots(EquipmentFacade eq){
		int[] ret= new int[(eq.narmor+eq.nequip+eq.nwpn)*2];
		int i=0;
		System.arraycopy(baseequip, 0, ret, i, eq.nequip*2);
		i+= eq.nequip*2;
		System.arraycopy(basearmr, 0, ret, i, eq.narmor*2);
		i+= eq.narmor*2;
		System.arraycopy(basewpn, 0, ret, i, eq.nwpn*2);
		i+= eq.nwpn*2;	
		return ret;
	}
}
