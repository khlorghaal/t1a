package com.typ1a.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import com.typ1a.common.T1A;
import com.typ1a.common.T1AGUIHandler;

public class ButtonOpenEquipment extends Clickable {

	private int refHash;
	public ButtonOpenEquipment(int xpos, int ypos, int refHash){
		super(xpos, ypos, 15, 15, 241, 0);
		this.refHash= refHash;
	}

	@Override
	public void clickRelease(){
		final EntityPlayer player= Minecraft.getMinecraft().thePlayer;
		player.openGui(T1A.instance, T1AGUIHandler.EQUIPMENT, player.worldObj, refHash, -1, -1);
	}
}
