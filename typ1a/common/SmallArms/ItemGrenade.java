package com.typ1a.common.SmallArms;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.typ1a.common.Explosion.EntitySmoke;
import com.typ1a.common.Projectiles.EntityGrenade;

import cpw.mods.fml.common.registry.GameRegistry;

public abstract class ItemGrenade extends ItemSmallArm{

	public static List<ItemGrenade> instances= new Vector<ItemGrenade>();
	
	final int type;

	public static class ItemGrenadeFragmentation extends ItemGrenade{
		public ItemGrenadeFragmentation(){
			super("Fragmentation Grenade", EntityGrenade.FRAG);
		}
	}
	public static class ItemGrenadeConcussion extends ItemGrenade{
		public ItemGrenadeConcussion(){
			super("Concussion Grenade", EntityGrenade.CONCUSSION);
		}		
	}
	public static class ItemGrenadeSmokeS extends ItemGrenade{
		public ItemGrenadeSmokeS(){
			super("Smoke Grenade", EntityGrenade.SMOKES);
		}		
	}
	public static class ItemGrenadeSmokeL extends ItemGrenade{
		public ItemGrenadeSmokeL(){
			super("Intense Smoke Grenade", EntityGrenade.SMOKEL);
		}		
	}
	public static class ItemGrenadeFlare extends ItemGrenade{
		public ItemGrenadeFlare() {
			super("Flare", EntityGrenade.FLARE);
		}
	}
	public static class ItemGrenadeDisco extends ItemGrenade{
		public ItemGrenadeDisco() {
			super("Disco Grenade", EntityGrenade.DISCO);
		}
	}
	

	public ItemGrenade(String name, int type) {
		super(name);
		this.type= type;
		instances.add(this);
		this.setMaxDamage(getInternalMagSize());
	}

	@Override
	public void use(EntityPlayer player, NBTTagCompound dat) {
		new TickerGrenade(player, type);
	}

	@Override
	public int getInternalMagSize() {return 60;}//time to throw at full power


	@Override
	public float getRecoilPitch(Random r) {		return 2;	}

	@Override
	public float getRecoilYaw(Random r) {		return 0;	}

	@Override
	public Item getAmmoItem() {return null;}

	@Override
	public float getDamage() {return 0;}

	@Override
	public int getRof() {return 0;
	}

	@Override
	public int getReloadSpeed() {return 0;}

	@Override
	public double getProjectileSpeed(){return 0.024;}

	@Override
	public double getDeviation() {return 0;}

	@Override
	public String getSound() {
		return "toss";
	}


	public static void registerCrafting(){

		for(Item itemG : ItemGrenade.instances)
			OreDictionary.registerOre("grenade", itemG);

		GameRegistry.addRecipe(new IRecipe() {
			int dyeDmg;
			@Override
			public boolean matches(InventoryCrafting inventorycrafting, World world) {
				boolean hasDye=false;
				boolean hasGrenade=false;
				
				for(int i=0; i<inventorycrafting.getSizeInventory(); i++){
					
					final ItemStack stack= inventorycrafting.getStackInSlot(i);
					if(stack!=null){
						
						final String name= OreDictionary.getOreName(Item.getIdFromItem(Items.dye));
						if(name!=null){
							
							System.out.println(name);
							if(name.equals("grenade"))
								hasGrenade=true;
							else if(name.equals("dye"))
								hasDye=true;

							if(hasDye && hasGrenade)
								return true;
						}
					}
				}
				return false;
			}

			@Override
			public int getRecipeSize() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public ItemStack getRecipeOutput() {
				return null;
			}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {

				return null;
			}
		});
	}

	public static int getColorInt(ItemStack stack) {
		if(stack.hasTagCompound()){
			int ret= stack.getTagCompound().getInteger("col");
			if(ret==0)
				ret=EntitySmoke.COLORDEFAULT;
			return ret;
		}
		return EntitySmoke.COLORDEFAULT;
	}
}
