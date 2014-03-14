package com.typ1a.common.SmallArms;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TickerStrip extends TickerReload {

	public TickerStrip(EntityPlayer player) {
		super(player);
		rls= 10;
		final NBTTagCompound gstc= ItemSmallArm.getSTC(held);
		boolean stripping= true;
		for(String atchtag : ItemAttachment.subnames)
			if(gstc.hasKey(atchtag)){
				stripping= false;
				break;
			}
		if(stripping){
			rls+=20;
			player.worldObj.playSoundAtEntity(player, "strip", 1, 1);
		}
		System.out.println("MMMMMOOOOOOO");
	}
	
	@Override
	public void reload(){			
			final ItemStack[] inv= player.inventory.mainInventory;
			NBTTagCompound gstc= ItemSmallArm.getSTC(held);
			
			for(String atchtag : ItemAttachment.subnames){
				if(gstc.hasKey(atchtag)){
					//remove attachment
					final ItemStack atchstack= ItemStack.loadItemStackFromNBT(gstc.getCompoundTag(atchtag));
					player.inventory.addItemStackToInventory(atchstack);
					gstc.removeTag(atchtag);
					player.worldObj.playSoundAtEntity(player, "detach", 1, 1);
					this.kill();
					return;
				}
			}
			
			//strip
			player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			player.inventory.addItemStackToInventory(ItemStack.loadItemStackFromNBT(gstc.getCompoundTag("ba")));
			player.inventory.addItemStackToInventory(ItemStack.loadItemStackFromNBT(gstc.getCompoundTag("bo")));
			player.inventory.addItemStackToInventory(ItemStack.loadItemStackFromNBT(gstc.getCompoundTag("a" )));
			player.inventory.addItemStackToInventory(ItemStack.loadItemStackFromNBT(gstc.getCompoundTag("s" )));
			this.kill();
	}

}
