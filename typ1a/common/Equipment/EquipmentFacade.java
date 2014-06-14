package com.typ1a.common.Equipment;

import java.util.List;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**The big class for attaching equipment to an Entity</br>
 * Must include in host: 
 * onUpdate - update 
 * onAttackedByEntity - absorbDamage 
 * corresponding calls to NBT*/
public class EquipmentFacade implements IInventory{

	private ItemStack[] inv;
	private ItemStack[] previnv;
	
	public final int nequip, narmor, nwpn;
	private int heat, maxHeat;
	private int energy, maxEnergy;
	
	float[] thrust;
	public float[] getThrust(){ return thrust; }

	private Shields shields;
	private Armor armor;
	private Deflectors deflectors;

	List<ItemStack> weaponStacks = new Vector<ItemStack>();
	List<ItemStack> armorStacks = new Vector<ItemStack>();
	List<ItemStack> shieldStacks = new Vector<ItemStack>();
	List<ItemStack> deflectorStacks = new Vector<ItemStack>();
	List<ItemStack> heatCapStacks = new Vector<ItemStack>();
	List<ItemStack> heatDumpStacks = new Vector<ItemStack>();
	List<ItemStack> agravStacks = new Vector<ItemStack>();
	List<ItemStack> sensorStacks = new Vector<ItemStack>();
	
	public EquipmentFacade(int nequip, int narmor, int nwpn, int maxHeat, int maxNrg){
		this.nequip= nequip; this.narmor= narmor; this.nwpn= nwpn;
		this.inv= new ItemStack[nequip+narmor+nwpn];
		this.maxHeat= maxHeat;
		this.energy= maxNrg;//TODO start at 0
		
		shields= new Shields(this);
		armor=new Armor();
	}


	/**Do all energy changing here*/
	public void update(){
		shields.update();
		armor.update();
	}


	public int getHeat(){return heat;}

	/**@return if was able to (was not overheated)*/
	public boolean addToHeat(int h){
		if( maxHeat-heat < h) return false;
		heat+=h; return true;
	}

	public int getEnergy() {return this.energy;}

	/**@return if was able to (was not empty)*/
	public boolean useEnergy(int e){
		if( energy-e > -1) return false;
		energy-=e; return true;
	}
	/**@return penetrated damage*/
	public float absorbDamage(float dmg){
		return armor.hit( shields.hit(dmg) );
	}
	public int getShieldGlow(){
		return shields.load;
	}


	public void writeToNBT(NBTTagCompound cmpnd){
		cmpnd.setInteger("nrg", energy);
		cmpnd.setInteger("heat", heat);

		NBTTagList stackTagCompoundList= new NBTTagList();
		for(int i=0; i< inv.length; i++){
			if(inv[i]!=null){
				NBTTagCompound stackCompound= new NBTTagCompound();
				inv[i].writeToNBT(stackCompound);
			}
		}
		cmpnd.setTag("EStacks", stackTagCompoundList);
	}
	public void readFromNBT(NBTTagCompound cmpnd){
		energy= cmpnd.getInteger("nrg");
		heat= cmpnd.getInteger("heat");

		NBTTagList stackTagCompoundList= cmpnd.getTagList("EStacks", 10);//10 is tc
		for(int i=0; i<stackTagCompoundList.tagCount(); i++){
			setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(
							stackTagCompoundList.getCompoundTagAt(i) ));
		}
	}

	//////
	//Inventory stuff
	//////

	private void addStack(ItemStack stack){

	}
	private void removeStack(ItemStack stack){

	}	

	@Override
	public void setInventorySlotContents(int i, ItemStack stack) {
		System.out.println("setslot "+i+" "+stack);
//
//		for(int i=0; i<getSizeInventory(); i++){
//			if(
//					(
//							inv[i]!=null && prevInv[i]!=null
//							&&
//							( inv[i]!=null && inv[i].itemID!=prevInv[i].itemID )
//							||
//							( prevInv[i]!=null && prevInv[i].itemID!=inv[i].itemID )
//							)
//
//							||
//
//							(inv[i]==null && prevInv[i]!=null) || (inv[i]!=null && prevInv[i]==null)
//
//					){
//				this.updateEquipment(i);
//				//im not sure how containers behave so imma be safe and check all the slots
//			}
//		}
		//for(int i=0; i<getSizeInventory(); i++)
		//	this.prevInv[i]=this.inv[i];	
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if( i >= inv.length){
			System.err.println("EquipmentFacade.class getStackInSlot failed as index excedes inv.length");
			return null;
		}
		return inv[i];
	}

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack decrStackSize(int i, int j){
		if (this.inv[i] != null){
			ItemStack localStack;
			if (this.inv[i].stackSize <= j){
				localStack = this.inv[i];
				setInventorySlotContents(i, null);
				return localStack;
			}
			else{
				localStack = this.inv[i].splitStack(j);
				return localStack;
			}
		}
		return null;
	}


	@Override
	public ItemStack getStackInSlotOnClosing(int i) {return null;}

	@Override
	public int getInventoryStackLimit() {
		return 255;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}
	
	public void updateEquipment(int index){
//		//Remove the old subunit from its equipment, if there was one
//		//If a subunit was added to inv then add it to equipment
//
//		//removal, only do if there was something there
//		if(prevInv[index]!=null){
//			Item removedItem=prevInv[index].getItem();
//			if(removedItem instanceof ItemEquipment){
//
//				if(removedItem instanceof ItemShield)
//					this.shields.removeItem((ItemEquipment)removedItem);
//				else if(removedItem instanceof ItemArmor)
//					this.armor.removeItem((ItemEquipment)removedItem);
//			}
//		}
//
//		if(inv[index]==null) return; //took something out (did not swap)
//
//		//insertion
//		final Item item = inv[index].getItem();
//		if(item instanceof ItemEquipment){
//
//			if(item instanceof ItemShield){
//				this.shields.addItem((ItemEquipment)item, inv[index]); return;}
//			if(item instanceof ItemArmor){
//				this.armor.addItem((ItemEquipment)item, inv[index]); return;}
//		}
	}


	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void markDirty() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

}
