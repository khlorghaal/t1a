package com.typ1a.common.crafting;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.typ1a.common.BlocksItems.T1ABlock;

public class BlockAssemblyRigging extends T1ABlock {
	public static Block instance;
	public BlockAssemblyRigging() {
		super("Assembly Rigging", Material.iron);
		instance= this;
	}

}
