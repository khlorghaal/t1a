package com.typ1a.common.SmallArms;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import com.typ1a.common.AmmoStuff.ItemAmmo;

public class ItemShotgun extends ItemSmallArm {
	
	public static Item instance;
	
	public ItemShotgun() {
		super("Shotgun");
		this.instance=this;
	}
	@Override
	public void use(EntityPlayer player, NBTTagCompound data) {
		new TickerShotgun(player, data);
	}
	
	@Override
	public Item getAmmoItem() {
		return ItemAmmo.ItemRoundShot.instance;
	}

	@Override
	public int getRof() {return 14;}
	
	@Override
	public int getReloadSpeed(){return 26;}

	@Override
	public double getProjectileSpeed() {return 2.4;}

	@Override
	public String getSound() {return "shotgun";}
	@Override
	public float getDamage() {return 1.1f;}
	@Override
	public double getDeviation() {return .26;}
	@Override
	public float getRecoilPitch(Random r) {
		return  (r.nextFloat()-.5f)*1f+5;
	}
	@Override
	public float getRecoilYaw(Random r) {
		return (r.nextFloat()-.5f)*6f+1;
	}
	@Override
	public int getInternalMagSize() {return 6;}
}
