package com.typ1a.common.SmallArms;

import java.util.List;

import javax.swing.Icon;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.typ1a.common.BlocksItems.T1AItem;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAttachment extends T1AItem{
	//NBT layout
	//short "dura" uses left before breaking
	//boolean "m" mastercrafted

	public static ItemAttachment instance;
	public static final IIcon[] icons= new IIcon[32];

	public static final String[] subnames= {
		"Suppressor", "Scope", "Laser Sight", "Muzzle Brake"};
	public static final int SUPPRESSOR=0, SCOPE=1, LASSIGHT=2, BRAKE=3;
	
	public ItemAttachment() {
		super("");
		setHasSubtypes(true);
		instance= this;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List lines, boolean par4){
		final int dmg= stack.getItemDamage();
		if(dmg==SUPPRESSOR){
			lines.add("Eliminates muzzle flash");
			lines.add("Makes much quieter");
			lines.add((100-ItemSmallArm.SUPPRDEV*100)+"% accuracy");
			return;
		}
		if(dmg==BRAKE){
			lines.add("-"+(100f-((int)(ItemSmallArm.BRAKERECOIL*100f)))+"% recoil");
			return;
		}
	}

	@SideOnly(Side.CLIENT)	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list){
		for (int j = 0; j!=subnames.length; ++j)
			list.add(new ItemStack(par1, 1, j));
	}


	@SideOnly(Side.CLIENT)	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack){
		return subnames[par1ItemStack.getItemDamage()];
	}

	public String getStatName(){return StatCollector.translateToLocal(this.getUnlocalizedName());}

	public String getItemStackDisplayName(ItemStack stack){return getItemDisplayName(stack);}
	public String getItemDisplayName(ItemStack stack){return getUnlocalizedName(stack);}

	@SideOnly(Side.CLIENT)	@Override
	public IIcon getIconFromDamage(int par1){return icons[par1];}

	@SideOnly(Side.CLIENT)	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		int i=0;
		final String pre= "t1a:gunparts/";
		icons[SUPPRESSOR]= par1IconRegister.registerIcon(pre+"suppressor");
		icons[SCOPE]= par1IconRegister.registerIcon(pre+"scope");
		icons[LASSIGHT]= par1IconRegister.registerIcon(pre+"lassight");
		icons[BRAKE]= par1IconRegister.registerIcon(pre+"brake");
	}

	public static void registerLanguages(){
		//		LanguageRegistry.

		//		for(String str:subnames)
		//			LanguageRegistry.instance().addStringLocalization(str+".name", "en_US", str);
	}


	public static void registerCrafting(){
		//ATTACHMENT ADDING
		GameRegistry.addRecipe(new IRecipe() {
			@Override
			public boolean matches(InventoryCrafting inv, World world) {
				int count=0;
				for(int i=0; i!=inv.getSizeInventory(); i++){
					if(inv.getStackInSlot(i)!=null)
						count++;
				}
				if(count!=2)
					return false;

				final ItemStack attach= T1AItem.getItemFromInv(inv, instance);
				if(attach==null)
					return false;
				final ItemStack gun= T1AItem.getGun(inv);
				if(gun==null)
					return false;
				
				//dont allow crafting over existing attachment occupying same 'slot'
				final String aname= subnames[attach.getItemDamage()];
				if(aname=="Suppressor" && gun.stackTagCompound.hasKey("Muzzle Brake")
					|| aname=="Muzzle Brake" && gun.stackTagCompound.hasKey("Suppressor"))
					return false;

				return true;
			}

			@Override
			public int getRecipeSize() {return 9;}

			@Override
			public ItemStack getRecipeOutput() {return null;}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv) {
				final ItemStack ret= T1AItem.getGun(inv).copy();
				final ItemStack atch= T1AItem.getItemFromInv(inv, instance);
				final NBTTagCompound gstc= ItemSmallArm.getSTC(ret);
				final String aname= subnames[atch.getItemDamage()];
				gstc.setTag(aname, atch.writeToNBT(new NBTTagCompound()));
				return ret;
			}
		});
		
		
		GameRegistry.addRecipe(new ItemStack(instance, 1, BRAKE),
				"xxx",
				"xxx",
				" x ",
				'x', Items.iron_ingot);
		
		GameRegistry.addRecipe(new ItemStack(instance, 1, SUPPRESSOR),
				"bbx",
				'x', Items.iron_ingot, 'b', Items.bucket);//bucket

		GameRegistry.addRecipe(new ItemStack(instance, 1, LASSIGHT),
				"xx",
				"tr",
				"xx",
				'x', Items.iron_ingot, 't', Blocks.redstone_torch, 'r', Blocks.redstone_block);
	}

	public static String getScopeInfo(NBTTagCompound stc) {
		return "\u0021aScope";
	}
}
