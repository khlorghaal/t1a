package com.typ1a.client.gui;

public class ToggleAbsRel extends Clickable {

	/**remembers state for the length of session*/
	public static int pstate=1; 
	
	public ToggleAbsRel(int xpos, int ypos) {
		super(xpos,ypos, 44, 11, 148, 0);
		state=pstate;
	}
	@Override
	protected void click(int x,int y){
		if(state==0)
			state=1;
		else
			state=0;
		pstate=state;
	}
	
	@Override
	public void clickRelease(){}
	
}
