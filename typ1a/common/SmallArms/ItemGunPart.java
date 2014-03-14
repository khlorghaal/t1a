package com.typ1a.common.SmallArms;

import java.util.List;

import javax.swing.Icon;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.typ1a.common.BlocksItems.T1AItem;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGunPart extends T1AItem{
	//NBT layout
	//short "dura" uses left before breaking
	//boolean "m" mastercrafted

	public static ItemGunPart instance;

	public static final String[] subnames= {
		"Small Barrel", "Long Barrel", "Semi Automatic Action", "Full Automatic Action",
		"Small Bolt", "Large Bolt","Stock","Bullpup Stock",

		"Cannon Barrel","Cannon Action","Cannon Bolt",
		"Lasing Medium","Missile Tube","Rail Accelerator"};
	public static final int
	BARRELSMALL=0, BARRELLONG=1, ACTIONSEMI=2, ACTIONFULL=3,
	BOLTSMALL=4, BOLTLARGE=5,STOCK=6,STOCKBULLPUP=7,

	BARRELCANNON=8, ACTIONCANNON=9, BOLTCANNON=10,
	LASINGMEDIUM=11, MISSILETUBE=12, RAIL=13;
	
	public static final int maxDmg = 1000;

	public static final IIcon[] icons= new TextureAtlasSprite[subnames.length+1];
	
	public ItemGunPart() {
		super("");
		setHasSubtypes(true);
		instance= this;
	}


	@SideOnly(Side.CLIENT) @Override
	public boolean hasEffect(ItemStack stack, int pass){
		return (isMaster(stack));
	}

	public boolean isMaster(ItemStack stack){
		return getSTC(stack).hasKey("m");
	}
	public static boolean isMasterwork(NBTTagCompound itc){
		return itc.getCompoundTag("tag").hasKey("m");
	}
	@SideOnly(Side.CLIENT)	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list){
		for (int j = 8; j!=subnames.length; ++j)
			list.add(new ItemStack(item, 1, j));
		for (int j = 0; j!=8; ++j){
			final ItemStack s1= new ItemStack(item, 1, j);
			final ItemStack s2= new ItemStack(item, 1, j);
			initTC(s1);
			initTC(s2);
			getSTC(s2).setTag("m", new NBTTagInt(0));
			list.add(s1);
			list.add(s2);
		}
	}


	@SideOnly(Side.CLIENT)	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack){
		return subnames[par1ItemStack.getItemDamage()];
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		addInformationS(par1ItemStack.writeToNBT(new NBTTagCompound()), par2EntityPlayer, par3List, par4);
	}
	public static void addInformationS(NBTTagCompound itc, EntityPlayer par2EntityPlayer, List lines, boolean par4){
		final int dmg= itc.getShort("Damage");

		String line="";

		final float durap= getDurabilityPercentage(itc);

		if(durap<.26f)//change color based on durability
			line+="\u0021c";
		else if(durap<.51f)
			line+="\u0021e";
		line+=" %"+(durap*100f);
		lines.add(line);


		if(isMasterwork(itc)){
			lines.add(" \u00215Masterwork\u00217");
			if(dmg==BARRELLONG || dmg==BARRELSMALL){
				lines.add(" +"+(100-ItemSmallArm.MBARRELDEV*100)+"% accuracy");
				lines.add(" +"+((int)(ItemSmallArm.MBARRELSPEED*100))+"% velocity");
			}
			if(dmg==BOLTSMALL || dmg==BOLTLARGE){
				lines.add(" -"+((int)(100f-ItemSmallArm.MBOLTJAM*100f))+"% jam chance");
				lines.add(" +"+-(100-ItemSmallArm.MBOLTSPEED*100)+"% accuracy");
			}
			if(dmg==ACTIONSEMI || dmg==ACTIONFULL)
				lines.add(" ASDF");
			if(dmg==STOCK || dmg==STOCKBULLPUP)
				lines.add(" MOO");
		}
	}

	public static NBTTagCompound getSTC(ItemStack part){
		if(part.stackTagCompound==null){
			initTC(part);
		}
		return part.stackTagCompound;
	}
	public static void initTC(ItemStack part){
		part.stackTagCompound= new NBTTagCompound();
		part.stackTagCompound.setInteger("dura", getMaxDurability(part));
	}

	public static int getDurability(ItemStack part){
		return getSTC(part).getInteger("dura");
	}
	public static int getDurability(NBTTagCompound itc){
		return itc.getCompoundTag("tag").getInteger("dura");
	}

	public static int getMaxDurability(ItemStack part){
		return getMaxDurability( part.getItemDamage() );
	}
	public static int getMaxDurability(NBTTagCompound itc){
		return getMaxDurability( itc.getShort("Damage") );
	}
	public static int getMaxDurability(int dmg){
		switch(dmg){
		case BARRELSMALL: return 100;
		case BARRELLONG: return 100;
		case ACTIONSEMI: return 100;
		case ACTIONFULL: return 100;
		case BOLTSMALL: return 100;
		case BOLTLARGE: return 100;
		case STOCK: return 100;
		case STOCKBULLPUP: return 100;
		default: return 1;
		}
	}
	public static float getDurabilityPercentage(ItemStack part){
		return ((float)getDurability(part))/((float)getMaxDurability(part));
	}
	public static float getDurabilityPercentage(NBTTagCompound itc){
		return ( (float)(getDurability(itc)) )/( (float)(getMaxDurability(itc)) );
	}

	public static void damage(ItemStack part){
		final NBTTagCompound stc= getSTC(part);
		stc.setInteger("dura", stc.getInteger("dura")-1 );

	}

	public String getStatName(){return StatCollector.translateToLocal(this.getUnlocalizedName());}

	public String getItemStackDisplayName(ItemStack stack){return getItemDisplayName(stack);}
	public String getItemDisplayName(ItemStack stack){return getUnlocalizedName(stack);
	//("" + StatCollector.translateToLocal(this.getLocalizedName(par1ItemStack) + ".name")).trim();
	}

	@SideOnly(Side.CLIENT)	@Override
	public IIcon getIconFromDamage(int par1){return icons[(par1)];}

	@SideOnly(Side.CLIENT)	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		int i=0;
		final String pre= "t1a:gunparts/";
		icons[BARRELSMALL]= par1IconRegister.registerIcon(pre+"barrelsmall");
		icons[BARRELLONG]= par1IconRegister.registerIcon(pre+"barrellong");
		icons[ACTIONSEMI]= par1IconRegister.registerIcon(pre+"actionsemi");
		icons[ACTIONFULL]= par1IconRegister.registerIcon(pre+"actionfull");
		icons[BOLTSMALL]= par1IconRegister.registerIcon(pre+"boltsmall");
		icons[BOLTLARGE]= par1IconRegister.registerIcon(pre+"boltlarge");
		icons[STOCK]= par1IconRegister.registerIcon(pre+"stock");
		icons[STOCKBULLPUP]= par1IconRegister.registerIcon(pre+"stockbullpup");

		icons[BARRELCANNON]= par1IconRegister.registerIcon(pre+"barrelcannon");
		icons[ACTIONCANNON]= par1IconRegister.registerIcon(pre+"actioncannon");
		icons[BOLTCANNON]= par1IconRegister.registerIcon(pre+"boltcannon");
		icons[MISSILETUBE]= par1IconRegister.registerIcon(pre+"missiletube");
		icons[RAIL]= par1IconRegister.registerIcon(pre+"rail");
		//		icons[13] = par1IconRegister.registerIcon(pre+"");
	}

	public static void registerLanguages(){
		//		LanguageRegistry.

		//		for(String str:subnames)
		//			LanguageRegistry.instance().addStringLocalization(str+".name", "en_US", str);
	}


	public static void registerCrafting(){
		//GUN ASSEMBLY
		GameRegistry.addRecipe(new IRecipe(){
			@Override
			public boolean matches(InventoryCrafting inventorycrafting, World world) {
				final ItemStack 
				barrel= inventorycrafting.getStackInRowAndColumn(0, 1),
				bolt= inventorycrafting.getStackInRowAndColumn(1, 1),
				stock= inventorycrafting.getStackInRowAndColumn(2, 1),
				action= inventorycrafting.getStackInRowAndColumn(1, 2)
				;

				if(bolt==null || barrel==null || stock==null || action==null
						|| barrel.getItem() != instance
						|| bolt.getItem() != instance
						|| stock.getItem() != instance
						|| action.getItem() != instance
						)
					return false;

				return true;
			}
			@Override
			public int getRecipeSize(){return 9;}
			@Override
			public ItemStack getRecipeOutput(){return null;}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inventorycrafting){
				final ItemStack
				barrel= inventorycrafting.getStackInRowAndColumn(0, 1),
				bolt= inventorycrafting.getStackInRowAndColumn(1, 1),
				stock= inventorycrafting.getStackInRowAndColumn(2, 1),
				action= inventorycrafting.getStackInRowAndColumn(1, 2);

				final int
				barreldmg= barrel.getItemDamage(),
				boltdmg= bolt.getItemDamage(),
				stockdmg= stock.getItemDamage(),
				actiondmg= action.getItemDamage();

				ItemStack ret=null;
				if(barreldmg==BARRELSMALL && boltdmg==BOLTSMALL && stockdmg==STOCKBULLPUP && actiondmg==ACTIONFULL)
					ret= new ItemStack(ItemSMG.instance);
				if(barreldmg==BARRELLONG && boltdmg==BOLTLARGE && stockdmg==STOCK && actiondmg==ACTIONFULL)
					ret= new ItemStack(ItemShotgun.instance);
				if(barreldmg==BARRELLONG && boltdmg==BOLTSMALL && stockdmg==STOCKBULLPUP && actiondmg==ACTIONSEMI)
					ret= new ItemStack(ItemAR.instance);
				if(barreldmg==BARRELLONG && boltdmg==BOLTLARGE && stockdmg==STOCK && actiondmg==ACTIONSEMI)
					ret= new ItemStack(ItemRifle.instance);

				if(ret==null)
					return null;

				if(ret!=null){
					ItemSmallArm.initTC(ret, barrel, bolt, action, stock);
				}

				return ret;
			}
		});
		
		
		GameRegistry.addRecipe(new ItemStack(instance, 1, BARRELSMALL),
				"xxx",
				"   ",
				"xxx",
				'x', Items.iron_ingot);
		GameRegistry.addRecipe(new ItemStack(instance, 1, BARRELLONG),
				"xx",
				'x', new ItemStack(instance, 1, BARRELSMALL));
		
		GameRegistry.addRecipe(new ItemStack(instance, 1, BOLTSMALL),
				"xxx",
				" xp",
				"xxx",
				'x', Items.iron_ingot, 'p', Blocks.piston);
		GameRegistry.addRecipe(new ItemStack(instance, 1, BOLTLARGE),
				"xp",
				'x', Blocks.iron_block, 'p', Blocks.piston);
		
		GameRegistry.addRecipe(new ItemStack(instance, 1, ACTIONSEMI),
				"x  ",
				"xxx",
				" l ",
				'x', Items.iron_ingot, 'l', Blocks.lever);
		GameRegistry.addRecipe(new ItemStack(instance, 1, ACTIONFULL),
				"xr ",
				"xxx",
				" l ",
				'x', Items.iron_ingot, 'l', Blocks.lever, 'r', Items.repeater);
		
		GameRegistry.addRecipe(new ItemStack(instance, 1, STOCK),
				"xxx",
				"xxo",
				" x ",
				'x', Items.iron_ingot, 'o', Blocks.wool);
		GameRegistry.addRecipe(new ItemStack(instance, 1, STOCKBULLPUP),
				"xxx",
				"xxo",
				"x  ",
				'x', Items.iron_ingot, 'o', Blocks.wool);
		
	}
}
