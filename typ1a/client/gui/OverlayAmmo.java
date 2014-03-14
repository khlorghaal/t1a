package com.typ1a.client.gui;

import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import com.typ1a.common.AmmoStuff.ItemAmmo;
import com.typ1a.common.SmallArms.Cluster;
import com.typ1a.common.SmallArms.ItemSmallArm;
import com.typ1a.common.SmallArms.SmallarmLoading;

public class OverlayAmmo {
	private static final Minecraft mc= Minecraft.getMinecraft();
	private static final RenderItem ri = new RenderItem();
	private static int scale;
	/**proportions of screen, 1 being bottom right*/
//	static int H= , 
//			OFFSX_CH= -, OFFSY_CH= -, 
//			X= --OFFSX_CH, Y= --OFFSY_CH;
	public static void render(ItemStack gun){
		glPushAttrib(GL_LIGHTING);
		glPushMatrix();
		final Cluster[] magcls= SmallarmLoading.getMagClusters(gun);
		
		final ItemSmallArm isa= (ItemSmallArm)gun.getItem();
		final int maxammo;
		if(isa.getInternalMagSize()>0)
			maxammo= isa.getInternalMagSize();
		else
			maxammo= ItemSmallArm.getSTC(gun).getCompoundTag("mag").getCompoundTag("tag").getShort("cap");
		
		final ScaledResolution scr= new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		glTranslatef(scr.getScaledWidth(), scr.getScaledHeight(), 0);
		glScalef(1.f, 1.f, 0);
		glTranslatef(-14, -20-maxammo*3, 0);
		
		final int cha= SmallarmLoading.getChamberRound(gun);
		
		if(cha==-2){//jam
			glPushMatrix();
			glRotatef(40, 0, 0, 1);
			renderRound(0, -6, -7);
			glPopMatrix();
		}
		else if(cha!=-1)
			renderRound(cha, -9, -1);
		
		int y=0;
		for(int i=magcls.length-1; i!=-1; i--){
			final Cluster cls= magcls[i];
			for(int n=cls.n; n!=0; n--){
				renderRound(cls.typ, 0,y);
				y+=3;
			}
		}
			
		glPopMatrix();
		glPopAttrib();
		
	}
	
	static void renderRound(int type, int x, int y){
		if(type==-1)
			return;
		ri.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, 
				new ItemStack(ItemAmmo.ItemRound5mm.instance, 1, type), x, y);
	}
}
