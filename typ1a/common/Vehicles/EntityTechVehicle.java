package com.typ1a.common.Vehicles;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.typ1a.common.T1A;
import com.typ1a.common.T1AGUIHandler;
import com.typ1a.common.Equipment.EquipmentFacade;
import com.typ1a.common.Equipment.Turrets.ITurretAcceptor;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public abstract class EntityTechVehicle extends EntityVehicle
implements ITurretAcceptor{

	private EquipmentFacade equipment= new EquipmentFacade(2,2,2, 10, 10);

	public EntityPlayer owner;
	
	@Override
	public EquipmentFacade getEquipmentFacade() {
		return equipment;
	}

	

	//The ItemStacks lists are necessarily separate from unique class lists
	//ItemStack lists must still be maintained for uniformity

	//ITurretAcceptor

	/////////////////

	//gfx
	public double walkProg=0, sideWalk=0, dYaw=0;

	//event timing
	public int deathTime;
	public int bailTime;

	private int health=this.getMaxHealth();

	////////////////
	public EntityTechVehicle(World par1World) {
		super(par1World);

	}
	public EntityTechVehicle(EntityPlayer owner, double x, double y, double z){
		super(owner.worldObj,x,y,z);
		this.owner= owner;

	}
	/////////////////////////////////////////
	@Override
	public void onUpdate(){	
		//gfx
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT){

		}

		super.onUpdate();
	}



	@Override
	public void updateRidden(){
		super.updateRidden();
	}

	//////////////

	public void setFiring(int index, boolean state){
		//TODO
	}

	@Override
	public boolean interactFirst(EntityPlayer player){
		//TODO keys
		if(player.isSneaking())
			player.openGui(T1A.instance, T1AGUIHandler.EQUIPMENT, player.worldObj, this.getEntityId(), 0, 0);
		else
			super.interactFirst(player);

		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource src, float dmg){
		super.attackEntityFrom(src, dmg);
		equipment.absorbDamage(dmg);
		//TODO fwd to eqf
		return true;
	}
	
	@Override
	public void updateDying() {
		super.updateDying();
		//TODO fancy death pewpew
	}
	@Override
	public void setDead(){
		if(!worldObj.isRemote)
			worldObj.createExplosion(this, posX, posY, posZ, 2, true);
		super.setDead();
	}


	@Override
	public void applyEntityCollision(Entity entity){
		super.applyEntityCollision(entity);
		//TODO fwd to kine
	}





	/////////////////////////////////////
	// boring stuff
	/////////////////////////////////////
	///////////
	@Override
	public void readEntityFromNBT(NBTTagCompound cmpnd){
		super.readEntityFromNBT(cmpnd);
		equipment.readFromNBT(cmpnd);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound cmpnd){
		super.writeEntityToNBT(cmpnd);
		equipment.writeToNBT(cmpnd);
	}


}