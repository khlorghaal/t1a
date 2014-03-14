package com.typ1a.client.rendery.guns;

import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import net.minecraft.client.model.ModelRenderer;

public class RenderAR extends RenderSmallArm {

	public RenderAR(){
		textureWidth = 32;
		textureHeight = 32;
		setTextureOffset("Mag.mag1", 14, 1);
		setTextureOffset("Bolt.Shape3", 0, 0);
		setTextureOffset("Main.Shape1", 2, 23);
		setTextureOffset("Main.Shape5", 26, 4);
		setTextureOffset("Main.Shape4", 20, 25);
		setTextureOffset("Main.Shape6", 25, 1);
		setTextureOffset("Main.Shape2", 26, 4);
		setTextureOffset("Main.Shape7", 19, 16);
		setTextureOffset("Main.Shape8", 25, 7);
		setTextureOffset("Barrel.barrel1", 0, 0);

		Mag = new ModelRenderer(this, "Mag");
		Mag.setRotationPoint(0F, 2F, -1F);
		Mag.mirror = true;
		Mag.addBox("mag1", 0.1F, 0F, 0F, 1, 3, 2);
		
		Bolt = new ModelRenderer(this, "Bolt");
		Bolt.setRotationPoint(0.5F, 0F, 0F);
		Bolt.mirror = true;
		Bolt.addBox("Shape3", 0F, 1F, 0F, 1, 1, 1);
		
		Main = new ModelRenderer(this, "Main");
		Main.setRotationPoint(0F, 0F, 0F);
		Main.mirror = true;
		Main.addBox("Shape1", 0F, 0F, 2F, 1, 2, 7);
		Main.addBox("Shape5", 0F, 2F, 4F, 1, 2, 1);
		Main.addBox("Shape4", 0F, 0F, -2F, 1, 3, 4);
		Main.addBox("Shape6", 0F, 2F, 5F, 1, 1, 1);
		Main.addBox("Shape2", 0.001F, 1.75F, 7F, 1, 1, 2);
		Main.addBox("Shape7", 0F, 0F, -7F, 1, 4, 5);
		Main.addBox(0.001F, 2F, 5F, 1, 2, 2);
		
		Barrel = new ModelRenderer(this, "Barrel");
		Barrel.setRotationPoint(0F, 0F, 0F);
		Barrel.mirror = true;
		Barrel.addBox("barrel1", 0.12F, 0.6F, 8.5F, 1, 1, 3);
	}
	@Override
	protected float getScale() {
		return 0.120f;
	}
	@Override
	protected float getWidthSqrt(){
		return 1.12f;	}

	@Override
	public float getRecoilMultiplier() {
		return 0.90f;
	}

	@Override
	protected String getTexName() {
		return "AR";
	}

	@Override
	protected void transform1stPerson() {
		glTranslatef(.45f, .4f, 0.2f);
		glRotatef(80, 0, 1, 0);//yaw
		glRotatef(-32, 1, 0, 0);//pitch
		glRotatef(180, 0, 0, 1);//roll	
	}

	@Override
	protected void transform3rdPerson() {
		glRotatef(-169, 0, 1, 0);
		glRotatef(-17, 0, 0, 1);
		glRotatef(170, 1, 0, 0);
		glTranslatef(-.67f, -.28f, -.38f);		
	}

}
