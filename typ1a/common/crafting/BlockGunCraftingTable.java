package com.typ1a.common.crafting;

import net.minecraft.block.material.Material;

import com.typ1a.common.BlocksItems.T1ABlock;

public class BlockGunCraftingTable extends T1ABlock {

	public static BlockGunCraftingTable instance;
	
	public BlockGunCraftingTable() {
		super("", Material.iron);
		instance= this;
	}

}
