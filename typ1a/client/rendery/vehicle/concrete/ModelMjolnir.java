package com.typ1a.client.rendery.vehicle.concrete;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;

import org.lwjgl.opengl.GL11;

import com.typ1a.client.rendery.vehicle.ModelGeneric;
import com.typ1a.common.Vehicles.EntityVehicle;

public class ModelMjolnir extends ModelGeneric {

	private final ResourceLocation s= new ResourceLocation("/assets/t1a/model/"+getResName()+".obj");
	private final WavefrontObject mesh;
	
	public ModelMjolnir(){
		mesh= (WavefrontObject) AdvancedModelLoader.loadModel(s);
	}
	final float scale= 0.7f;
	@Override
	public void render(EntityVehicle e, float pT) {
		bindTexture();
		GL11.glScalef(scale, scale, scale);
//		GL11.glRotatef(-90, 0, 1, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glColor3f(.5f, .5f, .5f);
		mesh.renderAll();
	}
	@Override
	public String getResName(){return "mjolnir";}
}
