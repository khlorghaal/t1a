package com.typ1a.client;

import static java.lang.Math.abs;

import java.util.EnumSet;
import java.util.Timer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

import t1a.client.gui.OverlayAmmo;
import t1a.client.rendery.guns.RenderRifle;
import t1a.client.rendery.guns.RenderSMG;
import t1a.client.rendery.guns.RenderShotgun;
import t1a.common.T1A;
import t1a.common.Ticker;
import t1a.common.BlocksItems.ItemUseable;
import t1a.common.Network.PacketTriggerPress;
import t1a.common.SmallArms.ItemSmallArm;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**mostly for non-keyboard input handling 
 * and sending corresponding packets.*/
public class T1ATickHandlerClient implements ITickHandler {

	public static Minecraft mc= Minecraft.getMinecraft();


	boolean hasSentRMB=false;

	/**funny thing, for differential applications, if it overflows
	 *  itll only cause one error until the next update*/
	public int currentTick= -0xffffffff, prevTick=0;

	/**for jamming fx*/
	public static boolean r= false, g= false, b= false;

	public static float vehiclestartupT=0f;

	@Override
	@SideOnly(Side.CLIENT)
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

		if(type.contains(TickType.CLIENT)){
			currentTick++;

			final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if(player==null)
				return;
			final ItemStack held = player.getHeldItem();



			//Trigger stuff
			if(!hasSentRMB && Mouse.isButtonDown(1) && mc.inGameHasFocus){
				PacketTriggerPress.instance.send(true, player);
				hasSentRMB= true;
			}
			else if(hasSentRMB && !Mouse.isButtonDown(1)){
				PacketTriggerPress.instance.send(false, player);
				hasSentRMB= false;
			}
		}
		else if(type.contains(TickType.RENDER)){
			updatepT();
		}



	}

	/**1/(frames/tick)
	 * a = Σ(a/n, n) = Π(a^(1/n), n)*/
	public static float pT;
	int timeLastFrame, timeThisFrame;
	int timeLastTick, timeThisTick;
	private void updatepT(){
		timeLastFrame= timeThisFrame;
		timeThisFrame= (int) System.currentTimeMillis();
		pT= 1/( (timeThisFrame - timeLastFrame +0.00001f)/ 20 );
		// = 1/[ frames/tick = frames/second / ticks/second ]
		// hits/time = 1/(time/hit)
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData){
		if(type.contains(TickType.RENDER)){

			//ammo overlay
			if(Minecraft.getMinecraft().inGameHasFocus){
				final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				if(player==null)
					return;
				final ItemStack held = player.getHeldItem();

				if(held!=null && held.getItem() instanceof ItemSmallArm
						&& held.stackTagCompound!=null)
					OverlayAmmo.render(held);
			}
			//vehicle startup
			//			if(mc.thePlayer.ridingEntity==null)
			//				vehiclestartupT=0;
			//			if(vehiclestartupT!=0){
			if(false){
				vehiclestartupT-= 1*((Float)tickData[0]);
				glPushMatrix();
				glPushAttrib(GL_ENABLE_BIT);
				glPushAttrib(GL_BLEND_SRC);
				glPushAttrib(GL_BLEND_DST);
				glLoadIdentity();
				glScalef(0.47f, 0.25f, 1);
				glEnable(GL_BLEND);
				glDisable(GL_LIGHTING);
				glDepthMask(false);
				glBlendFunc(GL_DST_COLOR, GL_SRC_COLOR);
				glDisable(GL_TEXTURE_2D);
				final int s= 1;//T1A.random.nextInt(3);
				switch(s){
				case 0:
					glColor4ub((byte)0xe0, (byte)0x0, (byte)0x0, (byte)0xff);
					break;
				case 1:
					glColor4ub((byte)0x0, (byte)0xe0, (byte)0x0, (byte)0xff);
					break;
				case 2:
					glColor4ub((byte)0x0, (byte)0x0, (byte)0xff, (byte)0xe0);
				}
				glBegin(GL_QUADS);
				glVertex3f(-1, -1,-0.55f);
				glVertex3f(1, -1,-0.55f);
				glVertex3f(1, 1,-0.55f);
				glVertex3f(-1, 1,-0.55f);
				glEnd();
				glPopAttrib();
				glPopAttrib();
				glPopAttrib();
				glPopMatrix();
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks(){
		return EnumSet.of(TickType.RENDER, TickType.CLIENT);}

	@Override
	public String getLabel() {return "T1AClient";}

}
