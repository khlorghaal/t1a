package com.typ1a.common.spacey;

import io.netty.buffer.ByteBuf;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntitySatBeam extends Entity implements IEntityAdditionalSpawnData{

	public double power;
	
	public EntitySatBeam(World par1World) {
		super(par1World);
		this.ignoreFrustumCheck= true;
	}

	public EntitySatBeam(World world, double x, double z){
		this(world);
		
		this.posX= x;
		findy();
		this.posZ= z;
		prevPosX=0;
		prevPosZ=0;
		
		world.spawnEntityInWorld(this);
	}
	@Override
	protected void entityInit() {}

	
	@Override
	public void onUpdate(){
		if(ticksExisted>80)
			kerplowie(1);
		
		if(((int)posX)!=((int)prevPosX) || ((int)posZ)!=((int)prevPosZ)){
			findy();
		}
		
		Random r= new Random();
		posX+= r.nextGaussian()/2;
		posY= r.nextGaussian()/2;
		posZ+= r.nextGaussian()/2;
		setPosition(posX, posY, posZ);
		if((ticksExisted&4)==4)
			worldObj.setBlock((int)posX, (int)posY, (int)posZ, Blocks.fire);
	}
	public void findy(){
		posY=256;
		while(posY>0 || worldObj.getBlock((int)posX, (int)posY, (int)posZ)==Blocks.air){
			posY--;
		}
	}
	
	public void kerplowie(double power){
		System.out.println("bang");
		setDead();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double par1){
		return true;
	}
	
	
	public void writeSpawnData(ByteBuf data){
		data.writeDouble(power);
	}

	@Override
	public void readSpawnData(ByteBuf data){
		power= data.readDouble();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound){}
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound){}
}
