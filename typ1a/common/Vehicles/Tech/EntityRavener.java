package com.typ1a.common.Vehicles.Tech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.lwjgl.util.vector.Vector3f;

import com.typ1a.common.Equipment.Turrets.TurretMount;
import com.typ1a.common.Vehicles.Seat;
import com.typ1a.common.utils.Vector3;

public class EntityRavener extends EntityMech {

	public EntityRavener(World zaWarudo){super(zaWarudo);}

	public EntityRavener(EntityPlayer player, double par4, double par5,
			double par6) {
		super(player, par4, par5, par6);
	}

	@Override
	public double getMountedYOffset(){		return 2.8d;	}

	@Override
	public int getMaxHealth() {		return 20;	}

	@Override
	public float getEyeHeight() {		// TODO	
		return 0;	}

	@Override
	protected Seat[] constructSeats() {
		return new Seat[]{ new Seat(this, new Vector3(0,1,0))};
	}

	@Override
	public float getMass() {return 1;}
	@Override
	public Vector3f getDrag() {	return new Vector3f(1,1,1);}

	@Override
	public TurretMount getTurretMount(int indx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector3f getDragAngular() {
		// TODO Auto-generated method stub
		return null;
	}
}
