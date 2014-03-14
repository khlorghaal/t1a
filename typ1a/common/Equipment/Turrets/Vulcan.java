package com.typ1a.common.Equipment.Turrets;

import java.util.Random;

import net.minecraft.item.ItemStack;

import com.typ1a.common.AmmoStuff.ItemAmmo;
import com.typ1a.common.Equipment.AmmoManager;
import com.typ1a.common.Equipment.Gun;
import com.typ1a.common.Projectiles.Barrel;
import com.typ1a.common.Projectiles.EntityBullet;

public class Vulcan extends Gun {

	public Vulcan(Barrel barrel, Random r) {
		super(1, barrel, r, new AmmoManager( ItemAmmo.ItemRoundArty.instance), null);
	}

	@Override
	public double getMuzzleVelocity() {
		return 2;
	}

	@Override
	public double getDeviation() {
		return 0.1;
	}

	@Override
	protected void shoot() {
		new EntityBullet(this.barrel, this.getMuzzleVelocity(), this.getDeviation(), r, 4, this.ammo.popShot());
		barrel.aimer.worldObj.playSoundAtEntity(barrel.aimer, "t1a:smg", 1, 0.1f);
	}

}
