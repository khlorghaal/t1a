package com.typ1a.client.rendery.guns;

import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslated;
import net.minecraft.client.model.ModelRenderer;

public class RenderM2 extends RenderSmallArm {

	public RenderM2(){
		textureWidth = 64;
		textureHeight = 64;
		setTextureOffset("Mag.Shape9", 0, 38);
		setTextureOffset("Main.Shape1", 22, 0);
		setTextureOffset("Main.Shape5", 58, 4);
		setTextureOffset("Main.Shape3", 0, 47);
		setTextureOffset("Main.Shape7", 0, 0);
		setTextureOffset("Main.Shape6", 56, 0);
		setTextureOffset("Main.Shape5", 58, 4);
		setTextureOffset("Main.Shape4", 0, 0);
		setTextureOffset("Main.Shape2", 48, 0);
		setTextureOffset("Main.Shape8", 57, 11);
		setTextureOffset("Main.Shape2", 48, 0);

		Barrel = new ModelRenderer(this, 6, 35);
		Barrel.addBox(-1F, -1F, -39F, 2, 2, 27);
		Barrel.setRotationPoint(0F, -4F, 0F);
		Barrel.setTextureSize(64, 64);
		Barrel.mirror = true;
		Bolt = new ModelRenderer(this, "Bolt");
		Bolt.setRotationPoint(0F, 0F, 0F);
		Bolt.mirror = true;
		
		Mag = new ModelRenderer(this, "Mag");
		Mag.setRotationPoint(2F, -3F, 2F);
		Mag.mirror = true;
		Mag.addBox("Shape9", 0.01F, -2.2F, -2F, 11, 4, 4);
		
		Main = new ModelRenderer(this, "Main");
		Main.setRotationPoint(0F, 0F, 0F);
		Main.mirror = true;
		Main.addBox("Shape1", -2F, -6F, -1F, 4, 6, 17);
		Main.addBox("Shape5", 2F, -5.6F, 15.5F, 1, 4, 2);
		Main.addBox("Shape3", -1.5F, -5.5F, -12F, 3, 3, 11);
		Main.addBox("Shape7", -2.5F, -1F, -0.2F, 5, 2, 1);
		Main.addBox("Shape6", -1F, -5.5F, 16F, 2, 1, 2);
		Main.addBox("Shape5", -3F, -5.6F, 15.5F, 1, 4, 2);
		Main.addBox("Shape4", -2F, 1F, -5F, 4, 1, 9);
		Main.addBox("Shape2", -1F, -6.6F, 0F, 2, 1, 1);
		Main.addBox("Shape8", -5F, -3F, 5F, 3, 1, 1);
		Main.addBox("Shape2", -1F, -6.6F, 13F, 2, 1, 1);
	}
	@Override
	protected float getScale() {
		return .095f;
	}

	@Override
	public float getRecoilMultiplier() {
		return .983f;
	}

	@Override
	protected void transform1stPerson() {
		//TODO
	}

	@Override
	protected void transform3rdPerson() {
		glTranslated(.4, .2, -.2);
		glRotatef(200, 0, 1, 0);//yaw
		glRotatef(00, 1, 0, 0);//pitch
		glRotatef(170, 0, 0, 1);//roll
	}

	@Override
	protected String getTexName() {
		return "BMG";
	}

}
