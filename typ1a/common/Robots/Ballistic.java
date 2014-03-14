package com.typ1a.common.Robots;

import net.minecraft.entity.Entity;

/**Used to get needed launch to hit target, nothing more*/
public class Ballistic {

	Entity shooter, target;
	double v;
	public final static double G=-0.04;
	public Ballistic(Entity shooter, Entity target, double projSpeed){
		this.shooter=shooter;
		this.target=target;
		this.v=projSpeed;
	}
	/**@return {yaw,pitch}*/
	public double[] get(){
		double
		dx= target.posX+target.motionX*0.3 - shooter.posX,
		dy= target.posY+target.motionY*0.3 - shooter.posY,
		dz= target.posZ+target.motionZ*0.3 - shooter.posZ;
		final double dist= Math.sqrt(dx*dx + dz*dz);
		//TODO leading targets
		//add motion*travel time
		final double yaw= Math.atan2(dz, dx);

		final double vsq=v*v;
		final double pitch= Math.atan(
				(vsq - Math.sqrt(vsq*vsq - G*(G*dist*dist + 2*dy*vsq) ) )
				/ (G*dist)
				);
		
//		//
//		final double cosp=Math.cos(pitch); 
//		final double vx=v* Math.cos(yaw)*cosp;//cant use math helper because it only takes floats
//		final double vz=v* Math.sin(yaw)*cosp;//like an actual gun this requires precision
//		final double vy=v* Math.sin(pitch);

		return new double[]{yaw,pitch};
	}
}
