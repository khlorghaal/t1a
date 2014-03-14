package com.typ1a.common.Network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import t1a.common.EphemeralEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/**Subclasses must be singletons.
 * Subclasses should statically new themself. This may cause issues?*/
public abstract class T1APacket {
	
	//instantiate all packet singletons; must be done here for ID certainty
	public static void initAll(){
		new PacketBombard();
		new PacketGuiUpdate();
		new PacketKeys();
		new PacketKeys();
		new PacketLaunchCode();
		new PacketOpenEquipment();
		new PacketPlayerMovement();
		new PacketVehicleLocus();
		new PacketTriggerPress();
	}
	
	private static final Map<Byte, T1APacket> channelMap = new Hashtable<Byte, T1APacket>();
	private static Byte currentID=0;
	private final Byte ID;

	//outstreams must be renewed each time a packet is sent
	private ByteArrayOutputStream baos;
	protected DataOutputStream dataOutStream;

	private Packet250CustomPayload packet;



	/** @param size for the subclass to assign when constructing itself 
	 * corresponding to the total data bytes in the packet*/
	protected T1APacket(){
		this.ID= currentID;
		channelMap.put(currentID++, this);
		System.out.println(ID);
		
		baos= new ByteArrayOutputStream();//to avoid null checks
		dataOutStream= new DataOutputStream(baos);
	}

	/**renew the streams and write id*/
	protected final void prepareToBuildPacket() throws IOException{
		baos.close();
		dataOutStream.close();
		baos= new ByteArrayOutputStream();
		dataOutStream= new DataOutputStream(baos);
		
		dataOutStream.writeByte(ID);
	}

	private final void prepareToSend(){
		packet= new Packet250CustomPayload();
		packet.channel = "1A";
		packet.data = baos.toByteArray();
		packet.length = baos.size();
	}	
	protected final void sendToServer(){
		prepareToSend();
		PacketDispatcher.sendPacketToServer(packet);
	}
	/**@param emitter assumes that most server-client packets will be regarding an Entity; 
	 * if this is not the case you can new a fake Entity with the needed worldObj and pos*/
	protected final void sendToClientsNear(Entity emitter, int range){
		prepareToSend();
		PacketDispatcher.sendPacketToAllAround(emitter.posX, emitter.posY, emitter.posZ,
				range, emitter.worldObj.provider.dimensionId, packet);
	}
	protected final void sendToClientsNearExcept(EntityPlayer emitter, int range){
		sendToClientsNearExcept(emitter, range, new double[]{emitter.posX,emitter.posY,emitter.posZ}, emitter.worldObj.provider.dimensionId);
	}
	protected final void sendToClientsNear(TileEntity emitter, int range){
		prepareToSend();
		PacketDispatcher.sendPacketToAllAround(emitter.xCoord, emitter.yCoord, emitter.zCoord,
				range, emitter.worldObj.provider.dimensionId, packet);
	}
	protected final void sendToClientsNearExcept(EntityPlayer except, int range, double[] p, int worldid){
		prepareToSend();
		FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager()
		.sendToAllNearExcept(except, p[0], p[1], p[2], range, worldid, packet);
	}
	protected final void sendToClient(EntityPlayer client){
		prepareToSend();
		PacketDispatcher.sendPacketToPlayer(packet, (Player) client);
	}
	protected final void sendToAllClients(){
		prepareToSend();
		PacketDispatcher.sendPacketToAllPlayers(packet);
	}


	public static void handle(DataInputStream iStream, EntityPlayer player){
		try {
			channelMap.get( iStream.readByte() ).process(iStream, player);
		} catch (IOException e) {System.err.println("Something went wrong while handling or processing a T1A packet from "+player.username);
		e.printStackTrace();}
	}
	protected abstract void process(DataInputStream iStream, EntityPlayer player) throws IOException;
}
