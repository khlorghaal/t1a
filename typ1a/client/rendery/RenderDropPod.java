package com.typ1a.client.rendery;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderDropPod extends Render {

	private Model model;

	public RenderDropPod(){
		this.model= new RenderDropPod.Model();
	}

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2,
			float f, float f1) {
		GL11.glPushMatrix();
		
		GL11.glTranslated(d0, d1+.8, d2);
		GL11.glRotatef(180, 1,0,0);
		GL11.glRotatef(entity.rotationYaw, 0, 1, 0);
		this.bindTexture(new ResourceLocation("t1a:textures/DropPod.png"));
		this.model.render();
		
		GL11.glPopMatrix();
	}

	private static class Model extends ModelBase{
		ModelRenderer Fins;
		ModelRenderer Main;

		public Model(){

			textureWidth = 64;
			textureHeight = 32;
			setTextureOffset("Fins.Shape1", 60, 0);
			setTextureOffset("Fins.Shape1", 60, 0);
			setTextureOffset("Fins.Shape1", 60, 0);
			setTextureOffset("Fins.Shape2", 60, 0);
			setTextureOffset("Main.lowerbody", 0, 13);
			setTextureOffset("Main.plate1", 32, 0);
			setTextureOffset("Main.upperbody", 0, 0);
			setTextureOffset("Main.plate2", 32, 0);
			setTextureOffset("Main.plate3", 32, 18);
			setTextureOffset("Main.plate3", 32, 18);
			setTextureOffset("Main.pad1", 48, 7);
			setTextureOffset("Main.pad2", 48, 7);
			setTextureOffset("Main.pad3", 48, 0);
			setTextureOffset("Main.pad4", 48, 0);

			Fins = new ModelRenderer(this, "Fins");
			Fins.setRotationPoint(0F, 0F, 0F);
			Fins.mirror = true;
			Fins.addBox("Shape1", -3F, -13F, -3F, 1, 7, 1);
			Fins.addBox("Shape1", 2F, -13F, 2F, 1, 7, 1);
			Fins.addBox("Shape1", -3F, -13F, 2F, 1, 7, 1);
			Fins.addBox("Shape2", 2F, -13F, -3F, 1, 7, 1);
			Main = new ModelRenderer(this, "Main");
			Main.setRotationPoint(0F, 0F, 0F);
			Main.mirror = true;
			Main.addBox("lowerbody", -4F, 3F, -4F, 8, 7, 8);
			Main.addBox("plate1", 3.5F, 0F, -3.5F, 1, 11, 7);
			Main.addBox("upperbody", -3.5F, -6F, -3.5F, 7, 6, 7);
			Main.addBox("plate2", -4.5F, 0F, -3.5F, 1, 11, 7);
			Main.addBox("plate3", -3.5F, 0F, -4.5F, 7, 11, 1);
			Main.addBox("plate3", -3.5F, 0F, 3.5F, 7, 11, 1);
			Main.addBox("pad1", -2.5F, 10F, 3F, 5, 2, 1);
			Main.addBox("pad2", -2.5F, 10F, -4F, 5, 2, 1);
			Main.addBox("pad3", 3F, 10F, -2.5F, 1, 2, 5);
			Main.addBox("pad4", -4F, 10F, -2.5F, 1, 2, 5);
		}
		
		public void render(){
			final float scale=.15f;
			this.Fins.render(scale);
			this.Main.render(scale);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
