package com.typ1a.common.Projectiles;

import com.typ1a.common.utils.Vector3;

public class Laser{
	int dmg, color;
	Vector3 origin, hit;
	Barrel barrel;
	
	public Laser(Barrel barrel, int dmg, int color) {
		this.barrel= barrel;
		this.dmg= dmg;
		this.color= color;
	}
	public void update(){
		barrel.calculatePosAndLook();
		origin= barrel.muzzleEndTransformed;
	}
}
