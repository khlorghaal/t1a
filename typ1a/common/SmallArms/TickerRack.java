package com.typ1a.common.SmallArms;

import net.minecraft.entity.player.EntityPlayer;

public class TickerRack extends TickerReload {

	public TickerRack(EntityPlayer player) {
		super(player);
		rls= 4;
	}
	@Override
	protected void reload(){
		SmallarmLoading.rack(held, player);
		player.worldObj.playSoundAtEntity(player, "rack", 1, 1);
	}
}
