package com.typ1a.common.Robots;

import java.util.List;

import net.minecraft.entity.Entity;

/**Does combat manoevers. Aims and shoots weapons.*/
public class MobileAICombat extends MobileAI {

	/**'has a lead' on a target*/
	private boolean isPursuing;
	private int aggroSetting;

	protected boolean isKillingAndNotEscorting;

	public MobileAICombat(Entity self) {
		super(self);
	}


	public void setTargetToKill(Entity t){
		super.setTargetToFollow(t);
		this.isKillingAndNotEscorting= true;
	}

	public void setTargetToEscort(Entity t){
		super.setTargetToFollow(t);
		this.isKillingAndNotEscorting= false;
	}

	@Override
	public void doTask() {
		if(hasShot())
			shootStart();
		else
			shootStop();
	}

	@Override
	public void lookForTask() {
		List<Entity> targets= getHostilesVisible();
		//if 
	}



	//Weapon stuff
	protected void shootStart(){

	}
	protected void shootStop(){
		
	}

	protected boolean hasShot(){
		return true;
	}
}
