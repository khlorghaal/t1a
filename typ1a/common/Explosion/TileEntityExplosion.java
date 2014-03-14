package com.typ1a.common.Explosion;
//package com.typ1a.common.Explosion;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import net.minecraft.block.Block;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.world.World;
//import t1a.common.utils.LinearAlgebra;
//import t1a.common.utils.ThreadSafeWorldOperations;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//
//public class TileEntityExplosion extends TileEntity {
//	public List<NukeParticle> particles = new ArrayList<NukeParticle>();
//	public List<NukeParticle> deadParticles = new ArrayList<NukeParticle>();
//	private List<Integer[]> destroyed = new ArrayList<Integer[]>();
//	private double x0,y0,z0;
//	private boolean firstTime = true;
//	private boolean read = false;
//	public double r0 = 0;
//	public int n = 0;
//	public int seed = new Random().nextInt(1000);
//	private double[] wind = {0,0};
//	private int num = 10;
//	public int radiation = 128;
////	private int falloutId = BlockFallout.instance.blockID;
//	double rMax;
//	public boolean first = true;
//	public int time = 0;
//
//	Random r = new Random();
//	
//	public TileEntityExplosion(){}
//
//	public TileEntityExplosion(World par1World, double r0, int n, double[] centre, List<Integer[]> destroyed) {
//		this.x0 = centre[0];
//		this.y0 = centre[2];
//		this.z0 = centre[1];
//		this.r0 = r0;
//		this.n = n;
//		this.destroyed=destroyed;
//		System.out.println("Spawned ");
//	}
//	
//	public TileEntityExplosion(World worldObj, double r0, int n, int seed){
//		this.r0 = r0;
//		this.n = n;
//		this.seed = seed;
//	}
//	
//    @SideOnly(Side.CLIENT)
//    public double getMaxRenderDistanceSquared()
//    {
//        return 65536.0D;
//    }
//
//	public void updateEntity(){	
//		
//		if(time>50){
//		this.x0 = xCoord;
//		this.y0 = yCoord;
//		this.z0 = zCoord;
//		explosionCalculations();
//		if(r0==0||(!firstTime&&particles.size()==0)||time>7000){
//			if(!worldObj.isRemote){
//				System.out.println("Despawning");
//				this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
//				this.worldObj.removeBlockTileEntity(xCoord, yCoord, zCoord);
//			}
//		}
//		}
//		if(time%100==99&&worldObj.isRemote){
//			if(particles.size()==0){
//				this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
//				this.worldObj.removeBlockTileEntity(xCoord, yCoord, zCoord);
//			}else{
//			System.out.println("Particles Remaining: "+particles.size());
//		}}
//		time++;
//	}
//
//	
//	private void explosionCalculations(){
//
//		ThreadSafeWorldOperations safe = new ThreadSafeWorldOperations();
//		if(particles.size()==0){
//			if(!firstTime&&particles.size()==0||time>7000){
//				this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
//				this.worldObj.removeBlockTileEntity(xCoord, yCoord, zCoord);
//			}else{
//				firstTime = false;
//				Random r = new Random();
//				r.setSeed(seed);
//				r0 = Math.min(r0,50); //Limits the size to "50"
//				for(int i = 0; i<n/(num+1); i++){
//					double x=10,y=10,z=10;
//					while(Math.sqrt(x*x+y*y+z*z)>1){
//						x = r.nextGaussian();
//						y = r.nextGaussian();
//						z = r.nextGaussian();
//					}
//					NukeParticle particle = new NukeParticle();
//					double[] vect = new double[] {x,y,z};
//					double[] vectHat = vec.vectorNormalize(vect);
//					double rho = Math.sqrt((x*x+y*y));
//					double	vx = 0.75*(rho>0.3?vectHat[0]*Math.exp((rho)*(rho)*(rho)):vectHat[0]/2-2*z/5)*Math.sqrt(10*r0/25),
//							vy = 0.75*(rho>0.3?vectHat[1]*Math.exp((rho)*(rho)*(rho)):vectHat[1]/2-2*z/5)*Math.sqrt(10*r0/25),
//							vz = 1.5*(Math.random()*Math.random()+(rho>0.3?Math.pow((vx*vx+vy*vy),1/5):(vectHat[2])*(vectHat[2])))*Math.sqrt(10*r0/49);
//					particle.x = vectHat[0]+x0;
//					particle.y = vectHat[2]+y0;
//					particle.z = vectHat[1]+z0;
//					particle.vx = vx;
//					particle.vy = vz;
//					particle.vz = vy;
//					particle.dvy = 0.01*(r.nextDouble()+r.nextDouble());
//					particles.add(particle);
//				}
//			}
//		}
//		else{
//			
//		for(NukeParticle particle : particles){
//			double 	x = particle.x,
//					y = particle.y,
//					z = particle.z,
//					vx = particle.vx,
//					vy = particle.vy,
//					vz = particle.vz;
//			
//		//	System.out.println(particle.x+" "+particle.y+" "+particle.z);
//			
//			double vMag = vec.vectorMag(new double[] {vx,vy,vz});
//			double[] vHat = vec.vectorNormalize(new double[] {vx,vy,vz});
//			
//			double[] current = {x,y,z};
//			double[] next = vec.findNextSolidBlock(worldObj, current, vHat, vMag);
//	
//			
//			if(!(next[0]==-1&&next[1]==-1&&next[2]==-1)&&!(next[0]==x0&&next[1]==y0&&next[2]==z0)){
//			//	System.out.println(Arrays.toString(next)+Arrays.toString(current)+Arrays.toString(vHat));
//				
//				int x1 = (int) (next[0]-vHat[0]), y1 = (int) (next[1]-vHat[1]), z1=(int)(next[2]-vHat[2]);
//				safe.safeLookUp(worldObj, x1, y1, z1);
//				int id = safe.ID;
//				int meta = safe.meta;
//				Block block = Block.blocksList[id];
//				
//				if(id==0){//If 1 ahead is air, add a fallout stack
//					deadParticles.add(particle);
//					
//					//only place/modify on server
//					if(!worldObj.isRemote){
////						safe.safeSet(worldObj,x1, y1, z1, BlockFallout.instance.blockID, num);
//					}
//					
//					
//				}
////				else if(id==BlockFallout.instance.blockID){ //if 1 ahead is fallout
////					if((meta+num) < 15){ //if the fallout is not full, add a stack
////						deadParticles.add(particle);
////						
////						//only place/modify on server
////						if(!worldObj.isRemote){
////							safe.safeSetMeta(worldObj,x1, y1, z1, meta+num);
////						}
////						
////						
////					}else{ //if the fallout is full, put fallout where currently is
////						deadParticles.add(particle);
////						safe.safeLookUp(worldObj,x1-2*vHat[0], y1-2*vHat[1], z1-2*vHat[2]);
////						//only place/modify on server
////						if(!worldObj.isRemote&&safe.ID==0){
////							safe.safeSet(worldObj,x1-2*vHat[0], y1-2*vHat[1], z1-2*vHat[2], BlockFallout.instance.blockID, num);
////						}else if(!worldObj.isRemote&&safe.ID==BlockFallout.instance.blockID){
////							safe.safeSetMeta(worldObj,x1-2*vHat[0], y1-2*vHat[1], z1-2*vHat[2], meta+num);
////						}
////					}
////				}
//			}
//		
//			
//			x = x + vx;
//			y = y + vy;
//			z = z + vz;
//			vy -= particle.dvy;
//			this.wind = safe.getWind(worldObj, x, z);
//			particle.x = x;
//			particle.y = y;
//			particle.z = z;
//			particle.vx = vx*0.9 - 0.001*vx + wind[0]*(y/r0)*(y/r0);
//			particle.vy = vy*0.9 - 0.001*vy;
//			particle.vz = vz*0.9 - 0.001*vz + wind[1]*(y/r0)*(y/r0);
//			
//			
//			
//		}
//		for(NukeParticle p : deadParticles){
//			particles.remove(p);
//			}
//		}
//		deadParticles.clear();
//		
//		
//	}
//	
//	 @Override
//     public boolean canUpdate(){ return true; }
//	
//	
//	public static class NukeParticle{
//		public double x,y,z,vx,vy,vz,dvy;
//		
//		public static NukeParticle readFromNBT(NBTTagCompound cmpnd, String tag){
//			
//			
//			NukeParticle tempParticle = new NukeParticle();
//			tempParticle.x = cmpnd.getDouble(tag+"x");
//			tempParticle.y = cmpnd.getDouble(tag+"y");
//			tempParticle.z = cmpnd.getDouble(tag+"z");
//			tempParticle.vx = cmpnd.getDouble(tag+"vx");
//			tempParticle.vy = cmpnd.getDouble(tag+"vy");
//			tempParticle.vz = cmpnd.getDouble(tag+"vz");
//			tempParticle.dvy = cmpnd.getDouble(tag+"dvy");
//			
//			if(tempParticle.x==tempParticle.y&&tempParticle.x==tempParticle.z&&tempParticle.x==0){
//				return null;
//			}
//			return tempParticle;
//			
//		}
//		
//		public static NBTTagCompound writeToNBT(NBTTagCompound cmpnd,String tag ,NukeParticle p){
//
//			cmpnd.setDouble(tag+"x",p.x);
//			cmpnd.setDouble(tag+"y",p.y);
//			cmpnd.setDouble(tag+"z",p.z);
//			cmpnd.setDouble(tag+"vx",p.vx);
//			cmpnd.setDouble(tag+"vy",p.vy);
//			cmpnd.setDouble(tag+"vz",p.vz);
//			cmpnd.setDouble(tag+"dvy",p.dvy);
//
//			return cmpnd;
//		}
//		
//	}
//	
//	
//	public void readFromNBT(NBTTagCompound cmpnd) {
//	/*
//		super.readFromNBT(cmpnd);
//		firstTime = cmpnd.getBoolean("firstTime");
//		first = cmpnd.getBoolean("first");
//		n = cmpnd.getInteger("n");
//		r0 = cmpnd.getDouble("r0");
//		seed = cmpnd.getInteger("seed");
//		radiation = cmpnd.getInteger("radiation");
//		x0 = cmpnd.getDouble("x0");
//		y0 = cmpnd.getDouble("y0");
//		z0 = cmpnd.getDouble("z0");
//		time = cmpnd.getInteger("time");
//		
//		Integer i = 0;
//		while(NukeParticle.readFromNBT(cmpnd,Integer.toString(i))!=null){
//			particles.add(NukeParticle.readFromNBT(cmpnd,Integer.toString(i)));
//    		i++;
//    	}
//		//*/
//	}
//
//	
//	public void writeToNBT(NBTTagCompound cmpnd) {
//	/*	
//		super.writeToNBT(cmpnd);
//		cmpnd.setBoolean("firstTime", firstTime);
//		cmpnd.setBoolean("first", first);
//		cmpnd.setInteger("n",particles.size()*(num+1));
//		cmpnd.setDouble("r0",r0);
//		cmpnd.setInteger("seed", seed);
//		cmpnd.setInteger("radiation",radiation);
//		cmpnd.setDouble("x0",x0);
//		cmpnd.setDouble("y0",y0);
//		cmpnd.setDouble("z0",z0);
//		cmpnd.setInteger("time", time);
//		
//		for(Integer i = 0;i<particles.size(); i++){
//			NukeParticle.writeToNBT(cmpnd,Integer.toString(i) ,particles.get(i));
//		}
//		
//	//*/
//	}
//}