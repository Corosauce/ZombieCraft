package zombiecraft.Forge;

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
import zombiecraft.Core.Items.ItemPerk;
import zombiecraft.Core.Items.ItemSwordZC;
import zombiecraft.Server.ZCGameMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.Item;
import net.minecraft.src.ItemSword;
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
		
		pMod.zcPotionSpeed = (new ZCPotionSpeed(25, false, 8171462)).setPotionName("potion.zc.speed");
		pMod.zcPotionExStatic = (new ZCPotionExStatic(26, false, 8171462)).setPotionName("potion.zc.exstatic");
        pMod.zcPotionJugg = (new ZCPotionJugg(27, false, 8171462)).setPotionName("potion.zc.jugg");
        //pMod.itemPerkCharge = (new ZCPotionJugg(28, false, 8171462)).setPotionName("potion.zc.charge");
        //pMod.itemPerkComrade = (new ZCPotionJugg(29, false, 8171462)).setPotionName("potion.zc.comrade");
        
        //Items \\
        pMod.itemSword = (new ItemSwordZC(11, EnumToolMaterial.IRON)).setIconCoord(2, 4).setItemName("swordIronZC");
        pMod.itemDEagle = (new ItemGunDEagle(pMod.itemPistolID)).setIconIndex(pMod.itemPistolTexID).setItemName("itemPistol").setCreativeTab(ZombieCraftMod.tabBlock);
        pMod.itemAk47 = (new ItemGunAk47(pMod.itemAk47ID)).setIconIndex(pMod.itemAk47TexID).setItemName("itemAk47").setCreativeTab(ZombieCraftMod.tabBlock);
        pMod.itemShotgun = (new ItemGunShotgun(pMod.itemShotgunID)).setIconIndex(pMod.itemShotgunTexID).setItemName("itemShotgun").setCreativeTab(ZombieCraftMod.tabBlock);
        pMod.itemM4 = (new ItemGunM4(pMod.itemM4ID)).setIconIndex(pMod.itemM4TexID).setItemName("itemM4").setCreativeTab(ZombieCraftMod.tabBlock);
        pMod.itemSniper = (new ItemGunSniper(pMod.itemSniperID)).setIconIndex(pMod.itemSniperTexID).setItemName("itemSniper").setCreativeTab(ZombieCraftMod.tabBlock);
        pMod.itemFlamethrower = (new ItemGunFlamethrower(pMod.itemFlamethrowerID)).setIconIndex(pMod.itemFlamethrowerTexID).setItemName("itemFlamethrower").setCreativeTab(ZombieCraftMod.tabBlock);
        
        //pMod.itemGrenade = (new ItemGrenade(pMod.itemGrenadeID)).setIconIndex(pMod.itemGrenadeTexID).setItemName("itemGrenade").setCreativeTab(ZombieCraftMod.tabBlock);
        //pMod.itemGrenadeStun = (new ItemGrenadeStun(pMod.itemGrenadeStunID)).setIconIndex(pMod.itemGrenadeStunTexID).setItemName("itemGrenadeStun").setCreativeTab(ZombieCraftMod.tabBlock);
        
        pMod.itemPerkSpeed = (new ItemPerk(pMod.itemPerkSpeedID, pMod.zcPotionSpeed.id, 20 * 30)).setIconCoord(15, 14).setItemName("itemPerkSpeed").setCreativeTab(ZombieCraftMod.tabBlock);
        pMod.itemPerkExStatic = (new ItemPerk(pMod.itemPerkExStaticID, pMod.zcPotionExStatic.id, 20 * 30)).setIconCoord(15, 14).setItemName("itemPerkExStatic").setCreativeTab(ZombieCraftMod.tabBlock);
        pMod.itemPerkJugg = (new ItemPerk(pMod.itemPerkJuggID, pMod.zcPotionJugg.id, 20 * 30)).setIconCoord(15, 14).setItemName("itemPerkJugg").setCreativeTab(ZombieCraftMod.tabBlock);
        pMod.itemPerkCharge = (new ItemPerk(pMod.itemPerkChargeID, 28, 20 * 30)).setIconCoord(15, 14).setItemName("itemPerkCharge").setCreativeTab(ZombieCraftMod.tabBlock);
        pMod.itemPerkComrade = (new ItemPerk(pMod.itemPerkComradeID, 29, 20 * 30)).setIconCoord(15, 14).setItemName("itemPerkComrade").setCreativeTab(ZombieCraftMod.tabBlock);
        
        ModLoader.addName(pMod.itemSword, "Sword");
        ModLoader.addName(pMod.itemDEagle, "Desert Eagle");
        ModLoader.addName(pMod.itemAk47, "AK 47");
        ModLoader.addName(pMod.itemShotgun, "Shotgun");
        ModLoader.addName(pMod.itemM4, "M4");
        ModLoader.addName(pMod.itemSniper, "Sniper Rifle");
        ModLoader.addName(pMod.itemFlamethrower, "Flamethrower");
        
        //ModLoader.addName(pMod.itemGrenade, "Grenade");
        //ModLoader.addName(pMod.itemGrenadeStun, "Stun Grenade");
        
        ModLoader.addName(pMod.itemPerkSpeed, "Speed Cola");
        ModLoader.addName(pMod.itemPerkExStatic, "ExStatic");
        ModLoader.addName(pMod.itemPerkJugg, "Juggernog");
        ModLoader.addName(pMod.itemPerkCharge, "Charge");
        ModLoader.addName(pMod.itemPerkComrade, "Comrade");
        //Items //
        
        //Blocks \\
        
        GameRegistry.registerTileEntity(TileEntityMobSpawnerWave.class, "z_tspawnblock");
        GameRegistry.registerTileEntity(TileEntityPurchasePlate.class, "z_tpurchaseblock");
        ModLoader.addName(ZCBlocks.b_mobSpawnerWave, "SpawnBlockWave");
        ModLoader.addName(ZCBlocks.b_buyBlock, "PurchaseBlock");
        
        ModLoader.addName(ZCItems.barricade,"ZC Barricade");
    	ModLoader.addName(ZCItems.editTool,"ZC Editor Tool");
    	//ModLoader.addName(ZCItems.buildTool,"ZC Build Tool");
    	ModLoader.addName(ZCBlocks.barrier,"ZC Barrier");
        
        //Blocks //
    	
    	ModLoader.addLocalization("itemGroup.ZombieCraft", "ZombieCraft Items");
        
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
