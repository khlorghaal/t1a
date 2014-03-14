package com.typ1a.client.gui;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glColor4b;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class ClickableDial extends Clickable {

	public int color= 0x00f04a;
	
	private float dialCenterX, dialCenterY;
	
	public ClickableDial(int xpos, int ypos, int w, int h, int u, int v, float dialCenterX, float dialCenterY) {
		super(xpos, ypos, w, h, u, v);
		this.dialCenterX= dialCenterX;
		this.dialCenterY= dialCenterY;
		this.stickySelection=true;
		this.animated=false;
	}

	@Override
	public void draw(Gui gui){
		super.draw(gui);
		
		glDisable(GL_TEXTURE_2D);
		
		glColor4b(
				(byte)(color&0xff0000/0x10000),
				(byte)(color&0x00ff00/0x100),
				(byte)(color&0x0000ff),
				(byte)(0xff));
		
		
		//TODO FIX

		glEnable(GL_TEXTURE_2D);
		glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("t1a:gui/Clickables.png"));
	}
}
