package com.typ1a.common.Vehicles.Tech;

import net.minecraft.entity.player.EntityPlayer;

import com.typ1a.common.Vehicles.EntityTechVehicle;

public abstract class EntityPlane extends EntityTechVehicle {

	public EntityPlane(EntityPlayer owner, double x, double y, double z) {
		super(owner, x, y, z);
	}

}
