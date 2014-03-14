package com.typ1a.common;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.util.vector.Matrix4f;

import com.typ1a.client.T1AClientProxy;
import com.typ1a.common.AmmoStuff.ItemAmmo;
import com.typ1a.common.AmmoStuff.ItemCasing;
import com.typ1a.common.AmmoStuff.ItemMagazine;
import com.typ1a.common.AmmoStuff.ItemMagazine5mm;
import com.typ1a.common.BlocksItems.BlockLightSource;
import com.typ1a.common.BlocksItems.BlockLunanite;
import com.typ1a.common.BlocksItems.BlockStoneBurner;
import com.typ1a.common.BlocksItems.EntityStoneBurner;
import com.typ1a.common.BlocksItems.ItemCommPack;
import com.typ1a.common.BlocksItems.ItemDebugginator;
import com.typ1a.common.BlocksItems.ItemDropPod;
import com.typ1a.common.BlocksItems.ItemKey;
import com.typ1a.common.BlocksItems.ItemPostalCannon;
import com.typ1a.common.BlocksItems.ItemSuspensor;
import com.typ1a.common.Equipment.ItemArmor;
import com.typ1a.common.Equipment.ItemShield;
import com.typ1a.common.Explosion.EntityExplosionFX;
import com.typ1a.common.Explosion.EntitySmoke;
import com.typ1a.common.Explosion.EntitySmokeDisco;
import com.typ1a.common.Explosion.EntitySmokeFlare;
import com.typ1a.common.Missiles.EntityICBM;
import com.typ1a.common.Missiles.EntityMissile;
import com.typ1a.common.Missiles.EntitySilo;
import com.typ1a.common.Missiles.EntityWGM;
import com.typ1a.common.Missiles.ItemSilo;
import com.typ1a.common.Network.T1APacketHandler;
import com.typ1a.common.Projectiles.EntityBullet;
import com.typ1a.common.Projectiles.EntityGrenade;
import com.typ1a.common.Robots.EntityMMW;
import com.typ1a.common.Robots.EntitySentry;
import com.typ1a.common.Robots.ItemSentry;
import com.typ1a.common.SmallArms.ItemAR;
import com.typ1a.common.SmallArms.ItemAttachment;
import com.typ1a.common.SmallArms.ItemBlamballberblerber;
import com.typ1a.common.SmallArms.ItemGrenade;
import com.typ1a.common.SmallArms.ItemGunPart;
import com.typ1a.common.SmallArms.ItemM2;
import com.typ1a.common.SmallArms.ItemMiningLaser;
import com.typ1a.common.SmallArms.ItemRifle;
import com.typ1a.common.SmallArms.ItemSMG;
import com.typ1a.common.SmallArms.ItemShotgun;
import com.typ1a.common.SmallArms.ItemSmallArm;
import com.typ1a.common.SmallArms.ItemTOW;
import com.typ1a.common.Vehicles.EntityDropPod;
import com.typ1a.common.Vehicles.ItemUnassembledVehicle;
import com.typ1a.common.Vehicles.Tech.EntityB;
import com.typ1a.common.Vehicles.Tech.EntityMjolnir;
import com.typ1a.common.Vehicles.Tech.EntityRavener;
import com.typ1a.common.crafting.BlockAssemblyRig;
import com.typ1a.common.crafting.BlockAssemblyRigging;
import com.typ1a.common.spacey.EntityODP;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@Mod( modid = "t1a", name="Type 1 Anarchy", version="0.20")
public class T1A{
	static final String MODID="T1A";
	static final String VERSION="1.7.2-0.31";
	@SidedProxy(clientSide = "t1a.client.T1AClientProxy", serverSide = "t1a.common.T1ACommonProxy")
	public static T1ACommonProxy commproxy;
	public static T1AClientProxy clntproxy;
	public static T1ATickHandlerCommon tickHandlerComm = new T1ATickHandlerCommon();

	@Instance("t1a")
	public static T1A instance;

	public static final Map<Entity, Integer> keysPressedByPlayer= new Hashtable<Entity, Integer>();

	public static final Random random= new Random();

	public static final Matrix4f identity= new Matrix4f();
	public static final FloatBuffer matbuf= ByteBuffer.allocate(4*4*Float.SIZE).asFloatBuffer(); 
			
	public static final int 
	W=1,
	A=2,
	S=4,
	D=8,
	JUMP=16,
	SHIFT=32,
	RELOAD=64,
	BAIL=128,
	COMMPACK=256,
	CTRL=512;
	public static final int[] nums= new int[]{
		W,A,S,D,JUMP,SHIFT,RELOAD,BAIL,COMMPACK,CTRL
	};
	/**@param keyIndex the bit index of the keydata corresponding to the key
	 * @param keyData the bits containing key presses*/
	public static boolean isKeyPressed(int keyData, int keyBit){
		if((keyData&keyBit)==keyBit)
			return true;
		return false;
	}
	
	public static final double SPACE = 120.0D; //y>= for objects to be considered in orbit

	public static final double[] wind= new double[]
			{EntitySmoke.r.nextGaussian(),EntitySmoke.r.nextGaussian(),EntitySmoke.r.nextGaussian()};
	
	public static CreativeTabs ctab = new CreativeTabs(MODID) {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return ItemShield.instance;
		}
	};

	//ALWAYS APPEND
	public static int id = 500;
	//TODO a config for initial id

	public static final Vector<Block> blocks = new Vector<Block>();
	public static final Vector<Item> items = new Vector<Item>();

	//_____________________________________________________________________
	@EventHandler
	public void preLoad(FMLPreInitializationEvent evt){

//		new BlockODP();
		new BlockLunanite();
		new BlockStoneBurner();
		new BlockLightSource();
//		new BlockFallout();
//		new BlockRebar();
//		new BlockConcrete();
//		new BlockLiquidConcrete();
//		new BlockREConcrete();
//		new BlockLiquidREConcrete();
		
		new ItemAR();
		new ItemSMG();
		new ItemMagazine5mm();
		new ItemAmmo.ItemRound5mm();
		
		new ItemShotgun();
		new ItemAmmo.ItemRoundShot();
		
		new ItemRifle();
		new ItemAmmo.ItemRoundBMG();
		
		new ItemAmmo.ItemRoundArty();

		new ItemTOW();
		new ItemAmmo.ItemMissile();
		
		new ItemBlamballberblerber();

		new ItemGrenade.ItemGrenadeConcussion();
		new ItemGrenade.ItemGrenadeFragmentation();
		new ItemGrenade.ItemGrenadeSmokeS();
		new ItemGrenade.ItemGrenadeSmokeL();
		new ItemGrenade.ItemGrenadeFlare();
		new ItemGrenade.ItemGrenadeDisco();
		
		new ItemArmor.ItemArmorKevlar();
		new ItemArmor.ItemArmorCeramic();
		new ItemArmor.ItemArmorSteel();
		new ItemShield("Shield","equipment/shield");
		new ItemShield.ItemShieldHeavy();
		new ItemShield.ItemShieldFast();

		new ItemSilo();
		new ItemDropPod();
		new ItemM2();
		new ItemMiningLaser();
		new ItemPostalCannon();
		new ItemGunPart();
		new ItemAttachment();
		
		new ItemSuspensor();
		
		new ItemCommPack();

//		new ItemLiquidLunanite();
		new ItemDebugginator();
		new ItemSentry();

		new ItemKey();
		new ItemUnassembledVehicle();
		new BlockAssemblyRig();
		new BlockAssemblyRigging();
		new ItemCasing();
		
		commproxy.loadSounds();
	}


	@EventHandler
	public void load(FMLInitializationEvent evt){
		new T1APacketHandler();
		commproxy.initClient();
		MinecraftForge.EVENT_BUS.register(tickHandlerComm);

		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new T1AGUIHandler());
		GameRegistry.registerWorldGenerator(new T1AWorldGenerator(),0);

		LanguageRegistry.instance().addStringLocalization("itemGroup.T1A", "en_US", "T1A");
		ItemGunPart.registerLanguages();
		
		//Block/Item registry
		for(Item item : items){
			LanguageRegistry.addName(item, item.getUnlocalizedName().substring(5));
		}
		for(Block block : blocks){
			GameRegistry.registerBlock(block, block.getUnlocalizedName());
			LanguageRegistry.addName(block, block.getUnlocalizedName().replaceAll("tile.", ""));
		}
		
		registerCrafting();

		//Entity Registrar

		//Spacey stuff
		registerEntity(EntityODP.class, "ODP");
		Satellite.load("Sats.obj");
		registerEntity(EntityDropPod.class, "Drop Pod");

		registerEntity(EntitySilo.class, "Silo");

		//Missiles
		registerEntity(EntityICBM.class, "ICBM");
		registerEntity(EntityMissile.class, "M");
		registerEntity(EntityWGM.class, "TOW");
		//Turrets
		registerEntity(EntitySentry.class, "Turret");
		//Mechs
		registerEntity(EntityRavener.class, "Ravener");
		registerEntity(EntityMjolnir.class, "Mjolnir");
		registerEntity(EntityB.class, "B");
		//Projectiles
		registerEntity(EntityBullet.class, "Bullet");
		registerEntity(EntityGrenade.class, "Grenade");
		
		registerEntity(EntityExplosionFX.class, "NukeFX");
		registerEntity(EntitySmoke.class, "SmokeCloud");
		registerEntity(EntitySmokeFlare.class, "FlareCloud");
		registerEntity(EntitySmokeDisco.class, "DiscoCloud");

		//registerEntity(EntityExplosion.class, "Asplosn");

		registerEntity(EntityStoneBurner.class, "StoneBurner");
		registerEntity(EntityMMW.class, "MMW");

		GameRegistry.registerTileEntity(BlockAssemblyRig.TileEntityAssemblyRig.class, "AssemblyRigTE");

	}
	private static void registerCrafting(){
		ItemGunPart.registerCrafting();
		ItemAttachment.registerCrafting();
		ItemSmallArm.registerCrafting();
		ItemGrenade.registerCrafting();
		ItemUnassembledVehicle.registerCrafting();
		ItemMagazine.registerCrafting();
		ItemAmmo.registerCrafting();
	}
	//////////////////////////////////////
	//Global Static Funcs
	static int entityID=0;
	public static void registerEntity(Class<? extends Entity> clas, String name, int freq){
		EntityRegistry.registerGlobalEntityID(clas, name, EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(clas, name, entityID++, T1A.instance, 128, 1, true);
	}
	public static void registerEntity(Class<? extends Entity> clas, String name){
		registerEntity(clas, name, 1);
	}

	public static boolean isInside(Entity projectile, Entity target){
		AxisAlignedBB aabb = target.getBoundingBox();
		if(aabb==null) return false;
		aabb = aabb.offset(-target.posX, -target.posY, -target.posZ);

		Vec3 vec = Vec3.createVectorHelper(projectile.motionX, projectile.motionY, projectile.motionZ);

		if(aabb.isVecInside(vec))
			return true;
		return false;

	}
	public static Entity getEntity(int worldid, int entid){
		return commproxy.getEntity(worldid, entid);
	}
	
	
	
	/**guiables are to be set when opened*/
	private static final Map<EntityPlayer, IGuiable> guiableAccessedByPlayer= new Hashtable<EntityPlayer, IGuiable>();
	private static final Map<EntityPlayer, IGuiable> guiableAccessedByPlayerClient= new Hashtable<EntityPlayer, IGuiable>();
	
	/**Prefer to be called by T1AGUIHanlder*/
	public static void setGuiableAccessedBy(EntityPlayer player, IGuiable igui){
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
			guiableAccessedByPlayer.put(player, igui);
		else
			guiableAccessedByPlayerClient.put(player, igui);
	}
	/**Prefer to only use in gui packet handling. 
	 * GUIable implementors have access to their own states.*/
	public static void setGuiableStates(EntityPlayer player, int[] states){
		final IGuiable ig;
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
			ig= guiableAccessedByPlayer.get(player);
		else
			ig= guiableAccessedByPlayerClient.get(player);
		if(ig==null || !ig.canPlayerAccess(player))
			return;
		ig.setStates(states);
	}
	/**Prefer to only use in gui packet handling*/
	public static int[] getGuiableStates(EntityPlayer player){
		final IGuiable ig;
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
			ig= guiableAccessedByPlayer.get(player);
		else
			ig= guiableAccessedByPlayerClient.get(player);
		if(ig==null)
			return new int[0];
		return guiableAccessedByPlayer.get(player).getStates();
	}
}