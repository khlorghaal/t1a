package com.typ1a.client.rendery.turret.concrete;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

import com.typ1a.common.Vehicles.EntityTechVehicle;


/**Remember to update guns in RenderTechVehicle*/
public abstract class ModelGunTurret extends ModelBase {

	
	/**@param looker set to null to make the weapon use pitch of the vehicle
	 * @param sideMount as opposed to top mounted*/
	public void render(EntityTechVehicle veh, Entity looker, double x, double y, double z, boolean sideMount){
		glPushMatrix();
		glTranslated(x,y,z);
		if(looker==null)
			actuallyRender(0, 0, sideMount);
		else
			actuallyRender(looker.rotationPitch, looker.rotationYaw, sideMount);
		glPopMatrix();
	}
	public abstract void actuallyRender(double azimuth, double inclination, boolean sideMount); 
}
