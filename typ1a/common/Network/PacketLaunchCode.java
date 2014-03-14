package com.typ1a.common.Network;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.typ1a.common.Missiles.EntityICBM;
import com.typ1a.common.Missiles.EntitySilo;

public class PacketLaunchCode extends T1APacket{

	public static PacketLaunchCode instance;

	protected PacketLaunchCode() {
		super();
		instance=this;
	}

	public void send(EntitySilo silo, int tx, int tz){

		try {
			prepareToBuildPacket();

			dataOutStream.writeInt(tx);
			dataOutStream.writeInt(tz);
			dataOutStream.writeInt(silo.getEntityId());
			//dont need worldid because you get that from player
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		sendToServer();
	}

	public void process(DataInputStream iStream, EntityPlayer player) throws IOException{
		int tx,tz;
		int dimen;
		String user;
		int siloID;
		World world;
		EntitySilo silo;

		tx = iStream.readInt();
		tz = iStream.readInt();
		siloID = iStream.readInt();
		//gets world and silo from their hashes
		world = player.worldObj;
		silo = (EntitySilo)world.getEntityByID(siloID);
		//TODO check if player can access that silo
		//launch missile
		if(true && !world.isRemote)
			world.spawnEntityInWorld(new EntityICBM(world, silo.posX, silo.posY+2, silo.posZ, (double)tx, (double)tz));
	}
}
