package com.typ1a.common.BlocksItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.typ1a.common.Vehicles.EntityDropPod;

public class ItemDropPod extends T1AItem {

	public ItemDropPod() {
		super("DropPod");
		
	}
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x,int y,int z,int s, float f0,float f1,float f2){
		if(world.isRemote)
			return false;//FFS DO NOT FORGET THIS D:
		
		switch(s){
		case 0:
			y--;
			break;
		case 1:
			y++;
			break;
		case 2:
			z--;
			break;
		case 3:
			z++;
			break;
		case 4:
			x--;
			break;
		case 5:
			x++;
			break;
		}
		if(world.getBlock(x, y, z)!=Blocks.air)
			return false;
		
		world.spawnEntityInWorld(new EntityDropPod(world, x+.5,y+1,z+.5));
		return true;
	}
}
