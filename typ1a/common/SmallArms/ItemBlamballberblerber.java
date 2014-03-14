package com.typ1a.common.SmallArms;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import com.typ1a.common.Projectiles.EntityBullet;

public class ItemBlamballberblerber extends ItemSmallArm{

	public ItemBlamballberblerber() {
		super("Blamball Berblerber");
	}
	@Override
	public void use(EntityPlayer player, NBTTagCompound data) {
		new TickerBBB(player, data);
	}
	@Override
	public void stop(EntityPlayer player){}
	
	public static class TickerBBB extends TickerSemiAuto{
		public TickerBBB(EntityPlayer player, NBTTagCompound data){
			super(player, data);
		}
		@Override
		public void doAction(){
			new EntityBullet(BarrelTracker.get(player), speed, deviation, r, dmg, EntityBullet.MININUKE, false);
		}
	}
	@Override
	public float getRecoilPitch(Random r) {
		return .1f;
	}

	@Override
	public float getRecoilYaw(Random r) {
		return 0;
	}

	@Override
	public Item getAmmoItem() {
		return null;
	}

	@Override
	public int getReloadSpeed() {
		return 1;
	}

	@Override
	public float getDamage() {
		return 0;
	}

	@Override
	public int getRof() {
		return 0;
	}

	@Override
	public double getProjectileSpeed() {
		return 6;
	}

	@Override
	public double getDeviation() {
		return 0;
	}

	@Override
	public String getSound() {
		return "bloop";
	}

	@Override
	public int getInternalMagSize() {
		return 10;
	}

}
