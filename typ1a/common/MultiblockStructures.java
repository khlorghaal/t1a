package com.typ1a.common;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class MultiblockStructures {
	final int r= 0;//rocket
	final int g= 0;//guidance
	final int h= 0;//hull
	
	public enum Direction {XPLUS,XMINUS,YPLUS,YMINUS,ZPLUS,ZMINUS}
	
	public static int getRigSize(World world, int x, int y, int z){
		return -1;//TODO
	}
	/**@return errors*/
	public static int matches(Structure s, Direction d, World world, int x, int y, int z){
		int errs=0;
		int index=0;

		for(int i=x; i<x+s.w; i++){
			for(int j=y; j<y+s.h; j++){
				for(int k=z; k<z+s.l; k++){
					index++;
					if(Block.getBlockById(s.composition[index])!= world.getBlock(i, j, k) )
						errs++;
				}
			}
		}
		return errs;
	}
	
	public static class Structure{
		public int[] composition;
		int w,h,l;
		public Structure(int[] comp, int w, int l, int h){
			this.composition= comp;
			this.w= w;
			this.h= h;
			this.l= l;
		}
	}
	
	public static final Structure IRBM= new Structure(new int[]{
			0,0,0,
			0,0,0,
			0,0,0,
			0,0,0,
			0,0,0,
			0,0,0,
			0,0,0,

			0,0,0,
			0,0,0,
			0,0,0,
			0,0,0,
			0,0,0,
			0,0,0,
			0,0,0,

			0,0,0,
			0,0,0,
			0,0,0,
			0,0,0,
			0,0,0,
			0,0,0,
			0,0,0,
	},
	3,3,7);
	
	public static final Structure ICBM= new Structure(new int[]{
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
	},
	4,4,12);
}
