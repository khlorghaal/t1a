package com.typ1a.client.gui;

import net.minecraft.client.gui.Gui;

public class Slider extends Clickable {

	public Slider(int xpos, int ypos) {
		super(xpos, ypos, 50,9,0,0);
		this.stickySelection= true;
	}

	@Override
	protected void click(int x, int y){
		this.state= 2*(x-this.x);
		constrainState();
		clickAction.run();
	}
	@Override
	public void clickRelease(){}
	@Override
	public void keyTyped(char c, int keyId){
		System.out.println(keyId);
		switch(keyId){
		case 203: state-=10; break;
		case 205: state+=10; break;
		case 200: state++; break;
		case 208: state--; break;
		}
		constrainState();
		this.clickAction.run();
	}	

	public void constrainState(){
		if(state>99)
			state=99;
		if(state<0)
			state=0;
	}
	
	@Override
	public void draw(Gui gui){
		gui.drawTexturedModalRect(x, y+2, 0, 251, 102, 5);
		gui.drawTexturedModalRect(x+(state/2)-2, y, 0, 242, 5, 9);
	}
}
