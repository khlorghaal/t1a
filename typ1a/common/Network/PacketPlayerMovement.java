package com.typ1a.common.Network;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

/**The mere fact that this needs to exist pisses me off*/
public class PacketPlayerMovement extends T1APacket {

	public static PacketPlayerMovement instance;
	
	protected PacketPlayerMovement() {
		super();
		instance=this;
	}

	public void send(EntityPlayer player){
		try {
			prepareToBuildPacket();
			dataOutStream.writeFloat((float) player.motionX);
			dataOutStream.writeFloat((float) player.motionY);
			dataOutStream.writeFloat((float) player.motionZ);
		} catch (IOException e) {e.printStackTrace();}
		
		sendToServer();
	}
	
	@Override
	protected void process(DataInputStream iStream, EntityPlayer player)
			throws IOException {
		
		new PlayerMotion(player, iStream.readFloat(), iStream.readFloat(), iStream.readFloat());
	}

	/**So the server can track client velocity.
	 * as it fucking should already... 
	 * Manually setting velocity doesnt work*/
	public static class PlayerMotion{
		private static Map<EntityPlayer, PlayerMotion> map= new Hashtable<EntityPlayer, PlayerMotion>();
		
		public double vx,vy,vz;
		private PlayerMotion(EntityPlayer player, double vx, double vy, double vz){
			this.vx= vx;
			this.vy= vy;
			this.vz= vz;
			
			map.put(player, this);
		}
		
		public static PlayerMotion get(EntityPlayer player){
			PlayerMotion r= map.get(player);
			if(r==null)
				new PlayerMotion(player, 0, 0, 0);
			return map.get(player);
		}
	}
}
