package com.typ1a.common.Equipment;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.lwjgl.util.vector.Vector3f;

public class Deflectors extends EquipmentSystem {
	
	private float range=0, stronk=0;
	
	public Deflectors(){
		
	}	

	@Override
	public void update(){
		
	}
	
	public static class Deflector implements Subunit{
		private float range, stronk;
		
		final AxisAlignedBB aabb= AxisAlignedBB.getBoundingBox(-range, range, -range, range, -range, range);
		
		public Deflector(float range, float stronk){
			this.range= range;
			this.stronk= stronk;
		}

		@Override
		public int update() {
			return 0;
		}
		
		/**@return nrg use*/
		public int deflect(World zawarudo, double[] pos){
			
//			for(Entity p : Entity.getWithinAABB(zawarudo, aabb.offset(pos[0], pos[1], pos[2]) ) ){
//				final Vector3f thr= new Vector3f();
//				//TODO
//			}
				
			
			return (int)(stronk);
		}
	}
}
