package com.typ1a.common.Robots;

import com.typ1a.common.Equipment.Gun;

/**Used to simulate a robotic gun moving around*/
public class AIAimer {
	Ballistic ballistic;
	Gun gun;

	final double DTURN;

	private boolean hasLock=false;

	AIAimer(Ballistic ballistic, Gun gun, double DTURN){
		this.ballistic=ballistic;
		this.gun=gun;
		this.DTURN=DTURN;
	}

	/**DO NOT call gun.onUpdate, that is for the Entity to do*/
	public void onUpdate() {
		this.gun.update();//TODO lol
		adjust();

		this.gun.barrel.calculatePosAndLook();
		
		if(hasLock){
			gun.setFiring(true);
		}
		else
			gun.setFiring(false);

	}

	private void adjust() {
		double[] ballisticGot= ballistic.get();
		
		final double yaw=this.gun.barrel.aimer.rotationYaw;//actual
		final double pitch=this.gun.barrel.aimer.rotationPitch;
		final double yawAim= ballisticGot[0];//aim
		final double pitchAim= ballisticGot[1];
		double yawSet, pitchSet;//op result

//		final double yawQ1= Math.abs(yawAim - (yaw+DTURN));
//		final double yawQ2= 0;
//		yawSet= yaw+ ((yawQ1 > yawQ2) ? DTURN : -DTURN );
//		
		yawSet=0;
		pitchSet=0;
		
		gun.barrel.aimer.rotationYaw= (float) yawAim;
		gun.barrel.aimer.rotationPitch= (float) pitchAim;
		
		if(true){
			this.hasLock=true;
		}
	}



}
