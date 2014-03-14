package com.typ1a.common.AmmoStuff;

import java.util.List;
import java.util.Vector;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.typ1a.common.BlocksItems.T1AItem;
import com.typ1a.common.Projectiles.EntityBullet;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemAmmo extends T1AItem {

	public IIcon[] icons= new IIcon[8];

	public static List<Item> subclasses= new Vector<Item>();

	public ItemAmmo(String name, int stackSize){
		super(name, stackSize, -1);
		hasSubtypes= true;
		subclasses.add(this);
	}

	public static char getColor(int typ){
		switch(typ){
		case 0:
			return '6';
		case 1:
			return 'c';
		case 2:
			return 'e';
		case 3:
			return 'b';
		default: return '7';
		}
	}
	@SideOnly(Side.CLIENT)	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list){
		for(int i=0; i<EntityBullet.BTYPES.length; i++)
			list.add(new ItemStack(par1, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		final int dmg= par1ItemStack.getItemDamage();
		if(dmg==0)
			return;
		par3List.add(EntityBullet.TYPENAMES[dmg]);
	}

	@SideOnly(Side.CLIENT)	@Override
	public IIcon getIconFromDamage(int par1){return icons[par1];}	
	@SideOnly(Side.CLIENT)	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		final String pre= "t1a:ammo/";
		icons[EntityBullet.NORM]= par1IconRegister.registerIcon(pre+texName);
		icons[EntityBullet.TRACE]= par1IconRegister.registerIcon(pre+texName+"trace");
		icons[EntityBullet.HE]= par1IconRegister.registerIcon(pre+texName+"he");
	}






	//One class for each type of gun's ammo, this eliminates need for reflection

	public static class  ItemRound5mm extends ItemAmmo{
		public static ItemRound5mm instance;
		public ItemRound5mm(){
			super("5mm round", 64);
			instance= this;
		}
	}
	public static class ItemRoundBMG extends ItemAmmo {
		public static Item instance;
		public ItemRoundBMG() {
			super(".50 BMG", 48);
			this.instance=this;
		}
	}
	public static class ItemRoundShot extends ItemAmmo {
		public static Item instance;
		public ItemRoundShot() {
			super("Shotgun Shell", 8);
			instance= this;
		}
	}
	//	public static class ItemRoundHvyShot extends ItemAmmo {
	//		public static Item instance;
	//		public ItemRoundHvyShot() {
	//			super("Shotcannon Shell", 8);
	//			this.instance=this;
	//		}
	//	}
	public static class ItemRoundArty extends ItemAmmo {
		public static final int HEAT=0, HESH=1, APFSDS=2, CANISTER=3;
		public static final int[] BTYPES= new int[]{HEAT, HESH, APFSDS, CANISTER};
		public static final String[] TYPENAMES= new String[]{"HEAT","HESH","APFSDS","Canister"};

		public static Item instance;
		public ItemRoundArty() {
			super("Artillery Shell", 8);
			this.instance=this;
		}

		@SideOnly(Side.CLIENT)	@Override
		public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list){
			for(int i=0; i<this.BTYPES.length; i++)
				list.add(new ItemStack(par1, 1, i));
		}

		@SideOnly(Side.CLIENT)	@Override
		public void registerIcons(IIconRegister par1IconRegister){
			icons[HEAT]= par1IconRegister.registerIcon("t1a:ammo/heat");
			icons[HESH]= par1IconRegister.registerIcon("t1a:ammo/hesh");
			icons[APFSDS]= par1IconRegister.registerIcon("t1a:ammo/apfsds");
			icons[CANISTER]= par1IconRegister.registerIcon("t1a:ammo/canister");
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
			final int dmg= par1ItemStack.getItemDamage();
			par3List.add(this.TYPENAMES[dmg]);
		}
	}





	public static class ItemMissile extends ItemAmmo {
		public static final int HEAT=0, HESH=1, CKEM=2;
		public static final int[] MTYPES= new int[]{HEAT, HESH, CKEM};
		public static final String[] TYPENAMES= new String[]{"HEAT","HESH","CKEM"};

		public static Item instance;
		public ItemMissile() {
			super("Missile", 8);
			this.instance=this;
		}

		@SideOnly(Side.CLIENT)	@Override
		public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list){
			for(int i=0; i<MTYPES.length; i++)
				list.add(new ItemStack(par1, 1, i));
		}

		@SideOnly(Side.CLIENT)	@Override
		public void registerIcons(IIconRegister par1IconRegister){
			icons[HEAT]= par1IconRegister.registerIcon("t1a:ammo/missileheat");
			icons[HESH]= par1IconRegister.registerIcon("t1a:ammo/missilehesh");
			icons[CKEM]= par1IconRegister.registerIcon("t1a:ammo/missileke");
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
			final int dmg= par1ItemStack.getItemDamage();
			par3List.add(this.TYPENAMES[dmg]);
		}
	}





	public static void registerCrafting() {
		GameRegistry.addRecipe(new ItemStack(ItemCasing.instance, 32),
				"xx",
				"x ",
				"xx",
				'x', Items.iron_ingot);
		GameRegistry.addRecipe(new ItemStack(ItemAmmo.ItemRound5mm.instance, 16), 
				"x",
				"g",
				"c",
				'x', Items.iron_ingot, 'c', ItemCasing.instance, 'g', Items.gunpowder);
		GameRegistry.addRecipe(new ItemStack(ItemAmmo.ItemRoundBMG.instance, 20), 
				" xx",
				"ggx",
				"cc ",
				'x', Items.iron_ingot, 'c', ItemCasing.instance, 'g', Items.gunpowder);
		GameRegistry.addRecipe(new ItemStack(ItemAmmo.ItemRoundShot.instance, 7), 
				"  x",
				" go",
				"c  ",
				'x', Items.iron_ingot, 'c', ItemCasing.instance, 'g', Items.gunpowder, 'o', Blocks.wool);

		//ammo types
		GameRegistry.addRecipe(new IRecipe(){
			final Item[] typants= {Items.redstone, Items.gunpowder};
			
			@Override
			public boolean matches(InventoryCrafting inv, World world) {
				boolean ret= false;
				
				for(Item aitm : subclasses)//if has round
					if(T1AItem.getItemFromInv(inv, aitm)!=null)
						return ret= true;
				if(!ret)
					return ret;
				for(Item tp : typants)//if has other type
					if(T1AItem.getItemFromInv(inv, tp)!=null)
						return ret= true;
				return ret;
			}
			@Override
			public int getRecipeSize(){return 9;}
			@Override
			public ItemStack getRecipeOutput(){return null;}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv){				
				boolean foundround=false;
				Item ammoitm=null;
				int ammoindx=0;
				//scan for the round, make sure there is only one
				for(int i=0; i!=inv.getSizeInventory(); i++){
					final ItemStack stack= inv.getStackInSlot(i);
					if(stack==null)
						continue;
					final Item stackitem= stack.getItem();
					if(subclasses.contains(stackitem)){
						ammoindx=i;
						ammoitm=stackitem;
					}
				}

				int typ=-1;
				//scan for the typant
				for(int i=0; i!=inv.getSizeInventory(); i++){
					final ItemStack stack= inv.getStackInSlot(i);
					if(stack==null)
						continue;
					final Item stackitem= stack.getItem();
					for(int j=0; j!=typants.length; j++)
						if(typants[j]==stackitem){
							typ= j+1;
							break;
						}
				}
				return new ItemStack(ammoitm, 1, typ);
			}
		});

	}
}
