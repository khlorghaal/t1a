package com.typ1a.common.Vehicles;

import io.netty.buffer.ByteBuf;

import java.util.Hashtable;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.io.ByteArrayDataInput;
import com.typ1a.common.Kinematics;
import com.typ1a.common.T1A;


public abstract class EntityVehicle extends Entity implements IControlled{

	public int health=getMaxHealth();
	private boolean dying=false;
	public int deathTimer=getTimeToDie();
	public int getTimeToDie(){return 100;}


	public Kinematics kin= new Kinematics(getMass(), getDrag(), getDragAngular());
	public Kinematics getKinematics(){return kin;}
	public abstract float getMass();
	public abstract Vector3f getDrag();
	public abstract Vector3f getDragAngular();
	private ControlBehavior ctrlbhv= new ControlBehavior(kin);

	public EntityVehicle(World world){
		super(world);
		this.preventEntitySpawning = false;
	}
	public EntityVehicle(World world, double x, double y, double z){
		this(world);
		this.setPos(x, y, z);	
		this.preventEntitySpawning = false;
		world.spawnEntityInWorld(this);
	}

	@Override
	protected void entityInit(){}

	@Override
	public void onUpdate(){
		if(onGround)
			updateOnGround();
		if(dying)
			updateDying();


		//		if(abs(vx)<.005)
		//			vy=0;
		//		if(abs(vy)<.005)
		//			vy=0;
		//		if(abs(vz)<.005)
		//			vz=0;

		if(posY< -64)
			setDead();

		if(seats[0].occupant!=null)
			ctrlbhv.setLook(seats[0].occupant.rotationYaw, seats[0].occupant.rotationPitch);
		//keypresses are hooked

		move();

		for(Seat seat : seats)
			seat.update();
	}

	@Override
	@Deprecated
	/**  VERY IMPORTANT 
	 * I override this to do nothing as to prevent whichever vanilla 
	 * class force updates it when desynched with the server. 
	 * This fixes the jittering problem.*/
	public void setPosition(double x,double y,double z){}

	public void setPos(double x,double y,double z){
		posX=x;
		posY=y;
		posZ=z;

		float f = this.width / 2.0F;
		float f1 = this.height;
		this.boundingBox.setBounds(x - (double)f, y - (double)this.yOffset + (double)this.ySize, z - (double)f, x + (double)f, y - (double)this.yOffset + (double)this.ySize + (double)f1, z + (double)f);
	}

	public void move(){
		ctrlbhv.tick();
		kin.tick();
		//		doCollision();
		prevPosX=posX;
		prevPosY=posY;
		prevPosZ=posZ;
		final Vector3f v= kin.getVelocity();
		posX+=v.x;
		posY+=v.y;
		posZ+=v.z;
		setPos(posX, posY, posZ);
//		rotationYaw= kin.getYaw();
//		rotationPitch= kin.getPitch();
	}
	/**Checks for hitting blocks etc; more betterer than vanilla*/
	public void doCollision(){
		//TODO
	}
	protected void updateOnGround(){}

	////
	//RIDING
	/**Avoid accessing this. Use getIn/Out*/
	private final Seat[] seats= constructSeats();
	protected abstract Seat[] constructSeats();
	private final Map<EntityPlayer, Integer> sittingPositions= new Hashtable<EntityPlayer, Integer>();

	public void getIn(EntityPlayer p){
		final int i= getNextFreeSeat();
		if(i!=-1)
			seats[i].getIn(p);
		sittingPositions.put(p, i);
	}
	private int getNextFreeSeat(){
		for(int i=0; i!=seats.length; i++)
			if(seats[i].occupant==null)
				return i;
		return -1;
	}
	public void getOut(EntityPlayer p, boolean eject){
		int i= getSeatPlayerIsIn(p);
		if(i==-1) return;
		seats[i].getOut();
		if(eject)
			eject(p);
		sittingPositions.remove(p);
	}
	public int getSeatPlayerIsIn(EntityPlayer p){
		int i=0;
		while(true){//find which
			if(seats[i].occupant==p)
				return i;

			i++;
			if(i==seats.length)
				return -1;
		}		
	}
	public static void eject(EntityPlayer p){
		p.motionY+=1.2;
	}
	public void switchSeat(EntityPlayer p, int index){
		if(index<seats.length && seats[index].occupant==null){
			getOut(p, false);
			seats[index].getIn(p);//use this.getIn for every case except this
		}
	}
	private void updateSeats(){
		for(Seat seat : seats)
			seat.update();
	}

	@Override
	public void handleKeys(EntityPlayer p, int data){
		if(!sittingPositions.containsKey(p))
			return;
		final int indx= sittingPositions.get(p);
		seats[indx].keys= data;

		if(T1A.isKeyPressed(data, T1A.BAIL))
			getOut(p, T1A.isKeyPressed(data, T1A.SHIFT));

		if(T1A.isKeyPressed(data, T1A.BAIL))
			getOut(p, T1A.isKeyPressed(data, T1A.SHIFT));
		if(indx==0){//driver - forward to ctrl bhv
			ctrlbhv.setKeys(data);
		}
	}
	@Override
	/**User right click 
	 * Get in*/
	public boolean interactFirst(EntityPlayer p){
		if(getSeatPlayerIsIn(p)==-1)//player is not riding
			this.getIn(p);
		return true;
	}
	@Override
	public boolean shouldRiderSit(){return true;}




	//killy stuff
	@Override
	public boolean attackEntityFrom(DamageSource src, float d){
		this.health-=d;
		if(this.health<1){
			this.startDying();
			if(this.health< this.getMaxHealth() / -2)//if it gets really damaged 
				this.setDead();//blow up immediately
		}
		return true;
	}
	public void startDying(){
		this.dying=true;
	}
	/**intended for fire extinguishers and core stabilizers*/
	public void stopDying(){
		this.dying=false;
		this.health=1;
	}
	protected void updateDying(){
		if(this.deathTimer--<1)
			this.setDead();
	}
	@Override
	public boolean isBurning(){
		return this.dying;
	}
	///////////

	public abstract int getMaxHealth();


	@Override
	public void applyEntityCollision(Entity e){
		//TODO elastic collision

		if(e instanceof EntityVehicle){
			//			(EntityVehicle)e).v.add(vectorB)
		}
		else{
			e.motionX= 0;
			e.motionY= 0;
			e.motionZ= 0;
		}
	}

	@Override
	public double getMountedYOffset(){
		return 1;
	}

	@Override
	public double getYOffset(){
		return 0;
	}

	@Override
	public boolean canBeCollidedWith(){return true;}
	@Override
	public boolean canBePushed(){return false;}//from vanillas perspective
	@Override
	public AxisAlignedBB getBoundingBox(){return this.boundingBox;}
	@Override
	public AxisAlignedBB getCollisionBox(Entity entity){return this.getBoundingBox();}


	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		setPos(posX, posY, posZ);

		//Load list of riders
		//TODO hook player loading instead
//		final NBTTagList riderTL= (NBTTagList) nbttagcompound.getTag("riders");
//		for(int i=0; i!=riderTL.tagCount(); i++){
//			final NBTTagCompound t= (NBTTagCompound) riderTL..getCompoundTagAt()..tagAt(i);
//			final EntityPlayer p= (EntityPlayer) worldObj.getEntityByID(t.getInteger("rEid"));
//			if(p!=null)
//				getIn( p );
//			switchSeat(p, t.getByte("indx"));
//			}
	}
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {

		//TODO do this with player instead
		//make a list of riders, so when you log back on youre still in the same vehicle
//		final Iterator<Entry<EntityPlayer, Integer>> sIter= sittingPositions.entrySet().iterator();
//		NBTTagList riderTL= new NBTTagList();
//		while( sIter.hasNext() ){
//			final Entry<EntityPlayer, Integer> e= sIter.next();
//			final NBTTagCompound eTC= new NBTTagCompound();
//			eTC.setInteger("rEid", e.getKey().getEntityId());
//			eTC.setByte("indx", (byte)e.getValue().intValue());
//			riderTL.appendTag(eTC);
//		}
//		nbttagcompound.setTag("riders", riderTL);
	}

	@Override
	public void writeSpawnData(ByteBuf data) {
		data.writeDouble(posX);
		data.writeDouble(posY);
		data.writeDouble(posZ);
	}
	@Override
	public void readSpawnData(ByteBuf data) {
		setPos(data.readDouble(), data.readDouble(), data.readDouble());
	}
}
