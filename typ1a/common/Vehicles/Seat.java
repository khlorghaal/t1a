package com.typ1a.common.Vehicles;

import net.minecraft.entity.player.EntityPlayer;

import com.typ1a.common.utils.Vector3;

public class Seat {
	public EntityPlayer occupant;
	public final EntityVehicle vehicle;
	public int keys;
	public final Vector3 mount;
	
	public Seat(EntityVehicle veh, Vector3 mount){
		this.vehicle= veh;
		this.mount= mount;
	}
	public void getIn(EntityPlayer p){
		System.out.println("arse down " + p);
		p.mountEntity(vehicle);
		keys=0;
		occupant=p;
	}
	public void getOut(){
		occupant.mountEntity(null);
		System.out.println("arse up " + occupant);
		keys=0;
		
		occupant= null;
	}
	public void update(){
		if(occupant==null)
			return;
//		final Vector3 pmount= mount.clone(); TODO
	}
}
