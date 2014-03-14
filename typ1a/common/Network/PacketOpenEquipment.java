package com.typ1a.common.Network;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import t1a.common.T1A;
import t1a.common.T1AGUIHandler;

public class PacketOpenEquipment extends T1APacket {

	public static PacketOpenEquipment instance;
	
	protected PacketOpenEquipment() {
		super();
		instance= this;
	}

	public void send(){
		try {
			prepareToBuildPacket();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendToServer();
	}
	
	@Override
	protected void process(DataInputStream iStream, EntityPlayer player)
			throws IOException {
		player.openGui(T1A.instance, T1AGUIHandler.EQUIPMENT, player.worldObj, -1, -1, -1);
	}

}
