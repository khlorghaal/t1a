package com.typ1a.common.Equipment;

import java.util.Random;

import com.typ1a.client.rendery.turret.concrete.ModelGunTurret;
import com.typ1a.common.Equipment.EquipmentSystem.Subunit;
import com.typ1a.common.Projectiles.Barrel;
import com.typ1a.common.Projectiles.EntityBullet;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public abstract class Gun implements Subunit{

	/**cooldown is actual time remaining, rof is constant*/
	public int cooldown=0, rof;
	public abstract double getMuzzleVelocity();
	protected Random r;
	public abstract double getDeviation();
	private boolean active=false;
	public boolean hasShotThisTick=false;
	public final Barrel barrel;
	protected final AmmoManager ammo;

	public boolean isSideMounted=false; 
	private static ModelGunTurret model;
	public ModelGunTurret getModel(){return model;}

	public Gun(int rof, Barrel barrel, Random r, AmmoManager ammo, ModelGunTurret model){
		this.rof=rof;
		this.barrel=barrel;
		this.r= r;	
		this.ammo=ammo;
		
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT)
			this.model=model;
	}

	public int update(){
		if(barrel.aimer==null){
			active=false; return 0;
		}
		hasShotThisTick=false;
		if(ammo.getShotsRemaining()>0 && cooldown--==0){//shouldnt have to use inequality
			cooldown=rof;
			if(active){
				hasShotThisTick=true;
				shoot();
				//this.ammo.popShot(); leave this up to shoot()
			}
		}
		return 0;//Ammo takes care of energy consumption
	}

	protected void shoot(){
		new EntityBullet(this.barrel, this.getMuzzleVelocity(), this.getDeviation(), r, 4, this.ammo.popShot());
	}

	public void setFiring(boolean state){
		this.active=state;
	}
}
