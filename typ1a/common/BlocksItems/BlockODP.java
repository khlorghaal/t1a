package com.typ1a.common.BlocksItems;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import com.typ1a.common.spacey.EntityODP;



public class BlockODP extends T1ABlock
{

	public BlockODP()
	{
		super("ODP",Material.tnt);
		this.setHardness(3.0f);
		this.setResistance(10.0f);
	}
	
	@Override
	public int quantityDropped(Random random){return 1;}

	
	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		 super.onBlockAdded(par1World, par2, par3, par4);
		 if (par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
	        {
//			 this.powered(par1World, par2, par3, par4);
	        }
	}	
//	@Override
//    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
//    {if (par5 > 0 && Block.blocksList[par5].canProvidePower() && par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
//        {
//            this.powered(par1World, par2, par3, par4);
//        }
//    }
//	@Override
//    public void powered(World par1World, int par2, int par3, Block par4)
//    {
//    	par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this);
//        EntityODP var6 = new EntityODP(par1World, (double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F));
//       if(!par1World.isRemote)par1World.spawnEntityInWorld(var6);
//        par1World.playSoundAtEntity(var6, "random.fuse", 1.0F, 1.0F);
//    }
	
}
