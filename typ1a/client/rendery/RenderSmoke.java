package com.typ1a.client.rendery;

import java.util.Arrays;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.typ1a.client.CubeRenderer;
import com.typ1a.common.Explosion.EntitySmoke;

public class RenderSmoke extends Render {

	private static final int VARIANCE= 0x0a0a0a00,
			VARIANCER= VARIANCE&0x0a000000,
			VARIANCEG= VARIANCE&0X000a0000,
			VARIANCEB= VARIANCE&0X00000a00,
			VARIANCEA= VARIANCE&0X00000000;

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2,
			float f, float f1) {
		final EntitySmoke e= (EntitySmoke)entity;
		GL11.glPushMatrix();
		GL11.glTranslated(d0, d1, d2);

		if(e.glowy)
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0x00);

		final int l= e.particles.length;

		int color= e.color;
//		color= rangeAdjustColor(color);
		
		CubeRenderer.drawCubes(
				Arrays.copyOfRange(e.particles, 0, l/3)
				, color+VARIANCE);
		CubeRenderer.drawCubes(
				Arrays.copyOfRange(e.particles, l/3, 2*l/3)
				, color);
		CubeRenderer.drawCubes(
				Arrays.copyOfRange(e.particles, 2*l/3, l)
				, color-VARIANCE);

		GL11.glPopMatrix();
	}

	private int rangeAdjustColor(int color){
		int r= color&0xff000000;
		int g= color&0x00ff0000;
		int b= color&0x0000ff00;
		int a= color&0x000000ff;
		r= clamp(r, -1, 0);
		g= clamp(g-VARIANCEG, 0+VARIANCEG, 0xff);
		b= clamp(b-VARIANCEB, 0+VARIANCEB, 0xff);
		a= clamp(a-VARIANCEA, 0+VARIANCEA, 0xff);
		return r|g|b|a;
	}
	private int clamp(int val, int min, int max){
		if(val<min)
			return min;
		if(val>max)
			return max;
		return val;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

}
