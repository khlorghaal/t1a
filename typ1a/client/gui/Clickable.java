//Author: Khlorghaal

package com.typ1a.client.gui;

import java.awt.event.MouseListener;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.Gui;


/***/
public class Clickable {
	//Used for finding points|boundaries on a scaled gui, allowing reference to true image pixel coordinates instead of screen coordinates

	/**texture sheet coords*/
	protected final int u,v,w,h;
	protected int x,y;
	/**is pressed or position.*/
	public int state=0;

	/**if should multiply state by height to determine image offset*/
	protected boolean animated=true;

	/**does this pane stay selected after click released*/
	public boolean stickySelection=false;

	/**get true w and h from looking directly at the png, remember to add 1*/
	public Clickable(int xpos,int ypos,int w, int h,int u,int v){
		this.x=xpos;
		this.y=ypos;

		this.w=w;
		this.h=h;
		this.u=u;
		this.v=v;
	}

	public boolean isClickInside(int clickX,int clickY){
		if(clickX>=x && clickX<=x+w   &&   clickY>=y && clickY<=y+h){
			this.click(clickX,clickY);
			return true;
		}
		return false;
	}

	public Runnable clickAction=null;
	protected void click(int x, int y){
		this.state=1;
		if(clickAction!=null)
			clickAction.run();
	}
	/**only called when this button was the last one clicked*/
	public void clickRelease(){
		this.state=0;
	}

	/**called when the active panel is no longer this*/
	public void deselect(){}

	/**only called when this is in focus*/
	public void keyTyped(char c, int keyId){}

	public void draw(Gui gui){
		gui.drawTexturedModalRect(x, y, u, v+ (animated? state*h : 0) , w, h);
	}

}