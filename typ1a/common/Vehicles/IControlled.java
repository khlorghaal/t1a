package com.typ1a.common.Vehicles;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

/**IEASD is necessary for setting position.... because it is*/
public interface IControlled extends IEntityAdditionalSpawnData{
	public void handleKeys(EntityPlayer p, int data);
	/**  VERY IMPORTANT 
	 * Only necssary with Entities. 
	 * Override your setPosition() to do nothing.
	 * Use this method to super.setPos()
	 * This is to prevent whichever class from force updating 
	 * when desynched with the server. 
	 * This fixes the jittering problem.*/
	public void setPos(double x,double y,double z);
}
