package zombiecraft.Forge;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;

import zombiecraft.Core.Buyables;
import zombiecraft.Core.ZCBlocks;
import zombiecraft.Core.ZCItems;
import zombiecraft.Core.Entities.Comrade;
import zombiecraft.Core.Entities.EntityChickenDropless;
import zombiecraft.Core.Entities.EntityWorldHook;
import zombiecraft.Core.Entities.Imp;
import zombiecraft.Core.Entities.Zombie;
import zombiecraft.Core.Entities.Projectiles.EntityBullet;
import zombiecraft.Core.Entities.Projectiles.EntityBulletFlame;
import zombiecraft.Server.ZCGameMP;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ZCCommonProxy implements IGuiHandler
{
    public ZombieCraftMod mod;

    public ZCCommonProxy()
    {
    }

    public void init(ZombieCraftMod pMod)
    {
        mod = pMod;
        TickRegistry.registerTickHandler(new ZCServerTicks(), Side.SERVER);
        
		ZCBlocks.load(pMod);
		ZCItems.load(pMod);
		
		//this should all really be moved to ZCItems and ZCBlocks load
		
        
        
    	
    	ModLoader.addLocalization("itemGroup.ZombieCraft", "ZombieCraft Items");
    	ModLoader.addLocalization("death.zc.zombie", "%1$s died by a zombie"); //LanguageRegistry.instance().addStringLocalization("death.moon", "%1$s was sucked to the moon"); where "moon" was my deathsource deathtype and %1$s becomes playername
    	ModLoader.addLocalization("deathScreen.spectate", "Spectate");
        
    	Buyables.initItems();
    	
    	if (ZCServerTicks.zcGame == null) {
    		ZCServerTicks.zcGame = new ZCGameMP(true);
	    	((ZCGameMP)ZCServerTicks.zcGame).mc = MinecraftServer.getServer();
	    	ZCServerTicks.zcGame.gameInit();
    	}
    	
        int entIDs = 800;
        EntityRegistry.registerModEntity(EntityBullet.class, "EntityBullet", entIDs++, pMod, 128, 1, true);
        EntityRegistry.registerModEntity(EntityBulletFlame.class, "EntityBulletFlame", entIDs++, pMod, 128, 1, true);
        EntityRegistry.registerModEntity(Zombie.class, "EntityZCZombie", entIDs++, pMod, 128, 1, true);
        EntityRegistry.registerModEntity(EntityWorldHook.class, "EntityWorldHook", entIDs++, pMod, 128, 1, true);
        EntityRegistry.registerModEntity(Comrade.class, "EntityZCComrade", entIDs++, pMod, 128, 1, true);
        EntityRegistry.registerModEntity(Imp.class, "EntityZCImp", entIDs++, pMod, 128, 1, true);
        EntityRegistry.registerModEntity(EntityChickenDropless.class, "EntityZCChicken", entIDs++, pMod, 128, 1, true);
    }

    public void registerRenderInformation()
    {
    }

    public void registerTileEntitySpecialRenderer()
    {
    }

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Entity getEntByID(int id) {
		ZCServerTicks.zcGame.getWorld().getEntityByID(id);
		//return FMLClientHandler.instance().().theWorld.getEntityByID(id);
		//return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0).getEntityByID(id) // bad, need world ref
		return null;
	}

	public void loadSounds() {
		
	}
}
