package com.typ1a.client.rendery.turret;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.typ1a.common.Robots.EntitySentry;



public class RenderTurret extends Render
{
	public ModelTurret model;
	public String tex;
	
	public enum type{CANNON, MACHINEGUN, PLASMA, LASER}
	
	public RenderTurret(type type) {
		switch (type){
		case CANNON:
		this.tex="t1a:textures/TurretCannon.png";
		model= new ModelTurret();
		break;
		default:
		}
		this.shadowSize=1;
	}

	public void Render(EntitySentry var1, double d1, double d2, double d3,
			float f1, float f2){
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d1, (float)(d2+.2), (float)d3);
		this.bindTexture(new ResourceLocation(tex));
		model.render(var1, .08F);
		GL11.glPopMatrix();
	}
	
	@Override
	public void doRender(Entity var1, double d1, double d2, double d3,
			float f1, float f2) {
		Render((EntitySentry)var1, d1, d2, d3, f1, f2);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
