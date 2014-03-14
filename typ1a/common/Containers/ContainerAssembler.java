package com.typ1a.common.Containers;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerAssembler extends ContainerProper {

	public ContainerAssembler(InventoryPlayer playerinv, IInventory holder) {
		super(playerinv, holder, new int[]{30,30}, 16);
	}

}
