package com.typ1a.common.SmallArms;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.typ1a.common.Projectiles.EntityBullet;
import com.typ1a.common.Projectiles.EntityGrenade;

public class TickerShotgun extends TickerSemiAuto {

	public TickerShotgun(EntityPlayer player, NBTTagCompound dat) {
		super(player, dat);
	}

	@Override
	public void doAction(){
		if(roundType==EntityBullet.HE)
			new EntityGrenade(BarrelTracker.get(player), speed*.75, deviation/5, EntityGrenade.CONCUSSIONSHOTGUN,-1);
		else
			for(int i=0; i<20; i++){
				//			EntityCreeper fjorg= new EntityCreeper(world);
				//			fjorg.setPosition(player.posX, player.posY, player.posZ);
				//			fjorg.func_94058_c("Fjorg");
				//			fjorg.initCreature();
				//			world.spawnEntityInWorld(fjorg);

				super.doAction();
			}
	}
	@Override
	public void playSound(){
		if(roundType==EntityBullet.HE)
			this.player.worldObj.playSoundAtEntity(player, "t1a:bloop", 3f, .865f);
		else
			super.playSound();
	}
}