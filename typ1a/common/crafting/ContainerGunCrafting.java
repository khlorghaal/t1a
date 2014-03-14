package com.typ1a.common.crafting;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public class ContainerGunCrafting extends ContainerWorkbench {

	public ContainerGunCrafting(InventoryPlayer par1InventoryPlayer,
			World par2World, int par3, int par4, int par5) {
		super(par1InventoryPlayer, par2World, par3, par4, par5);
	}

	@Override
    public void onCraftMatrixChanged(IInventory par1IInventory){
		
	}
}
