package com.typ1a.common.Missiles;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.typ1a.common.Explosion.ExplosionCustom;
import com.typ1a.common.Robots.Ballistic;
import com.typ1a.common.utils.Vector3;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
//TODO add this where needed and get these loading chunks


public class EntityICBM extends Entity implements IEntityAdditionalSpawnData {
	
	public boolean descending=false;
	public double tx,tz;//target
	public double x0,y0,z0;//silo
	public double vY,vZ,vX;//components of velocity
	public double vmax = 10; //not needed here, was a max speed, i replaced with max acceleration, but still use variable
	public double alpha = 1.51; //This is the angle of launch from the silo, I chose to use this instead of a max speed.
	public double accel = 0.02; //this is the acceleration of the missile during boost phase.
	public double detDist = 0;//this is how far above the ground the ICBM explodes.
	public double Pitch;
	public double Yaw;
	private int health=this.getMaxHealth();
	public final Boolean[] exploded = {false, false};
	public Entity owner;
	
	public EntityICBM(World par1World) {
		super(par1World);
		this.setSize(.5f, 2.0f);
		this.boundingBox.setBounds(-.5, 1, -.5, .5, -1.0, .5);
		this.boundingBox.addCoord(posX, posY, posZ);
	}

	
	public EntityICBM(World p1World, double x, double y, double z, double tx, double tz){
		this(p1World);
		this.setPosition(x, y, z);

		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.tx=tx;
		this.tz=tz;        
		
		double[] dirStuff = this.get(tx, 0, tz);
		vX=dirStuff[0];
		vZ=dirStuff[2];
		vY=dirStuff[1];

		this.motionX = 0.0d;
		this.motionY = 0.0d;
		this.motionZ = 0.0d;

	}
	public void entityInit(){}

	
	//
	public void onUpdate(){	
		
		if(!exploded[0]){
			this.rotationPitch= (float) Math.toDegrees(Math.acos(this.motionY/Math.sqrt(motionX*motionX + motionZ*motionZ+ motionY*motionY)));
			this.rotationYaw = (float) Math.toDegrees(Math.atan2(this.motionX, motionZ));
			this.Pitch= (float) Math.toDegrees(Math.acos(this.motionY/Math.sqrt(motionX*motionX + motionZ*motionZ+ motionY*motionY)));
			this.Yaw = (float) Math.toDegrees(Math.atan2(this.motionX, motionZ));
			this.prevPosX=this.posX;
			this.prevPosY=this.posY;
			this.prevPosZ=this.posZ;
			
			 //have the 500 ticks as longest it can boost for, this limits max range ~Thutmose
			//will probably replace that with a variable later.  I also use Descending lightly, it means not boosting ~Thutmose
			double speed = Math.sqrt(motionY*motionY + motionX*motionX + motionZ*motionZ);
			if((speed >= vmax)||(this.ticksExisted > 500)){ descending = true;}
			
			//enforce acceleration in boost phase
			if(!descending){
				accelerate(accel);
				this.worldObj.spawnParticle("dripLava", this.prevPosX, this.prevPosY, this.prevPosZ, 0.0, 0.0, 0.0);
				this.worldObj.spawnParticle("dripLava", this.prevPosX+Math.random(), this.prevPosY, this.prevPosZ+Math.random(), 0.0, 0.0, 0.0);
				this.worldObj.spawnParticle("dripLava", this.prevPosX+Math.random(), this.prevPosY, this.prevPosZ+Math.random(), 0.0, 0.0, 0.0);
				this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 1.0F, 0.75F);		
			}
			
			//when descending, accelerate due to gravity, that is all.~Thutmose
			if(descending){
				this.motionY+=Ballistic.G;
			}
			
			//Is it on ground or below detonation proximity?
			
			if((this.onGround || detHeight())&&!exploded[1]){
				this.explode();
				System.out.println("making dead");
				this.setDead();
			}
			this.moveEntity(motionX, motionY, motionZ);
		}
		else{
			this.setDead();
		}

	}


	public void explode(){
		ExplosionCustom.doExplosion(new Vector3(this), this.worldObj, 30, 1);
		this.exploded[1] = true;
	}
	//////////////////////////

	public boolean canBeCollidedWith(){return true;}
	
	protected boolean shouldDie(){
		if(this.health<1){
			return true;
		}
		if(this.isCollided)
			return true;
		return false;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound cmpnd) {
		super.readFromNBT(cmpnd);
		descending = cmpnd.getBoolean("descending");
		vX=cmpnd.getDouble("vx");
		vY=cmpnd.getDouble("vy");
		vZ=cmpnd.getDouble("vz");
	}
	
	
	@Override
	public void writeEntityToNBT(NBTTagCompound cmpnd) {
		super.writeToNBT(cmpnd);
		cmpnd.setBoolean("descending", descending);
		cmpnd.setDouble("vx", vX);
		cmpnd.setDouble("vy", vY);
		cmpnd.setDouble("vz", vZ);
	}	
	
	
	
	
	@Override
	public boolean attackEntityFrom(DamageSource src, float a){
		//TODO shield and armor
		this.health-=a;
		//TODO hurt sfx
		return true;
	}
	public int getMaxHealth() {return 20;} //lowering this drastically increases chance of interception if bullets didn't instakill them.
	@Override 
	public void setDead(){
		super.setDead();}
	
	public double[] get(double tx, double ty, double tz){ //this is modified from ballistics code.
		double g = -0.04;
		double		
		dx= tx - this.posX,
		dz= tz - this.posZ;
		
		double yaw= -Math.atan2(-dz, -dx);
		double pitch= alpha;	
		//
		double hdist = Math.sqrt(dx*dx + dz*dz);
		
		double dy= 0; //will make adjustments here later. ~Thutmose
		
		double v = Math.sqrt(-hdist*hdist*g/(hdist*Math.sin(2*pitch)+2*dy*Math.cos(pitch)*Math.cos(pitch)));											  
		vmax = v;
		//
		double cosp=Math.cos(pitch); 
		double vx=v* Math.cos(yaw)*cosp;
		double vz=v* Math.sin(yaw)*cosp;
		double vy=v* Math.sin(pitch);
		return new double[]{vx,vy,vz, yaw,pitch};
	}



	public void writeSpawnData(ByteBuf data){
		if(this.owner!=null)
			data.writeInt(this.owner.getEntityId());
	}
	public void readSpawnData(ByteBuf data){
		if(this.ticksExisted<2){
			try{
				this.owner=worldObj.getEntityByID(data.readInt());
			}catch(Exception e){}
		}
	}
	
	
	
	//This accelerates the missile along its intended direction of motion by the amount specified.
	private void accelerate(double amag){
		double[] velocity = {vX,vY,vZ};
		double[] vhat = {0,0,0};
		double vmag = 0;
		for(int i=0; i < 3; i = i+1){vmag = vmag + velocity[i]*velocity[i];}
		vmag = Math.sqrt(vmag);
		for(int i=0; i < 3; i = i+1){vhat[i]=velocity[i]/vmag;}
		//here we reuse the velocity vector, reassigning it to be missile velocity instead of goal velocity
		velocity[0] = this.motionX; 
		velocity[1] = this.motionY; 
		velocity[2] = this.motionZ;
		for(int i=0; i < 3; i = i+1){velocity[i] += amag * vhat[i];}//acceleration applied
		vmag = 0;
		for(int i=0; i < 3; i = i+1){vmag = vmag + velocity[i]*velocity[i];}
		vmag = Math.sqrt(vmag);
		//new velocities are assigned
		this.rotationPitch= (float) Math.toDegrees(Math.acos(velocity[1]/vmag));
		this.rotationYaw = (float) Math.toDegrees(Math.atan2(velocity[0], velocity[2]));
		this.Pitch= (float) Math.toDegrees(Math.acos(velocity[1]/vmag));
		this.Yaw = (float) Math.toDegrees(Math.atan2(velocity[0], velocity[2]));
		this.motionX = velocity[0];
		this.motionY = velocity[1];
		this.motionZ = velocity[2];
	}
	
	private boolean detHeight(){
		boolean detHeight = false;
		double[] velocity = {this.motionX,this.motionY,this.motionZ};
		double[] vhat = {0,0,0};
		double vmag = 0;
		for(int i=0; i < 3; i = i+1){vmag = vmag + velocity[i]*velocity[i];}
		vmag = Math.sqrt(vmag);
		for(int i=0; i < 3; i = i+1){vhat[i]=velocity[i]/vmag;}
		for(double i=0;i<detDist;i+=0.1){
			int testX = (int) (this.posX + this.motionX),
				testY = (int) (this.posY + this.motionY),
				testZ = (int) (this.posZ + this.motionZ);
			int id = Block.getIdFromBlock(this.worldObj.getBlock(testX,testY,testZ));
			if(id != 0){detHeight = true;}
		}
		return detHeight;
	}

}