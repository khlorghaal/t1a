package com.typ1a.common.SmallArms;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.typ1a.common.BlocksItems.BlockLightSource;

public class TickerMiningLaser extends TickerSmallArm {

	public TickerMiningLaser(EntityPlayer player, NBTTagCompound dat) {
		super(player, dat);
	}

	@Override
	protected void doAction(){
		world.setBlock((int)player.posX, (int)player.posY, (int)player.posZ, BlockLightSource.instance);
		
	}
}
