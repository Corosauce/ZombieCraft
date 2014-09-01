package zombiecraft.Forge;

import modconfig.ConfigMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import zombiecraft.Core.Dimension.ZCTeleporter;
import zombiecraft.Core.Dimension.ZCWorldProvider;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.config.ConfigMisc;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;

//@NetworkMod(channels = { "MLMP", "TileEnt", "Data", "Input", "Session", "MapConfig" }, clientSideRequired = true, serverSideRequired = true, packetHandler = ZCPacketHandler.class/*, versionBounds = "[2.0.0,2.1.0)"*/)
@Mod(modid = "ZombieCraftMod", name = "ZombieCraft Mod", version = "3.3.5")


public class ZombieCraftMod
{

    @SidedProxy(clientSide = "zombiecraft.Forge.ZCClientProxy", serverSide = "zombiecraft.Forge.ZCCommonProxy")
    public static ZCCommonProxy proxy;
    public static String modID = "zombiecraft";

	public Configuration preInitConfig;
	
	@Mod.Instance( value = "ZombieCraftMod" )
	public static ZombieCraftMod instance;
    
	public static boolean explosionsDestroyBlocks = false;
	public static boolean bulletsDestroyGlass = true;
	
	

	public static CreativeTabs tabBlock;
    //getNextID()-4
	
	public static String eventChannelName = "zombiecraft";
	public static final FMLEventChannel eventChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(eventChannelName);
    
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	//ConfigMod.addConfigFile(event, "zcids", new ConfigIDs(), false);
		eventChannel.register(new EventHandlerPacket());
    	ConfigMod.addConfigFile(event, "zcmisc", new ConfigMisc(), true);
        proxy.loadSounds();
    }
    
    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
    	
    	tabBlock = new CreativeTabZC(CreativeTabs.getNextID(), "ZombieCraft");
    	
        proxy.init(this);
        proxy.registerRenderInformation();
        
        int zcDimID = ZCGame.ZCDimensionID;
        boolean keepLoaded = false;
        
        DimensionManager.registerProviderType(zcDimID, ZCWorldProvider.class, keepLoaded);
		DimensionManager.registerDimension(zcDimID, zcDimID);		
        
		MinecraftForge.EVENT_BUS.register(new ZCEventHandler());
		//GameRegistry.registerPlayerTracker(new ZCPlayerTracker());
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		FMLCommonHandler.instance().bus().register(new EventHandlerFML());
		eventChannel.register(new EventHandlerPacket());
		
		//custom texture sheet info:
		//just do MinecraftForge.registerTextureSheet or something like that in ClientProxy
		//then in your block class in the constructor call setTexture("/yourterrain.png");
		//same goes with items
		
        
        //int what = Potion.potionTypes[0].id;
    }
    
    @Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
    	event.registerServerCommand(new CommandTeleportZC());
    	event.registerServerCommand(new CommandPoints());
    }

    @Mod.EventHandler
    public void modsLoaded(FMLPostInitializationEvent event)
    {
    	
    	
    	
    	
    }
    
    public static void teleportPlayerToggle(EntityPlayerMP player)
	{
		//TeleporterTropics tropicsTeleporter = new TeleporterTropics();
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		if (player.dimension != ZCGame.instance().ZCDimensionID) {
			scm.transferPlayerToDimension(player, ZCGame.instance().ZCDimensionID, new ZCTeleporter((WorldServer)player.worldObj));
		} else {
			scm.transferPlayerToDimension(player, 0, new ZCTeleporter((WorldServer)player.worldObj));
		}
		
	}
    
    public static void teleportPlayerToDim(EntityPlayerMP player, int dim)
	{
		//TeleporterTropics tropicsTeleporter = new TeleporterTropics();
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		//if (player.dimension == 0) {
			scm.transferPlayerToDimension(player, dim, new ZCTeleporter((WorldServer)player.worldObj));
		//} else {
			//scm.transferPlayerToDimension(player, 0, tropicsTeleporter);
		//}
		
	}
    
    
    // STUFF BROUGHT IN FROM 1.2.5 CODE \\
    
    
    
    /*public static void setHardness(int id, float val) {
		Block.blocksList[id].setHardness(val);
		//Sets original resistance, so explosives work, maybe change later?
		ZCUtil.setPrivateValueBoth(Block.class, Block.blocksList[id], ZCUtil.field_obf_blockResistance, ZCUtil.field_mcp_blockResistance, ZCUtil.blockHardness[id]*5F);
	}*/
    
    public static void dbg(Object obj) {
		if (true) {
			System.out.println(obj);
		}
	}
    
}


