package com.typ1a.client.rendery.guns;

import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import net.minecraft.client.model.ModelRenderer;

public class RenderShotgun extends RenderSmallArm {

	public RenderShotgun(){
		textureWidth = 32;
		textureHeight = 32;
		setTextureOffset("Bolt.Shape11", 0, 0);
		setTextureOffset("Main.Shape1", 12, 14);
		setTextureOffset("Main.Shape9", 0, 24);
		setTextureOffset("Main.Shape8", 12, 24);
		setTextureOffset("Main.Shape5", 0, 15);
		setTextureOffset("Main.Shape3", 8, 8);
		setTextureOffset("Main.Shape2", 0, 0);
		setTextureOffset("Main.Shape4", 0, 21);
		setTextureOffset("Main.Shape6", 0, 19);
		setTextureOffset("Main.Shape7", 0, 13);

		Bolt = new ModelRenderer(this, "Bolt");
		Bolt.setRotationPoint(0F, 0F, 0F);
		Bolt.mirror = true;
		Bolt.addBox("Shape11", 1.4F, 0.2F, 0F, 1, 1, 1);
		Main = new ModelRenderer(this, "Main");
		Main.setRotationPoint(0F, 0F, 0F);
		Main.mirror = false;
		Main.addBox("Shape1", 0F, 1.2F, 2F, 2, 2, 8);
		Main.addBox("Shape9", 0.5F, 0.7F, -13F, 1, 2, 3);
		Main.addBox("Shape8", 0.5F, 1.5F, -10F, 1, 1, 4);
		Main.addBox("Shape5", 0.5F, 2.5F, -6.6F, 1, 3, 1);
		Main.addBox("Shape3", 0.5F, 2F, 10F, 1, 1, 4);
		Main.addBox("Shape2", 0.5F, 0.2F, 2F, 1, 1, 12);
		Main.addBox("Shape4", 0F, 0F, -6F, 2, 3, 8);
		Main.addBox("Shape6", 0.5F, 0.6F, -14F, 1, 4, 1);
		Main.addBox("Shape7", 0.5F, 3F, -5.5F, 1, 1, 1);
	}

	@Override
	protected float getScale() {
		return 0.09f;
	}

	@Override
	public float getRecoilMultiplier() {
		return .93f;
	}

	@Override
	protected void transformRecoil(float r){
		glTranslatef(0, -r*.5f, 0);
		glRotatef(r*25, 1, 0, 0);
	}

	@Override
	protected void transform1stPerson() {
		glTranslatef(0.7f, 0.9f, 0f);
		glRotatef(70, 0, 1, 0);
		glRotatef(-26, 1, 0, 0);
		glRotatef(188, 0, 0, 1);
	}

	@Override
	protected void transform3rdPerson() {
		glRotatef(-169, 0, 1, 0);
		glRotatef(-17, 0, 0, 1);
		glRotatef(170, 1, 0, 0);
		glTranslatef(-.67f, -.28f, .38f);	
	}

	@Override
	protected String getTexName() {
		return "Shotgun";
	}
}
