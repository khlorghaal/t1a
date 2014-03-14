package com.typ1a.client.rendery.guns;

import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.client.IItemRenderer;


public class RenderSMG extends RenderSmallArm implements IItemRenderer{

	public RenderSMG()  {
		textureWidth = 32;
		textureHeight = 32;
		setTextureOffset("Mag.mag1", 14, 1);
		setTextureOffset("Bolt.Shape3", 15, 3);
		setTextureOffset("Main.Shape1", 3, 1);
		setTextureOffset("Main.Shape5", 26, 4);
		setTextureOffset("Main.Shape8", 25, 4);
		setTextureOffset("Main.Shape4", 0, 11);
		setTextureOffset("Main.Shape6", 25, 1);
		setTextureOffset("Main.Shape2", 26, 4);
		setTextureOffset("Barrel.barrel1", 25, 8);

		Mag= new ModelRenderer(this, "Mag");
		Mag.mirror = true;
		Mag.addBox("mag1", 0F, 4F, -2F, 1, 3, 2);

		Bolt= new ModelRenderer(this, "Bolt");
		Bolt.setRotationPoint(0.2F, 0F, -1F);
		Bolt.mirror = true;
		Bolt.addBox("Shape3", 0F, 0F, 0F, 1, 1, 1);

		Main= new ModelRenderer(this, "Main");
		Main.setRotationPoint(0F, 0F, 0F);
		Main.mirror = true;
		Main.addBox("Shape1", 0F, -1F, 1F, 1, 2, 8);
		Main.addBox("Shape5", 0F, 1F, 3F, 1, 2, 1);
		Main.addBox("Shape8", 0F, 2F, 1F, 1, 1, 2);
		Main.addBox("Shape4", 0F, -1F, -4F, 1, 4, 5);
		Main.addBox("Shape6", 0F, 1F, 4F, 1, 1, 1);
		Main.addBox("Shape2", 0F, 1F, 7F, 1, 2, 1);

		Barrel= new ModelRenderer(this, "Barrel");
		Barrel.setRotationPoint(-0.3F, 0F, 9F);
		Barrel.mirror = true;
		Barrel.addBox("barrel1", 0.6F, 0F, 0F, 1, 1, 1);
	}
	
	@Override
	protected void transformRecoil(float r){
		glTranslatef(0, -r*.12f, -r*0.18f);
		glRotatef(r*5f, 1, 0, 0);
	}
	
	protected void transform1stPerson(){
		glTranslatef(.45f, .4f, 0.2f);
		glRotatef(80, 0, 1, 0);//yaw
		glRotatef(-32, 1, 0, 0);//pitch
		glRotatef(180, 0, 0, 1);//roll		
	}
	protected void transform3rdPerson(){
		glRotatef(-169, 0, 1, 0);
		glRotatef(-17, 0, 0, 1);
		glRotatef(170, 1, 0, 0);
		glTranslatef(-.67f, -.28f, -.38f);		
	}
	@Override
	protected float getScale() {
		return 0.135f;	}
	@Override
	protected float getWidthSqrt(){
		return 1.2f;	}
	
	@Override
	public float getRecoilMultiplier() {
		return 0.865f;	}

	@Override
	protected String getTexName() {
		return "SMG";
	}
}
