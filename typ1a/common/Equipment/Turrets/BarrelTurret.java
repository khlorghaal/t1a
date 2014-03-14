package com.typ1a.common.Equipment.Turrets;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.typ1a.common.Projectiles.Barrel;

public class BarrelTurret extends Barrel {

	public final Turret turret;
	/**Defined instead of center and length; origin is always rotational center.*/
	final Vector4f muzzleEnd= new Vector4f();

	public BarrelTurret(Turret turret, Vector3f muzzleEnd){
		this.turret= turret;
		this.muzzleEnd.x= muzzleEnd.x;
		this.muzzleEnd.y= muzzleEnd.y;
		this.muzzleEnd.z= muzzleEnd.z;
		this.muzzleEnd.w= 0;
	}


	@Override
	public void calculatePosAndLook(){
		final Matrix4f rotation= turret.kin.getRotation();

		final Vector4f vel= Matrix4f.transform(rotation, new Vector4f(1,0,0,0), null);
		lookdir.x= vel.x;
		lookdir.y= vel.y;
		lookdir.z= vel.z;

		
		//muzzleendout= (vehicle rotation * turret mount) + (vehicle rotation*turret rotation * muzzleEnd)
		final Vector4f v1= Matrix4f.transform(turret.kinveh.getRotation(), turret.kin.mntps, null);
		final Matrix4f addedrot= Matrix4f.mul(turret.kin.getRotation(), turret.kinveh.getRotation(), null);
		final Vector4f v2= Matrix4f.transform(addedrot, muzzleEnd, null);
		
		muzzleEndTransformed.x= v1.x + v2.x;
		muzzleEndTransformed.y= v1.y + v2.y;
		muzzleEndTransformed.z= v1.z + v2.z;
	}
}
