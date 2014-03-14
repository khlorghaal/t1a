package com.typ1a.client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.typ1a.common.T1A;
import com.typ1a.common.Network.PacketKeys;
import com.typ1a.common.Vehicles.IControlled;

import cpw.mods.fml.common.Mod.EventHandler;

public class T1AKeyHandler{

	public static T1AKeyHandler instance;

	private static Minecraft mc;
	private boolean changed=false;
	private int packetData=0;
	
	public static KeyBinding[] keyBinds= new KeyBinding[]{
		new KeyBinding("t1a Vehicle Fwd", Keyboard.KEY_W),
		new KeyBinding("t1a Vehicle Strf L", Keyboard.KEY_A),
		new KeyBinding("t1a Vehicle Back", Keyboard.KEY_S),
		new KeyBinding("t1a Vehicle Strf R", Keyboard.KEY_D),
		new KeyBinding("t1a Vehicle Jump/Up", Keyboard.KEY_SPACE),
		new KeyBinding("t1a Vehicle Crouch/Down", Keyboard.KEY_LSHIFT),
		new KeyBinding("t1a Reload", Keyboard.KEY_R),
		new KeyBinding("t1a Vehicle Bail", Keyboard.KEY_B),
		new KeyBinding("t1a Open CommPack", Keyboard.KEY_BACKSLASH),
		new KeyBinding("t1a Control", Keyboard.KEY_LCONTROL)//index 9
	};

	public T1AKeyHandler() {
		super(keyBinds,
				//I have no idea what repeatings do
				new boolean[keyBinds.length]);

		this.instance=this;
		mc=Minecraft.getMinecraft();
	}

	@Override
	public String getLabel() {
		return "T1A_keyHandler";
	}

	@EventHandler
	public void keyDown(EnumSet<TickType> types, KeyBinding kb,	boolean tickEnd, boolean isRepeat) {
		if(tickEnd)
			return;

		if(mc.inGameHasFocus)
			for (KeyBinding key : mc.gameSettings.keyBindings )
				if (key.keyCode==kb.keyCode && key != kb){
					key.pressed = true; key.pressTime+= 1;}

		if(!mc.inGameHasFocus && kb.keyCode==mc.gameSettings.keyBindInventory.keyCode)
			mc.gameSettings.keyBindInventory.pressTime=0;//this shouldnt be necessary but it is

		changed=false;


		if(mc.inGameHasFocus){
			for(int i=0; i<T1A.nums.length; i++)
			if(kb.keyCode==keyBinds[i].keyCode){
				packetData |= T1A.nums[i]; changed=true;
			}
			//Tech Vechile riding
			if (mc.thePlayer.ridingEntity != null 
					&& mc.thePlayer.ridingEntity instanceof IControlled){

				

				//disallow these actions while riding
				if (mc.gameSettings.keyBindInventory.pressed){
					//TODO open vechile inventory
					System.out.println("OPEN EQUAPMENT");
					mc.gameSettings.keyBindInventory.pressed = false;
					mc.gameSettings.keyBindInventory.pressTime = 0;}

				if(mc.gameSettings.keyBindSneak.pressed){
					mc.gameSettings.keyBindSneak.pressed = false;
					mc.gameSettings.keyBindSneak.pressTime = 0;}				
			}

		}

//		if(isKeyPressed(packetData, T1A.COMMPACK) && mc.inGameHasFocus)
//			mc.thePlayer.openGui(T1A.instance, T1AGUIHandler.COMMPACK, mc.thePlayer.worldObj, 0, 0, 0);

		if(changed)
			PacketKeys.instance.send(packetData); //The packet class sets keys locally
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		if(tickEnd)
			return;

		for (KeyBinding key : mc.gameSettings.keyBindings)
			if (kb.keyCode == key.keyCode && key != kb){
				key.pressed= false; key.pressTime= 0;}

		changed=false;

		if(mc.inGameHasFocus){

				for(int i=0; i<T1A.nums.length; i++)
					if(kb.keyCode==keyBinds[i].keyCode){
						packetData &= ~T1A.nums[i]; changed=true;
					}
			}

		if(changed)
			PacketKeys.instance.send(packetData); //The packet class sets keys locally
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
