package com.typ1a.common.Robots;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

/**The goal of this class is to emulate a player - true AI.
 * Thus it should never directly control setting position/motion, 
 * only direction to move towards and with what acceleration, 
 * where the Entity limits accelerations */
public abstract class MobileAI {

	/**Is injured || low on ammo || low on energy || incoming gank*/
	protected boolean isJimmiesRustled= false;

	protected boolean isFearless=true;
	
	private Entity self;
	public Entity getSelf(){return self;}

	protected double homex,homey,homez;

	protected double destx,desty,destz;

	protected Entity target;
	
	public double preferredSpeed= 0xff;
	
	public MobileAI(Entity self){
		this.self= self;

		//TODO NBT
		homex= self.posX;
		homey= self.posY;
		homez= self.posZ;
		destx= homex;
		desty= homey;
		destz= homez;
	}

	public void update(){
		if( (self.ticksExisted & 0x10) == 0){
			checkJimmies();
			lookForTask();
		}
		doTask();
	}

	public abstract void doTask();
	public abstract void lookForTask();

	public void checkJimmies(){
		isJimmiesRustled= isGankLikely();//TODO
	}

	/**returns here when nothing interesting is happening or when low on something*/
	public final void setHome(double x, double y, double z){

	}
	public final void setDestination(double x, double y, double z){

	}
	/**meant for attacking*/
	public final void setDestination(double x, double y, double z, double yaw, double pitch){

	}

	public void setTargetToFollow(Entity t){
		
	}
	
	public List<Entity> getEntitiesVisible(){
		final Entity s= getSelf();
		final List<Entity> list= s.worldObj.getEntitiesWithinAABBExcludingEntity(s
				, AxisAlignedBB.getBoundingBox(-200, -200, -200, 200, 200, 200).offset(s.posX, s.posY, s.posZ) );

		return list;
	}

	public List<Entity> getHostilesVisible(){
		final List<Entity> list= getEntitiesVisible();
		
		return list;
	}

	public List<Entity> getAlliesVisible(){
		final List<Entity> list= getEntitiesVisible();
		
		return list;
	}

	protected boolean isGankLikely() {
		return !isFearless && false;
	}
}
