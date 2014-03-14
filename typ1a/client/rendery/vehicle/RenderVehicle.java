package com.typ1a.client.rendery.vehicle;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_WRITEMASK;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glColor4ub;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glMultMatrix;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.typ1a.client.CubeRenderer;
import com.typ1a.client.T1AClientProxy;
import com.typ1a.client.T1ATickHandlerClient;
import com.typ1a.common.Vehicles.EntityTechVehicle;
import com.typ1a.common.Vehicles.EntityVehicle;

public class RenderVehicle extends Render {

	public ModelGeneric model;
	
	public RenderVehicle(ModelGeneric model) {
		this.shadowSize=1;
		this.model= model;
	}
	final Minecraft mc= Minecraft.getMinecraft();

	@Override
	public void doRender(Entity var1, double d1, double d2, double d3,
			float f1, float pT) {
		final EntityVehicle e= (EntityVehicle)var1;
		glPushMatrix();
		model.bindTexture();
		final boolean fstp= e.riddenByEntity==mc.thePlayer && mc.gameSettings.thirdPersonView==0; 
		if(fstp){

		}
		else{
			glTranslatef((float)d1, (float)d2+2.2f, (float)d3);
			
			T1AClientProxy.MATRBUF.clear();
			e.kin.getRotationInterpolated(T1ATickHandlerClient.pT).store(T1AClientProxy.MATRBUF);
			T1AClientProxy.MATRBUF.clear();
			glMultMatrix(T1AClientProxy.MATRBUF);
//			glRotatef((float) Math.toDegres(e.yaw+(e.yaw-e.pyaw)*pT), 0, -1, 0);

			model.render(e, pT);
		}

		//shield glow
		if(e instanceof EntityTechVehicle){
			final int sg= ((EntityTechVehicle)e).getEquipmentFacade().getShieldGlow();
			if(sg!=0){
				mc.entityRenderer.disableLightmap(0);
				glPushAttrib(GL_CULL_FACE);
				glPushAttrib(GL_BLEND);
				glPushAttrib(GL_LIGHTING);
				glPushAttrib(GL_DEPTH_WRITEMASK);

				glDisable(GL_CULL_FACE);
				glEnable(GL_BLEND);
				glDisable(GL_LIGHTING);
				glDepthMask(false);
				glDisable(GL_TEXTURE_2D);
				int a= sg*10;
				if(a>255) a=255;
				glColor4ub( (byte)10, (byte)160, (byte)255, (byte)(140) );

				glTranslatef(1, -0.5f, 0);
				glScalef(10, 6, 8);

				glRotatef(-45, 0, 0, 1);
				glRotatef(45, 0, 1, 0);

				CubeRenderer.drawCube();

				glEnable(GL_TEXTURE_2D);
				glPopAttrib();
				glPopAttrib();
				glPopAttrib();
				glPopAttrib();
				mc.entityRenderer.enableLightmap(0);
			}
		}
		glPopMatrix();		
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {		return null;	}

}
