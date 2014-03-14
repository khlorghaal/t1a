package com.typ1a.client.rendery;


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.typ1a.common.Missiles.EntitySilo;

public class RenderSilo extends Render
{
	public ModelSilo model;

	public RenderSilo() {
		model= new ModelSilo();
		this.shadowSize=1;
	}

	public void Render(EntitySilo var1, double d1, double d2, double d3,
			float f1, float f2){

		GL11.glPushMatrix();
		GL11.glTranslatef((float)d1, (float)(d2+.7), (float)d3);
		this.bindTexture(new ResourceLocation("t1a:textures/Silo.png"));
		model.render(var1, .05F);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity var1, double d1, double d2, double d3,
			float f1, float f2) {
		Render((EntitySilo)var1, d1, d2, d3, f1, f2);
	}





	public class ModelSilo extends ModelBase{
		ModelRenderer Door1;
		ModelRenderer Door2;
		ModelRenderer Base;

		public ModelSilo(){

			textureWidth = 256;
			textureHeight = 64;
			setTextureOffset("Piece1.Shape1", 104, 0);
			setTextureOffset("Piece1.Shape2", 0, 0);

			Door1 = new ModelRenderer(this, 0, 31);
			Door1.addBox(0F, 0F, 0F, 9, 2, 18);
			Door1.setRotationPoint(0F, -14F, -9F);
			Door1.setTextureSize(64, 32);
			Door1.mirror = true;
			setRotation(Door1, 0F, 0F, 0F);
			
			Door2 = new ModelRenderer(this, 0, 31);
			Door2.addBox(0F, 0F, 0F, 9, 2, 18);
			Door2.setRotationPoint(-9F, -14F, -9F);
			Door2.setTextureSize(64, 32);
			Door2.mirror = true;
			setRotation(Door2, 0F, 0F, 0F);
			
			Base = new ModelRenderer(this, "Piece1");
			Base.setRotationPoint(0F, 0F, 0F);
			setRotation(Base, 0F, 0F, 0F);
			Base.mirror = true;
			Base.addBox("Shape1", -8F, -8F, -8F, 16, 32, 16);
			Base.addBox("Shape2", -13F, -12F, -13F, 26, 4, 26);
		}
		public void render(EntitySilo thing, float f5){
			GL11.glRotatef(180, 1, 0, 0);
			Door1.render(f5);
			Door2.render(f5);
			Base.render(f5);

		}

		private void setRotation(ModelRenderer model, float x, float y, float z){
			model.rotateAngleX = x;
			model.rotateAngleY = y;
			model.rotateAngleZ = z;
		}
	}





	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}

}

