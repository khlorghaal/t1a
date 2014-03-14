package com.typ1a.client.rendery;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.typ1a.common.spacey.EntitySatBeam;

public class RenderSatBeam extends Render {

	static final Tessellator t= Tessellator.instance;
	
	@Override
	public void doRender(Entity entity, double d0, double d1, double d2,
			float f, float f1) {
		EntitySatBeam e= (EntitySatBeam) entity;
		
		final double h= 512;
		
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

}
