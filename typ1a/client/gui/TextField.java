package com.typ1a.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class TextField extends Clickable {

	public StringBuilder str= new StringBuilder();
	public int color;
	public int capacity;
	
	/**@param capacity max strlen*/
	public TextField(int color, int xpos, int ypos, int capacity) {
		super(xpos, ypos, capacity*6+4, 13, 0,45);
		this.color=color;
		animated=false;
		stickySelection=true;
		this.capacity= capacity;
	}
	public TextField(int xpos,int ypos, int capacity){
		this(0xdadada, xpos, ypos, capacity);
	}

	@Override
	public void clickRelease(){}
	
	@Override
	public void deselect(){
		state=0;
	}
	
	@Override
	public void keyTyped(char c, int keyId){
		System.out.println(state);
		if(c==/*backspace*/(char)8 && str.length()!=0)
			str.setLength(str.length()-1);
		//only accept numbers, up to certain length
		else if((c>47 && c<58) && str.length()!=capacity)
			str=str.append(c);
		//accept negatives
		else if(c=='-'){
			if(str.length()==0)
				str.append('-');
			else if(str.charAt(0)!='-')
				str.insert(0, '-');
			else
				str.deleteCharAt(0);}
	}
	
	@Override
	public void draw(Gui gui){
		super.draw(gui);
		Minecraft.getMinecraft().fontRenderer.drawString(
				str.toString() +(state==1?str.length()<capacity?"_":"":""),//if active draw underscore to show that 
				x+3, y+3, color);
		//FR borks state, fix it
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("t1a:gui/Clickables.png"));
		GL11.glColor3f(1, 1, 1);
	}
}
