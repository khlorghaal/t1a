package com.typ1a.common.Containers;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerProper extends Container {

	public ContainerProper(InventoryPlayer playerinv, IInventory holder, int[] coords, int yOffset){
		assert(coords.length %2 == 0);

		for(int i=0; i!=coords.length;){
			final int sn= i/2;
			final int x= coords[i++];
			final int y= coords[i++];

			addSlotToContainer(new Slot(holder, sn, x, y));
		}

		bind(playerinv, yOffset);

	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {

		return true;
	}

	public void bind(InventoryPlayer playerinv, int yOffset){
		for (int x = 0; x < 9; x++) {
			this.addSlotToContainer(new Slot(playerinv, x, 8+x*18, 126+yOffset));	}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(playerinv, y*9+x+9, 8+x*18, 68+y*18+yOffset));	}
		}

	}

	@Override
	/**or you crash, dunno why TODO i guess*/
	public ItemStack transferStackInSlot(EntityPlayer player, int i){return null;}
}
