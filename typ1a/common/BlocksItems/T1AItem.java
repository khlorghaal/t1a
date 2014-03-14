package com.typ1a.common.BlocksItems;

import java.util.ArrayList;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.typ1a.common.T1A;
import com.typ1a.common.SmallArms.ItemSmallArm;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class T1AItem extends Item {

	public String texName;
	public T1AItem(String name, String texfilename, int stackSize, int maxDmg){
		super();

		this.texName=texfilename;
		this.setUnlocalizedName(name);
		maxStackSize = stackSize;
		setCreativeTab(T1A.ctab);
		if(maxDmg != -1)
			this.setMaxDamage(maxDmg);

		T1A.instance.items.add(this);
	}
	/**@param name also dictates texture file - ignores case and spaces*/
	public T1AItem(String name, int stackSize, int maxDmg){
		this(name,name.toLowerCase().replace(" ", ""),stackSize,maxDmg);
	}
	public T1AItem(String name){
		this(name, 1, -1);
	}
	public T1AItem(String name, String tex){
		this(name, tex, 1, -1);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)	    {
		this.itemIcon= iconRegister.registerIcon("t1a:"+texName);
	}

	/**Gets the least damaged or last stack from an inventory.
	 * Doesn't necessarily belong here it just seemed sensible to put it with item stuff
	 * */
	public static int getBestStack(ItemStack[] stacks, Item item){
		ArrayList<Integer> candidateIndxs= new ArrayList<Integer>();
		for(int i=0; i!= stacks.length; i++)
			if(stacks[i]!=null && stacks[i].getItem()==item)
				candidateIndxs.add(i);
		if(candidateIndxs.size()==0)
			return -1;
		if(item.getHasSubtypes()){//return last
			return candidateIndxs.get(candidateIndxs.size()-1);
		}
		
		int ret=-1;
		int leastdmg= 0x7fffffff;
		for(int cand : candidateIndxs){
			final ItemStack candstack= stacks[cand];
			if(stacks[cand].getItemDamage()<leastdmg){
				leastdmg= candstack.getItemDamage();
				ret= cand;
			}
		}

		return ret;
	}


	public static ItemStack getGun(InventoryCrafting inv){
		for(int i=0; i!=inv.getSizeInventory(); i++){
			final ItemStack stack= inv.getStackInSlot(i);
			if(stack!=null && (stack.getItem() instanceof ItemSmallArm))
				return stack;
		}
		return null;
	}
	public static ItemStack getItemFromInv(InventoryCrafting inv, Item item){
		for(int i=0; i!=inv.getSizeInventory(); i++){
			final ItemStack stack= inv.getStackInSlot(i);
			if(stack!=null && stack.getItem() == item)
				return stack;
		}
		return null;
	}
	
	public static void initTC(ItemStack stack){
		stack.stackTagCompound= new NBTTagCompound();
	}

}
