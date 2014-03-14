package com.typ1a.common.Vehicles.Tech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.lwjgl.util.vector.Vector3f;

import com.typ1a.common.Equipment.Turrets.TurretMount;
import com.typ1a.common.Vehicles.EntityTechVehicle;
import com.typ1a.common.Vehicles.Seat;
import com.typ1a.common.utils.Vector3;

public class EntityMjolnir extends EntityTechVehicle {

	public EntityMjolnir(World world){
		super(world);
	}
	public EntityMjolnir(EntityPlayer owner, double x, double y, double z) {
		super(owner, x, y, z);
	}

	final double[] gunm= new double[]{0,1,0};

	final static double[] pm0= new double[]{0,2,1}, 
			pm1= new double[]{0,0,0};

	@Override
	public int getMaxHealth() {
		return 20;
	}
	@Override
	protected Seat[] constructSeats() {
		return new Seat[]{
			new Seat(this, new Vector3(pm0)),
			new Seat(this, new Vector3(pm1))
		};
	}
	@Override
	public float getMass() {return 1;}
	@Override
	public Vector3f getDrag() {	return new Vector3f(1,1,1);}
	@Override
	public Vector3f getDragAngular() {	return new Vector3f(1,1,1);}
	@Override
	public TurretMount getTurretMount(int indx) {
		// TODO Auto-generated method stub
		return null;
	}

}
