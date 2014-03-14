package com.typ1a.common.Explosion;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.typ1a.common.Projectiles.EntityProjectile;
import com.typ1a.common.utils.Vector3;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntitySmoke extends Entity implements IEntityAdditionalSpawnData{
	public int lifespan;
	public float strength;
	public int color=COLORDEFAULT;
	public boolean glowy=false;
	public static final int COLORDEFAULT=0xd0d0d0e8;

	public static Random r= new Random();

	/**xn,yn,zn....vxn,vyn,vzn*/
	public float[] particles, particleVelocities, particleVelocities2;


	public EntitySmoke(World world){
    	super(world);
		this.ignoreFrustumCheck=true;
	}
	public EntitySmoke(World world, double x, double y, double z, int lifespan, float strength){
		this(world);

		this.posX=Math.floor(x)+0.5;
		this.posY=Math.floor(y)+0.5;
		this.posZ=Math.floor(z)+0.5;
		this.lifespan= lifespan;
		this.strength= strength;
		
		worldObj.spawnEntityInWorld(this);
	}
	public EntitySmoke(World world, double x, double y, double z
			, int lifespan, float strength, int color){
		this(world, x, y, z, lifespan, strength);
		this.color= color;
	}
	
	@Override
	public boolean isInRangeToRenderDist(double par1){
		return true;
	}

	@SideOnly(Side.CLIENT)
	protected void makeParticles(int num, float force){
		while(num%3!=0)//for the 3 different COLORDEFAULTs
			num++;		//array is divided into 3rds
		
		particles= new float[num*3];
		particleVelocities= new float[num*3];
		particleVelocities2= new float[num*3];

		for(int i=0; i<num*3; i+=3){
			float gau= ((float)r.nextGaussian());
			Vector3 vec= new Vector3(r.nextGaussian(), r.nextGaussian()*Math.PI, r.nextGaussian()*Math.PI);
			vec.x= Math.sqrt(-vec.x+1)/Math.sqrt(1);//trim outliers
			vec= vec.toCartesian().scalarMult(force);

			particleVelocities[i]= (float) vec.x;
			particleVelocities[i+1]= (float) vec.y;
			particleVelocities[i+2]= (float) vec.z;
		}
		//v1 already is populated with randoms, 
		//so just offset it since the two arent correlated
		//must offset by more than 3 otherwise there will be a noticeable swizzle
		particleVelocities2= Arrays.copyOf(Arrays.copyOfRange(particleVelocities, 4, num*3), num*3);
		for(int i=0; i<particleVelocities2.length; i++)
			particleVelocities2[i]/=24;
		
		/**deflection 0 none, 1 positive, -1 negative*/
		byte defx,defy,defz;

		if(EntityProjectile.getBlockBulletEffect(
				worldObj.getBlock((int)posX, (int)posY-1, (int)posZ))==0)
				defy=1;
		if(EntityProjectile.getBlockBulletEffect(
				worldObj.getBlock((int)posX, (int)posY+1, (int)posZ))==0)
				defy=-1;
		if(EntityProjectile.getBlockBulletEffect(
				worldObj.getBlock((int)posX, (int)posY, (int)posZ-1))==0)
				defz=1;
		if(EntityProjectile.getBlockBulletEffect(
				worldObj.getBlock((int)posX, (int)posY, (int)posZ+1))==0)
				defz=1;
		if(EntityProjectile.getBlockBulletEffect(
				worldObj.getBlock((int)posX-1, (int)posY, (int)posZ))==0)
				defx=1;
		if(EntityProjectile.getBlockBulletEffect(
				worldObj.getBlock((int)posX+1, (int)posY, (int)posZ))==0)
				defz=-1;
		//0x8000 0000
	}
	@SideOnly(Side.CLIENT)
	protected void spreadParticles(){

		operateV();

		for(int i=0; i<particles.length; i+=3){
			particles[i]+= particleVelocities[i] + particleVelocities2[i]; //+T1A.wind[0]/64;
			particles[i+1]+= particleVelocities[i+1] + particleVelocities2[i+1] +0.012;
			particles[i+2]+= particleVelocities[i+2] + particleVelocities2[i+2]; //T1A.wind[2]/64;
		}
		
		if(ticksExisted> this.lifespan- 0xff/2 && (color&0xff)>0)
			color-=2;//fade
	}
	@SideOnly(Side.CLIENT)
	protected void operateV(){
		for(int i=0; i<particles.length; i++){
			final float x= particleVelocities[i];
			particleVelocities[i]= 0.93f*(x);
		}
	}

	@Override
	public void onUpdate(){
		if(worldObj.isRemote){
			spreadParticles();
		}
		if(this.ticksExisted>this.lifespan)
			this.setDead();
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.ticksExisted= nbttagcompound.getInteger("t");
		this.lifespan= nbttagcompound.getInteger("life");
		this.strength= nbttagcompound.getFloat("stronk");
		this.color= nbttagcompound.getInteger("col");
	}
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("t", ticksExisted);
		nbttagcompound.setInteger("life", lifespan);
		nbttagcompound.setFloat("stronk", strength);		
		nbttagcompound.setInteger("col", color);
	}

	@Override
	public void writeSpawnData(ByteBuf data) {
		data.writeInt(lifespan);
		data.writeInt(ticksExisted);
		data.writeFloat(strength);
		data.writeInt(color);
		data.writeBoolean(glowy);
	}
	@Override
	public void readSpawnData(ByteBuf data) {
		this.lifespan= data.readInt();
		this.ticksExisted= data.readInt();
		this.strength= data.readFloat();
		this.color= data.readInt();
		this.glowy= data.readBoolean();

		makeParticles((int)(strength*150), strength/35);
	}
}
