package com.typ1a.common.Vehicles;

import net.minecraft.world.World;

import org.lwjgl.util.vector.Vector3f;

import com.typ1a.common.utils.Vector3;

public class EntityDropPod extends EntityVehicle{

	public int targetx,targety;

	public EntityDropPod(World par1World) {
		super(par1World);
		this.setSize(.98f, 3f);
		this.isImmuneToFire=true;
		yOffset=1;
	}

	public EntityDropPod(World world, double x,double y, double z){
		this(world);
		this.setPos(x, y, z);
		this.isImmuneToFire=true;
	}

	@Override
	public void onUpdate(){
		super.onUpdate();
		if(onGround)
			kin.hardStop();
	}
	
	
	@Override
	public double getMountedYOffset(){
		return 1;
	}
	@Override
	public boolean shouldRiderSit(){
		return false;
	}
	public int getMaxHealth() {	return 20;	}

	@Override
	public boolean canBePushed(){return false;}

	@Override
	protected Seat[] constructSeats() {
		return new Seat[]{ new Seat(this, new Vector3(0,1,0))};
	}

	@Override
	public float getMass() {return 1; }

	@Override
	public Vector3f getDrag() {	return new Vector3f();}

	@Override
	public Vector3f getDragAngular() {
		// TODO Auto-generated method stub
		return null;
	}
}
