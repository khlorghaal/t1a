package com.typ1a.client.gui;

import net.minecraft.entity.player.InventoryPlayer;

import com.typ1a.common.Containers.ContainerAssembler;
import com.typ1a.common.crafting.BlockAssemblyRig.TileEntityAssemblyRig;

public class GUIAssembler extends ProperGUI {

	public GUIAssembler(InventoryPlayer inventoryPlayer, TileEntityAssemblyRig rig) {
		super(new ContainerAssembler(inventoryPlayer, rig),  "assembler");
	}

}
