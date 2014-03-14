package com.typ1a.common;

import java.util.Random;

import net.minecraft.item.Item;

public interface IGun {
	public abstract float getRecoilPitch(Random r);
	public abstract float getRecoilYaw(Random r);

	/**ItemID of either magazine or round</br> 
	 * null if energy weapon*/
	public abstract Item getAmmoItem();
	public abstract int getReloadSpeed();
	
	public abstract float getDamage();
	/**Rof is actually ticks of interim. I know, it bothers me too, but would you know what TIntrm meant?*/
	public abstract int getRof();
	public abstract double getProjectileSpeed();
	public abstract double getDeviation();

	public abstract String getSound();
}
