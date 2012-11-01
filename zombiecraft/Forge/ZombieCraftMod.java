package zombiecraft.Forge;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import build.BuildEventHandler;

import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

import paulscode.sound.SoundSystem;
import zombiecraft.Core.PacketTypes;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Dimension.ZCTeleporter;
import zombiecraft.Core.Dimension.ZCWorldProvider;
import zombiecraft.Core.GameLogic.ZCGame;

@NetworkMod(channels = { "MLMP", "TileEnt", "Data", "Input" }, clientSideRequired = true, serverSideRequired = true, packetHandler = ZCPacketHandler.class)
@Mod(modid = "ZombieCraftMod", name = "ZombieCraft Mod", version = "v3.0")


public class ZombieCraftMod
{

    @SidedProxy(clientSide = "zombiecraft.Forge.ZCClientProxy", serverSide = "zombiecraft.Forge.ZCCommonProxy")
    public static ZCCommonProxy proxy;

	public Configuration preInitConfig;
	
	@Mod.Instance( value = "ZombieCraftMod" )
	public static ZombieCraftMod instance;
    
	public static boolean explosionsDestroyBlocks = false;
	public static boolean bulletsDestroyGlass = true;
	
	//Gun fields \\
    public static Item itemDEagle;
    public static Item itemAk47;
    public static Item itemShotgun;
    public static Item itemM4;
    public static Item itemSniper;
    public static Item itemFlamethrower;
    
    public static Item itemGrenade;
    public static Item itemGrenadeStun;
    
    public static Item itemPerkSpeed;
    public static Item itemPerkExStatic;
    public static Item itemPerkJugg;
    
    public static int itemPistolID;
    public static int itemAk47ID;
    public static int itemShotgunID;
    public static int itemM4ID;
    public static int itemSniperID;
    public static int itemFlamethrowerID;
    
    public static int itemGrenadeID;
    public static int itemGrenadeStunID;
    
    public static int itemPerkSpeedID;
    public static int itemPerkExStaticID;
    public static int itemPerkJuggID;
    
    public static int itemPistolTexID = 0;
    public static int itemAk47TexID = 0;
    public static int itemShotgunTexID = 0;
    public static int itemM4TexID = 0;
    public static int itemSniperTexID = 0;
    public static int itemFlamethrowerTexID = 0;
    
    public static int itemGrenadeTexID = 0;
    public static int itemGrenadeStunTexID = 0;
    
    public static int barricadeTopTexIDs[] = new int[] { 0, 0, 0, 0, 0, 0, 0 };
    //Gun fields //
    
    public static int itemIndexID = 22701;
    
    public static Potion zcPotionSpeed;
    public static Potion zcPotionExStatic;
    public static Potion zcPotionJugg;

	public static CreativeTabs tabBlock;
    //getNextID()-4
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        preInitConfig = new Configuration(event.getSuggestedConfigurationFile());

        try
        {
        	itemAk47ID = preInitConfig.getItem(Configuration.CATEGORY_ITEM, "itemAk47ID", itemIndexID++).getInt();
        	itemPistolID = preInitConfig.getItem(Configuration.CATEGORY_ITEM, "itemPistolID", itemIndexID++).getInt();
        	itemShotgunID = preInitConfig.getItem(Configuration.CATEGORY_ITEM, "itemShotgunID", itemIndexID++).getInt();
        	itemM4ID = preInitConfig.getItem(Configuration.CATEGORY_ITEM, "itemM4ID", itemIndexID++).getInt();
        	itemSniperID = preInitConfig.getItem(Configuration.CATEGORY_ITEM, "itemSniperID", itemIndexID++).getInt();
        	itemFlamethrowerID = preInitConfig.getItem(Configuration.CATEGORY_ITEM, "itemFlamethrowerID", itemIndexID++).getInt();
        	
        	itemGrenadeID = preInitConfig.getItem(Configuration.CATEGORY_ITEM, "itemGrenadeID", itemIndexID++).getInt();
        	itemGrenadeStunID = preInitConfig.getItem(Configuration.CATEGORY_ITEM, "itemGrenadeStunID", itemIndexID++).getInt();
        	
        	itemPerkSpeedID = preInitConfig.getItem(Configuration.CATEGORY_ITEM, "itemPerkSpeedID", itemIndexID++).getInt();
        	itemPerkExStaticID = preInitConfig.getItem(Configuration.CATEGORY_ITEM, "itemPerkExStaticID", itemIndexID++).getInt();
        	itemPerkJuggID = preInitConfig.getItem(Configuration.CATEGORY_ITEM, "itemPerkJuggID", itemIndexID++).getInt();
        	
            preInitConfig.load();
            
            //load here
        }
        catch (Exception e)
        {
            FMLLog.log(Level.SEVERE, e, "ZombieCraft has a problem loading it's configuration");
        }
        finally
        {
            preInitConfig.save();
        }
    }
    
    @Init
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
		
        
        //int what = Potion.potionTypes[0].id;
    }

    @PostInit
    public void modsLoaded(FMLPostInitializationEvent event)
    {
        
    }
    
    public static void teleportPlayerToggle(EntityPlayerMP player)
	{
		//TeleporterTropics tropicsTeleporter = new TeleporterTropics();
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		if (player.dimension == 0) {
			scm.transferPlayerToDimension(player, ZCGame.instance().ZCDimensionID, new ZCTeleporter());
		} else {
			scm.transferPlayerToDimension(player, 0, new ZCTeleporter());
		}
		
	}
    
    public static void teleportPlayerToDim(EntityPlayerMP player, int dim)
	{
		//TeleporterTropics tropicsTeleporter = new TeleporterTropics();
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		//if (player.dimension == 0) {
			scm.transferPlayerToDimension(player, dim, new ZCTeleporter());
		//} else {
			//scm.transferPlayerToDimension(player, 0, tropicsTeleporter);
		//}
		
	}
    
    
    // STUFF BROUGHT IN FROM 1.2.5 CODE \\
    
    
    
    public static void setHardness(int id, float val) {
		Block.blocksList[id].setHardness(val);
		//Sets original resistance, so explosives work, maybe change later?
		ZCUtil.setPrivateValueBoth(Block.class, Block.blocksList[id], "cc", "blockResistance", ZCUtil.blockHardness[id]*5F);
	}
    
}


