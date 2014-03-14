package com.typ1a.common;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;

public class Satellite extends Ticker{

	//The list of all satellites
	public static List<Satellite> sats = new Vector<Satellite>();
	public static Map<UUID, Satellite> ownerMap= new Hashtable();

	public String owner;
	public boolean active;
	int power, tMinus, times, delay;
	float dispersion;
	public int tx, tz, charge;
	public int amount=1;//simulated amount of satellites used for shooting down sats and determining max energy


	public void onUpdate(){if(this.active){

		if(this.tMinus==0){
			this.tMinus=delay-1;//-1 because 0 is (firing every tick) = 1 tick delay

			strike(this.tx+(int)(dispersion*(Math.random()-.5)), this.tz+(int)(dispersion*(Math.random()-.5)), this.power);}
		else
			this.tMinus--;

		if(this.times==0)
			this.active=false;

	}}
	//////////////////////////
	public void strike(int tx, int tz, float power){ if(canShoot()){
		this.charge-=this.power;
		this.times--;
		//TODO render and asplode
	}}
	/**Use -1 times for nonstop bombing untill.
	@param Delay of 0 is once per tick.
	@param dispersion is currently a direct multiplier*/
	public void bombard(int tx, int tz, int power, float dispersion, int delay, int times){
		this.active=true;
		this.tx=tx; this.tz=tz; this.power=power; this.dispersion=dispersion; this.delay=delay; this.times=times;
		if(delay<4)
			delay=4;
		this.delay=delay;
		if(dispersion<1.5)
			dispersion=1.5f;
	}


	public boolean canShoot(){
		return this.charge>=this.power;}
	/////////////////////////
	public static Satellite get(EntityPlayer who){
		//Access checking is implicitly done with the packet handling
		return ownerMap.get(who.getUniqueID());
	}	
	
	public static void save(String file){
		//TODO stuff
	}
	public static void load(String file){
		//TODO stuff
	}

	public Satellite(EntityPlayer owner){
		super();
		ownerMap.put(owner.getUniqueID(), this);
	}
	public void kill(){
		this.kill();
	}
}
