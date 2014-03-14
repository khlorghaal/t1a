package com.typ1a.common.Network;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

public class PacketBombard extends T1APacket {

	public static PacketBombard instance;
	
	protected PacketBombard() {
		super();
		instance=this;
	}

	/**types*/
	public static final byte LASER=0, ARTY=1, NUKE=2;  
	public void send(int type, int power, int focus, int n, double x, double z){
		try {
			prepareToBuildPacket();

			dataOutStream.writeByte(type);
			dataOutStream.writeByte(focus);
			dataOutStream.writeByte(n);
			dataOutStream.writeFloat((float) x);
			dataOutStream.writeFloat((float) z);
			
			sendToServer();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void process(DataInputStream iStream, EntityPlayer player) throws IOException{
		int type= iStream.readByte();
		int focus= iStream.readByte();
		int n= iStream.readByte();
		double x= iStream.readFloat();
		double z= iStream.readFloat();
		System.out.println("i gots");
	//	new EntitySatBeam(player.worldObj, player.posX, player.posZ);
	}

}
