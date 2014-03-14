package com.typ1a.common.SmallArms;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.typ1a.common.Missiles.EntityWGM;

public class TickerTOW extends TickerSemiAuto {

	public TickerTOW(EntityPlayer player, NBTTagCompound data) {
		super(player, data);
	}
	
	@Override
	public void doAction(){
			new EntityWGM(player.worldObj, player, BarrelTracker.get(player));
	}

}
