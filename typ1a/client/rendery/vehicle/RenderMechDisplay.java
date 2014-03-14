package com.typ1a.client.rendery.vehicle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import com.typ1a.common.Equipment.EquipmentFacade;
import com.typ1a.common.Vehicles.EntityTechVehicle;

public class RenderMechDisplay{

	public static void render(EntityTechVehicle e,float partialTick) {
		final EquipmentFacade ef= e.getEquipmentFacade();
	}
	
	static void renderPlainBar(float val, float x, float y){
		
	}
	static final FontRenderer fr= Minecraft.getMinecraft().fontRenderer;
	static void renderNumber(int val, float x, float y){
		
	}

}
