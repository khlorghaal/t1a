package com.typ1a.client.gui;

import net.minecraft.inventory.Container;

public abstract class GUIMachine extends ProperGUI {

	public GUIMachine(Container container, String texture) {
		super(container, texture);
	}
	
	public static class part{
		public boolean[][] material= new boolean[32][32];
		
		public void mill(int z, int y){
			
		}
		public void lathe(int x, int y){
			
		}
	}
}
