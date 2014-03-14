package com.typ1a.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import com.typ1a.client.T1AKeyHandler;

public class GUILaser extends GuiScreen {

	public static class buttons{
		public GuiButton a;
		
		public void draw(){
			
		}
	}
	public static buttons buttons;
	
	
	@Override
	public void mouseClicked(int x, int y, int c){
		super.mouseClicked(x, y, c);

		////////////////

		////////////////
	}
	@Override
	public void mouseMovedOrUp(int a, int b, int which){
		super.mouseMovedOrUp(a, b, which);
		
		}

	@Override
	protected void keyTyped(char par1, int par2){
		super.keyTyped(par1, par2);
		if(par2==T1AKeyHandler.keyBinds[5].keyCode){
			this.mc.displayGuiScreen((GuiScreen)null);
        	this.mc.setIngameFocus();}
		
    }
	
	@Override
	public void drawBackground(int par1){
		super.drawBackground(par1);
        
        this.buttons.draw();
	}

}
