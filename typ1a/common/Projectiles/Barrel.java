package com.typ1a.common.Projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import com.typ1a.common.utils.Vector3;
/** Uses proper angles*/
/** Uses proper angles*/
public class Barrel {

	protected int lastTickCrunched= 0;

	public float length,mountX,mountY,mountZ;
	public Entity aimer, vehicle;

	protected Vector3 mount, mountSphere;
	/**the result; where the bullets exit*/
	public Vector3 muzzleEndTransformed;
	/**used for determining bullet motion/laser vector*/
	public Vector3 lookdir;

	//public double yaw, pitch;

	public Barrel(){}
	
	/**Mount position and Entity to mount on 
	 * @param vehicle may be set to null to mount on the aimer
	 * @param length of the barrel*
	 * @param mountA rotational center of the weapon, relative to rotational cener the vehicle*/
	public Barrel(Entity aimer, Entity vehicle, float length, float mountX, float mountY, float mountZ){
		this.aimer= aimer;
		this.vehicle= vehicle;

		this.length= length;
		this.mountX= -mountX;
		this.mountY= -mountY;
		this.mountZ= mountZ;

		mount= new Vector3(this.mountX, this.mountY, this.mountZ);

		this.mountSphere= this.mount.toSpherical();
	}

	/**uses the aimer's rotations, use a dummy Entity if necessary
	 * @return x,y,z of where the bullet comes out*/
	public void calculatePosAndLook(){
		if(lastTickCrunched == aimer.ticksExisted)
			return;
		lastTickCrunched= aimer.ticksExisted;


		if(aimer instanceof EntityPlayer){
			lookdir= new Vector3(length, Math.toRadians(-aimer.rotationPitch), Math.toRadians(90+aimer.rotationYaw)).toCartesian();

			mountSphere.y+= Math.toRadians((getEntityMountedTo() instanceof EntityPlayer) ? 0: -getEntityMountedTo().rotationPitch);
			mountSphere.z+= Math.toRadians(getEntityMountedTo().rotationYaw);
		}
		else{
			lookdir= new Vector3(length, aimer.rotationPitch, aimer.rotationYaw).toCartesian();
			
			mountSphere.y+= getEntityMountedTo() instanceof EntityPlayer? 0: -getEntityMountedTo().rotationPitch;
			mountSphere.z+= getEntityMountedTo().rotationYaw;
		}

		muzzleEndTransformed= mountSphere.toCartesian().add(lookdir);

		muzzleEndTransformed.x+= aimer.posX;
		muzzleEndTransformed.y+= aimer.posY;
		muzzleEndTransformed.z+= aimer.posZ;

		mountSphere= mount.toSpherical();//be sure to reset it

		this.lookdir.normalize();
	}

	//	/**Use setVec if you can
	//	 * @param yaw pitch use normal radian angles not retarded ones*/
	//	public void setAngles(double yaw, double pitch){
	//		this.yaw= yaw;
	//		this.pitch= pitch;
	//	}

	/**@return the vehicle if not null, else the player*/
	public Entity getEntityMountedTo(){
		return this.vehicle==null ? this.aimer : this.vehicle;
	}
}
