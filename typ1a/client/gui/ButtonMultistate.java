package com.typ1a.client.gui;


public class ButtonMultistate extends Clickable {

	public int maxStates=2;
	
	private ButtonMultistate(int maxStates, int xpos, int ypos, int w, int h, int u, int v){
		super(xpos, ypos, w, h, u, v);
		this.maxStates= maxStates;
	}
	
	public static ButtonMultistate newBool(int xpos, int ypos){
		return new ButtonMultistate(2, xpos, ypos, 18,11, 73, 0);
	}
	public static ButtonMultistate newAggroSetter(int xpos, int ypos){
		return new ButtonMultistate(3, xpos, ypos, 73,11, 0, 0);
	}
	public static ButtonMultistate newTargetSizeSetter(int xpos, int ypos){
		return new ButtonMultistate(3, xpos, ypos, 62,11, 0, 59);
	}

	@Override
	protected void click(int x, int y){
		if(++state>=maxStates)
			state=0;
	}
	@Override
	public void clickRelease(){}
	
}
