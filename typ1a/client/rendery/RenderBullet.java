package com.typ1a.client.rendery;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glColor4ub;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslated;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.typ1a.client.CubeRenderer;
import com.typ1a.common.Projectiles.EntityBullet;

public class RenderBullet extends Render {

	Box box;
	public RenderBullet() {
		box = new Box();
	}

	public static class Box extends ModelBase{
		public Box(){}
		public void render(Entity e) {
			final float s= 0.07f;
			glScalef(s, s, s);
			CubeRenderer.drawCube();
		}
	}

	@Override
	public void doRender(Entity ent, double d1, double d2, double d3,
			float f1, float f2) {
		if(ent.isDead)
			return;

		final EntityBullet e= (EntityBullet)ent;
		glPushAttrib(GL_LIGHTING);
		glDisable(GL_LIGHTING);
		glPushAttrib(GL_BLEND);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glDisable(GL_TEXTURE_2D);//must explicitly reenable
		glColor4ub((byte)255, (byte)255, (byte)200, (byte)10);

		//if(e.isTracer){
		//	OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0x00);
		//	glDisable(GL_LIGHTING);
		//}

		glPushMatrix();
		glTranslated(d1, d2, d3);

		if(e.ticksExisted==0 && !e.silenced){//make muzzle flash
			//otherwise they look funny when spawning
			glDisable(GL_LIGHTING);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0x00);


			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

			glColor4f(.8f+ (float)( Math.random()*.13 ), .75f, .60f, .75f);
			glRotatef((float)Math.random()*360, 0F, 1F, 0F);
			glRotatef((float)Math.random()*360, 1F, 0F, 0F);
			glPushMatrix();
			glScalef(.2f, .2f, .2f);
			CubeRenderer.drawCube();
			glPopMatrix();
			glColor4f(.8f+ (float)( Math.random()*.13 ), .75f, .60f, .75f);
			glRotatef((float)Math.random()*360, 0F, 1F, 0F);
			glRotatef((float)Math.random()*360, 1F, 0F, 0F);
			glScalef(.2f, .2f, .2f);
			CubeRenderer.drawCube();
		}//end muzzle flash

		else if(e.ticksExisted>1){//normal rendering
			if(e.btype==e.TRACE){
				glColor4f(.99f, .6f, .6f, 1);
				glDisable(GL_LIGHTING);
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0x00);
			}

			glPushMatrix();
			glRotatef(-e.rotationYaw, 0F, 1F, 0F);
			glRotatef(-e.rotationPitch, 1F, 0F, 0F);
			box.render(e);
			glPopMatrix();

			if(!e.isCollided || e.btype==e.TRACE){//motion blur or tracer bloom
				glPushMatrix();
				glTranslated(d1-e.v[0]*1/3, d2-e.v[1]*1/3, d3-e.v[2]*1/3);
				glRotatef(-e.rotationYaw, 0F, 1F, 0F);
				glRotatef(-e.rotationPitch, 1F, 0F, 0F);
				if(e.btype!=e.TRACE)
					glColor4f(.94f, .71f, .25f, .5f);
				else{
					glColor4f(.99f, .1f, .1f, 0.80f);
					glScalef(3, 3, 3);
				}
				box.render(e);
				glPopMatrix();

				if(e.btype!=e.TRACE){
					glPushMatrix();
					glTranslated(d1-e.v[0]*2/3, d2-e.v[1]*2/3, d3-e.v[2]*2/3);
					glRotatef(-e.rotationYaw, 0F, 1F, 0F);
					glRotatef(-e.rotationPitch, 1F, 0F, 0F);
					glColor4f(.94f, .71f, .25f, .35f);
					box.render(e);
					glPopMatrix();
				}
			}
		}
		glPopMatrix();
		glEnable(GL_TEXTURE_2D);
		glPopAttrib();
		glPopAttrib();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
