package com.typ1a.common.Robots;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.typ1a.common.IGuiable;
import com.typ1a.common.Kinematics;
import com.typ1a.common.T1A;
import com.typ1a.common.T1AGUIHandler;
import com.typ1a.common.Equipment.EquipmentFacade;
import com.typ1a.common.Equipment.Turrets.ITurretAcceptor;
import com.typ1a.common.Equipment.Turrets.MachineGun;
import com.typ1a.common.Equipment.Turrets.TurretMount;
import com.typ1a.common.Projectiles.Barrel;
import com.typ1a.common.Projectiles.EntityBullet;
import com.typ1a.common.utils.RayHelper;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntitySentry extends EntityRobot 
implements IEntityAdditionalSpawnData, ITurretAcceptor, IGuiable {

	protected EquipmentFacade equipment;
	@Override public EquipmentFacade getEquipmentFacade(){return equipment;}

	public EntityPlayer owner;
	public static Map<String, EntitySentry> ownerMap = new Hashtable<String, EntitySentry>();
	//TODO ownership

	public float recoil=0;
	public static  float recoilDistance=.3f, recoilMultiplier=0.6f;

	public int restYaw=0, restPitch=0;

	//Shooty stuff
	public static final double range=30.0,rangesq=range*range;

	public Entity target=null;
	public boolean nbsi=false;

	private int health=this.getMaxHealth();

	private Random r;
	Ballistic ballistic;
	AIAimer aiAimer;

	public EntitySentry(World par1World){
		super(par1World);
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
	}

	public EntitySentry(EntityPlayer owner, double par2, double par4, double par6){
		this(owner.worldObj);
		this.owner= owner;
		this.setPosition(par2+.5, par4, par6+.5);
		this.motionX = 0.0d;
		this.motionY = 0.0d;
		this.motionZ = 0.0d;
		this.prevPosX = par2;
		this.prevPosY = par4;
		this.prevPosZ = par6;
	}
	@Override
	public void entityInit(){
		equipment= new EquipmentFacade(0,0,2, 100, 100);

		//this.dataWatcher.addObject(18, new Integer(this.health));
		//this.dataWatcher.addObject(0, new Float(this.gun.barrel.getYaw()));
		//this.dataWatcher.addObject(1, new Float(this.gun.barrel.getPitch()));
	}
	//////////////////////
	public void onUpdate(){
		//this.dataWatcher.updateObject(18, Integer.valueOf(health));
		if(this.health<1)
			this.setDead();
		this.motionX=this.motionY=this.motionZ=0;

		if(this.ticksExisted % 40 ==0)
			look(); //only look every 2s to save crunch

		System.out.println(target);
		if(this.aiAimer!=null){
			if(this.target!=null){
				this.aiAimer.onUpdate();
			}
			else{
				this.aiAimer.gun.setFiring(false);
			}
		}
		//this.gun.onUpdate();

		recoil*= recoilMultiplier;
		//if(this.gun.hasShotThisTick && this.worldObj.isRemote)
		recoil=recoilDistance;
	}
	///////////////////////////
	public void look(){
		List<Entity> blips = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBox(posX-range,posY-range,posZ-range,posX+range,posY+range,posZ+range));
		List<Entity> invalidTargets= new ArrayList<Entity>();
		//remove invalid targets
		for(Entity blip : blips.toArray(new Entity[]{})){
			//TODO FoF
			if( (blip instanceof EntityBullet)
					//|| blip==owner 
					||  (blip instanceof EntityItem) || (blip instanceof EntityXPOrb)
					|| ( (blip instanceof EntityTameable) && ((EntityTameable)blip).getOwner()==owner )
					|| this.rangesq > (blip.posX*blip.posX + blip.posY*blip.posY + blip.posZ*blip.posZ) 
					//|| !LinearAlgebra.isVisibleEntityFromEntity(this, blip, 0, true))
					)
				blips.remove(blip);
		}

		//from blips, select the closest
		Entity blip= RayHelper.getClosestEntity(blips, this.posX, this.posY, this.posZ);

		System.out.println(this.getEntityId()+" is targeting "+blip);
		//TODO prioritization based on request and range
		if(blip!=null){
			if(blip!=this.target){
				this.ballistic= new Ballistic(this, blip, 1.05);
				this.aiAimer= new AIAimer(this.ballistic, new MachineGun(new Barrel(this, null, 1.2f, 0, 0.8f, 0), r), 0.01);
			}
			this.target=blip;
			return;
		}
		//TODO data watch
		this.target=null;
	}
	//////////////////////////////


	@Override
	public boolean attackEntityFrom(DamageSource src, float a){
		this.health-= this.equipment.absorbDamage((int) a);
		System.out.println("ow");
		if(this.health<1){
			this.setDead();
			if(this.health< this.getMaxHealth() / -2)//if it gets really damaged 
				this.setDead();//blow up immediately
		}
		return true;
	}
	public int getMaxHealth() {return 20;}
	@Override
	public void setDead(){
		//TODO asplode
		super.setDead();}

	protected String getHurtSound(){return "mob.cow.hurt";}
	protected String getDeathSound(){return "mob.cow.hurt";}

	@SideOnly(Side.CLIENT)
	public float getShadowSize(){return 0.0F;}

	@Override
	public boolean interactFirst(EntityPlayer player){
		T1A.setGuiableAccessedBy(player, this);
		player.openGui(T1A.instance, T1AGUIHandler.SENTRY, this.worldObj, this.hashCode(),0,0);
		return true;
	}


	@Override
	public boolean canPlayerAccess(EntityPlayer player) {
		return player==owner;
	}

	@Override
	public int[] getStates() {
		System.out.println("getting entity states");
		return new int[]{restYaw/45, restPitch, 0,0,0,0,0,0, 0};
	}

	@Override
	public void setStates(int[] states) {
		System.out.println("setting entity states " + this);
		if(states.length!= 9)//may happen normally
			return;
		restYaw= states[0];
		restPitch= states[1];
		System.out.println(restYaw);
		if(worldObj.isRemote)
			System.out.println("BHWAWK");
	}

	@Override
	public boolean canBeCollidedWith(){return true;}
	@Override
	public boolean canBePushed(){return false;}
	@Override
	public AxisAlignedBB getBoundingBox(){return this.boundingBox;}
	@Override
	public AxisAlignedBB getCollisionBox(Entity entity){return this.getBoundingBox();}

	public boolean isUseableByPlayer(EntityPlayer par1player){
		//TODO coporate access
		if(ownerMap.get(par1player.getUniqueID()) == this)
			return true;
		return false;
	}

	//Inventory stuff

	///////////

	public void readEntityFromNBT(NBTTagCompound var1) {
		this.owner=(EntityPlayer) this.worldObj.getEntityByID(var1.getInteger("owner"));


	}
	public void writeEntityToNBT(NBTTagCompound cmpnd) {
		if(owner!=null)
			cmpnd.setInteger("owner", owner.getEntityId());
		else
			cmpnd.setInteger("owner", -1);
	}

	@Override
	public void writeSpawnData(ByteBuf data) {
		if(this.owner!=null)
			data.writeInt(owner.getEntityId());

	}

	@Override
	public void readSpawnData(ByteBuf data) {
		try{
			this.owner= (EntityPlayer) this.worldObj.getEntityByID(data.readInt());
		}
		catch(Exception e){}
	}

	@Override
	public TurretMount getTurretMount(int indx) {
		return null;
	}

	@Override
	public Kinematics getKinematics() {
		// TODO Auto-generated method stub
		return null;
	}
}
