package com.typ1a.common.SmallArms;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.typ1a.common.IGun;
import com.typ1a.common.T1A;
import com.typ1a.common.AmmoStuff.ItemAmmo;
import com.typ1a.common.AmmoStuff.ItemMagazine;
import com.typ1a.common.BlocksItems.ItemUseable;
import com.typ1a.common.BlocksItems.T1AItem;
import com.typ1a.common.Projectiles.EntityBullet;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public abstract class ItemSmallArm extends ItemUseable implements IGun{
	
	public static List<ItemSmallArm> instances= new Vector<ItemSmallArm>();

	/**Animation only. So that a weapon isnt lowered instantly after stopped using*/
	public static Map<Entity, Integer> playersUsing= new Hashtable<Entity, Integer>();

	public static final float //stat modifiers
	//speed and deviation in TickerSmallarm
	//recoil in CommonProxy
	SUPPRDEV=1.1f, MACTIONNDEV=.8f, MBARRELDEV=.7f,
	MBOLTSPEED=1.1f, MBARRELSPEED=.95f,
	MBOLTJAM=.61f,
	BRAKERECOIL=.65f, MSTOCKDEV=.7f;
	
	//SFX and VFX are done by the projectiles

	public ItemSmallArm(String name){
		super(name);
		instances.add(this);
	}
	/**For weapons with internal magazine only*/
	public abstract int getInternalMagSize();

	@Override
	public void use(EntityPlayer player, NBTTagCompound data){
		new TickerSmallArm(player, data);
	}
	@Override
	public void stop(EntityPlayer player) {
		TickerSmallArm.kill(player);
	}

	//player arm animation, packets done by CTH
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean held){
		if(held && world.isRemote
				&& playersUsing.containsKey(entity)){
			final EntityPlayer player= ((EntityPlayer)entity);

			final int t= playersUsing.get(player);

			if(t<80)//max time held up
				((EntityPlayer)entity).setItemInUse(stack, 800);
			else
				playersUsing.put(entity, t+1);
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack){
		return 20;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack s){
		return T1A.commproxy.getSmallarmUseAction(s);
	}
	//////////

	//NBT structure
	//TagCompound "mag" the magazine itemstack, will be id=0 on new gun 
	//byte "cha" btype of bullet in chamber, -1 empty, -2 jam
	//boolean c cocked
	//TC "parts" the 4 part stacks making up the gun
	//	TC<ItemStack> "ba" barrel
	//	TC<ItemStack> "bo" bolt
	//	TC<ItemStack> "a" action
	//	TC<ItemStack> "s" stock

	public static void initTC(ItemStack gun, 
			ItemStack barrel, ItemStack bolt, ItemStack action, ItemStack stock){
		gun.stackTagCompound= new NBTTagCompound();
		final NBTTagCompound gstc= gun.stackTagCompound;

		gstc.setByte("cha", (byte) -1);
		gstc.setByte("jam", (byte) 0);

		gstc.setTag("ba", barrel.writeToNBT(new NBTTagCompound()));
		gstc.setTag("bo", bolt.writeToNBT(new NBTTagCompound()));
		gstc.setTag("a", action.writeToNBT(new NBTTagCompound()));
		gstc.setTag("s", stock.writeToNBT(new NBTTagCompound()));

		final ItemSmallArm isa= (ItemSmallArm)gun.getItem();
		if( isa.getInternalMagSize()>0){
			//init internal mag
			final NBTTagCompound mstc= new NBTTagCompound();
			final NBTTagCompound mtc= new NBTTagCompound();
			mstc.setTag("tag", mtc);
			mtc.setShort("cap", (short)isa.getInternalMagSize());
			mtc.setByteArray("cls", new byte[0]);

			gstc.setTag("mag", mstc);
		}
	}
	public static NBTTagCompound getSTC(ItemStack gun){
		if(gun.stackTagCompound==null)//This will only happen for creative spawned stacks
			initTC(gun,
					new ItemStack(ItemGunPart.instance, 1, ItemGunPart.BARRELLONG),
					new ItemStack(ItemGunPart.instance, 1, ItemGunPart.BOLTLARGE),
					new ItemStack(ItemGunPart.instance, 1, ItemGunPart.ACTIONFULL),
					new ItemStack(ItemGunPart.instance, 1, ItemGunPart.STOCKBULLPUP)
					);
		return gun.stackTagCompound;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List lines, boolean par4){
		final NBTTagCompound gstc= getSTC(stack);

		if(gstc.hasKey("Scope"))
			lines.add(ItemAttachment.getScopeInfo(gstc.getCompoundTag("Scope").getCompoundTag("tag")));
		if(gstc.hasKey("Suppressor"))
			lines.add("\u0021aSuppressor");
		if(gstc.hasKey("Laser Sight"))
			lines.add("\u0021aLaser Sight");
		if(gstc.hasKey("Muzzle Brake"))
			lines.add("\u0021aMuzzle Brake");

		final NBTTagCompound baitc= gstc.getCompoundTag("ba");
		final NBTTagCompound boitc= gstc.getCompoundTag("bo");
		final NBTTagCompound aitc = gstc.getCompoundTag("a");
		final NBTTagCompound sitc = gstc.getCompoundTag("s");
		final ArrayList<String> bal= new ArrayList<String>();
		String bas="";
		final ArrayList<String> bol= new ArrayList<String>();
		String bos="";
		final ArrayList<String> al= new ArrayList<String>();
		String as="";
		final ArrayList<String> sl= new ArrayList<String>();
		String ss="";
		ItemGunPart.addInformationS(baitc, par2EntityPlayer, bal, par4);
		ItemGunPart.addInformationS(boitc, par2EntityPlayer, bol, par4);
		ItemGunPart.addInformationS(aitc, par2EntityPlayer, al, par4);
		ItemGunPart.addInformationS(sitc, par2EntityPlayer, sl, par4);
		bas+=bal.get(0);if(bal.size()!=1) bas+=bal.get(1);
		bos+=bol.get(0);if(bol.size()!=1) bos+=bol.get(1);
		as += al.get(0);if( al.size()!=1)  as+= al.get(1);
		ss += sl.get(0);if( sl.size()!=1)  ss+= sl.get(1);
		lines.add("Barrel"+bas);
		lines.add("Bolt"+bos);
		lines.add("Action"+as);
		lines.add("Stock"+ss);
		
		if(lines.size()!=1)
			lines.add(" ");

		final int cha= gstc.getByte("cha");
		String chas= "Empty";
		if(cha==-2)
			chas= "\u0021cJAMMED";
		else if(cha!=-1
				&& cha<EntityBullet.TYPENAMES.length)
			chas= EntityBullet.TYPENAMES[cha];

		lines.add(" Chamber");
		lines.add("\u0021"+ItemAmmo.getColor(cha)+chas);

		lines.add(" Magazine");
		final NBTTagCompound mtc= gstc.getCompoundTag("mag");
		if(mtc==null){
			lines.add("Empty");
			return;
		}
		final NBTTagCompound mstc= mtc.getCompoundTag("tag");
		//works for external and internal
		ItemMagazine.addInformationS(mstc, par2EntityPlayer, lines, par4);
	}

	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass){
		final NBTTagCompound gstc= getSTC(stack);

		if(ItemGunPart.isMasterwork(gstc.getCompoundTag("ba")) 
				|| ItemGunPart.isMasterwork(gstc.getCompoundTag("bo"))
				|| ItemGunPart.isMasterwork(gstc.getCompoundTag("a"))
				|| ItemGunPart.isMasterwork(gstc.getCompoundTag("s")))
			return true;
		return false;
	}
	//		@Override 
	//	public int getDisplayDamage(ItemStack stack){
	//			final NBTTagCompound mstc= ItemSmallArm.getSTC(stack).getCompoundTag("mag").getCompoundTag("tag");
	//			return mstc.getShort("cap")- Cluster.getCount(ItemMagazine.getClusters(mstc)); 
	//	}
	//doesnt work because its relative to singleton Item maxDamage, cannot ref magcap as max

	public static void registerCrafting() {

		//DISASSEMBLY
		//DETACHMENT
		GameRegistry.addRecipe(new IRecipe() {

			@Override
			public boolean matches(InventoryCrafting inv,
					World world) {
				int count=0;
				for(int i=0; i!=inv.getSizeInventory(); i++){
					if(inv.getStackInSlot(i)!=null)
						count++;
				}
				if(count>1)
					return false;

				final ItemStack gun= T1AItem.getGun(inv);
				return gun!=null && gun.getItem() instanceof ItemSmallArm;
			}

			@Override
			public ItemStack getCraftingResult(
					InventoryCrafting inventorycrafting) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getRecipeSize() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public ItemStack getRecipeOutput() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
}
