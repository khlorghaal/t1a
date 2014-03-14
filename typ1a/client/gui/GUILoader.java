package com.typ1a.client.gui;

import net.minecraft.entity.player.InventoryPlayer;

import com.typ1a.common.Containers.ContainerLoader;

public class GUILoader extends ProperGUI {
	//owned IGuiable handles buttonpresses
	
	Clickable load= new Clickable(161, 27, 11, 31, 193, 0);
	
	public GUILoader(InventoryPlayer inv){
		super(new ContainerLoader(inv), "Loader");
		this.ySize=165;
		addClickable(load);
	}
}
