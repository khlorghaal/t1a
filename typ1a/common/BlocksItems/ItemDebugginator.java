package com.typ1a.common.BlocksItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDebugginator extends T1AItem {
	private ItemStack inv[];

	public ItemDebugginator()
	{
		super("Debagher", 1, -1);
	}


	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player)
	{
		for(int x= (int) player.posX+1; x< (int)player.posX+20; x++)
			for(int y= (int) player.posY; y< (int)player.posY+20; y++)
				for(int z= (int) player.posZ+1; z< (int)player.posZ+20; z++)
					world.setBlock(x, y, z, Blocks.glass);
		
		System.out.println("wawk");
		return par1ItemStack;
	}

}