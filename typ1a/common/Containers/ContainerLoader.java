package com.typ1a.common.Containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

import com.typ1a.common.IGuiable;
import com.typ1a.common.AmmoStuff.ItemMagazine;

public class ContainerLoader extends ContainerProper {

	/**Issues with this, cannot set in constructor, must obtain a 'fresh' 
	 * player reference and reset it frequently. No idea why.*/
	ItemStack mag;

	static final int[] coords;
	static{
		coords= new int[2*(4*8)];

		final int mul= 18, offsx=17, offsy=7;
		for(int y=0; y!=4; y++)
			for(int x=0; x!=8; x++){
				final int i= y*8+x;
				coords[i*2]= x*mul +offsx;
				coords[i*2+1]= y*mul +offsy;
			}
	}

	/**An IInv to buffer the rounds in the container<br>
	 * ultimately converted into Clusters then inserted into player's held magstack*/
	private IInventory buffer;
	/**hack for working around construction order.*/
	private static IInventory sbufbuf= new InventoryBasic("Magazine Loader", false, coords.length/2);

	public ContainerLoader(InventoryPlayer playerinv){
		super(playerinv, sbufbuf, coords, 16);
		this.buffer= sbufbuf;
		sbufbuf= new InventoryBasic("Magazine Loader", false, coords.length/2);
	}

	public final GuiableLoader guiable= new GuiableLoader(this);
	public final class GuiableLoader implements IGuiable{
		public final ContainerLoader container;
		public int[] states= new int[]{0,0};
		public GuiableLoader(ContainerLoader container){
			this.container= container;
		}
		@Override
		public void setStates(int[] states) {
			this.states= states;

			//im not sure why i have to set this every time and in this location
			//took me a day to figure out through trial-error
			//it APPEARS that if setting this in constructor it points to an imaginary stack?
			if(this.container.crafters.size()==0)
				return;
			final EntityPlayer pl= (EntityPlayer)this.container.crafters.get(0);
			if(pl==null)
				return;
			mag= pl.getHeldItem();

			if(states[0]==1){
				container.load();
			}
		}		
		@Override
		public int[] getStates() {
			return states;
		}		
		@Override
		public boolean canPlayerAccess(EntityPlayer player){return true;}
	};

	/**load from the grid slots*/
	public void load(){
		boolean hasroom=false; 
		
		outer:
		for(int i=0; i!=buffer.getSizeInventory()-1; i++){
			
			final ItemStack ammostack= buffer.getStackInSlot(i);
			if(ammostack==null)
				continue;
			if(ammostack.getItem()!= ((ItemMagazine)mag.getItem()).getAmmoItem())
				continue;
			
			final int btype= buffer.getStackInSlot(i).getItemDamage();
			
			while(ammostack.stackSize>0){
				hasroom= ItemMagazine.pushRound(mag, ammostack.getItemDamage());
				if(hasroom)
					ammostack.stackSize--;
				else
					break outer;
			}
			//will only skip if stacksize>0
			this.putStackInSlot(i, null);
			
		}
		ItemMagazine.updateDamage(mag);
	}
	
	public void onContainerClosed(EntityPlayer par1EntityPlayer){
		for(int i=0; i!=buffer.getSizeInventory(); i++)
			par1EntityPlayer.inventory.addItemStackToInventory(buffer.getStackInSlot(i));
    }
}
