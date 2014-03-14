package com.typ1a.common.Explosion;

import io.netty.buffer.ByteBuf;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.typ1a.common.Maths;
import com.typ1a.common.T1A;
import com.typ1a.common.utils.Vector3;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityExplosionFX extends Entity  implements IEntityAdditionalSpawnData {
	public double r0 = 0;
	double rMax;
	public float[] pos3;
	public float[] v;
	public float[] heat;
	static final int PSIZ= 40;
	public float[][][] [] flow;
	public float[][][] [] flowtmp;
	int rseed= T1A.random.nextInt();
	Random r;

	public EntityExplosionFX(World par1World) {
		super(par1World);
		this.ignoreFrustumCheck = true;
	}
	public EntityExplosionFX(World worldObj, double[] centre,double r0) {
		this(worldObj);
		if(worldObj.isRemote){
			this.setDead();
			return;
		}
		this.setPosition(centre[0], centre[1], centre[2]);
		this.r0 = Math.min(r0,100);
		this.rMax = Math.min(2*r0,128);
		worldObj.spawnEntityInWorld(this);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double par1){  return true; }



	public int toGridOrdinate(int indx){
		return Maths.clamp( ((int)pos3[indx])+PSIZ/2,  0, PSIZ-1);
	}
	public void setGridLerped(float data, int x, int y, int z){
		final float
		xm1= x%1,
		ym1= y%1,
		zm1= z%1;

		//det heading of cube vol=4 rel to param cell
		final boolean
		xp= (xm1)>0.5f,
		yp= (ym1)>0.5f,
		zp= (zm1)>0.5f;

		//orig, adjacent, diagonal
		//c0+ca1+ca2+cd

	}

	@Override
	public void onUpdate(){
		if(this.ticksExisted>15*200)
			this.setDead();
//		if(true) return;

		if(worldObj.isRemote && ticksExisted>2){
			for(int x=1; x!=PSIZ-1; x++){//advection-diffusion transport
				for(int y=1; y!=PSIZ-1; y++){
					for(int z=1; z!=PSIZ-1; z++){
						final float 
						k= .99f,//diffusion constant
						kcom= 1f-k,
						thetamul= 0.001f;//vorticity multiplier
						
						//diffuse
						//x
						flowtmp[x][y][z][0]+= flow[x-1][y][z][0];//-00
						flowtmp[x][y][z][0]+= flow[x+1][y][z][0];//+--
						flowtmp[x][y][z][0]+= flow[x][y+1][z][0];//0+0
						flowtmp[x][y][z][0]+= flow[x][y-1][z][0];//0-0
						flowtmp[x][y][z][0]+= flow[x][y][z+1][0];//00+
						flowtmp[x][y][z][0]+= flow[x][y][z-1][0];//00-
						flowtmp[x][y][z][0]*= k;
						flowtmp[x][y][z][0]+= kcom*flow[x][y][z][0];//000
						//y
						flowtmp[x][y][z][1]+= flow[x-1][y][z][1];//-00
						flowtmp[x][y][z][1]+= flow[x+1][y][z][1];//+--
						flowtmp[x][y][z][1]+= flow[x][y+1][z][1];//0+0
						flowtmp[x][y][z][1]+= flow[x][y-1][z][1];//0-0
						flowtmp[x][y][z][1]+= flow[x][y][z+1][1];//00+
						flowtmp[x][y][z][1]+= flow[x][y][z-1][1];//00-
						flowtmp[x][y][z][1]*= k;
						flowtmp[x][y][z][1]+= kcom*flow[x][y][z][1];//000
						//z
						flowtmp[x][y][z][2]+= flow[x-1][y][z][2];//-00
						flowtmp[x][y][z][2]+= flow[x+1][y][z][2];//+--
						flowtmp[x][y][z][2]+= flow[x][y+1][z][2];//0+0
						flowtmp[x][y][z][2]+= flow[x][y-1][z][2];//0-0
						flowtmp[x][y][z][2]+= flow[x][y][z+1][2];//00+
						flowtmp[x][y][z][2]+= flow[x][y][z-1][2];//00-
						flowtmp[x][y][z][2]*= k;
						flowtmp[x][y][z][2]+= kcom*flow[x][y][z][2];//000

						//advect
						
						
						final float //vorticity
						//pattern 
						//rotation axis ==i; j,k interchangeable
						//j+1, a 
						//k+1, -b
						//j-1, -a
						//k-1, b
						shearx=(+flow[x][y][z+1][1]
								-flow[x][y+1][z][2]
								-flow[x][y][z-1][1]
								+flow[x][y-1][z][2]),
								
						sheary=(+flow[x+1][y][z][2]
								-flow[x][y][z+1][0]
								-flow[x-1][y][z][2]
								+flow[x][y][z-1][0]),
								
						shearz=(+flow[x+1][y][z][1]
								-flow[x][y+1][z][0]
								-flow[x-1][y][z][1]
								+flow[x][y-1][z][0]),

						//when infinitesimal,
						//angle axis is simply the sum of euler rotations
						theta= MathHelper.sqrt_float(shearx*shearx + sheary*sheary + shearz*shearz),
						omega_x= shearx/theta,
						omega_y= sheary/theta,
						omega_z= shearz/theta,
						
						//non pi/tau radians are actually useful for this
						//as the instant shear is approximated to circumferential velocity
						sinTheta= MathHelper.sin(theta*thetamul),
						cosTheta= MathHelper.cos(theta*thetamul),
						cosThetaCom= 1f-cosTheta,
						
						vx= flowtmp[x][y][z][0]/6,
						vy= flowtmp[x][y][z][1]/6,
						vz= flowtmp[x][y][z][2]/6,
						
						omegaDotV= omega_x*vx + omega_y*vy + omega_z*vz,
						omegaCrossV_x= omega_y*vz + omega_z*vy,
						omegaCrossV_y= omega_z*vx + omega_x*vz,
						omegaCrossV_z= omega_x*vy + omega_y*vx;

						//rodrigues
//						flowtmp[x][y][z][0]= ( vx*cosTheta + omegaCrossV_x*sinTheta + omega_x*omegaDotV*oneMinusCosTheta);
//						flowtmp[x][y][z][1]= ( vy*cosTheta + omegaCrossV_y*sinTheta + omega_y*omegaDotV*oneMinusCosTheta);
//						flowtmp[x][y][z][2]= ( vz*cosTheta + omegaCrossV_z*sinTheta + omega_z*omegaDotV*oneMinusCosTheta);
						flowtmp[x][y][z][0]/=6;
						flowtmp[x][y][z][1]/=6;
						flowtmp[x][y][z][2]/=6;
					}
				}
 			}
			for(int x=0; x!=PSIZ; x++){//arraycopy flowtmp into flow
				for(int y=0; y!=PSIZ; y++){
					for(int z=0; z!=PSIZ; z++){
						flow[x][y][z][0] = flowtmp[x][y][z][0];
						flow[x][y][z][1] = flowtmp[x][y][z][1];
						flow[x][y][z][2] = flowtmp[x][y][z][2];
					}
				}
			}

			for(int i=0; i!=pos3.length-3; i+=3){//move
				int ix= toGridOrdinate(i);
				int iy= toGridOrdinate(i+1);
				int iz= toGridOrdinate(i+2);
				v[i  ]= (flow[ix][iy][iz][0] )*.98f;
				v[i+1]= (flow[ix][iy][iz][1] )*.98f+	heat[i/3]-0.011f;
				v[i+2]= (flow[ix][iy][iz][2] )*.98f;

				pos3[i  ]+= v[i];
				pos3[i+1]+= v[i+1];
				pos3[i+2]+= v[i+2];
			}
		}
	}

	@Override
	protected void entityInit(){}
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound){this.setDead();}
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound){}

	@Override
	public void writeSpawnData(ByteBuf data){
		data.writeInt(rseed);
		data.writeDouble(this.r0);
	}

	@Override
	public void readSpawnData(ByteBuf data){
		this.r= new Random(data.readInt());
		this.r0 = data.readDouble();

		pos3= new float[1500*3];
		v= new float[pos3.length];
		flow= new float[PSIZ][PSIZ][PSIZ][3];
		flowtmp= new float[PSIZ][PSIZ][PSIZ][3];
		heat= new float[pos3.length/3];
		for(int i=0; i!=pos3.length;){
			heat[i/3]=.01f;
			float theta= (r.nextFloat()-.5f)*6.282f;
			float phi= (r.nextFloat()-.5f)*6.282f;
			float mag= r.nextFloat()*8f;
			final Vector3 ip= new Vector3(mag, theta, phi).toCartesian();
			pos3[i]= (float) ip.x;
			v[i]= (float) ip.x/64;
			i++;
			pos3[i]= (float) ip.y;
			v[i]=(float)(ip.y)/64;
			i++;
			pos3[i]= (float) ip.z;
			v[i]= (float) ip.z/64;
			i++;

			flow[20][20][20][1]=.9f;
			//			flow[31][32][32][0]=-1.7f;
			//			flow[33][32][32][0]=1.7f;
		}
	}


}
