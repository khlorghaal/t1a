package com.typ1a.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.typ1a.common.T1A;
import com.typ1a.common.Network.PacketGuiUpdate;
import com.typ1a.common.Network.PacketOpenEquipment;

/**Is not always a container, it is this way for convenience*/
public abstract class ProperGUI extends GuiContainer {

	/**screen position*/
	protected int windowX,windowY;
	/**For centering, mainly for commpack*/
	protected int offsx,offsy;

	private String texName;

	private List<Clickable> clickables= new ArrayList<Clickable>();

	Clickable activePanel;

	/**will remain false until server gives the state of the target*/
	private boolean synched=false;

	public ProperGUI(Container container, String texture) {
		super((container==null)? DummyContainer.instance : container);
		texName="t1a:gui/"+texture+".png";

		PacketGuiUpdate.instance.askForUpdate();
	}
	@Override
	public void initGui(){
		super.initGui();
	}

	protected void addClickable(Clickable clickable){
		clickables.add(clickable);
	}

	public void openEquipmentGUI(){
		PacketOpenEquipment.instance.send();
	}

	public void sendUpdate(){
		if(synched){
			//send state for every clickable
			final int[] states= new int[clickables.size()];
			for(int i=0; i!=clickables.size(); i++)
				states[i]= clickables.get(i).state;
			PacketGuiUpdate.instance.sendUpdateToServer(states);

//			System.out.println("update client states");//update local client
			T1A.setGuiableStates(mc.thePlayer, states);
		}
	}

	public void recieveUpdate(int[] states){
		synched=true;

		if(states.length!=clickables.size())
			return;

		for(int i=0; i<states.length; i++)
			clickables.get(i).state= states[i];
	}

	@Override
	public void mouseClicked(int x, int y, int c){
		super.mouseClicked(x, y, c);

		x-= windowX;
		y-= windowY;

		for(Clickable clickable : clickables){

			if(clickable.isClickInside(x, y)){
				if(activePanel!=null)
					activePanel.deselect();
				this.activePanel= clickable;

				this.sendUpdate();
				break;
			}
		}
	}

	@Override
	public void mouseMovedOrUp(int a, int b, int which){
		super.mouseMovedOrUp(a, b, which);
		if(activePanel==null)
			return;

		if(which!=-1){
			activePanel.clickRelease();
			if(!activePanel.stickySelection)
				this.activePanel=null;
		}
		this.sendUpdate();
	}

	@Override
	public void keyTyped(char c, int keyId){
		super.keyTyped(c, keyId);
		if(activePanel!=null){
			activePanel.keyTyped(c, keyId);
		}
		if(keyId==Keyboard.KEY_TAB)
			tabPressed();

		this.sendUpdate();
	}
	public void tabPressed(){}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		if(synched){
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glColor3f(1, 1, 1);

			this.windowX = (width - xSize +offsx) / 2;//center the scrren
			this.windowY = (height - ySize +offsy) / 2;

			this.mc.renderEngine.bindTexture(new ResourceLocation(texName));
			this.drawTexturedModalRect(windowX, windowY, 0, 0, xSize, ySize);

			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("t1a:gui/Clickables.png"));
			GL11.glPushMatrix();
			GL11.glTranslated(windowX, windowY,1);
			for(Clickable clickable : clickables){
				clickable.draw(this);
			}
			GL11.glPopMatrix();
		}
		else{
		//TODO draw synch wait bg
			
		}
	}

	public static class DummyContainer extends Container{
		public static DummyContainer instance= new DummyContainer();
		private DummyContainer(){}
		@Override
		public boolean canInteractWith(EntityPlayer entityplayer) {
			return false;
		}
	}
}
