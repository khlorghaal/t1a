package com.typ1a.common.BlocksItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ItemSuspensor extends ItemUseable {

	public ItemSuspensor() {
		super("Suspensor");
	}
	
	@Override
	public void use(EntityPlayer player, NBTTagCompound dat) {
		TickerAgrav.start(player);		
	}
	@Override
	public void stop(EntityPlayer player) {
		TickerAgrav.stop(player);		
	}


}
