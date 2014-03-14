package com.typ1a.common.Equipment;

import net.minecraft.item.ItemStack;


public class Shields extends EquipmentSystem{

	public int load=0;
	
	public Shields(EquipmentFacade mount) {
		super(mount);
	} 
	@Override
	public void update(){
		super.update();
		load-=4;
	}

	/**@return damage penetration*/
	public float hit(float dmg){
		System.out.println(this.subunitList.size());
		float trueDmg=dmg;
		for(Object shield : this.subunitList){
			trueDmg-=((Shield)shield).damage(trueDmg);//returns same as this

			if(trueDmg==0) break;
		}
		load+= dmg-trueDmg;//amount that got absorbed
		if(trueDmg<0)
			trueDmg=0;
		return trueDmg;
	}

	////////////////////////////////////////////////	
	public static class Shield implements Subunit{
		int maxHp, eUse;
		int hp=0, recharge;


		public Shield(ItemStack stack){
			maxHp=10;
			recharge=1;
			eUse=1;
		}
		@Override
		public final int update(){
			if(this.hp<=maxHp){
				this.hp+=this.recharge;   
				return eUse;
			}
			else return 0;
		}
		/**@return amount of carryover damage*/
		public final float damage(float trueDmg){
			if(trueDmg>hp){
				final float r= trueDmg-hp;
				hp=0;
				return r;
			}
			hp-=trueDmg;
			return 0;
		}
	}
	////////
	public static class HeavyShield extends Shield{
		public HeavyShield(ItemStack stack){
			super(stack);
			//dont call super
			maxHp=20;//2x base
			recharge=2;//2x base
			eUse=2;
		}
	}
	public static class FastShield extends Shield{
		public FastShield(ItemStack stack){
			super(stack);
			maxHp=10;//1x base
			recharge=4;//4x base
			eUse=2;

		}
	}
}
