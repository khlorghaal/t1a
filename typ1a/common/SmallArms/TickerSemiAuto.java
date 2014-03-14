package com.typ1a.common.SmallArms;

import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**This class is to prevent button mashing exceeding RoF. 
 * One or less instantances per player at any time.
 * 
 * Checks if able to and fires immediately during construction. 
 * Immediately terminates self if unable to fire, so as to not 
 * disturb the cooling instance.
 * 
 * Only stays alive to block creating others (cooling down).*/
public class TickerSemiAuto extends TickerSmallArm {

	/**Objects that are still cooling down. 
	 * Entries block new instances of this for themself*/
	private static HashSet cooling= new HashSet();
	private static HashSet coolingclient= new HashSet();//needs this for singleplay

	public TickerSemiAuto(EntityPlayer player, NBTTagCompound dat) {
		super(player, dat);
		//Only instantiate if one doesnt already exist.
		if(!player.worldObj.isRemote){	
			if(! cooling.contains(player)){
				cooling.add(player);
				tryShoot();
				return;
			}
		}
		else{	
			if(! coolingclient.contains(player)){
				coolingclient.add(player);
				tryShoot();
				return;
			}
		}

		//this is the only place this may be killed this way!
		super.kill();
		return;

	}

	/**Same thing as super except uses the map*/
	@Override
	public void updateFireCycle(){
		if(ticksDone>=rof)
			this.actuallyKill();
	}

	/**Will kill itself*/
	@Override
	public void kill(){}
	public void actuallyKill(){
		super.kill();
		if(!player.worldObj.isRemote)
			cooling.remove(player);
		else
			coolingclient.remove(player);
	}
}
