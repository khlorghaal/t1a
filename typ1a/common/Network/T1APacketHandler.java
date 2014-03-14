package com.typ1a.common.Network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class T1APacketHandler implements IPacketHandler{
	public T1APacketHandler(){
		T1APacket.initAll();
	}
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player par3Player) {
		EntityPlayer player= (EntityPlayer)par3Player;
		DataInputStream iStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		T1APacket.handle(iStream, player);
	}
}
