package zombiecraft.Forge;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import zombiecraft.Client.Blocks.TileEntityMobSpawnerWaveRenderer;
import zombiecraft.Client.Blocks.TileEntityPurchasePlateRenderer;
import zombiecraft.Client.Entities.RenderBullet;
import zombiecraft.Client.Entities.RenderBulletShot;
import zombiecraft.Client.Entities.RenderEntityWorldHook;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.Blocks.TileEntityPurchasePlate;
import zombiecraft.Core.Entities.Comrade;
import zombiecraft.Core.Entities.EntityBullet;
import zombiecraft.Core.Entities.EntityBulletFlame;
import zombiecraft.Core.Entities.EntityWorldHook;
import zombiecraft.Core.Entities.Zombie;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.common.Side;
@SideOnly(Side.CLIENT)
public class ZCClientProxy extends ZCCommonProxy
{
    public static Minecraft mc;

    public ZCClientProxy()
    {
        mc = ModLoader.getMinecraftInstance();
    }

    @Override
    public void init(ZombieCraftMod pMod)
    {
    	pMod.itemPistolTexID = ModLoader.addOverride("/gui/items.png", "/sdk/itemGunDeagle.png");
    	pMod.itemAk47TexID = ModLoader.addOverride("/gui/items.png", "/sdk/itemGunAk47.png");
    	pMod.itemShotgunTexID = ModLoader.addOverride("/gui/items.png", "/sdk/itemGunShotgun.png");
    	pMod.itemM4TexID = ModLoader.addOverride("/gui/items.png", "/sdk/itemGunM4.png");
    	pMod.itemSniperTexID = ModLoader.addOverride("/gui/items.png", "/sdk/itemGunSniper.png");
    	pMod.itemFlamethrowerTexID = ModLoader.addOverride("/gui/items.png", "/sdk/itemGunFlamethrower.png");
    	
    	pMod.barricadeTopTexIDs = new int[] { ModLoader.addOverride("/terrain.png", "/zc/blocks/barricade0.png"),
        		ModLoader.addOverride("/terrain.png", "/zc/blocks/barricade1.png"),
        		ModLoader.addOverride("/terrain.png", "/zc/blocks/barricade2.png"),
        		ModLoader.addOverride("/terrain.png", "/zc/blocks/barricade3.png"),
        		ModLoader.addOverride("/terrain.png", "/zc/blocks/barricade4.png"),
        		ModLoader.addOverride("/terrain.png", "/zc/blocks/barricade5.png"),
        		ModLoader.addOverride("/terrain.png", "/zc/blocks/barricadebottom.png")
        	};
    	
        super.init(pMod);
        TickRegistry.registerTickHandler(new ZCClientTicks(), Side.CLIENT);
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMobSpawnerWave.class, new TileEntityMobSpawnerWaveRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPurchasePlate.class, new TileEntityPurchasePlateRenderer());
        
        //RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new RenderBullet());
        RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new RenderBulletShot());
        RenderingRegistry.registerEntityRenderingHandler(EntityBulletFlame.class, new RenderBullet());
        RenderingRegistry.registerEntityRenderingHandler(Zombie.class, new RenderBiped(new ModelZombie(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(Comrade.class, new RenderBiped(new ModelZombie(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityWorldHook.class, new RenderEntityWorldHook());
        
        //MinecraftForge.EVENT_BUS.register(new EventHandler());
        KeyBindingRegistry.registerKeyBinding(new ZCKeybindHandler());
        
    }

    /**
     * This is for registering armor types, like ModLoader.addArmor used to do
     */
    public int getArmorNumber(String type)
    {
        return RenderingRegistry.addNewArmourRendererPrefix(type);
    }
    
    @Override
    public void registerRenderInformation()
    {
    	
    }

    @Override
    public void registerTileEntitySpecialRenderer()
    {
    	
    }
}
