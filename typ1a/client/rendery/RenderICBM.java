package com.typ1a.client.rendery;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.typ1a.common.Missiles.EntityICBM;


public class RenderICBM extends Render
{
	public ModelICBM model;
	public RenderICBM(){
		model=new ModelICBM();
	}

	public void doRender(Entity var1, double d1, double d2, double d3,
			float f1, float f2){

		GL11.glPushMatrix();
		GL11.glTranslatef((float)d1, (float)d2, (float)d3);
		GL11.glRotatef((float) (var1.motionY*80)+45, 0F, 1F, 0F);
		if(!((EntityICBM)var1).descending)
			GL11.glRotatef(180, 1F, 0F, 0F);
		this.bindTexture(new ResourceLocation("t1a:textures/ICBM.png"));
		model.render(var1);
		GL11.glPopMatrix();

		var1.worldObj.spawnParticle("lava", d1, d2-2, d3, 0, 0, 0);
	}

	private static class ModelICBM extends ModelBase{

		ModelRenderer Fin1;
		ModelRenderer Fin2;
		ModelRenderer Fin3;
		ModelRenderer Fin4;
		ModelRenderer Body;

		public ModelICBM(){
			textureWidth = 64;
			textureHeight = 32;
			setTextureOffset("Body.Shape1", 0, 0);
			setTextureOffset("Body.Shape4", 12, 20);
			setTextureOffset("Body.Shape3", 20, 0);

			Fin1 = new ModelRenderer(this, 0, 20);
			Fin1.addBox(1F, 4F, -0.5F, 5, 8, 1);
			Fin1.setRotationPoint(0F, 0F, 0F);
			Fin1.setTextureSize(64, 32);

			Fin2 = new ModelRenderer(this, 0, 20);
			Fin2.addBox(1F, 4F, -0.5F, 5, 8, 1);
			Fin2.setRotationPoint(0F, 0F, 0F);
			Fin2.setTextureSize(64, 32);

			Fin3 = new ModelRenderer(this, 0, 20);
			Fin3.addBox(1F, 4F, -0.5F, 5, 8, 1);
			Fin3.setRotationPoint(0F, 0F, 0F);
			Fin3.setTextureSize(64, 32);

			Fin4 = new ModelRenderer(this, 0, 20);
			Fin4.addBox(1F, 4F, -0.5F, 5, 8, 1);
			Fin4.setRotationPoint(0F, 0F, 0F);
			Fin4.setTextureSize(64, 32);

			Body = new ModelRenderer(this, "Body");
			Body.setRotationPoint(0F, 0F, 0F);
			Body.addBox("Shape1", -2.5F, -3F, -2.5F, 5, 14, 5);
			Body.addBox("Shape4", -1.5F, -16F, -1.5F, 3, 1, 3);
			Body.addBox("Shape3", -2F, -15F, -2F, 4, 12, 4);
		}

		static final float f5 = 0.075f;
		public void render(Entity entity){
			Body.render(f5);

			GL11.glRotatef(45, 0, 0, 1);
			Fin1.render(f5);
			GL11.glRotatef(-45, 0, 0, 1);

			GL11.glRotatef(90, 0, 1, 0);
			GL11.glRotatef(45, 0, 0, 1);
			Fin2.render(f5);
			GL11.glRotatef(-45, 0, 0, 1);

			GL11.glRotatef(90, 0, 1, 0);
			GL11.glRotatef(45, 0, 0, 1);
			Fin3.render(f5);
			GL11.glRotatef(-45, 0, 0, 1);

			GL11.glRotatef(90, 0, 1, 0);
			GL11.glRotatef(45, 0, 0, 1);
			Fin4.render(f5);
		}


		private static void setRotation(ModelRenderer model, float x, float y, float z)  {
			model.rotateAngleX = x;
			model.rotateAngleY = y;
			model.rotateAngleZ = z;
		}

		@Override
		public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
		{
			super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		}

	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {return null;}

}