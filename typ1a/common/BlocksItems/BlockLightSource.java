package com.typ1a.common.BlocksItems;

import net.minecraft.block.material.Material;

public class BlockLightSource extends T1ABlock {
	public static BlockLightSource instance;
	public BlockLightSource() {
		super(Material.air);
		setLightLevel(1);
		instance=this;
		setCreativeTab(null);
	}

	@Override
	public boolean isOpaqueCube(){
		return false;

	}
}
