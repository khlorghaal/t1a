package com.typ1a.common.spacey;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.typ1a.common.Satellite;
import com.typ1a.common.T1A;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityODP extends Entity{

	public EntityPlayer owner;
        
    public EntityODP(World par1World)
    {
        super(par1World);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
    }

    public EntityODP(World par1World, double par2, double par4, double par6)
    {
        this(par1World);
        this.setPosition(par2, par4, par6);
        this.motionX = 0.0d;
        this.motionY = 0.10d;
        this.motionZ = 0.0d;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
    }


    protected void entityInit() {}

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
        this.motionX *= 0.98D;
        this.motionY *= 0.98D;
        this.motionZ *= 0.98D;
        
        if (this.onGround)
        {
            this.motionX *= 0.9D;
            this.motionZ *= 0.9D;
        }
        else this.motionY -= 0.04D;
        
        if(this.posY > T1A.SPACE && this.Deploy())this.setDead();
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    
    public boolean Deploy(){
    	new Satellite(owner);
    	return true;
    }


    /**Returns true if other Entities should be prevented from moving through this Entity.*/
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
    
    
	protected void readEntityFromNBT(NBTTagCompound var1) {/* TODO Auto-generated method stub*/}
	protected void writeEntityToNBT(NBTTagCompound var1) {/* TODO Auto-generated method stub*/}
	
    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

	

	

}
