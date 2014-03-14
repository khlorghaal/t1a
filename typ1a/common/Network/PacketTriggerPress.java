package com.typ1a.common.Network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import t1a.common.T1A;
import t1a.common.T1ACommonProxy;
import t1a.common.BlocksItems.ItemUseable;
import t1a.common.SmallArms.ItemSmallArm;
import t1a.common.SmallArms.SmallarmLoading;
import t1a.common.Vehicles.EntityTechVehicle;

/**Standard protocol <br>
 * Player pushes button , sends to server, processes, 
 * bounces to all clients including sender, and does client effects<br><br>
 * 
 * boolean trigger<br>
 * <b>To client comp</b><br>
 * byte dimid<br>
 * int playerentityid<br>
 * NBTTC data<br>*/
public class PacketTriggerPress extends T1APacket{

	public static PacketTriggerPress instance;

	protected PacketTriggerPress() {
		super();
		instance=this;
	}

	@SideOnly(Side.CLIENT)
	public void send(boolean trigger, EntityPlayer player){
		try {
			prepareToBuildPacket();
			dataOutStream.writeBoolean(trigger);} catch(Exception ex){ex.printStackTrace();}

//		T1ACommonProxy.handleRightClick(player, trigger, T1ACommonProxy.getShootingData(player));
		sendToServer();
	}

	/**if server, bounces to nearby clients*/
	public void process(DataInputStream iStream, EntityPlayer player) throws IOException{
		final boolean trigger= iStream.readBoolean();
		final NBTTagCompound dat;
		
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER){
			dat= SmallarmLoading.getShootingData(player);
			bounce(trigger, player, dat);			
		}
		else{//client
			player= (EntityPlayer)(T1A.getEntity(iStream.readInt(), iStream.readInt()));
			dat= (NBTTagCompound) NBTBase.readNamedTag(iStream);
		}
		
		T1ACommonProxy.handleRightClick(player, trigger, dat);
	}

	/**resend to clients, including the one who sent*/
	void bounce(boolean trigger, EntityPlayer sender, NBTTagCompound dat){
		try {
			prepareToBuildPacket();
			dataOutStream.writeBoolean(trigger);

			dataOutStream.writeInt(sender.worldObj.provider.dimensionId);
			dataOutStream.writeInt(sender.entityId);

			NBTBase.writeNamedTag( SmallarmLoading.getShootingData(sender), dataOutStream);
			
		} catch(Exception ex){ex.printStackTrace();}
		sendToClientsNear(sender, 500);
	}

}
