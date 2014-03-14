package com.typ1a.client.rendery.guns;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_BLEND_DST;
import static org.lwjgl.opengl.GL11.GL_BLEND_SRC;
import static org.lwjgl.opengl.GL11.GL_DST_COLOR;
import static org.lwjgl.opengl.GL11.GL_ENABLE_BIT;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_COLOR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4ub;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import net.minecraft.client.model.ModelRenderer;

public class RenderTOW extends RenderSmallArm {

	ModelRenderer barrel0;
	ModelRenderer barrel1;
	ModelRenderer barrel2;
	ModelRenderer barrel3;
	ModelRenderer Shape2;
	ModelRenderer Body;

	public RenderTOW(){
		textureWidth = 64;
		textureHeight = 32;
		setTextureOffset("Body.Shape8", 4, 10);
		setTextureOffset("Body.Shape5", 0, 0);
		setTextureOffset("Body.Shape6", 0, 6);
		setTextureOffset("Body.Shape7", 0, 10);
		setTextureOffset("Body.Shape1", 11, 6);
		setTextureOffset("Body.Shape2", 11, 12);

		barrel0= new ModelRenderer(this, 0, 0);
		barrel0.addBox(-1F, -6F, 0F, 1, 6, 26);
		barrel0.setRotationPoint(-3F, 0F, -8F);
		barrel0.setTextureSize(64, 32);
		barrel0.mirror = true;
		barrel1= new ModelRenderer(this, 0, 0);
		barrel1.addBox(-1F, 0F, 0F, 1, 6, 26);
		barrel1.setRotationPoint(-3F, -1F, -8F);
		barrel1.setTextureSize(64, 32);
		barrel1.mirror = true;
		barrel1.rotateAngleZ= -1.570796F;
		barrel2= new ModelRenderer(this, 0, 0);
		barrel2.addBox(0F, 0F, 0F, 1, 6, 26);
		barrel2.setRotationPoint(3F, -1F, -8F);
		barrel2.setTextureSize(64, 32);
		barrel2.mirror = true;
		barrel2.rotateAngleZ= -3.141593F;
		barrel3 = new ModelRenderer(this, 0, 0);
		barrel3.addBox(0F, 0F, 0F, 1, 6, 26);
		barrel3.setRotationPoint(2F, -7F, -8F);
		barrel3.setTextureSize(64, 32);
		barrel3.mirror = true;
		barrel3.rotateAngleZ= 1.570796F;

		Body = new ModelRenderer(this, "Body");
		Body.setRotationPoint(0F, 0F, 0F);
		Body.mirror = true;
		Body.addBox("Shape2", 11F, 0F, 1F, 1, 2, 1);
		Body.addBox("Shape8", -2F, 1F, -2F, 2, 1, 1);
		Body.addBox("Shape5", 3.1F, -6F, 0F, 8, 5, 1);
		Body.addBox("Shape6", -3F, 0F, -2F, 1, 2, 1);
		Body.addBox("Shape7", 0F, 0F, -2F, 1, 2, 1);
		Body.addBox("Shape1", 11F, -3F, 0F, 1, 3, 2);
	}
	@Override
	protected void doRendering(float r){
		Body.render(getScale());
		barrel0.render(getScale());
		barrel1.render(getScale());
		barrel2.render(getScale());
		barrel3.render(getScale());

		if(this.currentPlayer==mc.thePlayer
				&& mc.gameSettings.thirdPersonView==0){
			//render screen
			glPushMatrix();
			glPushAttrib(GL_ENABLE_BIT);
			glPushAttrib(GL_BLEND_SRC);
			glPushAttrib(GL_BLEND_DST);
			glLoadIdentity();
//			transformRecoil(-r);
			glScalef(0.47f, 0.25f, 1);
			glEnable(GL_BLEND);
			glDisable(GL_LIGHTING);
			glDepthMask(false);
			glBlendFunc(GL_DST_COLOR, GL_SRC_COLOR);
			glDisable(GL_TEXTURE_2D);
			final int s= 1;//T1A.random.nextInt(3);
			switch(s){
			case 0:
				glColor4ub((byte)0xe0, (byte)0x0, (byte)0x0, (byte)0xff);
				break;
			case 1:
				glColor4ub((byte)0x0, (byte)0xe0, (byte)0x0, (byte)0xff);
				break;
			case 2:
				glColor4ub((byte)0x0, (byte)0x0, (byte)0xff, (byte)0xe0);
			}
			glBegin(GL_QUADS);
			glVertex3f(-1, -1,-0.55f);
			glVertex3f(1, -1,-0.55f);
			glVertex3f(1, 1,-0.55f);
			glVertex3f(-1, 1,-0.55f);
			glEnd();
			glPopAttrib();
			glPopAttrib();
			glPopAttrib();
			glPopMatrix();
		}
	}

	@Override
	protected float getScale() {
		return .1f;
	}

	@Override
	public float getRecoilMultiplier() {
		return -.012f;
	}
	@Override
	protected void transformRecoil(float r){
		glTranslatef(0, 0, r*.10f);
		glRotatef(r*3, 1, 0, 0);
	}

	@Override
	protected void transform1stPerson() {
		glLoadIdentity();

		glTranslatef(0.72f, -.35f, -0.55f);
		glRotatef(180, 0, 0, 1);
	}

	@Override
	protected void transform3rdPerson() {
		glRotatef(200, 0, 1, 0);//yaw
		glRotatef(-12, 1, 0, 0);//pitch
		glRotatef(165, 0, 0, 1);//roll
		glTranslated(.3, -.0, -0.1);
	}

	@Override
	protected String getTexName() {
		return "TOW";
	}

}
