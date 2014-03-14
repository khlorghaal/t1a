package com.typ1a.common.AmmoStuff;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.typ1a.common.T1A;
import com.typ1a.common.T1AGUIHandler;
import com.typ1a.common.BlocksItems.T1AItem;
import com.typ1a.common.Projectiles.EntityBullet;
import com.typ1a.common.SmallArms.Cluster;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemMagazine extends T1AItem {

	public ItemMagazine(String name, int maxDmg) {
		super(name, "mag5mm", 1, maxDmg);
	}

	public abstract Item getAmmoItem();

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		addInformationS(getTC(par1ItemStack), par2EntityPlayer, par3List, par4);
	}
	public static void addInformationS(NBTTagCompound mstc, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		final Cluster[] cls= getClusters(mstc);
		if(cls.length==0)
			par3List.add("Empty");
		for(int i=cls.length-1; i!=-1; i--){
			final char col= ItemAmmo.getColor(cls[i].typ);

			if(cls[i].typ >= EntityBullet.TYPENAMES.length){
				par3List.add("ERROR: INVALID STC");
				continue;
			}
			par3List.add("\u0021"+col+cls[i].n+" "+EntityBullet.TYPENAMES[cls[i].typ]);
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		player.openGui(T1A.instance, T1AGUIHandler.MAG, world, 0, 0, 0);
		return stack;
	}
	//NBT layout
	//short "cap" max capacity of the magazine, == max dmg
	//byte[] "cls"  size= number of times two non type similar bullets touch || 0 if empty 

	//no need to store itemid because it is checked when pushing

	static void initTC(ItemStack mag, int cap){
		mag.stackTagCompound= new NBTTagCompound();
		final NBTTagCompound stc= mag.stackTagCompound;
		stc.setShort("cap", (short)mag.getItem().getMaxDamage());
		stc.setByteArray("cls", new byte[0]);

	}
	public static NBTTagCompound getTC(ItemStack mag){
		if(mag.stackTagCompound==null)
			initTC(mag, 32);//TODO abstr cap getter
		return mag.stackTagCompound;
	}

	public static Cluster[] getClusters(ItemStack mag){
		return getClusters(getTC(mag));
	}
	public static Cluster[] getClusters(NBTTagCompound magtc){
		return Cluster.fromByteArr( magtc.getByteArray("cls") );
	}
	private static void setClusters(ItemStack mag, Cluster[] cls){
		setClusters(getTC(mag), cls);
	}
	private static void setClusters(NBTTagCompound magtc, Cluster[] cls){
		magtc.setByteArray("cls", Cluster.toByteArr(cls));
	}


	/**@return !full*/
	public static boolean pushRound(NBTTagCompound magtc, int btype){
		//		System.out.println(mag);
		//		System.out.println(btype);
		Cluster[] cls= getClusters(magtc);
		final int end= cls.length-1;

		if(Cluster.getCount(cls) == magtc.getShort("cap") )//is full
			return false;

		if(end==-1){//empty mag
			setClusters(magtc, new Cluster[]{new Cluster((byte) 1, (byte)btype)} );
			return true;
		}

		if(cls[end].typ!=btype ){//prev round is of different type

			//begin new btype cluster
			final Cluster append= new Cluster((byte)1, (byte)btype);
			final Cluster[] newcls= new Cluster[cls.length+1];
			System.arraycopy(cls, 0, newcls, 0, cls.length);
			newcls[end+1]= append;
			setClusters(magtc, newcls);
			return true;
		}

		//is same type
		cls[end].n++;
		setClusters(magtc, cls);
		return true;
	}
	public static boolean pushRound(ItemStack mag, int btype){
		return pushRound(getTC(mag), btype);
	}
	/**@return type of bullet used</br> -1 if empty*/
	public static int popRound(NBTTagCompound magtc){
		final Cluster[] cls= getClusters(magtc);

		if(cls.length==0)//empty
			return -1;

		final Cluster endcl= cls[cls.length-1];
		final int ret= endcl.typ;

		if(endcl.n>1){
			endcl.n--;
			setClusters(magtc, cls);
		}
		else{//delete end cluster
			final Cluster[] newcls= new Cluster[cls.length-1];
			System.arraycopy(cls, 0, newcls, 0, cls.length-1);
			setClusters(magtc, newcls);
		}
		return ret;
	}
	public static int popRound(ItemStack stack){
		return popRound(getTC(stack));
	}

	public static void updateDamage(ItemStack mag) {
		final int rounds= Cluster.getCount(ItemMagazine.getClusters(mag));
		final int cap= mag.getMaxDamage();
		mag.setItemDamage(cap-rounds);		
	}

	public static void registerCrafting(){
		GameRegistry.addRecipe(new ItemStack(ItemMagazine5mm.instance, 1, 1),
				"x x",
				"x x",
				"xpx",
				'x', Items.iron_ingot, 'p', Blocks.piston);
	}
}
