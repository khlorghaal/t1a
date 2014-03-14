package com.typ1a.common.Projectiles;

import io.netty.buffer.ByteBuf;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import com.typ1a.common.Explosion.EntitySmoke;
import com.typ1a.common.Explosion.EntitySmokeDisco;
import com.typ1a.common.Explosion.EntitySmokeFlare;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityGrenade extends EntityBullet {

	public int color=0;
	private static int i=0;
	public static final int FRAG=i++, CONCUSSION=i++,  CONCUSSIONSHOTGUN= i++, SMOKES=i++, SMOKEL=i++, FOAM=i++, DISCO=i++, FLARE=i++;
	public boolean isVolatile(){return btype==SMOKEL || btype==CONCUSSIONSHOTGUN || btype==DISCO;}
	
	private static final Random dummyrandom= new Random();
	public EntityGrenade(World world){
		super(world);
	}
	public EntityGrenade(Barrel barrel, double s, double deviation, int type, int color) {
		super(barrel, s, deviation, dummyrandom, 0, type);
	}

	@Override
	public void onUpdate(){
		super.onUpdate();
		if(ticksExisted>60 && !isVolatile()){
			this.explode();
		}
	}

	@Override
	protected void checkHit(){
		if(isVolatile())
			super.checkHit();
	}
	@Override
	protected void hit(Entity e){
		this.onFirstCollision();
	}
	@Override
	protected void onFirstCollision(){
		if(isVolatile())
			this.explode();
	}

	@Override
	protected double penetration(float resist, double speed){
		return 0.65;
	}

	public void explode(){
		posY= Math.floor(posY+0.05)+0.5;

		if(!worldObj.isRemote){
			if(btype==CONCUSSIONSHOTGUN)
				worldObj.createExplosion(owner, posX, posY, posZ, 2f, true);
			else if(btype==CONCUSSION)
				worldObj.createExplosion(owner, posX, posY, posZ, 3f, true);
			else if(btype==FRAG)
				worldObj.createExplosion(owner, posX, posY, posZ, 4.5f, false);
			else if(btype==SMOKES)
				new EntitySmoke(worldObj, posX, posY, posZ, 500, 9);
			else if(btype==SMOKEL)
				new EntitySmoke(worldObj, posX, posY, posZ, 290, 16);
			else if(btype==FOAM)
			{}
			else if(btype==DISCO){//3 because multicolored
				new EntitySmokeDisco(worldObj, posX, posY, posZ, 800, 7, 0);
				new EntitySmokeDisco(worldObj, posX, posY, posZ, 800, 7, 0);
				new EntitySmokeDisco(worldObj, posX, posY, posZ, 800, 7, 0);
			}
			else if(btype==FLARE)
				new EntitySmokeFlare(worldObj, posX, posY, posZ, 800, 7, 0);
				
			if(btype==SMOKEL || btype==SMOKES)
				worldObj.playSoundAtEntity(this, "t1a:smoke", 3.5f, 1);
		}
		this.setDead();
	}

//	public void writeSpawnData(ByteBuf data){
//		data.writeByte(btype);
//		data.writeDouble(v[0]);
//		data.writeDouble(v[1]);
//		data.writeDouble(v[2]);
//
//		if(this.owner!=null)
//			data.writeInt(this.owner.getu);
//		else
//			data.writeInt(-1);
//	}
//	public void readSpawnData(ByteBuf data){
//		btype= data.readByte();
//		v[0]= data.readDouble();
//		v[1]= data.readDouble();
//		v[2]= data.readDouble();
//
//		final int id= data.readInt();
//		if(id!=-1)
//			this.owner=worldObj.getEntityByID(id);
//	}
}
