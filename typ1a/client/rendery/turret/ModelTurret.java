package com.typ1a.client.rendery.turret;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import org.lwjgl.opengl.GL11;

import com.typ1a.common.Robots.EntitySentry;

public class ModelTurret extends ModelBase{

	ModelRenderer Base3;
	ModelRenderer Base2;
	ModelRenderer Base1;
	ModelRenderer Gimbals;
	ModelRenderer Base;
	ModelRenderer Gun;
	ModelRenderer Barrel;

	public ModelTurret(){
		textureWidth = 64;
		textureHeight = 32;
		setTextureOffset("Gun.Action", 0, 0);
		setTextureOffset("Gun.Barrel", 28, 0);
		setTextureOffset("Gun.Fancy2", 0, 7);
		setTextureOffset("Gun.Fancy3", 48, 15);
		setTextureOffset("Gun.Fancy4", 48, 27);

		Base3 = new ModelRenderer(this, 0, 15);
		Base3.addBox(-10F, 0.1F, -1.5F, 20, 1, 3);
		Base3.setRotationPoint(0F, 0F, 0F);
		Base3.setTextureSize(64, 32);
		Base3.mirror = true;
		setRotation(Base3, 0F, 0.7853982F, 0F);
		Base2 = new ModelRenderer(this, 0, 19);
		Base2.addBox(-6F, 0F, -6F, 12, 1, 12);
		Base2.setRotationPoint(0F, 0F, 0F);
		Base2.setTextureSize(64, 32);
		Base2.mirror = true;
		setRotation(Base2, 0F, 0F, 0F);
		Base1 = new ModelRenderer(this, 0, 15);
		Base1.addBox(-10F, 0.1F, -1.5F, 20, 1, 3);
		Base1.setRotationPoint(0F, 0F, 0F);
		Base1.setTextureSize(64, 32);
		Base1.mirror = true;
		setRotation(Base1, 0F, -0.7853982F, 0F);
		Gimbals = new ModelRenderer(this, 22, 0);
		Gimbals.addBox(3F, -5F, -1F, 1, 5, 2);
		Gimbals.addBox(-4F, -5F, -1F, 1, 5, 2);
		Gimbals.setRotationPoint(0F, 0F, 0F);
		Gimbals.setTextureSize(64, 32);
		Gimbals.mirror = true;
		setRotation(Gimbals, 0F, 0F, 0F);
		Base = new ModelRenderer(this, "Base");
		Base.setRotationPoint(0F, 0F, 0F);
		setRotation(Base, 0F, 1.041001F, 0F);
		Base.mirror = true;
		Gun = new ModelRenderer(this, "Gun");
		Gun.setRotationPoint(0F, -4.5F, 0F);
		setRotation(Gun, 0F, 0F, 0F);
		Gun.mirror = true;
		Gun.addBox("Action", -3F, 1F, -2F, 6, 2, 5);
		Gun.addBox("Fancy2", 1F, -1F, -2.2F, 2, 2, 6);
		Gun.addBox("Fancy3", -3F, -1F, -2.2F, 2, 2, 6);
		Gun.addBox("Fancy4", -2F, -2F, -1F, 4, 1, 4);
		
		Barrel = new ModelRenderer(this, 28, 0);
		Barrel.setRotationPoint(0F, -4.5F, 0F);
		setRotation(Barrel, 0F, 0F, 0F);
		Barrel.mirror = true;
		Barrel.addBox(-1F, -1F, -8F, 2, 2, 13);
	}

	public void render(EntitySentry thing, float f5){
		GL11.glRotatef(180, 1, 0, 0);
		
		GL11.glPushMatrix();
		Base.render(f5);
		Base3.render(f5);
		Base2.render(f5);
		Base1.render(f5);
		
		GL11.glRotatef((float) Math.toDegrees( thing.rotationYaw ), 0, 1, 0);
		Gimbals.render(f5);
		
		Gun.render(f5);

		GL11.glTranslatef(0,0, thing.recoil);
		Barrel.render(f5);
				
		GL11.glPopMatrix();

	}

	private static void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
