package zombiecraft.Forge;

import weather.blocks.TileEntityTSiren;
import weather.worldObjects.EntWorm;
import weather.worldObjects.ItemTornado;
import zombiecraft.Core.Buyables;
import zombiecraft.Core.ZCBlocks;
import zombiecraft.Core.ZCItems;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.Blocks.TileEntityPurchasePlate;
import zombiecraft.Core.Entities.Comrade;
import zombiecraft.Core.Entities.EntityBullet;
import zombiecraft.Core.Entities.EntityBulletFlame;
import zombiecraft.Core.Entities.EntityWorldHook;
import zombiecraft.Core.Entities.Zombie;
import zombiecraft.Core.Items.ItemGrenade;
import zombiecraft.Core.Items.ItemGrenadeStun;
import zombiecraft.Core.Items.ItemGunAk47;
import zombiecraft.Core.Items.ItemGunDEagle;
import zombiecraft.Core.Items.ItemGunFlamethrower;
import zombiecraft.Core.Items.ItemGunM4;
import zombiecraft.Core.Items.ItemGunShotgun;
import zombiecraft.Core.Items.ItemGunSniper;
import zombiecraft.Server.ZCGameMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ServerCommandManager;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

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
        
		ZCBlocks.load();
		ZCItems.load();
        
        //Items \\
        pMod.itemDEagle = (new ItemGunDEagle(pMod.itemPistolID)).setIconIndex(pMod.itemPistolTexID).setItemName("itemPistol").setCreativeTab(CreativeTabs.tabMisc);
        pMod.itemAk47 = (new ItemGunAk47(pMod.itemAk47ID)).setIconIndex(pMod.itemAk47TexID).setItemName("itemAk47").setCreativeTab(CreativeTabs.tabMisc);
        pMod.itemShotgun = (new ItemGunShotgun(pMod.itemShotgunID)).setIconIndex(pMod.itemShotgunTexID).setItemName("itemShotgun").setCreativeTab(CreativeTabs.tabMisc);
        pMod.itemM4 = (new ItemGunM4(pMod.itemM4ID)).setIconIndex(pMod.itemM4TexID).setItemName("itemM4").setCreativeTab(CreativeTabs.tabMisc);
        pMod.itemSniper = (new ItemGunSniper(pMod.itemSniperID)).setIconIndex(pMod.itemSniperTexID).setItemName("itemSniper").setCreativeTab(CreativeTabs.tabMisc);
        pMod.itemFlamethrower = (new ItemGunFlamethrower(pMod.itemFlamethrowerID)).setIconIndex(pMod.itemFlamethrowerTexID).setItemName("itemFlamethrower").setCreativeTab(CreativeTabs.tabMisc);
        
        pMod.itemGrenade = (new ItemGrenade(pMod.itemGrenadeID)).setIconIndex(pMod.itemGrenadeTexID).setItemName("itemGrenade").setCreativeTab(CreativeTabs.tabMisc);
        pMod.itemGrenadeStun = (new ItemGrenadeStun(pMod.itemGrenadeStunID)).setIconIndex(pMod.itemGrenadeStunTexID).setItemName("itemGrenadeStun").setCreativeTab(CreativeTabs.tabMisc);
        
        ModLoader.addName(pMod.itemDEagle, "Desert Eagle");
        ModLoader.addName(pMod.itemAk47, "AK 47");
        ModLoader.addName(pMod.itemShotgun, "Shotgun");
        ModLoader.addName(pMod.itemM4, "M4");
        ModLoader.addName(pMod.itemSniper, "Sniper Rifle");
        ModLoader.addName(pMod.itemFlamethrower, "Flamethrower");
        
        ModLoader.addName(pMod.itemGrenade, "Grenade");
        ModLoader.addName(pMod.itemGrenadeStun, "Stun Grenade");
        //Items //
        
        //Blocks \\
        
        GameRegistry.registerTileEntity(TileEntityMobSpawnerWave.class, "z_tspawnblock");
        GameRegistry.registerTileEntity(TileEntityPurchasePlate.class, "z_tpurchaseblock");
        ModLoader.addName(ZCBlocks.b_mobSpawnerWave, "SpawnBlockWave");
        ModLoader.addName(ZCBlocks.b_buyBlock, "PurchaseBlock");
        
        ModLoader.addName(ZCItems.barricade,"ZC Barricade");
    	ModLoader.addName(ZCItems.editTool,"ZC Editor Tool");
    	ModLoader.addName(ZCItems.buildTool,"ZC Build Tool");
    	ModLoader.addName(ZCBlocks.barrier,"ZC Barrier");
        
        //Blocks //
        
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
}
