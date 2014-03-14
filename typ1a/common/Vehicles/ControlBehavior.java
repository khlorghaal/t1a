package com.typ1a.common.Vehicles;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

import com.typ1a.common.Kinematics;
import com.typ1a.common.Maths;
import com.typ1a.common.T1A;

public class ControlBehavior {
	private Kinematics kin;
	private int keys;
	
	public ControlBehavior(Kinematics kinematics){
		this.kin= kinematics;
	}
	/**Avoid equations here, should be used for setting thrust.*/
	public void tick(){
//		kin.setForceGlobal(new Vector3f(0, .2f, 0));
	}
	
	public void setKeys(int data){
		keys= data;
		float f,r,u;
		f= T1A.isKeyPressed(data, T1A.W)? 1:0;
		f+= T1A.isKeyPressed(data, T1A.S)? -1:0;
		r= T1A.isKeyPressed(data, T1A.D)? 1:0;
		r+= T1A.isKeyPressed(data, T1A.A)? -1:0;
		u= T1A.isKeyPressed(data, T1A.JUMP)? 1:0;
		kin.setThrustLocal(f,r,u);
	}
	
	public void setLook(float yaw, float pitch){
		yaw= (float) Maths.defuckVanillaAngle(yaw);
		yaw= (float) Maths.constrainStandardRadianAngle(yaw);
		pitch= (float) Math.toRadians(pitch);
		final float dyaw= (float) Maths.angleShortestPathIncremented(kin.getYaw(), yaw, .1f);
		final float dpitch= (float) Maths.angleShortestPathIncremented(kin.getPitch(), pitch, .1f);
//		kin.addTorqueLocal(kin.YAW, dyaw );
//		kin.addTorqueLocal(kin.PITCH, dpitch );

//		kin.addTorqueLocal(kin.ROLL, .1f);
//		kin.addTorqueLocal(kin.PITCH, .1f );
//		kin.addTorqueLocal(1, .1f );
//		System.out.println(kin.getLocalAxis(0));
//		System.out.println(kin.getLocalAxis(1));
//		System.out.println(kin.getLocalAxis(2));
	}
	public void setLook(Vector3f vec){
		
	}
	public Vector3f getThrust(){
		return null;
	}
	public Matrix3f getTargetRotation(){
		return null;
	}
}
