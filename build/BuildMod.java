package build;

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
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
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

@NetworkMod(channels = { "Data" }, clientSideRequired = true, serverSideRequired = true, packetHandler = BuildPacketHandler.class)
@Mod(modid = "BuildMod", name = "Build Mod", version = "v1.0")

public class BuildMod
{

    @SidedProxy(clientSide = "build.BuildClientProxy", serverSide = "build.BuildCommonProxy")
    public static BuildCommonProxy proxy;
    
    public Configuration preInitConfig;
    
    public ItemEditTool itemEditTool; 
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        preInitConfig = new Configuration(event.getSuggestedConfigurationFile());

        try
        {
            preInitConfig.load();
            
            //load here
        }
        catch (Exception e)
        {
            FMLLog.log(Level.SEVERE, e, "SkeletonMod has a problem loading it's configuration");
        }
        finally
        {
            preInitConfig.save();
        }
    }
    
    @Init
    public void load(FMLInitializationEvent event)
    {
        proxy.init(this);
        proxy.registerRenderInformation();
        
        MinecraftForge.EVENT_BUS.register(new BuildEventHandler());
    }

    @PostInit
    public void modsLoaded(FMLPostInitializationEvent event)
    {
        
    }
    
}


