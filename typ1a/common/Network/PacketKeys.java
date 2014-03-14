package com.typ1a.common.Network;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import t1a.common.T1A;
import t1a.common.T1ACommonProxy;
import t1a.common.SmallArms.TickerRack;
import t1a.common.SmallArms.TickerReload;
import t1a.common.SmallArms.TickerStrip;
import t1a.common.Vehicles.IControlled;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

/**Standard protocol
 * Player pushes button does local effect, 
 * sends to server and does server effects, 
 * bounces to other clients and does their effects*/
public class PacketKeys extends T1APacket{

	public static PacketKeys instance;

	protected PacketKeys() {
		super();
		instance=this;
	}

	/**@param dat Bits correspond to KeyHandler Binding[i-1]; 0 = none pressed*/
	public void send(int dat){
		try {
			prepareToBuildPacket();
			dataOutStream.writeInt(dat);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		sendToServer();
		//move on the sender client
		T1A.commproxy.setKeysLocalPlayer(dat);
	}

	public void process(DataInputStream iStream, EntityPlayer player) throws IOException{
		int keys=-1;
		keys=iStream.readInt();

		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER){
			//server handle
			T1A.commproxy.setKeys(player, keys);
			bounce(keys, player);
		}
		else{//client side bounce handle
			T1A.getEntity(iStream.readInt(), iStream.readInt());

			//TODO !?!?!?
		}
	}

	private void bounce(int data, EntityPlayer sender){
		try {
			prepareToBuildPacket();
			dataOutStream.writeInt(data);

			dataOutStream.writeInt(sender.worldObj.provider.dimensionId);
			dataOutStream.writeInt(sender.entityId);
		} catch(Exception ex){ex.printStackTrace();}
		sendToClientsNear(sender, 500);
	}
}
