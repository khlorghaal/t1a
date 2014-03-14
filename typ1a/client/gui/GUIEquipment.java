package com.typ1a.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.typ1a.common.Containers.ContainerEquipment;
import com.typ1a.common.Equipment.EquipmentFacade;

public class GUIEquipment extends GuiContainer {

	protected EquipmentFacade eqf;
	final int[] cat0cov;
	final int[] cat1cov;
	final int[] cat2cov;
	public GUIEquipment(InventoryPlayer pla, EquipmentFacade eqf) {
		super(new ContainerEquipment(pla, eqf));
		xSize=176;
		ySize=221;
		this.eqf= eqf;

		cat0cov= new int[(ContainerEquipment.basewpn.length - eqf.nwpn*2)*4];//*4 for ammo slots
		cat1cov= new int[ContainerEquipment.basearmr.length - eqf.narmor*2];
		cat2cov= new int[ContainerEquipment.baseequip.length - eqf.nequip*2];
		
		System.arraycopy(ContainerEquipment.basewpn, eqf.nwpn*2, cat0cov, 0, cat0cov.length/4);
		System.arraycopy(ContainerEquipment.baseammo, eqf.nwpn*2*3, cat0cov, cat0cov.length/4, cat0cov.length*3/4);
		
		System.arraycopy(ContainerEquipment.basearmr, eqf.narmor*2, cat1cov, 0, cat1cov.length);
		System.arraycopy(ContainerEquipment.baseequip, eqf.nequip*2, cat2cov, 0, cat2cov.length);
	}

	private static ResourceLocation rsl= new ResourceLocation("t1a:gui//Vehicle.png");
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3){
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(rsl);
		final int x = (width - xSize) / 2;
		final int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glPushMatrix();
		GL11.glTranslatef(this.guiLeft, this.guiTop, 0);
		GL11.glColor3ub((byte)180, (byte)180, (byte)180);
		coverSlots(cat0cov);
		coverSlots(cat1cov);
		coverSlots(cat2cov);
		GL11.glPopMatrix();
	}

	protected void coverSlots(int[] coords){

		for(int i=0; i!=coords.length;){
			int x= coords[i++];
			int y= coords[i++];
			x-=1; y-=1;
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x, y+18);
			GL11.glVertex2f(x+18, y+18);
			GL11.glVertex2f(x+18, y);
			GL11.glEnd();
		}
	}
}

