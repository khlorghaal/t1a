package com.typ1a.common.SmallArms;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import com.typ1a.common.AmmoStuff.ItemAmmo;

public class ItemTOW extends ItemSmallArm{

	public static Item instance;
	
	public ItemTOW(){
		super("TOW");
		this.instance=this;
	}
	@Override
	public void use(EntityPlayer player, NBTTagCompound data) {
		new TickerTOW(player, data);
	}

	@Override
	public Item getAmmoItem() {		return ItemAmmo.ItemMissile.instance;	}
	@Override
	public int getInternalMagSize() {		return 1;	}
	
	@Override
	public int getRof() {		return 0;	}

	@Override
	public double getProjectileSpeed() {		return 0;	}

	@Override
	public String getSound() {
		return "missile";
	}

	@Override
	public int getReloadSpeed() {
		return 10;
	}

	@Override
	/**is missile*/
	public float getDamage() {
		return 0;
	}
	@Override
	public double getDeviation() {
		return 0;
	}
	@Override
	public float getRecoilPitch(Random r) {		return 5;	}
	@Override
	public float getRecoilYaw(Random r) {		return 0;	}
	
}