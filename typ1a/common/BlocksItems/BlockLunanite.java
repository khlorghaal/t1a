package com.typ1a.common.BlocksItems;

import java.util.Random;

import net.minecraft.block.material.Material;


public class BlockLunanite extends T1ABlock
{

	public static BlockLunanite instance;

	public BlockLunanite()
	{
		super("Lunanite", Material.rock);
		setHardness(1.0f);
		setResistance(1000.0f);
		instance=this;
	}
}