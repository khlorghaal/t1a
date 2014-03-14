package com.typ1a.common.BlocksItems;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityStoneBurner extends Entity
{
    /** How long the fuse is */
    public int fuse;

    public EntityStoneBurner(World par1World)
    {
        super(par1World);
        this.fuse = 0;
        this.preventEntitySpawning = true;
        this.setSize(0.5F, 0.5F);
        this.yOffset = this.height / 2.0F;
    }

    public EntityStoneBurner(World par1World, double par2, double par4, double par6)
    {
        this(par1World);
        this.setPosition(par2, par4, par6);
        float var8 = (float)(Math.random() * Math.PI * 2.0D);
        this.motionX = 0.00000000d;
        this.motionY = 1.8D;
        this.motionZ = 0.00000000d;
        this.fuse = 450;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
    }

    protected void entityInit() {}

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public int count1 = 0;
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.04D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionY *= 0.980D;

        if (this.onGround)
        {
            this.motionY *= -0.5D;
        }
        
        count1++;
        if ((this.fuse-- <=430) && (this.count1 % 6)==0)
        {
            this.explode();
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
        else if (this.fuse <= 0)
        {
            this.setDead();
        }
    }

    private void explode()
    {
        float var1 = 2.0F;
        this.worldObj.createExplosion((Entity)this, this.posX, this.posY, this.posZ, 2.5f, true);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setByte("Fuse", (byte)this.fuse);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.fuse = par1NBTTagCompound.getByte("Fuse");
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }
}
