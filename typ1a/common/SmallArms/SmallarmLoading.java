package com.typ1a.common.SmallArms;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.typ1a.common.T1ACommonProxy;
import com.typ1a.common.AmmoStuff.ItemMagazine;
import com.typ1a.common.Equipment.IEquipmentAcceptor;
import com.typ1a.common.Projectiles.Barrel;
import com.typ1a.common.utils.Vector3;

/**Static methods operating on the stack TC  
 * for the firing sequence and actions of a gun*/
public class SmallarmLoading {

	/**Remote Only </br>
	 * for keeping clients synched with the server when spraying ordered ammo*/
	public static NBTTagCompound getShootingData(EntityPlayer player){

		NBTTagCompound ret= new NBTTagCompound();
		if(player.ridingEntity!=null && player.ridingEntity instanceof IEquipmentAcceptor){
			//TODO get from ammo manager
		}
		else if(player.getHeldItem()!=null 
				&& player.getHeldItem().getItem() instanceof ItemSmallArm){
			player.getHeldItem().writeToNBT(ret);
			ret= ret.getCompoundTag("tag");			
		}
		ret.setInteger("rand", T1ACommonProxy.seedgen.nextInt());
		return ret;
	}


	public static int getChamberRound(ItemStack gun){
		return gun.stackTagCompound.getByte("cha");
	}

	/**Get all stacks in the gun's loaded or internal mag + including round in chamber
	 * @param stack a smallarm
	 * @return is never null*/
	public static Cluster[] getMagClusters(ItemStack gun){
		return ItemMagazine.getClusters(ItemSmallArm.getSTC(gun).getCompoundTag("mag").getCompoundTag("tag"));
	}
	/**For use with packets. Truncates to -1 if chamber is empty*/
	public static byte[] getAllRounds(ItemStack gun){
		final byte cha= ItemSmallArm.getSTC(gun).getByte("cha");
		if(cha==-1)
			return new byte[]{-1};

		final Cluster[] magcls= getMagClusters(gun);
		final byte[] magt= Cluster.toByteArr(magcls);
		final byte[] ret= new byte[magt.length+1];
		ret[0]= cha;
		System.arraycopy(magt, 0, ret, 1, magt.length);
		return ret;
	}


	/**ejects and returns current, loads new
	 * @return -1 if empty, -2 if cant fire else btype*/
	public static int tryShoot(NBTTagCompound gunstc){
		//check cocked or jammed
		//if cha is none then uncock, return none
		//pull round from mag
		//put maground in cha
		//return prevcha

		if(!gunstc.getBoolean("c"))//not cocked
			return -1;

		final byte cha= gunstc.getByte("cha");

		if(cha==-2){//jammed
			gunstc.setBoolean("c", false);
			return -1;
		}

		if(gunstc.getByte("cha")==-1){//empty
			gunstc.setBoolean("c", false);
			return -1;
		}

		loadFromMag(gunstc);

		//		damagePart(gun, "ba");
		//		damagePart(gun, "bo");
		//		damagePart(gun, "a");
		//stock doesnt have durability

		return cha;
	}
	/**will set jam*/
	static void damagePart(ItemStack gun, String part, Random rand){
		final NBTTagCompound pstc= gun.stackTagCompound.getCompoundTag("parts").getCompoundTag(part).getCompoundTag("tag");
		int dura= pstc.getShort("dura");

		if( doesJam(pstc, rand) )//jam the gun
			gun.stackTagCompound.setByte("cha", (byte) -2);

		pstc.setShort("dura", (short) (dura-1));
	}

	/**calculated for each individual part*/
	public static final double 
	MINJAMCHANCE= 0.004 /3,// /3 because 3 jammable parts
	/**function of durability after this point*/
	THRESHOLD_JAMCHANCE_INCREASE=250,
	MAXJAMCHANCE= 0.2 /3;

	private static boolean doesJam(NBTTagCompound pstc, Random r){
		final double p= r.nextFloat(); 
		final double d= ItemGunPart.getDurabilityPercentage(ItemStack.loadItemStackFromNBT(pstc)); 
		//		if()
		return p<MINJAMCHANCE;
	}

	public static byte loadFromMag(NBTTagCompound guntc){
		final NBTTagCompound mtc= guntc.getCompoundTag("mag").getCompoundTag("tag");
		final byte ret= (byte) ItemMagazine.popRound(mtc);
		guntc.setByte("cha", ret);
		return ret;
	}
	/**@param in either a mag or round, or null to eject mag
	 * @return ejected mag, null if none*/
	public static ItemStack reload(ItemStack gun, ItemStack in){
		final ItemSmallArm isa= (ItemSmallArm)gun.getItem();
		final NBTTagCompound gstc= ItemSmallArm.getSTC(gun);

		//external mag
		if(isa.getInternalMagSize()<1){
			final ItemStack ret= ItemStack.loadItemStackFromNBT(gstc.getCompoundTag("mag"));
			if(in==null){//eject curmag
				gstc.removeTag("mag");
				return ret;
			}
			gstc.setTag("mag", in.writeToNBT(new NBTTagCompound()));

			if(ret!=null)
				ItemMagazine.updateDamage(ret);
			return ret;//may be null
		}
		//internal mag
		else{
			final NBTTagCompound magstc= gstc.getCompoundTag("mag").getCompoundTag("tag");
			ItemMagazine.pushRound(magstc, in.getItemDamage());
			return null;
		}
	}
	public static void rack(ItemStack gun, EntityPlayer holder){
		final NBTTagCompound gstc= ItemSmallArm.getSTC(gun);
		gstc.setBoolean("c", true);

		final int cha= gstc.getByte("cha");

		//jam muckery
		if(cha==-2){
			gstc.setByte("cha", (byte)-1);
			return;
		}

		//normal racking

		if(cha!=-1 //eject round
				&& !holder.worldObj.isRemote){
			final ItemStack ejectitem;
			final ItemSmallArm isa= ((ItemSmallArm)gun.getItem());
			final Item isaaitem= isa.getAmmoItem();
			if(isaaitem==null)//energy weapon
				return;
			
			if(isa.getInternalMagSize()>0)//internal
				ejectitem= new ItemStack( isaaitem, 1, cha);
			else{//external
				final Item ammoitem= ((ItemMagazine)isaaitem).getAmmoItem();
				ejectitem= new ItemStack( ammoitem, 1, cha);
			}
			final Entity eitem= new EntityItem(
					holder.worldObj, holder.posX, holder.posY, holder.posZ, 
					ejectitem);
			final Barrel barrel= BarrelTracker.get(holder);
			barrel.calculatePosAndLook();
			Vector3 v= barrel.lookdir.toSpherical();
			v.add(2, Math.PI/2);
			v.x= .6;
			v= v.toCartesian();
			eitem.motionX= v.x;	eitem.motionY= v.y; eitem.motionZ= v.z;
			holder.worldObj.spawnEntityInWorld(eitem);
		}

		loadFromMag(gstc);
	}
}
