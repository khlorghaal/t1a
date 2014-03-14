package com.typ1a.common.Projectiles;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.typ1a.common.Explosion.ExplosionCustom;
import com.typ1a.common.Missiles.EntityMissile;
import com.typ1a.common.Robots.Ballistic;
import com.typ1a.common.utils.RayHelper;
import com.typ1a.common.utils.Vector3;

public class EntityBullet extends EntityProjectile{

	public static final int NORM=0, TRACE=1, HE=2, MININUKE=0x2000;
	public static final int[] BTYPES= new int[]{NORM, TRACE, HE};
	public static final String[] TYPENAMES= new String[]{"Copper Jacket", "Tracer", "High Explosive"};
	public int btype;
	public boolean silenced;

	public EntityBullet(World world){
		super(world);
		final Class cls= this.getClass();
		if(cls == EntityBullet.class 
				|| ( cls==EntityGrenade.class && btype!=EntityGrenade.HE))
			this.setDead();
	}

	public EntityBullet(Barrel barrel, double s, double deviation, Random r, float dmg, int btype){
		super(barrel, s, deviation, r, dmg);
		this.btype= btype;
		worldObj.spawnEntityInWorld(this);
		this.silenced= false;
	}
	public EntityBullet(Barrel barrel, double s, double deviation, Random r, float dmg, int btype, boolean silenced){
		this(barrel, s, deviation, r, dmg, btype);
		this.silenced= silenced;
	}

	@Override
	public void onUpdate(){
		//						this.setDead();
		//						if(true)
		//						return;
		firstTimeSpawning= false;

		if(this.ticksExisted>220)
			this.setDead();

		if(!this.worldObj.isRemote){
			if(abs(v[1]+v[1]+v[2])>0.05)
				this.rotationPitch= (float) toDegrees(atan2(this.v[1], sqrt(v[0]*v[0] + v[2]*v[2])));
		}

		if(!(isCollided || wasCollided)){
			if(getBlockBulletEffect(entering)!=2)
				this.v[1]+= Ballistic.G;
			else
				this.v[1]+= Ballistic.G/6;//in liquid
		}

		//		if(!this.worldObj.isRemote)

		this.checkHit();
		
		this.moveEntity();

//		motionX= v[0];
//		motionY= v[1];
//		motionZ= v[2];
	}

	@Override
	protected void checkHit(){

		//		final Entity EntityHit= RayHelper.getEntityNearestFromRay(
		//				worldObj, new Entity[]{this, owner},
		//				posX, posY, posZ, v[0], v[1], v[2]);
		//		
		//		if(EntityHit!=null){
		//			System.out.println(EntityHit);
		//			this.hit(EntityHit);
		//		}
		List<Entity> l= (List<Entity>)this.worldObj.getEntitiesWithinAABBExcludingEntity(this
				,this.boundingBox.expand(abs(this.v[0]), abs(this.v[1]), abs(this.v[2])) );

		if(l.isEmpty())
			return;

		for(Entity e: l.toArray(new Entity[]{}))
			if(e instanceof EntityProjectile
					|| e instanceof EntityItem
					|| e instanceof EntityXPOrb
					|| e==owner
					|| e instanceof EntityItemFrame
					|| e instanceof EntityPainting)
				l.remove(e);

		if(l.isEmpty())
			return;

//		this.hit(l.get(0));
		this.hit( RayHelper.getClosestEntity(l, this.posX, this.posY, this.posZ));
		onFirstCollision();
	}

	@Override
	protected void onFirstCollision(){
		if(!worldObj.isRemote){
			if(btype==HE){
				if( owner instanceof EntityPlayer
						&& worldObj.canMineBlock( (EntityPlayer)owner, (int)(posX),(int)(posY),(int)(posZ))
						){
					if(entering!=null
							&& entering.getExplosionResistance(owner)<50
							){
						worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, 
								new ItemStack(worldObj.getBlock((int)(posX),(int)(posY),(int)(posZ)))));
						worldObj.setBlock((int)(posX),(int)(posY),(int)(posZ), Blocks.air);
						worldObj.playSoundAtEntity(this, "t1a:firecracker", 2.5f, 1.7f);
					}
				}
				setDead();
			}
			else if(btype==MININUKE){
				ExplosionCustom.doExplosion(new Vector3(this), this.worldObj, 80, 30);
			}
		}
		else
			worldObj.playSoundAtEntity(this, "t1a:ping", 1f, 1f);
	}

	protected void hit(Entity entity){

		//		EntityCreeper fjorg= new EntityCreeper(world);
		//		fjorg.setPosition(player.posX, player.posY, player.posZ);
		//		fjorg.func_94058_c("Fjorg");
		//		fjorg.initCreature();
		//		world.spawnEntityInWorld(fjorg);

		if(entity instanceof EntityProjectile)
			return;

		if(entity!=null){
			if(entity instanceof EntityMissile)
				entity.setDead();
			else{				
				entity.hurtResistantTime=0;
				float dmg= (float)( this.dmg * getSpeed()/speed0 );
				if(btype==HE)
					dmg*=1.2;

				if(dmg>1)
					entity.attackEntityFrom(DamageSource.causePlayerDamage(
							(EntityPlayer) owner), dmg);
				final float maxKnock=0.01f;
				if(abs(entity.motionX) <maxKnock)
					entity.motionX+= v[0]/16;
				if(abs(entity.motionY) <maxKnock)
					entity.motionY+= v[1]/16;
				if(abs(entity.motionZ) <maxKnock)
					entity.motionZ+= v[2]/16;
			}
		}
		this.setDead();
		//TODO FX
	}

}
