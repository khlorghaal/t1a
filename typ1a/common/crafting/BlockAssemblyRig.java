package com.typ1a.common.crafting;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.typ1a.common.T1A;
import com.typ1a.common.T1AGUIHandler;
import com.typ1a.common.BlocksItems.T1ABlock;
import com.typ1a.common.Vehicles.ItemUnassembledVehicle;

public class BlockAssemblyRig extends T1ABlock {

	public BlockAssemblyRig() {
		super("Assembly Rig", Material.iron);
	}
	@Override
	public TileEntity createTileEntity(World world, int meta){
		return new TileEntityAssemblyRig();
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		par5EntityPlayer.openGui(T1A.instance, T1AGUIHandler.RIG, par1World, par2, par3, par4);
        return true;
    }


	public static class TileEntityAssemblyRig extends TileEntity implements IInventory{
		
		public ItemStack slot;
		public int timetoassemble=0,
		power=0,
		xsiz,ysiz,zsiz;
		
		@Override
		public void updateEntity(){
			if(timetoassemble==-1)
				return;
			if(timetoassemble==0){
				if(checkRig(xsiz,ysiz,zsiz)){
					//TODO done
					timetoassemble=-1;
					return;
				}
			}
			timetoassemble--;
		}
		/**@param minxyz minimum size of the rig */
		public boolean checkRig(int minx, int miny, int minz){
			final Block r= BlockAssemblyRigging.instance;
			int x= xCoord, y= yCoord, z= zCoord;
			int xpl,ypl,zpl;//the branches of the rig, polarity on respective axis
			
			//det pl
			if(worldObj.getBlock(x+1, y, z)==r)
				xpl= 1;
			else if(worldObj.getBlock(x-1, y, z)==r){
				xpl= -1;
			}
			else{//block on neither direction
				return false;
			}
			
			if(worldObj.getBlock(x, y+1, z)==r)
				ypl= 1;
			else if(worldObj.getBlock(x, y-1, z)==r){
				ypl= -1;
			}
			else{
				return false;
			}
			
			if(worldObj.getBlock(x, y, z+1)==r)
				zpl= 1;
			else if(worldObj.getBlock(x, y, z-1)==r){
				zpl= -1;
			}
			else{
				return false;
			}
			
			//count lengths
			while(worldObj.getBlock(x, y, z)==r){
				minx--; x+= xpl;
			}
			if(minx>0)
				return false;
			x= xCoord;
			
			while(worldObj.getBlock(x, y, z)==r){
				ysiz++; y+= ypl;
			}
			if(miny>0)
				return false;
			y= yCoord;
			
			while(worldObj.getBlock(x, y, z)==r){
				zsiz++; z+= zpl;
			}
			if(minz>0)
				return false;
			z= zCoord;
			
			
			return true;
		}
		public static final int ASSBEMLYTIME=100; 
		public void startAssembly(){
			if(!checkRig(xsiz,ysiz,zsiz)){
				//TODO sfx nope
				return;
			}
			timetoassemble= ASSBEMLYTIME;
		}
		public void spawnVehicle(){
			
		}
		
		@Override
		public void readFromNBT(NBTTagCompound par1NBTTagCompound){
			super.readFromNBT(par1NBTTagCompound);
			slot= ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("s"));
			timetoassemble= par1NBTTagCompound.getInteger("t");
			power= par1NBTTagCompound.getInteger("p");
	    }
		@Override
		public void writeToNBT(NBTTagCompound par1NBTTagCompound){
			super.writeToNBT(par1NBTTagCompound);
			par1NBTTagCompound.setTag("s", slot.writeToNBT(new NBTTagCompound()));
			par1NBTTagCompound.setInteger("t", timetoassemble);
			par1NBTTagCompound.setInteger("p", power);
	    }
		
		
		@Override
		public int getSizeInventory() {return 0;}

		@Override
		public ItemStack getStackInSlot(int i){return slot;}

		@Override
		public ItemStack decrStackSize(int i, int j) {
			// TODO Auto-generated method stub
			return slot;
		}

		@Override
		public ItemStack getStackInSlotOnClosing(int i) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setInventorySlotContents(int i, ItemStack itemstack) {
			slot= itemstack;
			
		}

		@Override
		public int getInventoryStackLimit() {
			return 1;
		}

		@Override
		public boolean isUseableByPlayer(EntityPlayer entityplayer){return true;}

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
		public void openInventory() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void closeInventory() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean isItemValidForSlot(int var1, ItemStack stack) {
			return stack.getItem()==ItemUnassembledVehicle.instance;
		}
	}
}
