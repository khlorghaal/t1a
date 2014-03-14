package com.typ1a.common.Vehicles;

import java.util.List;

import javax.swing.Icon;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.typ1a.common.BlocksItems.T1AItem;
import com.typ1a.common.Vehicles.Tech.EntityB;
import com.typ1a.common.Vehicles.Tech.EntityMjolnir;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemUnassembledVehicle extends T1AItem {
	public static final String[] subnames= {
		"A","Ravener","Brutalizer","Halcyon","Corax","Mjolnir","b"
	};
	public static final IIcon[] icons= new IIcon[subnames.length];
	public static final int
	A=0, RAVENER=1, BRUTALIZER=2, HALCYON=3, CORAX=4, MJOLNIR=5, B=6;
	
	public static ItemUnassembledVehicle instance;
	public ItemUnassembledVehicle()  {
		super("Unassembled Vehicle", "partsravener",1, -1);
		setHasSubtypes(true);
		instance=this;
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, int x, int y, int z, int s, float par8, float par9, float par10){
		if(world.isRemote
				|| !player.capabilities.isCreativeMode)//survival has to use assy rig
			return false;
		
		switch(s){
		case 0:
			y--;
			break;
		case 1:
			y++;
			break;
		case 2:
			z--;
			break;
		case 3:
			z++;
			break;
		case 4:
			x--;
			break;
		case 5:
			x++;
			break;
		}

		if (world.getBlock(x,y,z) == Blocks.air){
			spawnEntity(par1ItemStack, player, x+.5, y, z+.5);
			par1ItemStack.splitStack(1);
			return true;	
		}

		return false;
	}
	public void spawnEntity(ItemStack stack, EntityPlayer owner, double x, double y, double z){
		int dmg= stack.getItemDamage();
		switch(dmg){
		case A:
			break;
		case BRUTALIZER:
			break;
		case CORAX:
			break;
		case HALCYON:
			break;
		case MJOLNIR:
			new EntityMjolnir(owner, x, y, z);
			break;
		case RAVENER:
			break;
		case B:
			new EntityB(owner, x,y,z);
			System.out.println("wowo");
			break;
		}
	}
	
	
	
	@SideOnly(Side.CLIENT)	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list){
		for (int j = 0; j != subnames.length; ++j)
			list.add(new ItemStack(par1, 1, j));
	}
	@SideOnly(Side.CLIENT)	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack){
		return "Unassembled "+subnames[par1ItemStack.getItemDamage()];
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		//
	}
	
	public String getStatName(){return StatCollector.translateToLocal(this.getUnlocalizedName());}

	public String getItemStackDisplayName(ItemStack stack){return getItemDisplayName(stack);}
	public String getItemDisplayName(ItemStack stack){return getUnlocalizedName(stack);}

	@SideOnly(Side.CLIENT)	@Override
	public IIcon getIconFromDamage(int par1){return icons[par1];}
	@SideOnly(Side.CLIENT)	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		int i=0;
		final String pre= "t1a:";
		icons[A]= par1IconRegister.registerIcon(pre+"a");
		icons[BRUTALIZER]= par1IconRegister.registerIcon(pre+"brutalizer");
		icons[CORAX]= par1IconRegister.registerIcon(pre+"corax");
		icons[HALCYON]= par1IconRegister.registerIcon(pre+"halcyon");
		icons[MJOLNIR]= par1IconRegister.registerIcon(pre+"mjolnir");
		icons[RAVENER]= par1IconRegister.registerIcon(pre+"ravener");
	}


	public static void registerCrafting(){
		
	}


}