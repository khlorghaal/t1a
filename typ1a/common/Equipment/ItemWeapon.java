package com.typ1a.common.Equipment;

import net.minecraft.item.Item;

import com.typ1a.common.AmmoStuff.ItemAmmo;
import com.typ1a.common.BlocksItems.T1AItem;

/**Because of small arm cross-compatability not all weapons extend this*/
public abstract class ItemWeapon extends T1AItem {

	/**MEDIUM slots can fit launchers, LAUNCHER is exclusive
	 * larger slots can fit smaller weapons*/
	public static enum Size{LIGHT, MEDIUM, HEAVY, LAUNCHER};
	public static enum FireMode{AIMED, LOCKING};

	public final Size size;
	public final boolean energyWeapon;
	public final Item ammoItem;
	public final FireMode fireMode;

	/**@param AmmoItem null if energy weapon*/
	public ItemWeapon(String name, String tex, Size s, Item ammoItem, FireMode m) {
		super(name, tex, -1, 100);
		this.size=s;
		this.fireMode=m;

		if(ammoItem==null){
			this.energyWeapon=true;
			this.ammoItem=null;
		}
		else{
			this.ammoItem=ammoItem;
			this.energyWeapon=false;
		}
	}


	public static class ItemLauncher extends ItemWeapon{
		public ItemLauncher(String tex) {
			super("Missile Launcher", tex, Size.LAUNCHER, ItemAmmo.ItemMissile.instance, FireMode.LOCKING);
		}	}
	public static class ItemBombBay extends ItemWeapon{
		public ItemBombBay(String tex) {
			super("Bomb Bay", tex, Size.LAUNCHER, ItemAmmo.ItemMissile.instance, FireMode.AIMED);
		}	}


	public static class ItemLaser1MJ extends ItemWeapon{
		public ItemLaser1MJ(String tex) {
			super("1MJ Pulse Laser", tex, Size.LIGHT, null, FireMode.AIMED);
		}	}
	public static class ItemLaser5MJ extends ItemWeapon{
		public ItemLaser5MJ(String tex) {
			super("5MJ Pulse Laser", tex, Size.MEDIUM, null, FireMode.AIMED);
		}	}	

	public static class ItemHeavyShotgun extends ItemWeapon{
		public ItemHeavyShotgun(String tex){
			super("Heavy Shotgun", tex, Size.LIGHT, ItemAmmo.ItemRoundArty.instance, FireMode.AIMED);
		}	}
	public static class ItemAutocannon extends ItemWeapon{
		public ItemAutocannon(String tex) {
			super("Autocannon", tex, Size.LIGHT, ItemAmmo.ItemRoundBMG.instance, FireMode.AIMED);
		}	}	
	public static class ItemArtillery extends ItemWeapon{
		public ItemArtillery(String tex) {
			super("Artillery Cannon", tex, Size.HEAVY, ItemAmmo.ItemRoundArty.instance, FireMode.AIMED);
		}	}

	public static class ItemRPC extends ItemWeapon{
		public ItemRPC(String tex) {
			super("Relativistic Proton Cannon", tex, Size.HEAVY, null, FireMode.AIMED);
		}	}

}
