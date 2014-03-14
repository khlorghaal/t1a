package com.typ1a.client.rendery;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.typ1a.common.Projectiles.EntityGrenade;

public class RenderGrenade extends Render{

	ModelHesh modelHesh= new ModelHesh();
	ModelFrag modelFrag= new ModelFrag();
	ModelConc modelConc= new ModelConc();
	ModelSmoke modelSmoke= new ModelSmoke();
	
	ResourceLocation thesh= new ResourceLocation("t1a:textures/hesh.png");
	ResourceLocation tconc= new ResourceLocation("t1a:textures/Conc.png");
	ResourceLocation tfrag= new ResourceLocation("t1a:textures/Frag.png");
	ResourceLocation tsmoke= new ResourceLocation("t1a:textures/Smoker.png");
	ResourceLocation tflare= new ResourceLocation("t1a:textures/Flare.png");
	ResourceLocation tdisco= new ResourceLocation("t1a:textures/Disco.png");
	
	public static class ModelFrag extends ModelBase{
		ModelRenderer shape;

		ModelFrag(){
			setTextureOffset("shape.Shape1", 0, 0);
			setTextureOffset("shape.Shape3", 3, 2);
			setTextureOffset("shape.Shape2", 1, 2);
			setTextureOffset("shape.trig0", 18, 21);
			setTextureOffset("shape.trig1", 54, 17);
			setTextureOffset("shape.Shape4", 0, 23);

			shape = new ModelRenderer(this, "shape");
			shape.addBox("Shape1", -5F, -4F, -5F, 10, 10, 10);
			shape.addBox("Shape3", -4.5F, 6F, -4.5F, 9, 3, 9);
			shape.addBox("Shape2", -4F, -8F, -4F, 8, 4, 8);
			shape.addBox("trig0", -1.5F, -11.1F, -6F, 3, 4, 1);
			shape.addBox("trig1", -1.5F, -9F, -7F, 3, 14, 1);
			shape.addBox("Shape4", -1.5F, -11F, -5F, 3, 3, 6);
		}

		void render(ResourceLocation l){
			Minecraft.getMinecraft().renderEngine.bindTexture(l);
			shape.render(0.02f);
		}
	}
	public static class ModelConc extends ModelBase{
		ModelRenderer shape;

		ModelConc(){
			this.textureWidth = 64;
			this.textureHeight = 32;

			setTextureOffset("shape.Shape1", 0, 0);
			setTextureOffset("shape.trig0", 18, 21);
			setTextureOffset("shape.Shape2", 1, 2);
			setTextureOffset("shape.trig1", 54, 18);
			setTextureOffset("shape.Shape3", 3, 2);
			setTextureOffset("shape.Shape4", 40, 20);

			shape = new ModelRenderer(this, "shape");
			shape.setRotationPoint(0F, 0F, 0F);
			shape.addBox("Shape1", -5F, -4F, -5F, 10, 10, 10);
			shape.addBox("trig0", -2F, -8.1F, -7F, 4, 4, 1);
			shape.addBox("Shape2", -4.5F, -5F, -4.5F, 9, 1, 9);
			shape.addBox("trig1", -2F, -5F, -8F, 4, 13, 1);
			shape.addBox("Shape3", -4.5F, 6F, -4.5F, 9, 1, 9);
			shape.addBox("Shape4", -2F, -9F, -6F, 4, 4, 8);
		}

		void render(ResourceLocation l){
			Minecraft.getMinecraft().renderEngine.bindTexture(l);
			shape.render(0.02f);
		}
	}
	public static class ModelSmoke extends ModelBase{
		ModelRenderer shape;

		ModelSmoke(){
			textureWidth = 64;
			textureHeight = 32;
			setTextureOffset("shape.Shape2", 28, 0);
			setTextureOffset("shape.trig1", 10, 17);
			setTextureOffset("shape.Shape4", 0, 23);
			setTextureOffset("shape.trig0", 0, 21);

			shape = new ModelRenderer(this, "shape");
			shape.mirror = true;
			shape.addBox("Shape2", -4.5F, -4F, -4.5F, 9, 17, 9);
			shape.addBox("trig1", -1.5F, -5F, -7F, 3, 14, 1);
			shape.addBox("Shape4", -1.5F, -7F, -5F, 3, 3, 6);
			shape.addBox("trig0", -1.5F, -7.1F, -6F, 3, 4, 1);

		}
		void render(ResourceLocation l){
			Minecraft.getMinecraft().renderEngine.bindTexture(l);
			shape.render(0.02f);
		}
	}

	public static class ModelHesh extends ModelBase{
		ModelRenderer shape;

		public ModelHesh(){
			textureWidth = 32;
			textureHeight = 32;
			setTextureOffset("shape.Shape1", 0, 0);
			setTextureOffset("shape.Shape3", 0, 10);

			shape = new ModelRenderer(this, "shape");
			shape.setRotationPoint(0F, 0F, 0F);
			shape.addBox("Shape1", -2F, -2F, -5F, 4, 4, 6);
			shape.addBox("Shape3", -1.5F, -1.5F, -0.3F, 3, 3, 2);
		}

		public void render(ResourceLocation l){
			Minecraft.getMinecraft().renderEngine.bindTexture(l);
			shape.render(0.06f);
		}
	}




	@Override
	public void doRender(Entity entity, double d0, double d1, double d2,
			float f, float f1) {
		EntityGrenade e= (EntityGrenade)entity;
		glPushMatrix();

		glTranslated(d0, d1+0.2, d2);
		glRotatef(-e.rotationYaw, 0, 1, 0);
		glRotatef(-e.rotationPitch, 1, 0, 0);

		if(e.btype== EntityGrenade.CONCUSSIONSHOTGUN)
			modelHesh.render(thesh);
		else if(e.btype== EntityGrenade.FRAG)
			modelFrag.render(tfrag);
		else if(e.btype== EntityGrenade.CONCUSSION)
			modelConc.render(tconc);
		else if(e.btype== EntityGrenade.SMOKEL 
				|| e.btype==  EntityGrenade.SMOKES)
			modelSmoke.render(tsmoke);
		else if(e.btype== EntityGrenade.FLARE)
			modelSmoke.render(tflare);
		else if(e.btype== EntityGrenade.DISCO)
			modelSmoke.render(tdisco);

		glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}
}
