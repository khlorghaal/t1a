package com.typ1a.common.BlocksItems;

import java.util.Hashtable;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

import com.typ1a.common.Ticker;

public class TickerAgrav extends Ticker {

	private double lift=0.075;
	private double vx0, vy0, vz0;
	/**retry activating next time in the air, used when player tries using while on ground*/
	private boolean groundedLastTick;
	private static Map<Entity, Ticker> map = new Hashtable<Entity, Ticker>();
	private static Map<Entity, Ticker> mapServer = new Hashtable<Entity, Ticker>();

	public Entity user;

	private TickerAgrav(Entity entity){
		super();
		this.user=entity;

		vx0= user.motionX;
		vy0= user.motionY;
		vz0= user.motionZ;

		if(user.onGround)
			groundedLastTick=true;
	}
	@Override
	protected void onUpdate() {
		user.fallDistance=0;
		//is client only except for fall damage (HERESY)
		if(!user.worldObj.isRemote)
			return;

		if(user==null){
			this.kill();
			return;
		}

		if(!user.onGround){ //dont activate unless in air

			if(groundedLastTick){
				vx0= user.motionX;
				vy0= user.motionY;
				vz0= user.motionZ;
				groundedLastTick=false;
			}

			if(user instanceof EntityLiving)//dont flail legs
				((EntityLiving)user).limbSwing= 0;

			if(user.isCollidedHorizontally){
				//stop when hitting a wall
				vx0=0;
				vy0=0;
				vz0=0;
			}

			user.setVelocity(vx0, vy0, vz0);
			user.fallDistance= 0;
			//TODO drag?
		}
		else
			groundedLastTick=true;
	}

	@Override
	public void kill(){
		super.kill();
		if(!user.worldObj.isRemote)
			mapServer.remove(user);
		else
			map.remove(user);
	}

	public static void start(EntityPlayer player) {
		if(!player.worldObj.isRemote){//only to reinterpret fall damage
			if(!mapServer.containsKey(player)){
				mapServer.put(player, new TickerAgrav(player));
			}
		}
		
		else if(!map.containsKey(player)){
			map.put(player, new TickerAgrav(player));
		}
	}
	public static void stop(EntityPlayer player) {
		if(!player.worldObj.isRemote){
			if(mapServer.containsKey(player)){
				mapServer.get(player).kill();
				mapServer.remove(player);
			}
		}
		
		else if(map.containsKey(player)){
			map.get(player).kill();
			map.remove(player);
		}
	}

}
