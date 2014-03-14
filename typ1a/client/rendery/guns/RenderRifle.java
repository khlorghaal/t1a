package com.typ1a.client.rendery.guns;

import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.client.IItemRenderer;


public class RenderRifle extends RenderSmallArm implements IItemRenderer{

	public RenderRifle()  {
		textureWidth = 64;
		textureHeight = 32;
		setTextureOffset("Bolt.Shape6", 0, 12);
		setTextureOffset("Bolt.Shape7", 0, 17);
		setTextureOffset("Main.Shape5", 26, 16);
		setTextureOffset("Main.Shape1", 0, 0);
		setTextureOffset("Main.Shape2", 0, 0);
		setTextureOffset("Main.Shape3", 10, 0);
		setTextureOffset("Main.Shape4", 0, 0);

		Bolt= new ModelRenderer(this, "Bolt");
		Bolt.mirror = true;
		Bolt.setRotationPoint(1.265f, 0.23f, 0);
		Bolt.addBox("Shape6", -0.5F, -0.5F, -1F, 1, 1, 4);
		Bolt.addBox("Shape7", -0.5F, -0.5F, -0.99F, 2, 1, 1);

		Main= new ModelRenderer(this, "Main");
		Main.setRotationPoint(0F, 0F, 0F);
		Main.mirror = true;
		Main.addBox("Shape5", 0.5F, 1F, 0.1666667F, 1, 1, 1);
		Main.addBox("Shape1", 0F, 0F, -1F, 2, 1, 11);
		Main.addBox("Shape2", 0F, 0F, -7F, 1, 3, 6);
		Main.addBox("Shape3", 0.5F, -0.5F, 0F, 1, 1, 17);
		Main.addBox("Shape4", 0F, 0F, -3F, 2, 2, 2);
	}
	@Override
	protected void doRendering(float r){
		Main.render(getScale());

		glScalef(.9f, .9f, 1);
		this.transformBolt(r);
		Bolt.render(getScale());
	}	
	@Override
	protected float getScale() {
		return 0.14f;
	}
	@Override
	public float getRecoilMultiplier() {
		return -0.013f;
	}
	@Override
	protected void transform1stPerson() {
		glTranslated(.5, .6, -.1);
		glRotatef(65, 0, 1, 0);//yaw
		glRotatef(-21, 1, 0, 0);//pitch
		glRotatef(189, 0, 0, 1);//roll

	}
	@Override
	protected void transform3rdPerson() {
		glRotatef(-167, 0, 1, 0);
		glRotatef(167, 1, 0, 0);
		glRotatef(13, 0, 0, 1);
		glTranslatef(-.67f, -.28f, .10f);

	}
	@Override
	public void transformBolt(float t){
		Bolt.rotateAngleZ= (float) Math.toRadians(0);
		
		final float T1=0.68f, T2=0.57f, T3=0.35f, T4=0.22f, TILT=55, DIST=2;
		if(t<T4){

			Bolt.rotateAngleZ-= (float) Math.toRadians(t *TILT*1/T4);
		}
		else if(t<T3){

			Bolt.rotateAngleZ+= (float) Math.toRadians(-45);
			glTranslatef(0f,0f,-(t-T3)*getScale()*DIST *1/(T3-T4) -getScale()*DIST);
		}
		else if(t<T2){

			Bolt.rotateAngleZ+= (float) Math.toRadians(-45);
			glTranslatef(0f,0f,+(t-T2)*getScale()*DIST *1/(T2-T3));
		}
		else if(t<T1){

			Bolt.rotateAngleZ+= (float) Math.toRadians((t-(T1)) *TILT*1/(T1-T2));
		}
		glScalef(0.9f, 0.9f, 0.9f);
	}
	@Override
	protected String getTexName() {
		return "Rifle";
	}
}
