package com.typ1a.common.utils;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IRebar {
	abstract boolean[] sides(IBlockAccess worldObj, int x, int y, int z);
	
	abstract boolean[] sides(World worldObj, int x, int y, int z);
}
