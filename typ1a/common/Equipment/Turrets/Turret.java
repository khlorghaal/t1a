package com.typ1a.common.Equipment.Turrets;

import org.lwjgl.util.vector.Vector3f;

import com.typ1a.common.Kinematics;
import com.typ1a.common.Equipment.EquipmentFacade;
import com.typ1a.common.Equipment.Gun;

public abstract class Turret{
	public static enum TurretSize {LIGHT, MED, HVY, BOMB}

	public EquipmentFacade equ;
	public final Gun gun;

	public final Kinematics kinveh;
	public final KinematicsTurret kin;

	public static final Vector3f getAngularDrag(TurretSize size){
		switch(size){
		case LIGHT: return new Vector3f(1,1,1);
		case MED: return new Vector3f(2,2,2);
		case HVY: return new Vector3f(3,3,3);
		case BOMB: return new Vector3f(0,0,0);
		default: return null;
		}
	}

	public Turret(ITurretAcceptor mount, int indx, Gun gun, TurretSize size){
		this.gun= gun;
		this.equ= mount.getEquipmentFacade();
		this.kinveh= mount.getKinematics();

		final Vector3f angdrg= new Vector3f();
		kin= new KinematicsTurret(kinveh, mount.getTurretMount(indx).pos, mount.getTurretMount(indx).normal, getAngularDrag(size));
	}

	public void update(){
		gun.update();
		gun.barrel.calculatePosAndLook();
	}
	
	public void setFiring(boolean state){
		gun.setFiring(state);
	}
}
