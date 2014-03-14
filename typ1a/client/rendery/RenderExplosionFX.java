package com.typ1a.client.rendery;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.util.Arrays;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.typ1a.client.CubeRenderer;
import com.typ1a.common.Maths;
import com.typ1a.common.T1A;
import com.typ1a.common.Explosion.EntityExplosionFX;

public class RenderExplosionFX extends Render {

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2,
			float f, float f1) {
		final int tick= entity.ticksExisted;
		glPushMatrix();
		glTranslated(d0, d1, d2);
		glBindTexture(GL_TEXTURE_2D, 0);
		final int VARIANCE=0x0a0a0a00;
		final float[] arr= ((EntityExplosionFX)entity).pos3;
		
//		System.out.println(entity.getBrightnessForRender(1));
		int color= 0xccccccff;
		if(tick<20*20){//modify lightmap for glowyawesomeness
			int u= (int)(0xf0*(1f-(tick/400f)) );
			u= Maths.clamp(u, 1, 0xf0);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, u, 0xf0);
		}
		final int l= arr.length;
		if(tick>0){
		CubeRenderer.drawCubes(
				Arrays.copyOfRange(arr, 0, l/3)
				, color+VARIANCE);
		CubeRenderer.drawCubes(
				Arrays.copyOfRange(arr, l/3, 2*l/3)
				, color);
		CubeRenderer.drawCubes(
				Arrays.copyOfRange(arr, 2*l/3, l)
				, color-VARIANCE);
		}
		if(false)
		if(tick<350){
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			final float a= 1f-(tick/200f);
			GL11.glTranslatef(0, 4, 0);

			//glow
			final float s1;
			final float sadd;
			if(tick<6) sadd=6;
			else if(tick>45) sadd=45;
			else sadd= tick;
			
			s1= T1A.random.nextInt(5)+sadd;
			glScalef(s1, s1, s1);
			glRotatef(T1A.random.nextFloat()*8, (T1A.random.nextFloat()-.5f), 1, (T1A.random.nextFloat()-.5f));
			if(tick<180){
				GL11.glColor4f(1, 1, 1, 10f*(a*a*a*a));
				CubeRenderer.drawCube();
			}
			GL11.glColor4f(1, 1, 1, .4f*a);
			glScalef(2f, 2f, 2f);
			CubeRenderer.drawCube();
			
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
		glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
