package com.typ1a.common.Network;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import t1a.common.Vehicles.IControlled;

public class PacketVehicleLocus extends T1APacket {

	public static PacketVehicleLocus instance;
	
	protected PacketVehicleLocus() {
		super();
		instance=this;
	}

	public void send(Entity e){

		try {
			prepareToBuildPacket();

			dataOutStream.writeInt(e.entityId);
			//4 bytes

			//use floats because doubles are hueg
			dataOutStream.writeFloat((float) e.posX);
			dataOutStream.writeFloat((float) e.posY);
			dataOutStream.writeFloat((float) e.posZ);

			//outputStream.writeFloat((float) e.motionX);
			//outputStream.writeFloat((float) e.motionY);
			//outputStream.writeFloat((float) e.motionZ);
			//12 bytes

			//yaw and pitch are automatically watched by vanilla			
		} catch(IOException ex){ex.printStackTrace();}


		sendToClientsNear(e, 400);
	}

	@Override
	public void process(DataInputStream iStream, EntityPlayer player) throws IOException{
		int id=0;
		double x,y,z;//dx,dy,dz;

		id= iStream.readInt();

		x= iStream.readFloat();
		y= iStream.readFloat();
		z= iStream.readFloat();

		//dx= iStream.readFloat();
		//dy= iStream.readFloat();
		//dz= iStream.readFloat();

		final Entity e= player.worldObj.getEntityByID(id);
		if(e==null){
			System.out.println("Error: Got null pointing entityId from PacketVehicleLocus.");
			return;
		}
		assert(e instanceof IControlled);//I dont see why this wouldnt be true

		//Only do locus correction if there is substantial error
		//this prevents unncessary jittering
		if(Math.abs(e.posX-x)>2
				|| Math.abs(e.posY-y)>7
				|| Math.abs(e.posZ-z)>2)
			((IControlled)e).setPos(x, e.posY, z);

		//motion correction seems unnecessary
	}

}
