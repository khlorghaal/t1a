package com.typ1a.common.SmallArms;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import com.typ1a.common.AmmoStuff.ItemMagazine5mm;

public class ItemAR extends ItemSmallArm {
	
	public static ItemAR instance;
	public ItemAR() {
		super("Assault Rifle");
		instance= this;
	}
	
	@Override
	public void use(EntityPlayer player, NBTTagCompound dat) {
		new TickerSemiAuto(player, dat);
	}
	
	@Override
	public float getRecoilPitch(Random r) {
		return (r.nextFloat()-.5f)*22f+20;
	}

	@Override
	public float getRecoilYaw(Random r) {
		return (r.nextFloat()-.5f)*52f;
	}

	@Override
	public Item getAmmoItem() {	return ItemMagazine5mm.instance;}

//	@Override
//	public int getMaxAmmo() {return 32+1;}
	/**used when constructing magazine Item*/
	public static int getMaxAmmo = 32+1;

	@Override
	public float getDamage() {
		return 5;
	}

	@Override
	public int getRof(){return 6;}

	@Override
	public int getReloadSpeed() {return 40;}

	@Override
	public double getProjectileSpeed() {return 5.0;}

	@Override
	public double getDeviation() {
		return .07;
	}

	@Override
	public String getSound() {return "tavor";}

	@Override
	public int getInternalMagSize(){return 0;}

}
