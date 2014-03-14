package com.typ1a.client.gui;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import net.minecraft.client.renderer.RenderHelper;

import com.typ1a.common.Network.PacketBombard;

public class GUICommPack extends ProperGUI {

	Slider power= new Slider(94, 21)
	, focus= new Slider(94, 36)
	, rate= new Slider(94, 51);
	TextField ptf= new TextField(75, 20, 2)
	,dtf= new TextField(75, 35, 2)
	,rtf= new TextField(75, 50, 2)
	;
	Clickable launchButton = new Clickable(209,27 ,11,11,137,0);

	public GUICommPack() {
		super(null, "CommPack");

		this.xSize=248;
		this.ySize=193;
		this.offsy=-45;
		launchButton.clickAction= new Runnable() {
			public void run() {
			}
		};

		power.state= 99;
		focus.state= 99;
		
		addClickable(power);
		addClickable(focus);
		addClickable(rate);
		
		addClickable(ptf);
		addClickable(dtf);
		addClickable(rtf);
		power.clickAction= new Adjustor.StateToNumberText(power, ptf);
//		ptf.clickAction= new Adjustor.NumberTextToState(ptf, power);
		focus.clickAction= new Adjustor.StateToNumberText(focus, dtf);
//		dtf.clickAction= new Adjustor.NumberTextToState(dtf, dispersion);
		rate.clickAction= new Adjustor.StateToNumberText(rate, rtf);
//		rtf.clickAction= new Adjustor.NumberTextToState(rtf, rate);

		addClickable(launchButton);

		launchButton.clickAction= calldown;
		
		
		power.clickAction.run();
		focus.clickAction.run();
		rate.clickAction.run();
	}

	final Runnable calldown= new Runnable() {
		public void run() {
			PacketBombard.instance.send(0, power.state, focus.state, 1, -1, -1);
		}
	};
	///////////////////////////////////////////
	private boolean justOpened=true;
	@Override
	public void keyTyped(char c, int keyId){
		if(c=='\\'){
			if(!justOpened){
				this.mc.thePlayer.closeScreen();
				return;
			}
			justOpened=false;
		}
		
		if(c==' ')
			calldown.run();
		
		super.keyTyped(c, keyId);	
	}
	

	@Override
	public void drawDefaultBackground(){}
	@Override
	public void drawScreen(int par1, int par2, float par3){
		super.drawScreen(par1, par2, par3);
		RenderHelper.disableStandardItemLighting();
    }
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		glPushAttrib(GL_BLEND);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		super.drawGuiContainerBackgroundLayer(f, i, j);
		glPopAttrib();
	}
}
