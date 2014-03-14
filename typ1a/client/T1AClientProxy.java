package com.typ1a.client;

import java.nio.FloatBuffer;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.BufferUtils;

import com.typ1a.client.gui.ProperGUI;
import com.typ1a.client.rendery.RenderBullet;
import com.typ1a.client.rendery.RenderDropPod;
import com.typ1a.client.rendery.RenderExplosionFX;
import com.typ1a.client.rendery.RenderGrenade;
import com.typ1a.client.rendery.RenderICBM;
import com.typ1a.client.rendery.RenderMMW;
import com.typ1a.client.rendery.RenderMissile;
import com.typ1a.client.rendery.RenderODP;
import com.typ1a.client.rendery.RenderRockBurner;
import com.typ1a.client.rendery.RenderSilo;
import com.typ1a.client.rendery.RenderSmoke;
import com.typ1a.client.rendery.guns.RenderAR;
import com.typ1a.client.rendery.guns.RenderM2;
import com.typ1a.client.rendery.guns.RenderRifle;
import com.typ1a.client.rendery.guns.RenderSMG;
import com.typ1a.client.rendery.guns.RenderShotgun;
import com.typ1a.client.rendery.guns.RenderTOW;
import com.typ1a.client.rendery.turret.RenderTurret;
import com.typ1a.client.rendery.vehicle.RenderMech;
import com.typ1a.client.rendery.vehicle.RenderVehicle;
import com.typ1a.client.rendery.vehicle.concrete.ModelB;
import com.typ1a.client.rendery.vehicle.concrete.ModelMjolnir;
import com.typ1a.client.rendery.vehicle.concrete.ModelRavener;
import com.typ1a.common.T1A;
import com.typ1a.common.T1ACommonProxy;
import com.typ1a.common.BlocksItems.EntityStoneBurner;
import com.typ1a.common.Explosion.EntityExplosionFX;
import com.typ1a.common.Explosion.EntitySmoke;
import com.typ1a.common.Missiles.EntityICBM;
import com.typ1a.common.Missiles.EntityMissile;
import com.typ1a.common.Missiles.EntitySilo;
import com.typ1a.common.Projectiles.EntityBullet;
import com.typ1a.common.Projectiles.EntityGrenade;
import com.typ1a.common.Robots.EntitySentry;
import com.typ1a.common.SmallArms.ItemAR;
import com.typ1a.common.SmallArms.ItemM2;
import com.typ1a.common.SmallArms.ItemRifle;
import com.typ1a.common.SmallArms.ItemSMG;
import com.typ1a.common.SmallArms.ItemShotgun;
import com.typ1a.common.SmallArms.ItemTOW;
import com.typ1a.common.Vehicles.EntityDropPod;
import com.typ1a.common.Vehicles.IControlled;
import com.typ1a.common.Vehicles.Tech.EntityB;
import com.typ1a.common.Vehicles.Tech.EntityMjolnir;
import com.typ1a.common.Vehicles.Tech.EntityRavener;
import com.typ1a.common.spacey.EntityODP;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;

public class T1AClientProxy extends T1ACommonProxy{
	final Minecraft mc= Minecraft.getMinecraft();

	public static final ParticleRenderer prl= new ParticleRenderer();

	public static T1ATickHandlerClient clntTH= new T1ATickHandlerClient();

	public static final FloatBuffer MATRBUF= BufferUtils.createFloatBuffer(4*4);

	/**this does not apply to screen shake as 
	 *recoil methods which are in item classes 
	 *are called by projectiles when they spawn*/
	public static Map<Entity, Float> recoilTracker= new Hashtable<Entity, Float>();
	/**Must be here because can't add to EntityPlayer
	 * @param constn If negative is subtractive from start 1<br>
	 * if positive is exponential from start 1*/
	public static Float getRecoilForEntity(Entity e, float constn){
		Float ret= recoilTracker.get(e);
		if(ret!=null)
			T1AClientProxy.recoilTracker.put(e
					, constn>=0? 
							ret* (float)(Math.pow(constn, 1/T1ATickHandlerClient.pT)) 
							: // a = Σ(a/n, n) = Π(a^(1/n), n)
								ret>0? ret+constn/T1ATickHandlerClient.pT : 0);
		else
			ret=0f;
		return ret;
	}

	@Override
	public void initClient()
	{


		TickRegistry.registerTickHandler(clntTH, Side.CLIENT);
		KeyBindingRegistry.registerKeyBinding(new T1AKeyHandler());

		//		RenderingRegistry.registerBlockHandler(1253,new BlockRenderHandler());

		//TechVecicles
		RenderingRegistry.registerEntityRenderingHandler(EntityRavener.class, new RenderMech(new ModelRavener()));
		RenderingRegistry.registerEntityRenderingHandler(EntityMjolnir.class, new RenderVehicle(new ModelMjolnir()));
		RenderingRegistry.registerEntityRenderingHandler(EntityB.class, new RenderVehicle(new ModelB()));

		//Blockish Things
		RenderingRegistry.registerEntityRenderingHandler(EntityODP.class, new RenderODP());
		RenderingRegistry.registerEntityRenderingHandler(EntityStoneBurner.class, new RenderRockBurner());
		RenderingRegistry.registerEntityRenderingHandler(EntityICBM.class, new RenderICBM());
		RenderingRegistry.registerEntityRenderingHandler(EntitySilo.class, new RenderSilo());

		//Misc Entities and nonTechVehicles
		RenderingRegistry.registerEntityRenderingHandler(EntityDropPod.class, new RenderDropPod());

		//Dakka
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new RenderBullet());
		RenderingRegistry.registerEntityRenderingHandler(EntityMissile.class, new RenderMissile(RenderMissile.kinetic));
		RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, new RenderGrenade());
		RenderingRegistry.registerEntityRenderingHandler(EntitySmoke.class, new RenderSmoke());
		RenderingRegistry.registerEntityRenderingHandler(EntitySentry.class, new RenderTurret(RenderTurret.type.CANNON));

		//PewPew
		RenderingRegistry.registerEntityRenderingHandler(EntityExplosionFX.class, new RenderExplosionFX());

		//Items
		MinecraftForgeClient.registerItemRenderer(ItemSMG.instance, new RenderSMG());
		MinecraftForgeClient.registerItemRenderer(ItemRifle.instance, new RenderRifle());
		MinecraftForgeClient.registerItemRenderer(ItemShotgun.instance, new RenderShotgun());
		MinecraftForgeClient.registerItemRenderer(ItemTOW.instance, new RenderTOW());
		MinecraftForgeClient.registerItemRenderer(ItemM2.instance, new RenderM2());
		MinecraftForgeClient.registerItemRenderer(ItemAR.instance, new RenderAR());


	}
	@Override
	public void loadSounds(){
		try{
			MinecraftForge.EVENT_BUS.register(new T1AClientProxy.Sounds());}
		catch(Exception e){	e.printStackTrace(); }
	}
	public static class Sounds{
		@SubscribeEvent
		public void onSound(SoundLoadEvent event){
			event.manager.addSound("t1a:shotgun.wav");
			event.manager.addSound("t1a:sniper.wav");
			event.manager.addSound("t1a:smg.wav");
			event.manager.addSound("t1a:m2.wav");
			event.manager.addSound("t1a:tavor.wav");
			event.manager.addSound("t1a:suppr1.wav");
			event.manager.addSound("t1a:suppr2.wav");
			event.manager.addSound("t1a:suppr3.wav");

			event.manager.addSound("t1a:bloop1.wav");
			event.manager.addSound("t1a:bloop2.wav");

			event.manager.addSound("t1a:missile.wav");
			event.manager.addSound("t1a:smoke.wav");
			event.manager.addSound("t1a:firecracker.wav");

			event.manager.addSound("t1a:empty.wav");
			event.manager.addSound("t1a:reloadMagM15.wav");
			event.manager.addSound("t1a:reloadMagAK.wav");
			event.manager.addSound("t1a:reloadShotgun1.wav");
			event.manager.addSound("t1a:reloadShotgun2.wav");
			event.manager.addSound("t1a:reloadShotgun3.wav");
			event.manager.addSound("t1a:reloadMissile.wav");
		}
	}
	@Override
	public void playSound(String snd, double x, double y, double z, float vol, float pitch){
		this.mc.sounsndManager.playSound(snd, (float)x, (float)y, (float)z, vol, pitch);
		//.theWorld.playSound(x,y,z, snd, vol, pitch, false);
	}
	@Override
	public void doRecoil(EntityPlayer shooter, Random r){
		if(shooter==null)
			return;

		T1AClientProxy.recoilTracker.put(shooter, new Float(1.00));
		super.doRecoil(shooter, r);
	}

	@Override
	public void setKeysLocalPlayer(int keys){
		Entity riding = mc.thePlayer.ridingEntity;
		if(riding!=null && riding instanceof IControlled)
			((IControlled)riding).handleKeys(mc.thePlayer, keys);

		T1A.keysPressedByPlayer.put(mc.thePlayer, keys);
	}


	@Override
	public EnumAction getSmallarmUseAction(ItemStack s){
		if(mc.thePlayer!=null && mc.thePlayer.getHeldItem()!=null
				&& mc.thePlayer.getHeldItem().itemID==s.itemID //is yours
				&& mc.gameSettings.thirdPersonView==0)
			return EnumAction.none;
		else
			return EnumAction.bow;
	}

	@Override
	public void setGuiStates(int[] states){
		GuiScreen s= mc.currentScreen;
		if(s==null || !(s instanceof ProperGUI ))
			return;
		final ProperGUI g= (ProperGUI) s;
		g.recieveUpdate(states);
	}

	@Override
	public void onRenderTick(){

	}

	@Override
	public EntityPlayer getLocalPlayerIfClient(){
		return mc.thePlayer;
	}

	@Override
	public Entity getEntity(int worldid, int entid) {
		return Minecraft.getMinecraft().theWorld.getEntityByID(entid);
	}
}
