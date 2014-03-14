package com.typ1a.common.Missiles;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.typ1a.common.Maths;
import com.typ1a.common.Projectiles.Barrel;
import com.typ1a.common.Projectiles.EntityBullet;
import com.typ1a.common.Robots.Ballistic;
import com.typ1a.common.utils.Vector3;

public class EntityMissile extends EntityBullet{

//	/**0 KE, 1 EXPL, 2 FLAK*/
//	public int type;

	static final int updateFreq=4;
	Maths aimer;

	//kinematics
	public static final double dturn= updateFreq*PI/(25.00000), TAU= 2* PI;
	public double aConst=.045,
			speed= 1.80, 
			drag= 1 - aConst/speed;
	public double dx,dy,dz,dyaw,dpitch,dist=99,prevdist=999,pitch=PI/2,yaw=0,pitchAim=0,yawAim=0, a=aConst;
	//max speed when a <= v(1-drag); thus max speed = a/(1-drag) 
	public boolean bloop=true;

	//gfx
	public float roll=0;

	public Entity owner, target;

	public EntityMissile(World par1World) {
		super(par1World);
		this.setSize(.5f, .5f);
	}

	public EntityMissile(World p1World, Entity owner, Entity target, Barrel barrel){
		super(barrel, 0.1, 0, null, 0, 1);

		this.target=target;
		this.owner=owner;

		barrel.calculatePosAndLook();
		final Vector3 m= barrel.muzzleEndTransformed;
		this.setPos(m.x, m.y, m.z);
		
		this.v[0] = barrel.lookdir.x;
		this.v[1] = barrel.lookdir.y;
		this.v[2] = barrel.lookdir.z;
		this.v[0]+= owner.motionX;
		this.v[1]+= owner.motionY;
		this.v[2]+= owner.motionZ;
	}
	@Override
	public void deviate(double d){}
	public void entityInit(){}

	/////////////////////////////////////////////////
	@Override
	public void onUpdate(){
		super.onUpdate();
		
//		this.rotationYaw= (float) Math.atan2(v[2], v[0]);
//		this.rotationPitch= (float) Math.asin(v[1]);
		
		this.prevdist = this.dist;

		aim();	

		a=aConst;
		a*= 1- ( abs(yaw-yawAim) + abs(pitch-pitchAim) )/TAU; //decreases thrust when facing away from target, using arithmetic mean of pitch & yaw; /(...)
		this.roll+=a*900;

		final double cosp= cos(pitch);
		v[1]+=a*sin(pitch);
		v[0]+=a*cos(yaw)*cosp;
		v[2]+=a*sin(yaw)*cosp;
		v[1]*=drag;
		v[0]*=drag;
		v[2]*=drag;
		v[1]-=Ballistic.G;
		moveEntity();

		if(shouldExplode())
			this.explode();
	}
	protected boolean shouldExplode(){
		if(this.isCollided)
			return true;
		return false;
	}
	@Override
	protected void onFirstCollision(){
		this.explode();
	}
	public void explode(){
//		ExplosionCustom.doExplosion(this.worldObj, this.posX,this.posY,this.posZ, 20, true);
		worldObj.createExplosion(owner, posX, posY, posZ, 4, true);
		switch(this.btype){
		case 0://KE

			//sfx donk
			break;
		case 1://EXPL

			//sfx boom
			break;
		case 2://FLAK

			//sfx pwap
			break;
		default:
			System.err.println("invalid missile type?"+'\n'+this.toString());
		}
		this.setDead();
	}

	protected void aim(){
		if(target==null){
			//this.setDead();
			return;
		}
		dx= target.posX - this.posX;
		dy= target.posY - this.posY;
		dz= target.posZ - this.posZ;
		yawAim=Math.atan2(dx,dz); 
		pitchAim=Math.atan2(sqrt( dx*dx + dz*dz ), dy);


		Maths.angleShortestPathIncremented(yaw, yawAim, dturn);
		Maths.angleShortestPathIncremented(pitch, pitchAim, dturn);

	}
	//////////////////////////
	protected void readEntityFromNBT(NBTTagCompound var1) {
		this.btype= var1.getByte("Type");

		if(this instanceof EntityWGM) return;
		this.target= this.worldObj.getEntityByID(
				var1.getInteger("TargetID"));
	}
	protected void writeEntityToNBT(NBTTagCompound var1) {
		var1.setByte("Type", (byte)this.btype);

		if(this instanceof EntityWGM) return;
		var1.setInteger("TargetID", this.target.getEntityId());
	}
}
