package com.typ1a.client.rendery;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.typ1a.common.Missiles.EntityMissile;


public class RenderMissile extends Render {

	public static final int
	kinetic=0, boomy=1;

	box box;
	public RenderMissile(int id) {
		box = new box(id);
	}

	public class box extends ModelBase{
		ModelRenderer body;
		int id;
		float size=.08F;
		public box(int id){
			this.id=id;
			textureWidth = 32;
			textureHeight = 32;
			setTextureOffset("body.body", 0, 0);
			setTextureOffset("body.fin", 0, 0);

			body= new ModelRenderer(this,"body");
			body.setRotationPoint(0.5F, 0.5F, 1F);
			body.rotateAngleX=0;
			body.rotateAngleY=0;
			body.rotateAngleZ=0;			
			body.addBox("body", 0F, 0F, 0F, 1, 1, 7);

			body.addBox("fin", -.5F, 0F, .30F, 2, 1, 1);
			body.addBox("fin", 0F, -.5F, .30F, 1, 2, 1);
		}
		public void render(EntityMissile entity){
			body.rotateAngleY=(float) entity.pitch;
			body.rotateAngleX=(float) entity.yaw;
			body.render(size);
		}
	}

	public void Render(Entity var1, double d1, double d2, double d3,
			float f1, float f2){

		GL11.glPushMatrix();
		GL11.glTranslatef((float)d1, (float)d2, (float)d3);
		//	GL11.glRotatef(((EntityMissile)(var1)).roll, 0F, 0F, 1F); TODO
		this.bindTexture(new ResourceLocation("t1a:textures/Pewpew.png"));
		box.render((EntityMissile)var1);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity var1, double d1, double d2, double d3,
			float f1, float f2) {
		Render(var1, d1, d2, d3, f1, f2);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
