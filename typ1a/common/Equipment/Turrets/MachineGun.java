package com.typ1a.common.Equipment.Turrets;

import java.util.Random;

import net.minecraft.item.ItemStack;

import com.typ1a.common.AmmoStuff.ItemAmmo;
import com.typ1a.common.Equipment.AmmoManager;
import com.typ1a.common.Equipment.Gun;
import com.typ1a.common.Projectiles.Barrel;

public class MachineGun extends Gun {
	
	public MachineGun(Barrel barrel, Random r) {
		super(1, barrel, r,
				new AmmoManager( ItemAmmo.ItemRoundBMG.instance, new ItemStack[5]), 
				null);
	}

	@Override
	public double getDeviation(){
		return .03;
	}
	@Override
	public double getMuzzleVelocity() {
		return 1.0;
	}
}
