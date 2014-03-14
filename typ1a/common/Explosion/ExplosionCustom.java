package com.typ1a.common.Explosion;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.typ1a.common.utils.MainThreadQueue;
import com.typ1a.common.utils.Vector3;

public class ExplosionCustom{

	public static final double sqrt3 = Math.sqrt(3.0D);
	public static double pi = Math.PI;
	public static double sqrt2 = Math.sqrt(2.0D);
	public static byte MAX_RADIUS = 80;
	public static int DUSTID = 0;

	public static void doExplosion(final Vector3 centre, final World worldObj,
			final int radius, final double strength) {
		ExplosionExecutor.execute(new Runnable() {
			public void run() {
				Cruncher.destroyInRangeV4(centre, worldObj, radius, strength, false,false);
			}
		});
	}

	public void kineticImpactor(final World worldObj, final Vector3 velocity,
			final Vector3 hitLocation, final Vector3 acceleration,
			final double mass, final float energy) {
		doKineticImpactor(worldObj, velocity, hitLocation, acceleration, mass,
				energy);

	}

	public void doKineticImpactor(World worldObj, Vector3 velocity,
			Vector3 hitLocation, Vector3 acceleration, double mass, float energy) {
		if (mass < 0 || energy <= 0) {
			return;
		}
		double eid = Math.random();
		System.out.println("energy: " + energy + " Location: "
				+ hitLocation.toString());
		int n = 0;
		List<Vector3> locations = new ArrayList<Vector3>();
		List<Float> blasts = new ArrayList<Float>();

		float resist = hitLocation.getExplosionResistance(worldObj);
		float blast = Math.min(resist, energy);

		if (resist > energy) {
			hitLocation = hitLocation.subtract(velocity.normalize());
			Cruncher.destroyInRangeV4(hitLocation, worldObj,
					(int) (int) Math.max((energy / 2), 10), energy, true, true);
			return;
		}

		Vector3 absorbedLoc = new Vector3();
		float remainingEnergy = 0;
		Block id = hitLocation.getBlock(worldObj);
		energy -= resist;

		while (energy > 0) {
			locations.add(hitLocation.subtract(velocity.normalize()));
			blasts.add(blast);
			hitLocation = hitLocation.add(velocity.normalize());
			id = hitLocation.getBlock(worldObj);
			velocity.add(acceleration);
			resist = (float) Math.max(
					hitLocation.getExplosionResistance(worldObj), 0);
			blast = Math.min(resist, energy);
			if (resist > energy) {
				absorbedLoc.set(hitLocation);
				remainingEnergy = energy;
				break;
			}
			energy -= (resist + 0.1);
		}

		n = locations.size();
		if (n != 0)

			for (int i = 0; i < n; i++) {
				Vector3 source = locations.get(i);
				float strength = Math.min(blasts.get(i) * blasts.get(i)
						* (((float) n - (float) i) / ((float) n)), 256);
				if (worldObj.doChunksNearChunkExist(source.intX(),
						source.intY(), source.intZ(), (int) (strength / 2))) {
					if (strength != 0)
						Cruncher.destroyInRangeV4(source, worldObj,
								(int) Math.max(10, strength / 2), strength,
								true, true);
				}
			}
		if (remainingEnergy > 10) {
			absorbedLoc = absorbedLoc.subtract(velocity.normalize());
			Cruncher.destroyInRangeV4(absorbedLoc, worldObj, 50,
					remainingEnergy, true, true);
		}
	}


	public static class Cruncher {
		public Double[] set1 = new Double[] { 123456d };
		public Double[] set2 = new Double[] { 123456d };
		public Double[] set3 = new Double[] { 123456d };
		public Double[] set4 = new Double[] { 123456d };
		public Integer[] set5 = new Integer[] { 123456 };
		public Integer[] set6 = new Integer[] { 123456 };
		public Integer[] set7 = new Integer[] { 123456 };
		public Integer[] set8 = new Integer[] { 123456 };
		public Double[] set9 = new Double[] { 123456d };
		public Double[] set10 = new Double[] { 123456d };
		public byte[][] set11 = new byte[][] { { 123 } };
		public int n;
		public Boolean[] done = { Boolean.valueOf(false) };

		public static final byte size = (byte) ExplosionCustom.MAX_RADIUS;

		public static final ArrayList<Integer> locs = new ArrayList<Integer>();
		private static boolean init = true;


		double temp = 0.0D;
		int temp2 = 0;
		float temp3 = 0.0F;
		byte[] temp4 = { 0 };

		public void sort2(Double[] vals1, byte[][] quadrant) {
			if ((vals1 == null) || (vals1.length == 0)) {
				return;
			}
			this.set1 = vals1;
			this.set11 = quadrant;
			this.n = this.set1.length;

			quicksort(0, this.n - 1);
		}

		public void sort22(Double[] vals1, Integer[] vals2) {
			if ((vals1 == null) || (vals1.length == 0)) {
				return;
			}
			this.set1 = vals1;
			this.set5 = vals2;
			this.n = this.set1.length;

			quicksort(0, this.n - 1);
		}

		public void sort3(Double[] vals1, Double[] vals2, Double[] vals3) {
			if ((vals1 == null) || (vals1.length == 0)) {
				return;
			}
			this.set1 = vals1;
			this.set2 = vals2;
			this.set3 = vals3;
			this.n = this.set1.length;

			quicksort(0, this.n - 1);
		}

		public void sort5(Double[] vals1, Integer[] vals2, Integer[] vals3,
				Integer[] vals4, Integer[] vals5) {
			if ((vals1 == null) || (vals1.length == 0)) {
				return;
			}
			this.set1 = vals1;
			this.set5 = vals2;
			this.set6 = vals3;
			this.set7 = vals4;
			this.set8 = vals5;
			this.n = this.set1.length;

			quicksort(0, this.n - 1);
		}

		public void sort6(Double[] vals1, Integer[] vals2, Integer[] vals3,
				Integer[] vals4, Double[] vals5, Double[] vals6) {
			if ((vals1 == null) || (vals1.length == 0)) {
				return;
			}
			this.set1 = vals1;
			this.set5 = vals2;
			this.set6 = vals3;
			this.set7 = vals4;
			this.set2 = vals5;
			this.set3 = vals6;
			this.n = this.set1.length;

			quicksort(0, this.n - 1);
		}

		public void sort7(Double[] vals1, Integer[] vals2, Integer[] vals3,
				Integer[] vals4, Double[] vals5, Double[] vals6, Integer[] vals7) {
			if ((vals1 == null) || (vals1.length == 0)) {
				return;
			}
			this.set1 = vals1;
			this.set5 = vals2;
			this.set6 = vals3;
			this.set7 = vals4;
			this.set2 = vals5;
			this.set3 = vals6;
			this.set8 = vals7;
			this.n = this.set1.length;

			quicksort(0, this.n - 1);
		}

		private void quicksort(int low, int high) {
			int i = low;
			int j = high;
			double pivot = this.set1[(low + (high - low) / 2)].doubleValue();
			while (i <= j) {
				while (this.set1[i].doubleValue() < pivot)
					i++;
				while (this.set1[j].doubleValue() > pivot)
					j--;
				if (i <= j) {
					exchange(i, j);
					i++;
					j--;
				}
			}
			if (low < j)
				quicksort(low, j);
			if (i < high)
				quicksort(i, high);
		}

		private void exchange(int i, int j) {
			if ((this.set1[0] != 123456d) || (this.set1.length == this.n)) {
				this.temp = this.set1[i].doubleValue();
				this.set1[i] = this.set1[j];
				this.set1[j] = Double.valueOf(this.temp);
			}
			if ((this.set2[0] != 123456d) || (this.set2.length == this.n)) {
				this.temp = this.set2[i].doubleValue();
				this.set2[i] = this.set2[j];
				this.set2[j] = Double.valueOf(this.temp);
			}
			if ((this.set3[0] != 123456d) || (this.set3.length == this.n)) {
				this.temp = this.set3[i].doubleValue();
				this.set3[i] = this.set3[j];
				this.set3[j] = Double.valueOf(this.temp);
			}
			if ((this.set4[0] != 123456d) || (this.set4.length == this.n)) {
				this.temp = this.set4[i].doubleValue();
				this.set4[i] = this.set4[j];
				this.set4[j] = Double.valueOf(this.temp);
			}
			if ((this.set5[0] != 123456) || (this.set5.length == this.n)) {
				this.temp2 = this.set5[i].intValue();
				this.set5[i] = this.set5[j];
				this.set5[j] = (this.temp2);
			}
			if ((this.set6[0] != 123456) || (this.set6.length == this.n)) {
				this.temp2 = this.set6[i].intValue();
				this.set6[i] = this.set6[j];
				this.set6[j] = (this.temp2);
			}
			if ((this.set7[0] != 123456) || (this.set7.length == this.n)) {
				this.temp2 = this.set7[i].intValue();
				this.set7[i] = this.set7[j];
				this.set7[j] = (this.temp2);
			}
			if ((this.set8[0] != 123456) || (this.set8.length == this.n)) {
				this.temp2 = this.set8[i].intValue();
				this.set8[i] = this.set8[j];
				this.set8[j] = (this.temp2);
			}
			if ((this.set9[0] != 123456d) || (this.set9.length == this.n)) {
				this.temp = this.set9[i].doubleValue();
				this.set9[i] = this.set9[j];
				this.set9[j] = Double.valueOf(this.temp);
			}
			if ((this.set10[0] != 123456d) || (this.set10.length == this.n)) {
				this.temp = this.set10[i].doubleValue();
				this.set10[i] = this.set10[j];
				this.set10[j] = Double.valueOf(this.temp);
			}
			if ((this.set11.length == this.n)) {
				this.temp4 = this.set11[i];
				this.set11[i] = this.set11[j];
				this.set11[j] = this.temp4;
			}
		}

		public static byte[][] populatePoints() {
			byte[][] quadrant;
			Double[] radii;
			List<byte[]> templist = new ArrayList<byte[]>();
			List<Double> tempRadii = new ArrayList<Double>();
			Cruncher sort = new Cruncher();

			for (byte z = (byte) -size; z <= size; z++)
				for (byte y = (byte) -size; y <= size; y++)
					for (byte x = (byte) -size; x <= size; x++) {

						double radSq = (double) (x * x + y * y + z * z);
						if (radSq > size * size)
							continue;

						templist.add(new byte[] { x, y, z });
						tempRadii.add(radSq);
					}
			quadrant = templist.toArray(new byte[0][0]);
			radii = tempRadii.toArray(new Double[0]);
			templist.clear();
			tempRadii.clear();

			sort.sort2(radii, quadrant);

			radii = sort.set1;
			quadrant = sort.set11;

			sort.set1 = null;
			sort.set11 = null;

			return quadrant;
		}

		static boolean initialized= false;
		static {//populate int

			new Thread(new Runnable() {
				public void run() {

					System.out.println("Initializing explosion volume");

					Integer[] quadrant;
					Double[] radii;
					List<Integer> templist = new ArrayList<Integer>();
					List<Double> tempRadii = new ArrayList<Double>();
					Cruncher sort = new Cruncher();

					for (int z = -size; z <= size; z++)
						for (int y = -size; y <= size; y++)
							for (int x = -size; x <= size; x++) {

								double radSq = (double) (x * x + y * y + z * z);
								if (radSq > size * size)
									continue;
								int i = ((x + size) + (y + size) * 256 + (z + size) * 256 * 256);
								// System.out.println(x+" "+y+" "+z+" "+i+" "+((i&255)-size)+" "+(((i/256)&255)-size)+" "+(((i/(256*256))&255)-size)+" ");
								templist.add(i);
								tempRadii.add(radSq);
							}
					quadrant = templist.toArray(new Integer[0]);
					radii = tempRadii.toArray(new Double[0]);
					templist.clear();
					tempRadii.clear();

					sort.sort22(radii, quadrant);

					radii = sort.set1;
					quadrant = sort.set5;

					sort.set1 = null;
					sort.set5 = null;

					int[] ret = new int[quadrant.length];
					for (int i = 0; i < quadrant.length; i++)
						ret[i] = quadrant[i];

					for (int i : ret) {
						locs.add(i);
					}

					System.out.println("Explosion volume Initialized");
					initialized= true;
				}
			}).start();
		}

		public static int getIndex(Vector3 rHat, int linearFactor) {
			return (int) (rHat.x * linearFactor)
					+ linearFactor
					+ 1
					+ (((int) (rHat.y * linearFactor) + linearFactor + 1) * linearFactor)
					+ (((int) (rHat.z * linearFactor) + linearFactor + 1)
							* linearFactor * linearFactor);
		}

		public static boolean isInShadow(Vector3 source, Vector3 near,
				Vector3 far) {
			double dNear = near.distanceTo(source);
			double dFar = far.distanceTo(source);
			double dNF = near.distanceTo(far);
			if (dNear >= dFar) {
				return false;
			}
			Vector3 nearHat = near.normalize();
			Vector3 farHat = far.normalize();

			double factor = 1 - (dNear) / dFar;

			if (factor < 0.5)
				return false;

			if (nearHat.dot(farHat) > factor) {
				return true;
			}

			return false;
		}

		public static double dot(int a, Vector3 b) {
			int x = (((a) & 255) - size), y = (((a / 256) & 255) - size), z = (((a / (256 * 256)) & 255) - size);
			Vector3 c = new Vector3(x, y, z).normalize();
			return b.dot(c);
		}

		public static void destroyInRangeV4(final Vector3 centre,
				final World worldObj, int radi, final double strength,
				final boolean dust, final boolean melt) {

			while(!initialized){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1){e1.printStackTrace();}
			}

			worldObj.theProfiler.startSection("explosion");
			long startTime = System.nanoTime();
			final double scaleFactor = 1000;

			int linearFactor = size;

			double x0 = centre.x;
			double y0 = centre.y;
			double z0 = centre.z;

			double rad;

			final int radius = Math.min(
					(int) Math.sqrt(strength * scaleFactor / 0.25),
					Math.min(radi, size));

			int index;

			double radSq = radius * radius, rMag;

			int n = 0;
			int l = 0;
			int g = 0;
			int f = 0, x=0, y=0, z=0;
			float resist;

			Vector3 r, rAbs, rHat, rTest, rTestPrev, rTestAbs;

			long estimatedTime;
			final Set<Integer> map = new HashSet<Integer>();
			final List<Vector3> removed = new ArrayList<Vector3>();

			final Map<Integer, Float> damps = new HashMap<Integer, Float>();
			final BitSet blocked = new BitSet();
			final List<Integer> toRemove = new ArrayList<Integer>();
			final Map<EntityLivingBase, Float> damages = new HashMap<EntityLivingBase, Float>();
			final List<Chunk> affected = new ArrayList<Chunk>();

			final Map<Integer, Float> resists = new HashMap<Integer, Float>();

			MainThreadQueue.add(new Runnable(){			
				public void run() {
					new EntityExplosionFX(worldObj, centre.toArray(), radius);
			}});
			
			for (int i : locs) {
				byte[] s = new byte[] { (byte) ((i & 255) - size),
						(byte) (((i / 256) & 255) - size),
						(byte) (((i / (256 * 256)) & 255) - size) };

				x = s[0];
				y = s[1];
				z = s[2];
				double rSq = x * x + y * y + z * z;
				if (rSq > radSq)
					continue;

				r = new Vector3(x, y, z);
				rAbs = r.add(centre);

				if (rAbs.isAir(worldObj) && !(x == 0 && y == 0 && z == 0))
					continue;
				float explrst= rAbs.getExplosionResistance(worldObj);
				if(explrst==100) explrst=0.05f;//is water or lava
				if(explrst>strength * scaleFactor / r.magSq())
					continue;

				rHat = r.normalize();

				index = getIndex(rHat, linearFactor);

				if (blocked.get(index)) {
					continue;
				}
				boolean stop = false;
				rTest = new Vector3();
				rTestPrev = new Vector3();
				rMag = r.mag();
				float dj = 1;
				resist = 0;

				for (float j = 0F; j <= rMag; j += dj) {
					rTest = rHat.scalarMult(j);

					if (!(rTest.sameBlock(rTestPrev))) {
						rTestAbs = rTest.add(centre);

						resist += rTestAbs.getExplosionResistance(worldObj);

						if (resist > strength * scaleFactor / r.magSq()) {
							blocked.set(index);
							removed.add(r);
							stop = true;
							break;
						}
					}

					rTestPrev.set(rTest);
				}
				if (stop)
					continue;

				toRemove.add(i);
			}

			int count = 0;

			//			centre.doExplosion(worldObj, (float) Math.min(100, strength), false);

			//MTQ doesnt seem necessary for blocks, at worst client chunks desynch
			//afaik blocks wont cmodex
			//MainThreadQueue.add(new Runnable(){ 
			//		public void run(){
			for (int j = 0; j < toRemove.size(); j++) {
				int i = toRemove.get(j);

				byte[] s = new byte[] { (byte) ((i & 255) - size),
						(byte) (((i / 256) & 255) - size),
						(byte) (((i / (256 * 256)) & 255) - size) };

				x = s[0];
				y = s[1];
				z = s[2];

				r = new Vector3(x, y, z);
				rAbs = r.add(centre);

				Chunk chunk = worldObj.getChunkFromBlockCoords(rAbs.intX(),
						rAbs.intZ());

				if (!affected.contains(chunk))
					affected.add(chunk);

				rAbs.setBlock(worldObj, Blocks.air, 0, 0);
				//TOOD test

				worldObj.markBlockForUpdate(rAbs.intX(), rAbs.intY(),
						rAbs.intZ());
			}

			List<Entity> targets = worldObj
					.getEntitiesWithinAABBExcludingEntity(null, centre
							.getAABB().expand(radius, radius, radius));
			for (Entity e : targets) {
				if (e instanceof EntityLivingBase
						&& !damages.containsKey((EntityLivingBase) e)) {
					EntityLivingBase ent = (EntityLivingBase) e;
					if (Vector3.isVisibleLocation(worldObj, centre,
							new Vector3(e))) {
						float damage = (float) (strength * scaleFactor / (centre
								.distToSq(new Vector3(e))));
						damages.put(ent, damage);
					}

				}
			}

			MainThreadQueue.add(new Runnable(){
				public void run() {
					for (Chunk c : affected) {
						c.setChunkModified();
					}
					for (EntityLivingBase e : damages.keySet()) {
						e.attackEntityFrom(DamageSource.setExplosionSource(null),
								damages.get(e));
					}
				}});

			worldObj.theProfiler.endSection();
			removed.clear();

		}
	}
}
