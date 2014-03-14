package com.typ1a.client.rendery.vehicle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import com.typ1a.common.Vehicles.EntityVehicle;

public abstract class ModelGeneric {
	public abstract void render(EntityVehicle e, float pT);
	public abstract String getResName();
	
	final TextureManager tm= Minecraft.getMinecraft().renderEngine;
	private final ResourceLocation resloc= new ResourceLocation("t1a:textures/"+getResName()+".png");

	public void bindTexture(){
		tm.bindTexture(resloc);
	}
}
