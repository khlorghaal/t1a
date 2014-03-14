package com.typ1a.common.Vehicles.Tech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.typ1a.common.Vehicles.EntityTechVehicle;

public abstract class EntityMech extends EntityTechVehicle
{   

	public EntityMech(World par1World){
		super(par1World);
		this.setSize(2.98F, 2.98F);
		this.stepHeight=1.1f;
        //this.entityCollisionReduction=0f;
	}
	
	public EntityMech(EntityPlayer owner, double x, double y, double z){
		super(owner, x, y, z);
		this.setSize(2.98F, 2.98F);
		this.stepHeight=1.1f;
	}
}
