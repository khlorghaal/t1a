package com.typ1a.common.Vehicles.Tech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.lwjgl.util.vector.Vector3f;

import com.typ1a.common.Equipment.Turrets.Turret.TurretSize;
import com.typ1a.common.Equipment.Turrets.TurretMount;
import com.typ1a.common.Vehicles.EntityTechVehicle;
import com.typ1a.common.Vehicles.Seat;
import com.typ1a.common.utils.Vector3;

public class EntityB extends EntityTechVehicle {

	public EntityB(World par1World) {
		super(par1World);
	}

	public EntityB(EntityPlayer owner, double x, double y, double z) {
		super(owner, x, y, z);
	}

	@Override
	public float getMass() {
		return .1f;
	}

	@Override
	public Vector3f getDrag() {
		return new Vector3f(1,1,1);
	}

	@Override
	protected Seat[] constructSeats() {
		return new Seat[]{new Seat(this, new Vector3(0,1,0))};
	}

	@Override
	public int getMaxHealth() {
		return 30;
	}

	private final TurretMount tm= new TurretMount(TurretSize.LIGHT, new Vector3f(0,1,1), new Vector3f(0,1,0));
	@Override
	public TurretMount getTurretMount(int indx) {
		return tm;
	}
	
	@Override
	public Vector3f getDragAngular() {
		return new Vector3f(1,1,1);
	}

}
