package com.typ1a.common.Equipment.Turrets;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.typ1a.common.Kinematics;

public class KinematicsTurret extends Kinematics{

	public final Kinematics mounted;
	public final Vector4f mntps= new Vector4f();
	public final Vector4f normal= new Vector4f();
	
	public KinematicsTurret(Kinematics mounted, Vector3f mountpos, Vector3f normal, Vector3f angulardrag) {
		super(0, new Vector3f(0,0,0), angulardrag);
		this.mounted= mounted;
		mntps.w=0;
		mntps.x= mountpos.x; mntps.y= mountpos.y; mntps.z= mountpos.z;
	}
	
	public Vector4f getLocalPosition(){
		return Matrix4f.transform(mounted.getRotation(), mntps, null);
	}
	public Vector4f getLocalPositionInterpolated(float pT){
		return Matrix4f.transform(mounted.getRotationInterpolated(pT), mntps, null);
	}
	
	/**own rotation + mounted rotation * offset*/
	public Matrix4f getTransformation(){
		final Matrix4f ret= getRotation();
		final Vector4f transl= Matrix4f.transform(mounted.getRotation(), mntps, null);
		ret.m30= transl.x; ret.m31= transl.y; ret.m32= transl.z;
		return ret;
	}
	public Matrix4f getTransformationInterpolated(float pT){
		final Matrix4f ret= getRotationInterpolated(pT);
		final Vector4f transl= Matrix4f.transform(mounted.getRotationInterpolated(pT), mntps, null);
		ret.m30= transl.x; ret.m31= transl.y; ret.m32= transl.z;
		return ret;
	}
}
