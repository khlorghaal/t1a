package com.typ1a.common.Missiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import com.typ1a.common.IGuiable;
import com.typ1a.common.T1A;
import com.typ1a.common.T1AGUIHandler;

public class EntitySilo extends Entity implements IInventory, IGuiable{	
	public Entity owner;
	
	public EntitySilo(World par1World)
	{
		super(par1World);
		this.preventEntitySpawning = true;
		this.setSize(1.01F, 2.01F);
		this.yOffset = 0;
	}

	public EntitySilo(World par1World, double par2, double par4, double par6)
	{
		this(par1World);
		this.setPosition(par2+.5, par4, par6+.5);
		this.motionX = 0.0d;
		this.motionY = 0.10d;
		this.motionZ = 0.0d;
		this.prevPosX = par2;
		this.prevPosY = par4;
		this.prevPosZ = par6;
	}
	@Override
	protected void entityInit() {}
	
	public boolean canBeCollidedWith() {        return !this.isDead;    }

	//////////////////////
	public void onUpdate()
	{

		this.motionX=this.motionY=this.motionZ=0;
		super.onUpdate();
	}

	//
	@Override
	public boolean interactFirst(EntityPlayer player){
		T1A.setGuiableAccessedBy(player, this);
		player.openGui(T1A.instance, T1AGUIHandler.SILO, this.worldObj,this.getEntityId(),0,0);
		return true;
	}






	public int getMaxHealth() {
		return 80;}	  
	public void onDeath(){
		super.setDead();}

	public boolean isUseableByPlayer(EntityPlayer par1player){    	return true;    }

	protected String getHurtSound(){   return "mob.cowhurt";}
	protected String getDeathSound(){  return "mob.cowhurt";}
	protected float getSoundVolume(){  return 0.3F;}



	//Inventory stuff
	ItemStack[] inv = new ItemStack[this.getSizeInventory()];
	@Override
	public int getSizeInventory() {		return 3;	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int par2) {
		if (this.inv[slot] != null)
		{
			ItemStack var3;
			if (this.inv[slot].stackSize <= par2)
			{
				var3 = this.inv[slot];
				this.inv[slot] = null;
				this.updateInventory();
				return var3;
			}
			else
			{
				var3 = this.inv[slot].splitStack(par2);

				if (this.inv[slot].stackSize == 0)
					this.inv[slot] = null;

				this.updateInventory();
				return var3;
			}
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.inv[slot] != null)
		{
			ItemStack closed = this.inv[slot];
			this.inv[slot] = null;
			return closed;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inv[slot] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();

		this.updateInventory();
	}


	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	///////////

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readFromNBT(var1);

		NBTTagList taglist = var1.getTagList("Items", 10);
		this.inv = new ItemStack[this.getSizeInventory()];

		for(int i = 0; i < taglist.tagCount(); ++i){
			NBTTagCompound tagCompound = (NBTTagCompound)taglist.getCompoundTagAt(i);
			this.inv[tagCompound.getByte("Slot") & 255] = ItemStack.loadItemStackFromNBT(tagCompound);
		}

	}
	public void writeEntityToNBT(NBTTagCompound cmpnd) {
		super.writeToNBT(cmpnd);

		NBTTagList taglist = new NBTTagList();
		for (int i = 0; i < this.inv.length; ++i)
		{if (this.inv[i] != null){
			NBTTagCompound tagCompound2 = new NBTTagCompound();
			tagCompound2.setByte("Slot", (byte)i);
			this.inv[i].writeToNBT(tagCompound2);
			taglist.appendTag(tagCompound2);
		}}
		cmpnd.setTag("Items", taglist);
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public int[] getStates() {
		return new int[]{};
	}

	@Override
	public void setStates(int[] states) {
		
	}

	@Override
	public boolean canPlayerAccess(EntityPlayer player) {
		return player==owner;
	}

	public void updateInventory(){
		
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
