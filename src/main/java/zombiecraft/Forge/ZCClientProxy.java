package zombiecraft.Forge;

import net.minecraft.entity.Entity;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import zombiecraft.Client.Blocks.TileEntityMobSpawnerWaveRenderer;
import zombiecraft.Client.Blocks.TileEntityMysteryBoxRenderer;
import zombiecraft.Client.Blocks.TileEntityPacketTesterRenderer;
import zombiecraft.Client.Blocks.TileEntityPurchasePlateRenderer;
import zombiecraft.Client.Blocks.TileEntitySessionRenderer;
import zombiecraft.Client.Blocks.TileEntityTowerRenderer;
import zombiecraft.Client.Entities.RenderBullet;
import zombiecraft.Client.Entities.RenderBulletShot;
import zombiecraft.Client.Entities.RenderEntityWorldHook;
import zombiecraft.Client.Entities.RenderZCComrade;
import zombiecraft.Client.Entities.RenderZCImp;
import zombiecraft.Client.Entities.RenderZCZombie;
import zombiecraft.Core.ZCItems;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.Blocks.TileEntityMysteryBox;
import zombiecraft.Core.Blocks.TileEntityPacketTester;
import zombiecraft.Core.Blocks.TileEntityPurchasePlate;
import zombiecraft.Core.Blocks.TileEntitySession;
import zombiecraft.Core.Blocks.TileEntityTower;
import zombiecraft.Core.Entities.Comrade;
import zombiecraft.Core.Entities.EntityWorldHook;
import zombiecraft.Core.Entities.Imp;
import zombiecraft.Core.Entities.Zombie;
import zombiecraft.Core.Entities.Projectiles.EntityBullet;
import zombiecraft.Core.Entities.Projectiles.EntityBulletFlame;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ZCClientProxy extends ZCCommonProxy
{

    public ZCClientProxy()
    {
        
    }

    @Override
    public void init(ZombieCraftMod pMod)
    {
    	/*ZCItems.itemPistolTexID = ModLoader.addOverride("/gui/items.png", "/zc/guns/itemGunDeagle.png");
    	ZCItems.itemAk47TexID = ModLoader.addOverride("/gui/items.png", "/zc/guns/itemGunAk47.png");
    	ZCItems.itemShotgunTexID = ModLoader.addOverride("/gui/items.png", "/zc/guns/itemGunShotgun.png");
    	ZCItems.itemM4TexID = ModLoader.addOverride("/gui/items.png", "/zc/guns/itemGunM4.png");
    	ZCItems.itemSniperTexID = ModLoader.addOverride("/gui/items.png", "/zc/guns/itemGunSniper.png");
    	ZCItems.itemFlamethrowerTexID = ModLoader.addOverride("/gui/items.png", "/zc/guns/itemGunFlamethrower.png");
    	
    	ZCItems.itemM1911TexID = ModLoader.addOverride("/gui/items.png", "/zc/guns/itemGunM1911.png");
    	ZCItems.itemRifleTexID = ModLoader.addOverride("/gui/items.png", "/zc/guns/itemGunRifle.png");
    	ZCItems.itemUziTexID = ModLoader.addOverride("/gui/items.png", "/zc/guns/itemGunUzi.png");
    	ZCItems.itemRaygunTexID = ModLoader.addOverride("/gui/items.png", "/zc/guns/itemGunRaygun.png");
    	ZCItems.itemRPGTexID = ModLoader.addOverride("/gui/items.png", "/zc/guns/itemGunRPG.png");
    	ZCItems.itemChickenGunTexID = ModLoader.addOverride("/gui/items.png", "/zc/guns/itemGunChickenGun.png");
    	
    	ZCItems.itemPickupDoublePointsTexID = ModLoader.addOverride("/gui/items.png", "/zc/items/doublepoints.png");
    	ZCItems.itemPickupInstaKillTexID = ModLoader.addOverride("/gui/items.png", "/zc/items/instakill.png");
    	ZCItems.itemPickupMaxAmmoTexID = ModLoader.addOverride("/gui/items.png", "/zc/items/maxammo.png");
    	ZCItems.itemPickupNukeTexID = ModLoader.addOverride("/gui/items.png", "/zc/items/nuke.png");*/
    	
    	/*ZCItems.barricadeTopTexIDs = new int[] { ModLoader.addOverride("/terrain.png", "/zc/blocks/barricade0.png"),
        		ModLoader.addOverride("/terrain.png", "/zc/blocks/barricade1.png"),
        		ModLoader.addOverride("/terrain.png", "/zc/blocks/barricade2.png"),
        		ModLoader.addOverride("/terrain.png", "/zc/blocks/barricade3.png"),
        		ModLoader.addOverride("/terrain.png", "/zc/blocks/barricade4.png"),
        		ModLoader.addOverride("/terrain.png", "/zc/blocks/barricade5.png"),
        		ModLoader.addOverride("/terrain.png", "/zc/blocks/barricadebottom.png")
        	};*/
    	
    	//ZCBlocks.bettyTexID = ModLoader.addOverride("/terrain.png", "/zc/blocks/betty.png");
    	
        super.init(pMod);
        //TickRegistry.registerTickHandler(new ZCClientTicks(), Side.CLIENT);
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMobSpawnerWave.class, new TileEntityMobSpawnerWaveRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPurchasePlate.class, new TileEntityPurchasePlateRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPacketTester.class, new TileEntityPacketTesterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySession.class, new TileEntitySessionRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTower.class, new TileEntityTowerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMysteryBox.class, new TileEntityMysteryBoxRenderer());
        
        //Item render registers
        MinecraftForgeClient.registerItemRenderer(ZCItems.itemAk47, new GunRenderer());
        
        //RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new RenderBullet());
        RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new RenderBulletShot());
        RenderingRegistry.registerEntityRenderingHandler(EntityBulletFlame.class, new RenderBullet());
        RenderingRegistry.registerEntityRenderingHandler(Zombie.class, new RenderZCZombie());//new RenderBiped(new ModelZombie(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(Imp.class, new RenderZCImp());//new RenderBiped(new ModelZombie(), 0.5F));
        
        RenderingRegistry.registerEntityRenderingHandler(Comrade.class, new RenderZCComrade());
        RenderingRegistry.registerEntityRenderingHandler(EntityWorldHook.class, new RenderEntityWorldHook());
        
        //MinecraftForge.EVENT_BUS.register(new EventHandler());
        //KeyBindingRegistry.registerKeyBinding(new ZCKeybindHandler());
        
        RenderingRegistry.registerBlockHandler(new ZCBarricadePlaceableRenderer());
        
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
    
    @Override
	public void loadSounds() {
		//MinecraftForge.EVENT_BUS.register(new SoundLoader());
	}
    
	@Override
	public Entity getEntByID(int id) {
		return FMLClientHandler.instance().getClient().theWorld.getEntityByID(id);
	}
}
