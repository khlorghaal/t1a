package com.typ1a.common.SmallArms;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import com.typ1a.common.AmmoStuff.ItemAmmo;

public class ItemRifle extends ItemSmallArm {

	public static Item instance;
	
	public ItemRifle() {
		super("Rifle");
		this.instance=this;
	}
	@Override
	public void use(EntityPlayer player, NBTTagCompound dat) {
		new TickerSemiAuto(player, dat);
	}
	
	@Override
	public float getRecoilPitch(Random r) {		return 80;	}
	@Override
	public float getRecoilYaw(Random r) {		return (r.nextFloat()-.5f)*14f+10f;	}
	
	@Override
	public Item getAmmoItem() {		return ItemAmmo.ItemRoundBMG.instance;	}
	@Override
	public float getDamage() {		return 12;	}
	@Override
	public int getRof() {		return 42;	}
	@Override
	public int getReloadSpeed() {		return 20;	}
	@Override
	public double getProjectileSpeed() {		return 5.7;	}
	@Override
	public double getDeviation() {		return .065;	}
	@Override
	public String getSound() {		return "sniper";	}
	@Override
	public int getInternalMagSize(){return 5;}
}
