package com.typ1a.client.rendery.vehicle.concrete;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import com.typ1a.client.rendery.vehicle.ModelMech;
import com.typ1a.common.Vehicles.EntityVehicle;
import com.typ1a.common.Vehicles.Tech.EntityMech;

public class ModelRavener extends ModelMech{

	@Override
	public void render(EntityVehicle e, float pT) {
		GL11.glRotatef(180, 0, 0, 1);
		GL11.glRotatef(-90, 0, 1, 0);
		bindTexture();
		this.animate((EntityMech)e, pT);
		model.render();
	}

	@Override
	public void animate(EntityMech e, float pT) {
		model.FemurL.rotateAngleX= MathHelper.cos((float) e.walkProg);
		model.FemurR.rotateAngleX= MathHelper.cos((float) (e.walkProg +3.14));

		model.FemurL.rotateAngleZ=MathHelper.cos((float) e.sideWalk );
		model.FemurR.rotateAngleZ=MathHelper.cos((float) e.sideWalk );
	}
	
	@Override
	public String getResName() {
		return "ravener";
	}
	
	Model model= new Model();
	class Model extends ModelBase{
		//fields
		ModelRenderer FemurR;
		ModelRenderer FemurL;
		ModelRenderer Body;

		Model(){
			textureWidth = 128;
			textureHeight = 128;
			setTextureOffset("FemurR.FemurR", 0, 47);
			setTextureOffset("TibiaR.TibiaR", 0, 71);
			setTextureOffset("FootR.FootR", 0, 107);
			setTextureOffset("FemurL.FemurL", 0, 47);
			setTextureOffset("TibiaL.TibiaL", 0, 71);
			setTextureOffset("FootL.FootL", 0, 107);
			setTextureOffset("Body.Body", 0, 0);
			setTextureOffset("Body.Gun1", 74, 100);
			setTextureOffset("Body.Gun2", 74, 100);
			setTextureOffset("Body.Launcher", 104, 84);
			setTextureOffset("Body.Shape1", 47, 47);

			FemurR = new ModelRenderer(this, "FemurR");
			FemurR.setRotationPoint(10F, 0F, 2F);
			FemurR.mirror = true;
			FemurR.addBox("FemurR", -2F, -5F, -10F, 8, 8, 15);
			ModelRenderer TibiaR = new ModelRenderer(this, "TibiaR");
			TibiaR.setRotationPoint(1F, 0F, -9F);
			TibiaR.mirror = true;
			TibiaR.addBox("TibiaR", -2F, -2F, -6F, 6, 22, 8);
			ModelRenderer FootR = new ModelRenderer(this, "FootR");
			FootR.setRotationPoint(1F, 18F, -3F);
			FootR.mirror = true;
			FootR.addBox("FootR", -4F, 1F, -6F, 8, 4, 16);
			TibiaR.addChild(FootR);
			FemurR.addChild(TibiaR);
			FemurL = new ModelRenderer(this, "FemurL");
			FemurL.setRotationPoint(-10F, 0F, 2F);
			FemurL.mirror = true;
			FemurL.mirror = true;
			FemurL.addBox("FemurL", -6F, -5F, -10F, 8, 8, 15);
			FemurL.mirror = false;
			ModelRenderer TibiaL = new ModelRenderer(this, "TibiaL");
			TibiaL.setRotationPoint(-2F, 0F, -8F);
			TibiaL.mirror = true;
			TibiaL.mirror = true;
			TibiaL.addBox("TibiaL", -3F, -2F, -7F, 6, 22, 8);
			TibiaL.mirror = false;
			ModelRenderer FootL = new ModelRenderer(this, "FootL");
			FootL.setRotationPoint(0F, 18F, -3F);
			FootL.mirror = true;
			FootL.mirror = true;
			FootL.addBox("FootL", -4F, 1F, -6F, 8, 4, 16);
			FootL.mirror = false;
			TibiaL.addChild(FootL);
			FemurL.addChild(TibiaL);
			Body = new ModelRenderer(this, "Body");
			Body.setRotationPoint(0F, 0F, -1F);
			Body.mirror = true;
			Body.addBox("Body", -8F, -9F, -13F, 16, 14, 32);
			Body.addBox("Gun1", 2F, 5F, -1F, 3, 3, 24).mirror=true;
			Body.addBox("Gun2", -5F, 5F, -1F, 3, 3, 24);
			Body.addBox("Launcher", -4F, -10F, -14F, 9, 12, 3);
			Body.addBox("Shape1", -5F, -10F, -3F, 10, 1, 18);
		}
		
		public void render() {
			float scale= .1f;
			FemurR.render(scale);
			FemurL.render(scale);
			Body.render(scale);

		}
	}
}
