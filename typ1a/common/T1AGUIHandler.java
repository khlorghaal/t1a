package com.typ1a.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

import com.typ1a.client.gui.GUIAssembler;
import com.typ1a.client.gui.GUICommPack;
import com.typ1a.client.gui.GUIEquipment;
import com.typ1a.client.gui.GUILoader;
import com.typ1a.client.gui.GUISilo;
import com.typ1a.client.gui.GUITurret;
import com.typ1a.common.AmmoStuff.ItemMagazine;
import com.typ1a.common.Containers.ContainerAssembler;
import com.typ1a.common.Containers.ContainerEquipment;
import com.typ1a.common.Containers.ContainerLoader;
import com.typ1a.common.Containers.ContainerSilo;
import com.typ1a.common.Equipment.EquipmentFacade;
import com.typ1a.common.Equipment.IEquipmentAcceptor;
import com.typ1a.common.Missiles.EntitySilo;
import com.typ1a.common.Robots.EntitySentry;
import com.typ1a.common.crafting.BlockAssemblyRig.TileEntityAssemblyRig;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;

public class T1AGUIHandler implements IGuiHandler {

	/**guiIds*/
	public static final int 
	SILO= 0,
	SENTRY= 1,
	empty= 2,
	COMMPACK= 4,
	EQUIPMENT=5,
	MAG=6,
	RIG=7;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		Entity ent= world.getEntityByID(x);
		IInventory inv=null;

		if(ent!=null && ent instanceof IInventory)
				inv= (IInventory) ent;

		System.out.println(FMLCommonHandler.instance().getEffectiveSide());
		switch(id){
		case MAG:
			if(player.getHeldItem()==null 
			|| !(player.getHeldItem().getItem() instanceof ItemMagazine))
				return null;
			final ContainerLoader ret= new ContainerLoader(player.inventory);
			T1A.setGuiableAccessedBy(player, ret.guiable);
			return ret;
		case COMMPACK:
			return null;
		case SILO:
			return new ContainerSilo(player.inventory, inv);
		case SENTRY:
			return null;
		case EQUIPMENT:
			return new ContainerEquipment(player.inventory, ((IEquipmentAcceptor)ent).getEquipmentFacade());
		case RIG:
			return new ContainerAssembler(player.inventory, (TileEntityAssemblyRig)(world.getTileEntity(x, y, z)));
		}
		return null;
	}


	/////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////


	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,	int x, int y, int z) {

		Entity ent= world.getEntityByID(x);
		IInventory inv=null;
		EquipmentFacade eqf=null;
		
		if(x!=0 && ent!=null){
			if(ent instanceof IEquipmentAcceptor)
				eqf= ((IEquipmentAcceptor)ent).getEquipmentFacade();
			else
				inv= (IInventory) ent;
		}
		
		switch(id){
		case MAG:
			return new GUILoader(player.inventory);
		case COMMPACK:
			return new GUICommPack();
		case SILO:
			return new GUISilo(player.inventory, (EntitySilo)world.getEntityByID(x));
		case SENTRY:
			return new GUITurret(player.inventory, (EntitySentry)world.getEntityByID(x));
			//	case Ravener:
			//			return new GUITechVehicle(player.inventory, (EntityTechVehicle)world.getEntityByID(x));
			//		case Brutalizer:
			//	//		return new ContainerBrutalizer(player.inventory, (EntityBrutalizer)world.getEntityByID(x));		}
			//		
		case RIG:
			return new GUIAssembler(player.inventory, (TileEntityAssemblyRig)(world.getTileEntity(x, y, z)));
		case EQUIPMENT:
			return new GUIEquipment(player.inventory, eqf);
		}
		return null;
	}

}