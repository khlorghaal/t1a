package com.typ1a.common.Containers;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerSilo extends ContainerProper{

	static int[] coords= {
		26,16,
		17,44,
		35,44};
	
	public ContainerSilo (InventoryPlayer playerinv, IInventory holder){
		super(playerinv, holder, coords,0);
	}

}