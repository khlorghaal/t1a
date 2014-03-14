package com.typ1a.common.SmallArms;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

public class ItemMiningLaser extends ItemSmallArm {

	public ItemMiningLaser() {
		super("Mining Laser");
	}

	@Override
	public void use(EntityPlayer player, NBTTagCompound dat){
		new TickerMiningLaser(player, dat);
	}
	
	@Override
	public float getRecoilPitch(Random r) {
		return (float)r.nextGaussian()*3;
	}

	@Override
	public float getRecoilYaw(Random r) {
		return (float)r.nextGaussian()*3;
	}

	@Override
	public Item getAmmoItem() {
		return null;
	}


	@Override
	public float getDamage() {
		return 1;
	}

	@Override
	public int getRof() {
		return 0;
	}

	@Override
	public int getReloadSpeed() {
		return 25;
	}

	@Override
	public double getProjectileSpeed() {
		return 0;
	}

	@Override
	public double getDeviation() {
		return 0;
	}

	@Override
	public String getSound() {
		return null;
	}

	@Override
	public int getInternalMagSize() {
		return 4000;
	}
}
