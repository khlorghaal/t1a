package com.typ1a.common.SmallArms;

import java.util.Hashtable;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.typ1a.common.Ticker;
import com.typ1a.common.BlocksItems.T1AItem;

public class TickerReload extends Ticker {

	private static final Map<Entity, TickerReload> users= new Hashtable<Entity, TickerReload>();
	private static final Map<Entity, TickerReload> usersclient= new Hashtable<Entity, TickerReload>();
	final EntityPlayer player;
	final ItemStack held;
	ItemSmallArm itemSmallArm;
	int rls;

	public TickerReload(EntityPlayer player){
		super();

		this.player=player;
		this.held=player.getHeldItem();

		if(held==null){
			this.kill();
			return;
		}
		if(! (held.getItem() instanceof ItemSmallArm)){
			this.kill();
			return;
		}
		itemSmallArm= (ItemSmallArm)held.getItem();

		if(! player.worldObj.isRemote){
			if(users.containsKey(player)){
				this.kill();
				return;
			}
			users.put(player, this);
		}
		else{
			if(usersclient.containsKey(player)){
				this.kill();
				return;
			}
			usersclient.put(player, this);
		}
		rls= itemSmallArm.getReloadSpeed();


		String sound="";
		if(held.getItem()==ItemTOW.instance)
			sound= "t1a:reloadTOW";
		else if(held.getItem()==ItemShotgun.instance)
			sound= "t1a:reloadShotgun";
		else if(held.getItem()==ItemAR.instance
				|| held.getItem()==ItemSMG.instance)
			sound= "t1a:reloadMagM15";
		else if(held.getItem()==ItemRifle.instance)
			sound= "t1a:reloadRifle";
		player.worldObj.playSoundAtEntity(player, sound, 1, 1);
	}

	@Override
	public void onUpdate() {		
		//make sure item is still being held
		if(player.getHeldItem() != held){
			this.kill();
			return;
		}

		if(itemSmallArm!=null && this.ticksDone==rls)
			reload();
	}
	protected void reload(){
		final ItemSmallArm isa= ((ItemSmallArm)held.getItem());
		final Item aitem= isa.getAmmoItem();
		if(aitem==null)
			return;

		final boolean internalmag= isa.getInternalMagSize()>0;

		final ItemStack[] inv= player.inventory.mainInventory;
		final int stackindx= T1AItem.getBestStack(inv, aitem);
		if(stackindx==-1){//none in inv
			this.kill();
			if(internalmag)
				return;
			player.inventory.addItemStackToInventory( SmallarmLoading.reload(held, null) );
			return;
		}
		//full internal mag
		if( internalmag && Cluster.getCount(SmallarmLoading.getMagClusters(held)) == isa.getInternalMagSize()){
			this.kill();
			return;
		}
		final ItemStack ammoStack = inv[stackindx];

		final ItemStack rres= SmallarmLoading.reload(held, ammoStack);

		if(ammoStack==null){
			this.kill();return;}

		//MISSILE
		//consume a missile and set the tow's dmgval
		//			if(ammoStack.getItem().itemID == ItemAmmo.ItemMissile.instance
		//					&& held.getItemDamage() >= 1){
		//				player.inventory.consumeInventoryItem(ammoStack.itemID);
		//				held.setItemDamage(held.getItemDamage()-1);
		//			}

		if(internalmag){
			//cycle until full
			this.kill();
			new TickerReload(player);
		}

		ammoStack.stackSize--;
		if(ammoStack.stackSize<1){
			inv[stackindx]= null;
		}
		player.inventory.addItemStackToInventory(rres);

		player.worldObj.playSoundAtEntity(player, "reload", 1, 1);
		this.kill();
	}
	public static void interrupt(EntityPlayer player){
		final Map map;
		if(player.worldObj.isRemote)
			map= usersclient;
		else 
			map= users;

		if(map.containsKey(player))
			((TickerReload) map.get(player)).kill();
	}

	@Override
	public void kill(){
		super.kill();
		if(!player.worldObj.isRemote){
			if(users.containsKey(player))
				users.remove(player);
		}
		else if(usersclient.containsKey(player))
			usersclient.remove(player);
	}
}
