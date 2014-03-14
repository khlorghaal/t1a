package com.typ1a.common;

import net.minecraft.entity.player.EntityPlayer;

/**for easy synching objects with the ProperGui class.*/
public interface IGuiable extends IUpdateable{
	public boolean canPlayerAccess(EntityPlayer player);
}
