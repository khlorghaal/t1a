package com.typ1a.common.SmallArms;

import java.util.Random;

import net.minecraft.item.Item;

import com.typ1a.common.AmmoStuff.ItemMagazine5mm;


public class ItemSMG extends ItemSmallArm {

	public static Item instance;
	
	public ItemSMG() {
		super("SMG");
		this.instance=this;
	}

	
	@Override
	public int getInternalMagSize() {return 0;}
	
	@Override
	public int getRof(){return 1;}
	@Override
	public int getReloadSpeed() {return 85;}
	@Override
	public double getProjectileSpeed() {return 3.5;}
	@Override
	public String getSound() {return "smg";}
	@Override
	public Item getAmmoItem() {	return ItemMagazine5mm.instance;}

	@Override
	public float getDamage() {
		return 2.5f;
	}

	@Override
	public double getDeviation() {
		return .25;
	}


	@Override
	public float getRecoilPitch(Random r) {
		return (r.nextFloat()-.5f)*15f+15;
	}

	@Override
	public float getRecoilYaw(Random r) {
		return (r.nextFloat()-.5f)*40f;
	}

}
