package com.typ1a.common.Missiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.typ1a.common.BlocksItems.T1AItem;


public class ItemSilo extends T1AItem{

	public ItemSilo()
	{
		super("Missile Silo");
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int n0,int n1,int n2,int n3,float f0, float f1, float f2){
		if(!world.isRemote){
			world.spawnEntityInWorld(new EntitySilo(world, n0,n1,n2));
			//if(!player.capabilities.isCreativeMode)
				stack.stackSize=0;
		}
		return false;
	}
	
}

