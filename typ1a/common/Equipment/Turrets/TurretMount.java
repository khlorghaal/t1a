package com.typ1a.common.Equipment.Turrets;

import org.lwjgl.util.vector.Vector3f;

import com.typ1a.common.Equipment.Turrets.Turret.TurretSize;

public class TurretMount {
	public final TurretSize size;
	public final Vector3f pos;
	public final Vector3f normal;
	
	public TurretMount(TurretSize size, Vector3f pos, Vector3f norm){
		this.size= size;
		this.pos= pos;
		this.normal= norm;
	}
}
