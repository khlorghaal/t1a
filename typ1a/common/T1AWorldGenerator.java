package com.typ1a.common;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.typ1a.common.BlocksItems.BlockLunanite;

import cpw.mods.fml.common.IWorldGenerator;

public class T1AWorldGenerator implements IWorldGenerator
{

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
//		if(world.provider.isSurfaceWorld()){
//			if(random.nextInt(10)==1){
//			int x = chunkX*16 + random.nextInt(16);
//			int y = chunkZ*16 + random.nextInt(16);
//			int z = 85 + random.nextInt(10);
//			//TODO MYAH 
//			  (new WorldGenMinable(BlockLunanite.instance, 20)).generate(world, random, x, z, y);
//			}
//		}
		

	}
}