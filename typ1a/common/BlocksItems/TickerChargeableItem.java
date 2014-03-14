package com.typ1a.common.BlocksItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.typ1a.common.SmallArms.TickerSmallArm;

public abstract class TickerChargeableItem extends TickerSmallArm {

	protected int maxUseDuration;

	public TickerChargeableItem(EntityPlayer player) {
		super(player, new NBTTagCompound());
		maxUseDuration= 40;//TODO stack.getItem().getMaxDamage()-2;
	}
	@Override
	public void onUpdate() {
		checkHeld();

		if(this.ticksDone>maxUseDuration)
			ticksDone=maxUseDuration;
		stack.setItemDamage(maxUseDuration-this.ticksDone+1);
	}
	public boolean checkHeld(){
		if(player==null || stack==null || player.getHeldItem()==null 
				|| (player.getHeldItem().getItem()!=item)){
			if(this.ticksDone>0 
					&& player!=null && stack!=null){
				stack.setItemDamage(0);
			}
			return false;
		}

		return true;
	}
	@Override
	protected void tryShoot(){
		if((player.getHeldItem().getItem()==stack.getItem())){
			doAction();
			playSound();
		}
	}

	/**If a single use item then do this before breaking the stack*/
	@Override
	public void kill(){
		this.tryShoot();
		super.kill();
		stack.setItemDamage(0);//incase in creative
		stack.damageItem(0xfffffff, player);
	}
}
