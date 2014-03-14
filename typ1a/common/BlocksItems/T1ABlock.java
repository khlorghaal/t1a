package com.typ1a.common.BlocksItems;

import javax.swing.Icon;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;

import com.typ1a.common.T1A;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class T1ABlock extends Block {

	public String texName;
	public T1ABlock(Material par2Material) {
		super(par2Material);
	}

	public T1ABlock(String name, String texfilename, Material mat){
		super(mat);

		System.out.println(texfilename);
		this.texName=texfilename;
		this.setBlockName(name);
		setCreativeTab(T1A.ctab);

		T1A.blocks.add(this);
	}
	/**@param name also dictates texture file - ignores case and spaces*/
	public T1ABlock(String name, Material mat){
		this(name, name.replace(" ", "").toLowerCase(), mat);
	}

//	@Override
//	@SideOnly(Side.CLIENT)
//	/**
//	 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
//	 */
//	public IIcon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5){
//		return this.blockIcon;
//	}
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void registerIcons(IIconRegister iconRegister){
//		this.blockIcon= iconRegister.registerIcon("t1a:"+texName);
//	}
}
