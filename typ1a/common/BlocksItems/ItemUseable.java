package com.typ1a.common.BlocksItems;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


public abstract class ItemUseable extends T1AItem {

	public ItemUseable(String name, String tex) {
		super(name, tex);
	}
	public ItemUseable(String name) {
		super(name);
	}

	public abstract void use(EntityPlayer player, NBTTagCompound data);
	public abstract void stop(EntityPlayer player);

	//dont do usage here because vanilla will inhib player movement
}
