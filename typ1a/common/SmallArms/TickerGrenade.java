package com.typ1a.common.SmallArms;

import net.minecraft.entity.player.EntityPlayer;

import com.typ1a.common.BlocksItems.TickerChargeableItem;
import com.typ1a.common.Projectiles.EntityGrenade;

public class TickerGrenade extends TickerChargeableItem {

	final int type;
	
	public TickerGrenade(EntityPlayer player, int type) {
		super(player);
		this.type= type;
	}

	@Override
	public void doAction(){		
		if(!player.worldObj.isRemote){
			final int color= ItemGrenade.getColorInt(stack);
			new EntityGrenade(BarrelTracker.get(player), speed*this.ticksDone, deviation, type, color);
		}
	}
}
