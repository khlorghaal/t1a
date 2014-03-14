package com.typ1a.common.Network;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import net.minecraft.entity.player.EntityPlayer;
import t1a.client.T1AClientProxy;
import t1a.common.IGuiable;
import t1a.common.T1A;
import t1a.common.T1AGUIHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketGuiUpdate extends T1APacket {

	public static PacketGuiUpdate instance;

	protected PacketGuiUpdate() {
		super();
		instance=this;
	}

	static final int REQUEST= -1;
	/**ask the server for an update*/
	public void askForUpdate(){
		try {
			prepareToBuildPacket();
			this.dataOutStream.writeInt(REQUEST);
			super.sendToServer();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendUpdateToServer(int[] states){
		try {
			super.prepareToBuildPacket();

			this.dataOutStream.writeInt(states.length);
			for(int state : states){
				this.dataOutStream.writeInt(state);
			}

			super.sendToServer();
		} catch(IOException e){ e.printStackTrace(); }
	}

	public void sendUpdateToClient(int[] states, EntityPlayer player){
		try {
			super.prepareToBuildPacket();

			this.dataOutStream.writeInt(states.length);
			for(int state : states){
				this.dataOutStream.writeInt(state);
			}
			super.sendToClient(player);
		} catch(IOException e){ e.printStackTrace(); }
	}


	@Override
	protected void process(DataInputStream iStream, EntityPlayer player)
			throws IOException {
		final int length= iStream.readInt();


		if(length==REQUEST){//client is requesting, wont happen on clients
			this.instance.sendUpdateToClient(T1A.getGuiableStates(player), (EntityPlayer) player);
			return;
		}

		int[] states= new int[length];
		for(int i=0; i!=states.length; i++){
			states[i]= iStream.readInt();
		}
		
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
//			System.out.println("update server variables");
			T1A.setGuiableStates(player, states);
		}
		else{
			//update client GUI
			T1A.commproxy.setGuiStates(states);
		}
	}
}
