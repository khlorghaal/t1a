package com.typ1a.common.Missiles;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import com.typ1a.common.Maths;
import com.typ1a.common.Projectiles.Barrel;

public class EntityWGM extends EntityMissile {

	public EntityWGM(World par1World) {
		super(par1World);
	}

	public EntityWGM(World p1World, Entity owner, Barrel barrel) {
		super(p1World, owner, null, barrel);
	}
	
	
	@Override
	public void aim(){
		if(owner==null)
			return;
		
		yawAim=Math.atan2(owner.getLookVec().zCoord, owner.getLookVec().xCoord); 
		pitchAim=Math.asin(owner.getLookVec().yCoord);

		yaw= Maths.angleShortestPathIncremented(yaw, 0, .1);
		pitch = Maths.angleShortestPathIncremented(pitch, 0, .1);
		yaw=yawAim;
		pitch=pitchAim;
	}

}
