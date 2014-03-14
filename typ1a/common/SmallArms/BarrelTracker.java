package com.typ1a.common.SmallArms;

import java.util.Hashtable;
import java.util.Map;

import net.minecraft.entity.Entity;

import com.typ1a.common.Projectiles.Barrel;

/**Used for entities without an equipment system to use guns, mainly small arms*/
public class BarrelTracker {

	public static Map<Entity, Barrel> map= new Hashtable<Entity, Barrel>();
	
	public static Barrel get(Entity entity){
		map.clear();
		//TODO ^
		Barrel ret= map.get(entity);
		if(ret==null){
			addNewBarrel(entity);
			ret= map.get(entity);
		}
		return ret;
	}
	
	public static void addNewBarrel(Entity entity){
		map.put(entity, new Barrel(entity, null, .9f, .25f, 1.63f, -.17f));
		//uses the default barrel for a players held gun
	}

}
