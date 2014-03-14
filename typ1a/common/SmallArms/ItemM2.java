package com.typ1a.common.SmallArms;

import java.util.Random;

import net.minecraft.item.Item;

import com.typ1a.common.AmmoStuff.ItemMagazineBMG;

public class ItemM2 extends ItemSmallArm {

	public static ItemM2 instance;
	
	public ItemM2() {
		super("M2 BMG");
		instance=this;
	}

	@Override
	public float getRecoilPitch(Random r) {
		return (r.nextFloat()*20);
	}

	@Override
	public float getRecoilYaw(Random r) {
		return (r.nextFloat()*20 -10);
	}

	@Override
	public Item getAmmoItem() {
		return ItemMagazineBMG.instance;
	}

	@Override
	public int getInternalMagSize() {
		return 100;
	}

	@Override
	public float getDamage() {
		return 14;
	}

	@Override
	public int getRof() {
		return 2;
	}

	@Override
	public int getReloadSpeed() {
		return 60;
	}

	@Override
	public double getProjectileSpeed() {
		return 5.5;
	}

	@Override
	public double getDeviation() {
		return .1;
	}

	@Override
	public String getSound() {return "m2";}

}
