package com.typ1a.common.utils;

import java.io.File;
import java.io.Serializable;

import net.minecraft.world.World;

/**Util for saving objects in the directory of the currently loaded world. 
 * Can only be used when a world is loaded - autochecks for that.*/
public class SerialObjectFileManager {
	private File file;

	/**Also used when loading*/
	public SerialObjectFileManager(World world){
		//TODO
	}
	
	public void save(Serializable obj, String filename, boolean append){
		//TODO
	}
}
