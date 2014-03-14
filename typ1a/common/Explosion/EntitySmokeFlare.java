package com.typ1a.common.Explosion;

import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntitySmokeFlare extends EntitySmoke {

	public EntitySmokeFlare(World world) {
		super(world);
		glowy= true;
	}
	
	public EntitySmokeFlare(World world, double x, double y, double z,
			int lifespan, float strength, int color) {
		super(world, x, y, z, lifespan, strength, color);
		glowy= true;
	}
	@Override
	public void entityInit(){
	}
	
	@Override
	public void onUpdate(){
		if(!worldObj.isRemote)
			return;
		glowy=true;
		final int l= (EntitySmokeDisco.r.nextInt(0x30)+0x0a)<<8;
		color= 0xf00000e8 | l | (l<<8);
		super.onUpdate();
	}
	@SideOnly(Side.CLIENT)
	@Override
	protected void operateV(){
		for(int i=0; i<particles.length; i++){
			particleVelocities[i]*= 0.95f;
		}
	}
	@SideOnly(Side.CLIENT)
	@Override
	protected void makeParticles(int num, float force){
		num/=10;
		force/=10f;
		super.makeParticles(num, force);
		for(int i=0; i!=particleVelocities.length; i++)
			particleVelocities[i]*=1f;
		for(int i=0; i!=particleVelocities2.length; i++)
			particleVelocities2[i]*=2f;
	}
}
