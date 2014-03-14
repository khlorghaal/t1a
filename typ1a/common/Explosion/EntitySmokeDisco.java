package com.typ1a.common.Explosion;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntitySmokeDisco extends EntitySmoke {

	public EntitySmokeDisco(World world) {
		super(world);
		glowy= true;
	}
	
	public EntitySmokeDisco(World world, double x, double y, double z,
			int lifespan, float strength, int color) {
		super(world, x, y, z, lifespan, strength, color);
		glowy= true;
	}
	@Override
	public void entityInit(){
	}
	final static Random r= new Random();
	@Override
	public void onUpdate(){
		if(!worldObj.isRemote)
			return;
		glowy=true;
		color= r.nextInt() |0xa0;
		System.out.println(color);
		super.onUpdate();
	}
	@SideOnly(Side.CLIENT)
	@Override
	protected void operateV(){
		for(int i=0; i<particles.length; i++){
			particleVelocities2[i]+= 0.0001f;
			final float theta= particleVelocities2[i]*12000f;
			particleVelocities[i]= (MathHelper.sin( theta ))/1;
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
			particleVelocities2[i]*=54f;
	}
}
