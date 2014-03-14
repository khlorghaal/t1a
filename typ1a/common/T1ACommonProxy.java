package com.typ1a.common;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

import com.typ1a.common.BlocksItems.ItemUseable;
import com.typ1a.common.Network.PacketVehicleLocus;
import com.typ1a.common.SmallArms.ItemGunPart;
import com.typ1a.common.SmallArms.ItemSmallArm;
import com.typ1a.common.SmallArms.TickerRack;
import com.typ1a.common.SmallArms.TickerReload;
import com.typ1a.common.SmallArms.TickerStrip;
import com.typ1a.common.Vehicles.EntityTechVehicle;
import com.typ1a.common.Vehicles.EntityVehicle;
import com.typ1a.common.Vehicles.IControlled;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class T1ACommonProxy
{	
	public static Random seedgen= new Random();
	public void initClient(){}
	public void loadSounds(){}

	public void doRecoil(EntityPlayer shooter, Random r){
		if(shooter==null)
			return;
			if(shooter.getHeldItem()!=null
					&& shooter.getHeldItem().getItem() instanceof ItemSmallArm){
				final ItemSmallArm heldGun= ((ItemSmallArm)shooter.getHeldItem().getItem());
				float rdamp=1;
				if(ItemGunPart.isMasterwork(shooter.getHeldItem().stackTagCompound.getCompoundTag("s")));
					rdamp*=ItemSmallArm.MSTOCKDEV;
				if(shooter.getHeldItem().stackTagCompound.hasKey("Muzzle Brake"))
					rdamp*=ItemSmallArm.BRAKERECOIL;
				shooter.rotationYaw+= heldGun.getRecoilYaw(r)*rdamp;
				shooter.rotationPitch+= heldGun.getRecoilPitch(r)*rdamp;
			}
	}

	public void onRenderTick(){}	
	public void setGuiStates(int[] states){}

	public EnumAction getSmallarmUseAction(ItemStack s) {
		return EnumAction.bow;
	}

	public static void handleRightClick(EntityPlayer player, boolean trigger, NBTTagCompound data){
		if(player.ridingEntity!=null && player.ridingEntity instanceof EntityVehicle){
			//dont go firing guns inside your own airplane
			//ghetto gunships dont set riders
			
			if(player.ridingEntity instanceof EntityTechVehicle){
				((EntityTechVehicle)player.ridingEntity).setFiring(0, trigger);
			}
			return;
		}

		if(player.getHeldItem()!=null){
			final Item item = player.getHeldItem().getItem();

			if(item !=null && item instanceof ItemUseable){
				if(item instanceof ItemSmallArm){
					TickerReload.interrupt(player);

					if(FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT){
						//animate TODO TODO
						if(!trigger)
							ItemSmallArm.playersUsing.put(player, 0);
						else
							ItemSmallArm.playersUsing.put(player, 0x80000000);
					}
				}
				if(trigger){
					((ItemUseable)item).use(player, data);
				}
				else
					((ItemUseable)item).stop(player);
			}
		}
	}

	public void setKeys(EntityPlayer player, int keys) {
		T1A.keysPressedByPlayer.put(player, keys);
		
		final Entity ent= player.ridingEntity;
		if(ent!=null && ent instanceof IControlled){
			final IControlled vehicle = (IControlled) ent;
			vehicle.handleKeys(player, keys);
			PacketVehicleLocus.instance.send(ent);
		}
		else if(T1A.isKeyPressed(keys, T1A.RELOAD)){
			if(T1A.isKeyPressed(keys, T1A.SHIFT))
				if(T1A.isKeyPressed(keys, T1A.CTRL))
					new TickerStrip(player);
				else
					new TickerRack(player);
			else
				new TickerReload(player);				
		}
	}
	public void setKeysLocalPlayer(int keys) {}


	public EntityPlayer getLocalPlayerIfClient(){
		return null;
	}
	/**dont broadcast to clients, use world.playsound for those cases*/
	public void playSound(String snd, double x, double y, double z, float vol, float pitch){}
	
	public Entity getEntity(int worldid, int entid){
		return DimensionManager.getWorld(worldid).getEntityByID(entid);
	}
}