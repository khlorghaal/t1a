package com.typ1a.common.utils;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class Vector3 
{
	public double x;
	public double y;
	public double z;
	public static final int length = 3;
	public static final int dataSize = 12;
	public boolean valid = true;
	
	public static double SQRT_3 = Math.sqrt(3);
	public static double SQRT_2 = Math.sqrt(2);
	
	public static boolean collisionDamage = true;

	public static final Vector3 secondAxis = new Vector3(0,1,0);
	public static final Vector3 firstAxis =  new Vector3(1,0,0);
	public static final Vector3 thirdAxis =  new Vector3(0,0,1);
	
	public static class IntVec3 extends Vector3
	{
		public IntVec3()
		{
			x=y=z=0;
		}
		public IntVec3(byte[] a)
		{
			x = a[0];
			y = a[1];
			z = a[2];
		}
		
		public IntVec3(Vector3 vec)
		{
			x = vec.x;
			y = vec.y;
			z = vec.z;
		}
		
		public boolean equals(Object vec)
		{
			if(!(vec instanceof Vector3))
				return false;
			Vector3 v = (Vector3)vec;
			return sameBlock(v);
		}
	}
	
	public Vector3(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	/**
	 *  This takes degrees then converts to radians, as it seems most people like to work with degrees.
	 * @param pitch
	 * @param yaw
	 */
	public Vector3(double pitch, double yaw)
	{
		this.x = 1;
		this.y = Math.toRadians(pitch);
		this.z = Math.toRadians(yaw);
	}
	
	public Vector3()
	{
		this.x = this.y = this.z = 0;
	}
	
	public Vector3 (Entity e)
	{
		if(e!=null)
		{
			this.x = e.posX;
			this.y = e.posY;
			this.z = e.posZ;
		}
	}
	
	public Vector3 (Vec3 vec)
	{
		this.x = vec.xCoord;
		this.y = vec.yCoord;
		this.z = vec.zCoord;
	}
	
	public Vec3 toVec3()
	{
		return Vec3.createVectorHelper(x, y, z);
	}
	
	public Vector3 (Entity e, boolean bool)
	{
		if(e!=null&&bool)
		{
			this.x = e.posX;
			this.y = e.posY+e.height/2;
			this.z = e.posZ;
		}
		else if(e!=null)
		{
			this.x = e.posX;
			this.y = e.posY+e.yOffset;
			this.z = e.posZ;
		}
	}
	
	public Vector3 (TileEntity e)
	{
		this(e.xCoord,e.yCoord,e.zCoord);
	}
	
	public Vector3(double[] a) {
		this(a[0],a[1],a[2]);
	}
	
	public Vector3(Object a, Object b)
	{
		this();
		Vector3 A = new Vector3(a);
		Vector3 B = new Vector3(b);
		this.set(B.subtract(A));
	}
	
	public Vector3(Object a)
	{
		this();
		if(a instanceof Entity)
		{
			 this.set(new Vector3((Entity)a));
		}
		else if(a instanceof TileEntity)
		{
			this.set(new Vector3((TileEntity)a));
		}
		else if(a instanceof double[])
		{
			this.set(new Vector3((double[])a));
		}
		else if(a instanceof ForgeDirection)
		{
			ForgeDirection side = (ForgeDirection)a;
			this.set(new Vector3(side.offsetX, side.offsetY, side.offsetZ));
		}
	}

	@Override
	public Vector3 clone(){
		return new Vector3(this.x, this.y, this.z);
		
	}
	
	public List<Entity> livingEntityInBox(World worldObj)
	{
		int x0 = intX(), y0 = intY(), z0 = intZ();
		List<Entity> targets = worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(x0, y0, z0, x0+1, y0+1, z0+1));
		return targets;
	}
	
	public boolean isNaN()
	{
		return Double.isNaN(x)||Double.isNaN(z)||Double.isNaN(y);
	}
	public boolean isEmpty()
	{
		return x==0&&z==0&&y==0;
	}
	public List<Entity> livingEntityAtPoint(World worldObj)
	{
		int x0 = intX(), y0 = intY(), z0 = intZ();
		List<Entity> ret = new ArrayList<Entity>();
		List<Entity> targets = worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(x0, y0, z0, x0+1, y0+1, z0+1));
		for(Entity e:targets)
		{
			if(!isPointClearOfEntity(x, y, z, e))
			{
				ret.add(e);
			}
		}
		return ret;
	}

	public List<Entity> livingEntityAtPointExcludingEntity(World worldObj, Entity entity)
	{
		int x0 = intX(), y0 = intY(), z0 = intZ();
		List<Entity> ret = new ArrayList<Entity>();
		List<Entity> targets = worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(x0, y0, z0, x0+1, y0+1, z0+1));
		for(Entity e:targets)
		{
			if(!isPointClearOfEntity(x, y, z, e)&&e!=entity)
			{
				ret.add(e);
			}
		}
		return ret;
	}
	
	public static double convertYawRadians(float rotationYaw)
	{
		return (PI*((rotationYaw%360)-90)/180);
	}	
	public static double convertYaw(float rotationYaw)
	{
		return ((rotationYaw%360)-90);
	}	
	public float convertPitch(float rotationPitch)
	{
		return 0;
	}
	public void addVelocities(Entity e)
	{
		e.addVelocity(x, y, z);
	}
	public AxisAlignedBB getAABB()
	{
		return AxisAlignedBB.getBoundingBox(x, y, z, x, y, z);
	}
	//*/
	public boolean setBlock(World worldObj, Block block, int meta)
	{
		return setBlock(worldObj, block, meta, 3);
	}
	public boolean setBlock(World worldObj, Block block, int meta, int flag)
	{
		if(worldObj.blockExists(intX(), intY(), intZ()))
		{		
			//WorldCore.instance.ops.safeSet(worldObj, x, y, z, id, meta, flag);
			worldObj.setBlock(intX(), intY(), intZ(), block, meta, flag);
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean setBlock(World worldObj, Block block)
	{
		return setBlock(worldObj, block, 0, 3);
	}
	public void setAir(World worldObj)
	{
		setBlock(worldObj,Blocks.air);
	}
	//*/
	public boolean aabbClear(AxisAlignedBB aabb)
	{
		if(y<=aabb.maxY&&y>=aabb.minY)
			return false;
		if(z<=aabb.maxZ&&z>=aabb.minZ)
			return false;
		if(x<=aabb.maxX&&x>=aabb.minX)
			return false;
		
		return true;
	}
	
	public Vector3 offset(ForgeDirection side)
	{
		Vector3 ret = this.Copy();
		ret = ret.add(new Vector3(side));
		return ret;
	}
	
	public boolean inMatBox(Matrix3 matrix3)
	{
		Vector3 min = matrix3.get(0);
		Vector3 max = matrix3.get(1);
		boolean ycheck = false, xcheck = false, zcheck = false;
		
		if(y<=max.y&&y>=min.y)
			ycheck = true;
		if(z<=max.z&&z>=min.z)
			zcheck = true;
		if(x<=max.x&&x>=min.x)
			xcheck = true;
		
		return ycheck&&zcheck&&xcheck;
	}
	
	public double perpendicularDistance(Vector3 vec)
	{
		Vector3 a = this.subtract(vec);
		Vector3 b = this.normalize().scalarMult(this.normalize().dot(a));
		
		return (a.subtract(b)).mag();
		
	}
	
	public List<Entity> anyEntity(World worldObj)
	{
		int x0 = intX(), y0 = intY(), z0 = intZ();
		List<Entity> targets = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x0, y0, z0, x0+1, y0+1, z0+1));

		return targets;
	}
	
	public void setTileEntity(World worldObj, TileEntity te)
	{
		worldObj.setTileEntity(intX(), intY(), intZ(), te);
	}
	
	public double distToEntity(Entity e)
	{
		return vectorMag(vectorSubtract(this, entity(e)));
	}
	
	public double distanceTo(Vector3 vec)
	{
		return vectorMag(vectorSubtract(this, vec));
	}
	
	public boolean equals(Object vec)
	{
		if(!(vec instanceof Vector3))
			return false;
		Vector3 v = (Vector3)vec;
		return v.x==x&&v.y==y&&v.z==z;
	}
	
	public boolean sameBlock(Vector3 vec)
	{
		return this.intX() == vec.intX() && this.intY() == vec.intY() && this.intZ() == vec.intZ();
	}
	
	public Vector3 set(Vector3 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		
		return this;
	}
	
	public void set(double[] vec)
	{
		this.x = vec[0];
		this.y = vec[1];
		this.z = vec[2];
	}
	
	public Vector3 set(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public double get(int i)
	{
		assert(i<3);
		return i==0?x:i==1?y:z;
	}
	
	public void set(int i, double j)
	{
		if(i==0)
		{
			x=j;
		}
		else if(i==1)
		{
			y=j;
		}
		else if(i==2)
		{
			z=j;
		}
	}
	
	public void add(int i, double j)
	{
		if(i==0)
		{
			x+=j;
		}
		else if(i==1)
		{
			y+=j;
		}
		else if(i==2)
		{
			z+=j;
		}
	}
	
	public float getExplosionResistance(World worldObj)
	{
		Block block = getBlock(worldObj);
		
		if(block!=null&&block!=Blocks.air)
		{
			return block.getExplosionResistance(null);
		}
		return 0;
		
	}
	
	public void doExplosion(World worldObj, float strength, boolean real)
	{
		worldObj.createExplosion(null, x, y, z, strength, real);
	}
	
	public void playSoundEffect(World worldObj, float volume, float pitch, String sound)
	{
		worldObj.playSoundEffect(x, y, z, sound, volume, pitch);
	}
	
	public int intX()
	{
		return MathHelper.floor_double(x);
	}
	
	public int intY()
	{
		return MathHelper.floor_double(y);
	}
	
	public int intZ()
	{
		return MathHelper.floor_double(z);
	}
	
	public Vector3 intVec()
	{
		return new Vector3(intX(), intY(), intZ());
	}
	
	public static Vector3 ArrayTo(double[] a)
	{
		assert(a.length==3);
		return new Vector3(a[0],a[1],a[2]);
	}
	
	public static Vector3 ArrayTo(Double[] a)
	{
		assert(a.length==3);
		return new Vector3(a[0],a[1],a[2]);
	}
	
	public static Vector3 entity(Entity e)
	{
		if(e!=null)
			return new Vector3(e.posX,e.posY,e.posZ);
		return null;
	}
	
	public double[] toArray()
	{
		return new double[] {x,y,z};
	}
	
	public Double[] toArrayD()
	{
		return new Double[] {x,y,z};
	}
	
	public static int Int(double x)
	{
		return ((int)x);
	}
	
	public String toString()
	{
		return "x:"+x+" y:"+y+" z:"+z;
	}
	
	public String toString(boolean bool)
	{
		return "x:"+intX()+" y:"+intY()+" z:"+intZ();
	}
	
	public double HorizonalDist(Vector3 vec)
	{
		return Math.sqrt((x-vec.x)*(x-vec.x)+(z-vec.z)*(z-vec.z));
	}
	
	  /**
	   * Returns the unit vector in with the same direction as vector.
	   * @param vector
	   * @return unit vector in direction of vector.
	   */
	  public static Vector3 vectorNormalize(Vector3 vector)
	  {
			double vmag = vectorMag(vector);
		  	Vector3 vhat = vectorScalarMult(vector, 1/vmag);
			return vhat;
	  }
	  
	  /**
	   * Returns the unit vector in with the same direction as vector.
	   * @param vector
	   * @return unit vector in direction of vector.
	   */
	  public Vector3 normalize()
	  {
			double vmag = vectorMag(this);
		  	Vector3 vhat = vectorScalarMult(this, 1/vmag);
			return vhat;
	  }
	  
	  /**
	   * Returns the unit vector in with the same direction as vector.
	   * @param vector
	   * @return unit vector in direction of vector.
	   */
	  public static Vector3 vectorToSpherical(Vector3 vector, boolean minecraft)
	  {
		  Vector3 vectorSpher = new Vector3();
		  vectorSpher.x = vectorMag(vector);
		  vectorSpher.y = acos(vector.get(minecraft?1:2)/vectorSpher.x)-PI/2;
		  vectorSpher.z = atan2(vector.get(minecraft?2:1),vector.x);
		  
		  
		  return vectorSpher;
	  }
	  
	  /**
	   * Returns the unit vector in with the same direction as vector.
	   * @param vector
	   * @return unit vector in direction of vector.
	   */
	  public Vector3 toSpherical()
	  {
		  Vector3 vectorSpher = new Vector3();
		  vectorSpher.x = vectorMag(this);
		  vectorSpher.y = acos(this.get(1)/vectorSpher.x)-PI/2;
		  vectorSpher.z = atan2(this.get(2),this.x);
		  return vectorSpher;
	  }
	  
	  /**
	   * Returns the unit vector in with the same direction as vector.
	   * @param vector
	   * @return unit vector in direction of vector.
	   */
	  public Vector3 toCartesian()
	  {
		  Vector3 vectorCart = new Vector3();
		  vectorCart.x = x*cos(y)*cos(z);
		  vectorCart.z = x*cos(y)*sin(z);
		  vectorCart.y = x*sin(y);
		  return vectorCart;
	  }
	  
	  public Vector3 anglesTo(Vector3 target)
	  {
		  Vector3 ret = new Vector3();
		  ret = (this.toSpherical()).subtract(target.toSpherical());
		  return ret;
	  }
	  
	  public static Vector3 horizonalPerp(Vector3 vector)
	  {
		  Vector3 vectorH = new Vector3(vector.x, 0, vector.z);
		  return vectorRotateAboutLine(vectorH, secondAxis, PI/2);
	  }
	  
	  public Vector3 horizonalPerp()
	  {
		  Vector3 vectorH = new Vector3(x, 0, z);
		  return vectorNormalize(vectorRotateAboutLine(vectorH, secondAxis, PI/2));
	  }
	  
	  /**
	   * Adds vectorA to vectorB
	   * @param vectorA
	   * @param vectorB
	   * @return
	   */
	  public static Vector3 vectorAdd(Vector3 vectorA, Vector3 vectorB)
	  {
		  	Vector3 vectorC = new Vector3();
			for(int i=0; i < vectorA.length; i++){vectorC.set(i, vectorA.get(i)+vectorB.get(i));}
			return vectorC;
	  }
	  
	  /**
	   * Adds vectorA to vectorB
	   * @param vectorA
	   * @param vectorB
	   * @return
	   */
	  public Vector3 add(Vector3 vectorB)
	  {
		  	Vector3 vectorC = new Vector3();
			for(int i=0; i < 3; i++){vectorC.set(i, this.get(i)+vectorB.get(i));}
			return vectorC;
	  }
	  
	  /**
	   * Subtracts vectorB from vectorA
	   * @param vectorA
	   * @param vectorB
	   * @return
	   */
	  public static Vector3 vectorSubtract(Vector3 vectorA, Vector3 vectorB){
		  	Vector3 vectorC = new Vector3();
			for(int i=0; i < vectorA.length; i++){vectorC.set(i, vectorA.get(i)-vectorB.get(i));}
			return vectorC;
	  }
	  
	  
	  /**
	   * Subtracts vectorB from vectorA
	   * @param vectorA
	   * @param vectorB
	   * @return
	   */
	  public Vector3 subtract(Vector3 vectorB){
		  	Vector3 vectorC = new Vector3();
			for(int i=0; i < 3; i++){vectorC.set(i, this.get(i)-vectorB.get(i));}
			return vectorC;
	  }
	  
	  public static double moduloPi(double num)
	  {
		  double newnum = num;
		  if(num>PI)
		  {
			  while(newnum>PI)
			  {
				  newnum-=PI;
			  }
		  }
		  else if(num<-PI)
		  {
			  while(newnum<-PI)
			  {
				  newnum+=PI;
			  }
		  }
		  return newnum;
	  }
	  
	  /**
	   * Returns the magnitude of vector
	   * @param vector
	   * @return
	   */
	  public static double vectorMag(Vector3 vector){
			double vmag = 0;
			for(int i=0; i < 3; i = i+1){vmag = vmag + vector.get(i)*vector.get(i);}
			vmag = Math.sqrt(vmag);
			return vmag;
	  }
	  
	  /**
	   * Returns the magnitude of vector squared
	   * @param vector
	   * @return
	   */
	  public double vectorMagSq(Vector3 vector){
			double vmag = 0;
			for(int i=0; i < vector.length; i = i+1){vmag = vmag + vector.get(i)*vector.get(i);}
			return vmag;
	  }
	  
	  /**
	   * Returns the magnitude of vector
	   * @param vector
	   * @return
	   */
	  public double mag(){
			double vmag = Math.sqrt(magSq());
			return vmag;
	  }
	  
	  /**
	   * Returns the magnitude of vector squared
	   * @param vector
	   * @return
	   */
	  public double magSq(){
			double vmag = 0;
			for(int i=0; i < this.length; i = i+1){vmag = vmag + this.get(i)*this.get(i);}
			return vmag;
	  }
	  
	  /**
	   * Multiplies the vector by the constant.
	   * @param vector
	   * @param constant
	   * @return
	   */
	  public static Vector3 vectorScalarMult(Vector3 vector, double constant){
			Vector3 newVector = new Vector3();
		  	for(int i=0; i < vector.length; i = i+1){newVector.set(i, constant*vector.get(i));}
			return newVector;
	  }
	  
	  /**
	   * Multiplies the vector by the constant.
	   * @param vector
	   * @param constant
	   * @return
	   */
	  public Vector3 scalarMult(double constant){
			Vector3 newVector = new Vector3();
		  	for(int i=0; i < this.length; i++){newVector.set(i, constant*this.get(i));}
			return newVector;
	  }
	  
	    
	  /**
	   * Left multiplies the Matrix by the Vector
	   * @param Matrix
	   * @param vector
	   * @return
	   */
	  public static Vector3 vectorMatrixMult(Matrix3 Matrix, Vector3 vector){
		  Vector3 newVect = new Vector3();
		  for(int i=0;i<3;i++){
			  for(int j=0;j<vector.length;j++){
				  newVect.add(i, Matrix.get(i).get(j) * vector.get(j));
			  }
		  }
		  return newVect;
	  }
	  
	  /**
	   * Reflects the vector off the corresponding plane.  The plane vector is the coefficient
	   * a,b,c of ax + by + cz = 0
	   * @param vector
	   * @param plane
	   * @return
	   */
	  public Vector3 vectorReflect(Vector3 vector, Vector3 plane){
		  Vector3 ret;
		  double 	a = plane.x,
				  	b = plane.y,
				  	c = plane.z;
		  double vMag = vectorMag(vector);
		  ret = vectorNormalize(vector);
		  Matrix3 Tmatrix = new Matrix3(new double[]{1-2*a*a,-2*a*b,-2*a*c},
				  new double[]{-2*a*b,1-2*b*b,-2*b*c},
				  new double[]{-2*c*a,-2*c*b,1-2*c*c});
		  ret = vectorMatrixMult(Tmatrix, vector);
		  return vectorScalarMult(ret,vMag);
	  }
	  
	  public static Vector3 linearInterpolate(Vector3 A, Vector3 B, double t)
	  {
		  return vectorAdd(A,vectorScalarMult(vectorSubtract(B, A), t));
	  }
	  
	  /**
	   * Rotates the given vector around the given line by the given angle.
	   * This internally normalizes the line incase it is not already normalized
	   * @param vectorH
	   * @param line
	   * @param angle
	   * @return
	   */
	  public static Vector3 vectorRotateAboutLine(Vector3 vector, Vector3 line, double angle){
		  line = line.normalize();
		  Vector3 ret;
		  
		  Matrix3 TransMatrix = new Matrix3();
		  
		  TransMatrix.get(0).x = line.get(0)*line.get(0)*(1-Math.cos(angle))+Math.cos(angle);
		  TransMatrix.get(0).y = line.get(0)*line.get(1)*(1-Math.cos(angle))-line.get(2)*Math.sin(angle);
		  TransMatrix.get(0).z = line.get(0)*line.get(2)*(1-Math.cos(angle))+line.get(1)*Math.sin(angle);
		  
		  TransMatrix.get(1).x = line.get(1)*line.get(0)*(1-Math.cos(angle))+line.get(2)*Math.sin(angle);
		  TransMatrix.get(1).y = line.get(1)*line.get(1)*(1-Math.cos(angle))+Math.cos(angle);
		  TransMatrix.get(1).z = line.get(1)*line.get(2)*(1-Math.cos(angle))-line.get(0)*Math.sin(angle);
		  
		  TransMatrix.get(2).x = line.get(2)*line.get(0)*(1-Math.cos(angle))-line.get(1)*Math.sin(angle);
		  TransMatrix.get(2).y = line.get(2)*line.get(1)*(1-Math.cos(angle))+line.get(0)*Math.sin(angle);
		  TransMatrix.get(2).z = line.get(2)*line.get(2)*(1-Math.cos(angle))+Math.cos(angle);
		  
		  ret = vectorMatrixMult(TransMatrix, vector);
		  
		  return ret;
	  }
	  
	  /**
	   * Rotates the given vector around the given line by the given angle.
	   * This internally normalizes the line incase it is not already normalized
	   * @param vectorH
	   * @param line
	   * @param angle
	   * @return
	   */
	  public Vector3 rotateAboutLine(Vector3 line, double angle){
		  line = vectorNormalize(line);
		  Vector3 ret;
		  Matrix3 TransMatrix = new Matrix3();
		  
		  TransMatrix.get(0).x = line.get(0)*line.get(0)*(1-Math.cos(angle))+Math.cos(angle);
		  TransMatrix.get(0).y = line.get(0)*line.get(1)*(1-Math.cos(angle))-line.get(2)*Math.sin(angle);
		  TransMatrix.get(0).z = line.get(0)*line.get(2)*(1-Math.cos(angle))+line.get(1)*Math.sin(angle);
		  
		  TransMatrix.get(1).x = line.get(1)*line.get(0)*(1-Math.cos(angle))+line.get(2)*Math.sin(angle);
		  TransMatrix.get(1).y = line.get(1)*line.get(1)*(1-Math.cos(angle))+Math.cos(angle);
		  TransMatrix.get(1).z = line.get(1)*line.get(2)*(1-Math.cos(angle))-line.get(0)*Math.sin(angle);
		  
		  TransMatrix.get(2).x = line.get(2)*line.get(0)*(1-Math.cos(angle))-line.get(1)*Math.sin(angle);
		  TransMatrix.get(2).y = line.get(2)*line.get(1)*(1-Math.cos(angle))+line.get(0)*Math.sin(angle);
		  TransMatrix.get(2).z = line.get(2)*line.get(2)*(1-Math.cos(angle))+Math.cos(angle);
		  
		  ret = vectorMatrixMult(TransMatrix, this);
		  return ret;
	  }
	  
	  /**
	   *  Rotates the given vector by the given amounts of pitch and yaw.
	   * @param vector
	   * @param pitch
	   * @param yaw
	   * @return
	   */
	  public static Vector3 vectorRotateAboutAngles(Vector3 vector, double pitch, double yaw){
		  return vectorRotateAboutLine(vectorRotateAboutLine(vector, secondAxis, yaw),horizonalPerp(vector), pitch);
	  }
	  
	  /**
	   *  Rotates the given vector by the given amounts of pitch and yaw.
	   * @param vector
	   * @param pitch
	   * @param yaw
	   * @return
	   */
	  public Vector3 rotateAboutAngles(double pitch, double yaw){
		  if(this.isEmpty()||pitch==0&&yaw==0)
		  {
			  return this;
		  }
		  Vector3 ret = vectorRotateAboutLine(vectorRotateAboutLine(this, secondAxis, yaw),horizonalPerp(this), pitch);
		  if(ret.isNaN())
		  {
			  return this;
		  }
		  
		  return ret;
	  }
	  
	  
	  public static void rotateAboutAngles(Vector3[] points, double pitch, double yaw)
	  {
		  for(Vector3 p:points)
		  {
			  p = p.rotateAboutAngles(pitch, yaw);
		  }
	  }
	  
	  public static void rotateAboutAngles(Vector3[] points, Vector3 angles)
	  {
		  for(Vector3 p:points)
		  {
			  p = p.rotateAboutAngles(angles.y, angles.z);
		  }
	  }
	  
	  /**
	   * Returns the dot (scalar) product of the two vectors
	   * @param vector1
	   * @param vector2
	   * @return
	   */
	  public static double vectorDot(Vector3 vector1, Vector3 vector2){
		  double dot = 0;
		  for(int i=0; i<vector1.length;i++){
			  dot += vector1.get(i)*vector2.get(i);
		  }
		  return dot;
	  }
	  
	  /**
	   * Returns the dot (scalar) product of the two vectors
	   * @param vector1
	   * @param vector2
	   * @return
	   */
	  public double dot(Vector3 vector2){
		  double dot = 0;
		  for(int i=0; i<3;i++){
			  dot += this.get(i)*vector2.get(i);
		  }
		  return dot;
	  }
	  
	  /**
	   * Returns the angle between two vectors
	   * @param vector1
	   * @param vector2
	   * @return
	   */
	  public static  double vectorAngle(Vector3 vector1, Vector3 vector2){
		 return Math.acos(vectorDot(vectorNormalize(vector1),vectorNormalize(vector2)));
	  }
	  
	  /**
	   * Returns the angle between two vectors
	   * @param vector1
	   * @param vector2
	   * @return
	   */
	  public double angle(Vector3 vector2){
		 return Math.acos(vectorDot(vectorNormalize(this),vectorNormalize(vector2)));
	  }
	  
	  /**
	   * Swaps the ith and jth element of the vector
	   * useful for converting from x,y,z to x,z,y
	   * @param vector
	   * @param i
	   * @param j
	   * @return
	   */
	  public static Vector3 vectorSwap(Vector3 vector, int i, int j){
		  Vector3 ret = new Vector3();
		  ret.set(i, vector.get(j));
		  ret.set(j, vector.get(i));
		  return vector;
	  }
	  
	  /**
	   * Swaps the ith and jth element of the vector
	   * useful for converting from x,y,z to x,z,y
	   * @param i
	   * @param j
	   * @return
	   */
	  public void swap(int i, int j){
		  this.set(i, this.get(j));
		  this.set(j, this.get(i));
	  }
	  
	  /**
	   * Returns the cross (vector) product of the two vectors.
	   * @param vector1
	   * @param vector2
	   * @return
	   */
	  public  static Vector3 vectorCross(Vector3 vector1, Vector3 vector2){
		  Vector3 vector3 = new Vector3();
		  for(int i=0;i<3;i++){
			  vector3.set(i, vector1.get((i+1)%3)*vector2.get((i+2)%3)-vector1.get((i+2)%3)*vector2.get((i+1)%3));
		  }
		  return vector3;
	  }
	  
	  /**
	   * Returns the cross (vector) product of the two vectors.
	   * @param vector1
	   * @param vector2
	   * @return
	   */
	  public Vector3 cross(Vector3 vector2){
		  Vector3 vector3 = new Vector3();
		  for(int i=0;i<3;i++){
			  vector3.set(i, this.get((i+1)%3)*vector2.get((i+2)%3)-this.get((i+2)%3)*vector2.get((i+1)%3));
		  }
		  return vector3;
	  }
	  
	  public static Vector3 findMidPointD(List<Double[]> points){
		  Vector3 mid = new Vector3();
		  for(int j=0;j<points.size();j++){
			  mid=vectorAdd(Vector3.ArrayTo(points.get(j)),mid);
		  }
		  if(points.size()!=0){
		  mid = vectorScalarMult(mid, 1/points.size());
		  }
		  return mid;
	  }
	  
	  public void setBiome(BiomeGenBase biome, World worldObj)
	  {
		  	int x = intX();
		  	int z = intZ();
		  
	    	Chunk chunk = worldObj.getChunkFromBlockCoords(x, z);
	    	byte[] biomes = chunk.getBiomeArray();
	    	
	    	byte newBiome = (byte) biome.biomeID;
	    	
	    	int chunkX = Math.abs(x%16);
	    	int chunkZ = Math.abs(z%16);
	    	
	    	int point = 0;
	    	if(chunk.xPosition>=0&&chunk.zPosition>=0)
	    	{
	    		point = chunkX+16*chunkZ;
	    	}
	    	if(chunk.xPosition<0&&chunk.zPosition>=0)
	    	{
	    		chunkX = 15-Math.abs((x+1)%16);
	    		point = chunkX+16*chunkZ;
	    	}
	    	if(chunk.xPosition<0&&chunk.zPosition<0)
	    	{
	    		chunkX = 15-Math.abs((x+1)%16);
	    		chunkZ = 15-Math.abs((z+1)%16);
	    		point = chunkX+16*chunkZ;
	    	}
	    	if(chunk.xPosition>=0&&chunk.zPosition<0)
	    	{
	    		chunkZ = 15-Math.abs((z+1)%16);
	    		point = (chunkX)+16*(chunkZ);
	    	}
	    	
	    	if(biomes[point]!= newBiome)
	    	{
		    	biomes[point] = newBiome;
		    	chunk.setBiomeArray(biomes);
		    	chunk.setChunkModified();
	    	}
	  }
	  
	  public static Vector3 findMidPoint(List<Vector3> points){
		  Vector3 mid = new Vector3();
		  for(int j=0;j<points.size();j++){
			  mid=vectorAdd(points.get(j),mid);
		  }
		  if(points.size()!=0){
		  mid = vectorScalarMult(mid, 1/points.size());
		  }
		  return mid;
	  }
	  
	  public  static double distBetween(Vector3 pointA, Vector3 pointB){
		  return vectorMag(vectorSubtract(pointA,pointB));
	  }
	  
	  public  double distTo(Vector3 pointB){
		  return vectorMag(vectorSubtract(this,pointB));
	  }
	  
	  public  double distToSq(Vector3 pointB){
		  return vectorMagSq(vectorSubtract(this,pointB));
	  }
	  
	  public  static double distToEntity(Vector3 pointA, Entity target)
	  {
		  if(target!=null)
		  return vectorMag(vectorSubtract(pointA, new Vector3(target.posX,target.posY,target.posZ)));
		  return -1;
	  }
	  
	  public Vector3 Copy(){
		  Vector3 newVector = new Vector3(x,y,z);
		  return newVector;
	  }
	  
	  public void moveEntity(Entity e)
	  {
		  e.posX=x;
		  e.posY=y;
		  e.posZ=z;
	  }
	  
	  public void writeToNBT(NBTTagCompound nbt, String tag)
	  {
		  nbt.setDouble(tag+"x", x);
		  nbt.setDouble(tag+"y", y);
		  nbt.setDouble(tag+"z", z);
	  }
	  
	  public static Vector3 readFromNBT(NBTTagCompound nbt, String tag)
	  {
		  Vector3 ret = new Vector3();
		  ret.x = nbt.getDouble(tag+"x");
		  ret.y = nbt.getDouble(tag+"y");
		  ret.z = nbt.getDouble(tag+"z");
		  return ret;
	  }
	  
	  public void writeToOutputStream(DataOutputStream dos)
	  {
          try {
        	  dos.writeDouble(x);
	          dos.writeDouble(y);
	          dos.writeDouble(z);
		} catch (IOException e) {
			System.err.println("error in writing Vector3 to stream");
			e.printStackTrace();
		}
	  }
	  

		public void writeToOutputStream(ByteArrayDataOutput data) {
			data.writeDouble(x);
			data.writeDouble(y);
			data.writeDouble(z);
		}
		
	  
	  public static Vector3 readFromInputSteam(ByteArrayDataInput dat)
	  {
		  Vector3 ret = new Vector3();
		  ret.x = dat.readDouble();
		  ret.y = dat.readDouble();
		  ret.z = dat.readDouble();
	        return ret;
	  }
	  
	  public Vector3 findNextSolidBlock(IBlockAccess worldObj,Vector3 direction, double range)
	  {
		  return getNextSurfacePoint(worldObj, this, direction, range);
	  }
	  
	  /**
	   * Locates the first solid block in the line indicated by the direction vector, starting from the source
	   * if range is given as 0, it will check out to 320 blocks.
	   * @param worldObj
	   * @param source
	   * @param direction
	   * @param range
	   * @return
	   */
	  public  static Vector3 findNextSolidBlock(World worldObj,Vector3 source,Vector3 direction, double range){
		  	direction = vectorNormalize(direction);
			int n = 0;
			double xprev = source.x, yprev = source.y, zprev = source.z;
			double dx,dy,dz;
			
			for(double i=0;i<range;i+=0.0625)
			{
				dx = i*direction.x;
				dy = i*direction.y;
				dz = i*direction.z;
				
			  double  xtest = (source.x+dx),
					  ytest = (source.y+dy),
					  ztest = (source.z+dz);
			  
			  if(!(Int(xtest)==Int(xprev)&&Int(ytest)==Int(yprev)&&Int(ztest)==Int(zprev)))
			  {
				  
				  Block block = worldObj.getBlock(Int(xtest), Int(ytest), Int(ztest));
				  
				  boolean clear = block==null||block.isAir(worldObj, Int(xtest), Int(ytest), Int(ztest))||(new Vector3(xtest, ytest, ztest)).isFluid(worldObj);
				  
				  if(!clear){
					  return new Vector3(Int(xtest), Int(ytest), Int(ztest));
				  }
			  }
			  
			  yprev = ytest; xprev = xtest; zprev = ztest;
			}
			return null;
	  }
	
	  public static Vector3 getVectorToEntity(Vector3 source, Entity target)
	  {
		  if(target!=null)
		  return vectorSubtract(new Vector3(target.posX, target.posY, target.posZ), source);
		  else
			  return new Vector3();
	  }
	  
	  /**
	   * determines whether the source can see out as far as range in the given direction.
	   * @param worldObj
	   * @param source
	   * @param direction
	   * @param range
	   * @return
	   */
	  public static boolean isVisibleRange(World worldObj, Vector3 source, Vector3 direction, double range){
		  direction = vectorNormalize(direction);
			int n = 0;
			double xprev = source.x, yprev = source.y, zprev = source.z;
			double dx,dy,dz;
			boolean notNull = true;
			double closest = range;
			double blocked = 0;
			
			for(double i=0;i<range;i+=0.0625)
			{
				dx = i*direction.x;
				dy = i*direction.y;
				dz = i*direction.z;
				
				  double  xtest = (source.x+dx),
						  ytest = (source.y+dy),
						  ztest = (source.z+dz);
				  
				  boolean check = isPointClearBlocks(xtest, ytest, ztest, worldObj);
				  blocked = Math.sqrt(dx*dx+dy*dy+dz*dz);
				  if(!check){
					  return false;
				  }
				  yprev = ytest; xprev = xtest; zprev = ztest;
			}
			return true;
	  }
	  
	  /**
	   * determines whether the source can see out as far as range in the given direction.
	   * @param worldObj
	   * @param source
	   * @param direction
	   * @param range
	   * @return
	   */
	  public static Vector3 getNextSurfacePoint(IBlockAccess worldObj, Vector3 source, Vector3 direction, double range){
		  direction = vectorNormalize(direction);
		  
			double xprev = source.x, yprev = source.y, zprev = source.z;
			double dx,dy,dz;
			
			for(double i=0;i<range;i+=0.0625)
			{
				dx = i*direction.x;
				dy = i*direction.y;
				dz = i*direction.z;
				
				  double  xtest = (source.x+dx),
						  ytest = (source.y+dy),
						  ztest = (source.z+dz);
				  
				  boolean check = isPointClearBlocks(xtest, ytest, ztest, worldObj);
				  if(!check){
					  return new Vector3(xtest, ytest, ztest);
				  }
				  yprev = ytest; xprev = xtest; zprev = ztest;
			}
			return null;
	  }
	  
	  /**
	   * determines whether the source can see out as far as range in the given direction.
	   * @param worldObj
	   * @param source
	   * @param direction
	   * @param range
	   * @return
	   */
	  public static Vector3 getNextSurfacePointFunction(World worldObj, Vector3 source, Vector3 direction, Vector3 acceleration, double range){
		  direction = vectorNormalize(direction);
		  
			double xprev = source.x, yprev = source.y, zprev = source.z;
			double dx,dy,dz;
			
			for(double i=0;i<range;i+=0.0625)
			{
				dx = i*(direction.x+i*acceleration.x/2);
				dy = i*(direction.y+i*acceleration.y/2);
				dz = i*(direction.z+i*acceleration.z/2);
				
				  double  xtest = (source.x+dx),
						  ytest = (source.y+dy),
						  ztest = (source.z+dz);
				  
				  boolean check = isPointClearBlocks(xtest, ytest, ztest, worldObj);
				  if(!check){
					  return new Vector3(xtest, ytest, ztest);
				  }
				  yprev = ytest; xprev = xtest; zprev = ztest;
			}
			return null;
	  }
	  
	  /**
	   * determines whether the location given is visible from source.
	   * @param worldObj
	   * @param source
	   * @param direction
	   * @param range
	   * @return
	   */
	  public static boolean isVisibleLocation(World worldObj, Vector3 source, Vector3 location){
		  Vector3 direction = vectorSubtract(location, source);
		  double range = direction.mag();
		  return isVisibleRange(worldObj, source, direction, range);
	  }
	  
    public static boolean isVisibleEntityFromEntity(Entity looker, Entity target, double rMax)
    {
    	return isVisibleRange(looker.worldObj, entity(target), entity(looker), entity(looker).distToEntity(target));
    }
  
	public static Vector3 firstEntityLocation(double range, Vector3 direction, Vector3 source, World worldObj, boolean effect)
	{
		direction = vectorNormalize(direction);
		int n = 0;
		double xprev = source.x, yprev = source.y, zprev = source.z;
		double dx,dy,dz;
		boolean notNull = true;
		double closest = range;
		double blocked = 0;
		
		Vector3 temp = new Vector3();
		
		for(double i=0;i<range;i+=0.0625)
		{
			dx = i*direction.x;
			dy = i*direction.y;
			dz = i*direction.z;
			
			  double  xtest = (source.x+dx),
					  ytest = (source.y+dy),
					  ztest = (source.z+dz);
			  
			  boolean check = isPointClearBlocks(xtest, ytest, ztest, worldObj);
			  blocked = Math.sqrt(dx*dx+dy*dy+dz*dz);

			  if(!check){
				  break;
			  }
			  
			  if(effect&&worldObj.isRemote)
			  {
				 worldObj.spawnParticle("flame",xtest, ytest, ztest, 0, 0, 0);
			  }
			  
			  if(!((int)xtest==(int)xprev&&(int)ytest==(int)yprev&&(int)ztest==(int)zprev))
			  {
				  int x0 = (xtest>0?(int)xtest:(int)xtest-1), y0 = (ytest>0?(int)ytest:(int)ytest-1), z0 = (ztest>0?(int)ztest:(int)ztest-1);
				  List<Entity> targets = worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(x0, y0, z0, x0+1, y0+1, z0+1));
				  if(targets!=null&&targets.size()>0)
				  {
					  return new Vector3(xtest, ytest, ztest);
				  }
			  }
			  yprev = ytest; xprev = xtest; zprev = ztest;
		}
		
		return null;
	}
	
	public static Vector3 firstEntityLocationExcluding(double range, Vector3 direction, Vector3 source, World worldObj, boolean effect, Entity excluded)
	{
		direction = vectorNormalize(direction);
		int n = 0;
		double xprev = source.x, yprev = source.y, zprev = source.z;
		double dx,dy,dz;
		double blocked = 0;
		
		Vector3 temp = new Vector3();
		
		for(double i=0;i<range;i+=0.0625)
		{
			dx = i*direction.x;
			dy = i*direction.y;
			dz = i*direction.z;
			
			  double  xtest = (source.x+dx),
					  ytest = (source.y+dy),
					  ztest = (source.z+dz);
			  
			  boolean check = isPointClearBlocks(xtest, ytest, ztest, worldObj);
			  blocked = Math.sqrt(dx*dx+dy*dy+dz*dz);

			  if(!check){
				  break;
			  }
			  
			  if(effect&&worldObj.isRemote)
			  {
				 worldObj.spawnParticle("flame",xtest, ytest, ztest, 0, 0, 0);
			  }
			  
			  if(!((int)xtest==(int)xprev&&(int)ytest==(int)yprev&&(int)ztest==(int)zprev))
			  {
				  int x0 = (xtest>0?(int)xtest:(int)xtest-1), y0 = (ytest>0?(int)ytest:(int)ytest-1), z0 = (ztest>0?(int)ztest:(int)ztest-1);
				  List<Entity> targets = worldObj.getEntitiesWithinAABBExcludingEntity(excluded, AxisAlignedBB.getBoundingBox(x0, y0, z0, x0+1, y0+1, z0+1));
				  if(targets!=null&&targets.size()>0)
				  {
					  List<Entity> ret = new ArrayList<Entity>();
					  for(Entity e: targets)
					  {
						  if(e instanceof EntityLiving)
						  {
							  ret.add(e);
						  }
					  }
					  if(ret!=null&&ret.size()>0)
						  return new Vector3(xtest, ytest, ztest);
				  }
			  }
			  yprev = ytest; xprev = xtest; zprev = ztest;
		}
		
		return null;
	}
	
	public static Vector3 firstEntityLocationFunctionExcluding(double range, Vector3 direction, Vector3 source, Vector3 acceleration, World worldObj, boolean effect, Entity excluded)
	{
		Vector3 normalizedDirection = vectorNormalize(direction);
		int n = 0;
		double xprev = source.x, yprev = source.y, zprev = source.z;
		double dx,dy,dz;
		boolean notNull = true;
		double closest = range;
		double blocked = 0;
		
		Vector3 temp = new Vector3();
		
		for(double i=0;i<range;i+=0.0625)
		{
			dx = i*(normalizedDirection.x);
			dy = i*(normalizedDirection.y);
			dz = i*(normalizedDirection.z);
			direction = direction.add(acceleration);
			normalizedDirection = vectorNormalize(direction);
			  double  xtest = (source.x+dx),
					  ytest = (source.y+dy),
					  ztest = (source.z+dz);
			  
			  boolean check = isPointClearBlocks(xtest, ytest, ztest, worldObj);
			  blocked = Math.sqrt(dx*dx+dy*dy+dz*dz);

			  if(!check){
				  break;
			  }
			  
			  if(effect&&worldObj.isRemote)
			  {
				 worldObj.spawnParticle("flame",xtest, ytest, ztest, 0, 0, 0);
			  }
			  
			  if(!((int)xtest==(int)xprev&&(int)ytest==(int)yprev&&(int)ztest==(int)zprev))
			  {
				  int x0 = (xtest>0?(int)xtest:(int)xtest-1), y0 = (ytest>0?(int)ytest:(int)ytest-1), z0 = (ztest>0?(int)ztest:(int)ztest-1);
				  List<Entity> targets = worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(x0, y0, z0, x0+1, y0+1, z0+1));
				  if(targets!=null&&targets.size()>0)
				  {
					  if(!(targets.size()==1&&targets.contains(excluded)))
					  return new Vector3(xtest, ytest, ztest);
				  }
			  }
			  yprev = ytest; xprev = xtest; zprev = ztest;
		}
		
		return null;
	}
	
	public  static boolean isEntityVisibleInDirection(Entity target, Vector3 direction, Vector3 source, World worldObj)
	{
		direction = vectorNormalize(direction);
		Vector3 vec = firstEntityLocation(distToEntity(source, target), direction, source, worldObj, false);
		return target==null?false:vec==null?false:vec.livingEntityInBox(worldObj)==null?false:vec.livingEntityInBox(worldObj).contains(target);
	}
	  
	public static boolean isPointClearBlocks(double x, double y, double z, IBlockAccess worldObj)
	{
		int x0 = (x>0?(int)x:(int)x-1), y0 = (y>0?(int)y:(int)y-1), z0 = (z>0?(int)z:(int)z-1);
		
		Block block = worldObj.getBlock(x0, y0, z0);
		
		if(block==null)
			return true;
		
		List<AxisAlignedBB> aabbs = new ArrayList();
		
		if(worldObj instanceof World)
		block.addCollisionBoxesToList((World)worldObj, x0, y0, z0, AxisAlignedBB.getBoundingBox(x, y, z, x, y, z), aabbs, null);
		
		if(aabbs.size()==0)
			return true;
		
		
		for(AxisAlignedBB aabb: aabbs)
		{
			if(aabb!=null)
			{
			if(y<=aabb.maxY&&y>=aabb.minY)
				return false;
			if(z<=aabb.maxZ&&z>=aabb.minZ)
				return false;
			if(x<=aabb.maxX&&x>=aabb.minX)
				return false;
			}
		}
		
		return true;
	}
	
	public boolean clearOfBlocks(World worldObj)
	{
		return isPointClearBlocks(x, y, z, worldObj);
	}
	
	public boolean pointClear(World worldObj)
	{
		return isPointClearBlocks(x, y, z, worldObj)&&livingEntityAtPoint(worldObj)==null;
	}
	  
	public boolean pointClearExcludingEntity(World worldObj, Entity e)
	{
		return isPointClearBlocks(x, y, z, worldObj)&&livingEntityAtPointExcludingEntity(worldObj, e)==null;
	}
	
	public boolean isPointClearOfaabb(double x, double y, double z, AxisAlignedBB aabb)
	{
		if(y<=aabb.maxY&&y>=aabb.minY)
			return false;
		if(z<=aabb.maxZ&&z>=aabb.minZ)
			return false;
		if(x<=aabb.maxX&&x>=aabb.minX)
			return false;
		
		return true;
	}
	public boolean isPointClearOfEntity(double x, double y, double z, Entity e)
	{
		AxisAlignedBB aabb = e.boundingBox;
		
		if(y<=aabb.maxY&&y>=aabb.minY)
			return false;
		if(z<=aabb.maxZ&&z>=aabb.minZ)
			return false;
		if(x<=aabb.maxX&&x>=aabb.minX)
			return false;
		
		return true;
	}
	
	public static Map<String, Fluid> fluids;
	
	 /**
     * Whether or not a certain block is considered a fluid.
     * @param world - world the block is in
     * @return if the block is a liquid
     */
    public boolean isFluid(World world)
    {
    	boolean ret;
    	Block id = getBlock(world);
    	int meta = getBlockMetadata(world);
    	
    	boolean fluidCheck = false;
    	
    	if(fluids==null)
    		fluids = FluidRegistry.getRegisteredFluids();
    	
    	for(String s:fluids.keySet())
    	{
    		fluidCheck = fluids.get(s).getBlock()==id&&fluids.get(s).getViscosity()!=Integer.MAX_VALUE;
    		if(fluidCheck)
    			return true;
    	}
    	
    	ret = fluidCheck || id == Blocks.water ||  id == Blocks.lava;
    	
    	return ret;
    }
    
    
    public void clear() 
    {
		this.set(new Vector3());
	}
    
	public Block getBlock(IBlockAccess worldObj) {
		return worldObj.getBlock(intX(), intY(), intZ());
	}
	public Block getBlock(IBlockAccess worldObj, ForgeDirection side) {
		return worldObj.getBlock(intX()+side.offsetX, intY()+side.offsetY, intZ()+side.offsetZ);
	}
	public int getBlockMetadata(IBlockAccess worldObj, ForgeDirection side) {
		return worldObj.getBlockMetadata(intX()+side.offsetX, intY()+side.offsetY, intZ()+side.offsetZ);
	}
	public int getBlockMetadata(IBlockAccess worldObj) {
		return worldObj.getBlockMetadata(intX(), intY(), intZ());
	}
	public Material getBlockMaterial(IBlockAccess worldObj)
	{
		return worldObj.getBlock(intX(), intY(), intZ()).getMaterial();
	}
	public boolean isAir(IBlockAccess worldObj) 
	{
		if(worldObj instanceof World)
		{	
			return getBlock(worldObj)==null||getBlockMaterial(worldObj)==null||getBlock(worldObj).isAir((World) worldObj, intX(), intY(), intZ())||getBlockMaterial(worldObj)==Material.air;//||worldObj.isAirBlock(intX(), intY(), intZ())
		}
		return getBlock(worldObj)==null||getBlockMaterial(worldObj)==null||getBlockMaterial(worldObj)==Material.air;//||worldObj.isAirBlock(intX(), intY(), intZ())
	}
	public TileEntity getTileEntity(IBlockAccess worldObj)
	{
		return worldObj.getTileEntity(intX(), intY(), intZ());
	}
	public TileEntity getTileEntity(IBlockAccess worldObj, ForgeDirection side)
	{
		return worldObj.getTileEntity(intX()+side.offsetX, intY()+side.offsetY, intZ()+side.offsetZ);
	}
	public void scheduleUpdate(World worldObj)
	{
		if(getBlock(worldObj)!=null)
		worldObj.scheduleBlockUpdate(intX(), intY(), intZ(), getBlock(worldObj), getBlock(worldObj).tickRate(worldObj));
	}
	public void breakBlock(World worldObj, boolean drop)
	{
		if(getBlock(worldObj)!=null)
			worldObj.setBlock(intX(), intY(), intZ(), Blocks.air);
	}
	public boolean isBlockOnSideSolid(IBlockAccess worldObj, ForgeDirection side)
	{
		boolean ret = worldObj.isSideSolid(intX()+side.offsetX, intY()+side.offsetY, intZ()+side.offsetZ, side.getOpposite(), false);
		return ret;
	}
}