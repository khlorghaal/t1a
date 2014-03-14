package com.typ1a.common.SmallArms;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.typ1a.common.T1A;
import com.typ1a.common.Ticker;
import com.typ1a.common.Projectiles.EntityBullet;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class TickerSmallArm extends Ticker{

	/**Used mostly for killing*/
	private static Map<Object, TickerSmallArm> map= new Hashtable<Object, TickerSmallArm>();
	/**this is needed for singleplay because for some retarded reason entityid is the hashvalue*/
	private static Map<Object, TickerSmallArm> mapClient= new Hashtable<Object, TickerSmallArm>();

	protected EntityPlayer player;
	protected ItemStack stack;
	protected ItemSmallArm item;
	protected NBTTagCompound dat;

	protected int rof, cooldown;
	protected float dmg;
	protected double speed;
	public int roundType;

	protected double deviation;
	protected Random r;
	World world;

	public TickerSmallArm(EntityPlayer player, NBTTagCompound data) {
		super();
		this.kill(player);
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
			this.map.put(player, this);
		else 
			this.mapClient.put(player, this);

		this.dat= data;
		this.r= new Random(dat.getInteger("rand"));

		this.player=player;
		this.world= player.worldObj;

		this.stack= player.getHeldItem();
		this.item= (ItemSmallArm) stack.getItem();
		this.rof= item.getRof();
		this.cooldown=0;
		this.dmg= item.getDamage();
		this.deviation= item.getDeviation();
		this.speed= item.getProjectileSpeed();
	}


	@Override
	public void onUpdate() {
		if(checkHeld())
			updateFireCycle();
	}

	public boolean checkHeld(){
		if(player==null || stack==null || player.getHeldItem()==null
				||(player.getHeldItem().getItem()!=item)){
			this.kill();
			return false;
		}
		return true;
	}
	protected void updateFireCycle(){
		if(cooldown-- <= 0){
			this.tryShoot();
			cooldown=rof;
		}
	}
	protected void tryShoot(){
		roundType= SmallarmLoading.tryShoot(dat);
		if(roundType==-1){//empty
			if(!player.capabilities.isCreativeMode){
				this.kill();
				this.player.worldObj.playSoundAtEntity(player, "t1a:empty", 1f, 1f);
				return;
			}
			roundType=1;
		}
		doAction();
		playSound();
	}
	protected void doAction(){
		float devmod= 1;
		float speedmod= 1;
		boolean silenced= false;
		if(stack.stackTagCompound.hasKey("Suppressor")){
			devmod*=ItemSmallArm.SUPPRDEV;
			silenced= true;
		}
		if(ItemGunPart.isMasterwork(stack.stackTagCompound.getCompoundTag("a")))
			devmod*=ItemSmallArm.MACTIONNDEV;
		if(ItemGunPart.isMasterwork(stack.stackTagCompound.getCompoundTag("ba"))){
			devmod*=ItemSmallArm.MBARRELDEV;
			speedmod*=ItemSmallArm.MBARRELSPEED;
		}
		if(ItemGunPart.isMasterwork(stack.stackTagCompound.getCompoundTag("bo")))
			speedmod+=ItemSmallArm.MBOLTSPEED;

		new EntityBullet(BarrelTracker.get(player), speed*speedmod, deviation*devmod, r, dmg, roundType, silenced);
	}
	protected void playSound(){
		if(player.worldObj.isRemote){
			if(ItemGunPart.getSTC(stack).hasKey("Suppressor"))
				T1A.commproxy.playSound("t1a:suppr", player.posX,player.posY,player.posZ, 1f, 1f);
			else
				T1A.commproxy.playSound("t1a:"+((ItemSmallArm)item).getSound(), player.posX,player.posY,player.posZ, 6f, 1f);
		}
	}
	@Override
	public void kill(){
		super.kill();
		if(player.worldObj.isRemote)
			this.map.remove(this);
		else
			this.mapClient.remove(this);
	}
	public static void kill(EntityPlayer owner){
		final TickerSmallArm ticky;
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
			ticky= map.get(owner);
		else
			ticky= mapClient.get(owner);

		if(ticky==null)
			return;
		ticky.kill();
	}

}