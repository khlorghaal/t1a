package com.typ1a.client.rendery;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.typ1a.common.utils.Vector3;

public class RenderLaser {
	static final ResourceLocation rsl= new ResourceLocation("t1a:textures/laser.png");
	static final Tessellator t= Tessellator.instance;
	
	public static void render(Vector3 origin, Vector3 polar, int color){
		glPushMatrix();
		glTranslated(origin.x, origin.y, origin.z);
		final float l= (float) polar.x;
		final float lrcp= .06f/l;
		glScalef(l, l, l);
		glRotated(polar.y, 1, 0, 0);
		glRotated(polar.z, 0, 1, 0);
		final int pt= glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		Minecraft.getMinecraft().renderEngine.bindTexture(rsl);
		
		glPushAttrib(GL_LIGHTING);
		glDisable(GL_LIGHTING);
		
		glPushAttrib(GL_BLEND);
		glEnable(GL_BLEND);
		
		final int lmxp= (int) OpenGlHelper.lastBrightnessX, 
				lmyp= (int) OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		GL11.glColor3f(1, 0, 0);
		t.startDrawingQuads();
		t.addVertexWithUV(0, -lrcp, 0, 1, 0);
		t.addVertexWithUV(0, -lrcp, 1, 0, 0);
		t.addVertexWithUV(0, lrcp, 0, 0, 1);
		t.addVertexWithUV(0, lrcp, 1, 1, 1);
		t.draw();
		GL11.glColor3f(1, 1, 1);
//		CubeRenderer.drawCube();
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lmxp, lmyp);
		glBindTexture(GL_TEXTURE_2D, pt);
		glPopAttrib();
		glPopAttrib();
		glPopMatrix();
	}

}
