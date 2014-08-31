package zombiecraft.Core;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import zombiecraft.Core.Items.ItemAbility;
import zombiecraft.Core.Items.ItemBarricade;
import zombiecraft.Core.Items.ItemBarricadePlaceable;
import zombiecraft.Core.Items.ItemEditTool;
import zombiecraft.Core.Items.ItemGunAk47;
import zombiecraft.Core.Items.ItemGunChickenGun;
import zombiecraft.Core.Items.ItemGunDEagle;
import zombiecraft.Core.Items.ItemGunFlamethrower;
import zombiecraft.Core.Items.ItemGunM1911;
import zombiecraft.Core.Items.ItemGunM4;
import zombiecraft.Core.Items.ItemGunRPG;
import zombiecraft.Core.Items.ItemGunRaygun;
import zombiecraft.Core.Items.ItemGunRifle;
import zombiecraft.Core.Items.ItemGunShotgun;
import zombiecraft.Core.Items.ItemGunSniper;
import zombiecraft.Core.Items.ItemGunUzi;
import zombiecraft.Core.Items.ItemSwordZC;
import zombiecraft.Core.config.ConfigIDs;
import zombiecraft.Forge.ZombieCraftMod;
import CoroUtil.util.CoroUtilItem;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ZCItems {
	
	//@MLProp public static int z_ItemIDStart = 5600;
	
	public static Item barricade;
	public static Item barricadePlaceable;
	public static Item editTool;
	public static Item buildTool;
	
	//Guns
	public static Item itemSword;
    public static Item itemDEagle;
    public static Item itemAk47;
    public static Item itemShotgun;
    public static Item itemM4;
    public static Item itemSniper;
    public static Item itemFlamethrower;
    
    public static Item itemM1911;
    public static Item itemRifle;
    public static Item itemUzi;
    public static Item itemRaygun;
    public static Item itemRPG;
    public static Item itemChickenGun;
    
    //Throwables
    public static Item itemGrenade;
    public static Item itemGrenadeStun;
    
    //Perks
    public static Item itemPerkSpeed;
    public static Item itemPerkExStatic;
    public static Item itemPerkJugg;
    public static Item itemPerkCharge;
    public static Item itemPerkComrade;
    
    //Pickups
    public static Item itemPickupDoublePoints;
    public static Item itemPickupMaxAmmo;
    public static Item itemPickupNuke;
    public static Item itemPickupInstaKill;
    
    public static Item itemPlacerTower;
    public static Item itemPlacerWall;
    
    /*public static int itemSwordID;
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
    public static int itemPerkChargeID;
    public static int itemPerkComradeID;*/
    
    /*public static int itemPistolTexID = 0;
    public static int itemAk47TexID = 0;
    public static int itemShotgunTexID = 0;
    public static int itemM4TexID = 0;
    public static int itemSniperTexID = 0;
    public static int itemFlamethrowerTexID = 0;
    
    public static int itemM1911TexID = 0;
    public static int itemRifleTexID = 0;
    public static int itemUziTexID = 0;
    public static int itemRaygunTexID = 0;
    public static int itemRPGTexID = 0;
    public static int itemChickenGunTexID = 0;
    
    public static int itemGrenadeTexID = 0;
    public static int itemGrenadeStunTexID = 0;
    
    public static int itemPickupDoublePointsTexID = 0;
    public static int itemPickupMaxAmmoTexID = 0;
    public static int itemPickupNukeTexID = 0;
    public static int itemPickupInstaKillTexID = 0;*/
    
    //public static int barricadeTopTexIDs[] = new int[] { 0, 0, 0, 0, 0, 0, 0 };
    
    //Gun fields //
    
    //public static int itemIndexID;
    public static int abilityIndexID = 25;
    
    //public static Potion zcPotionSpeed;
    //public static Potion zcPotionExStatic;
    //public static Potion zcPotionJugg;
	
	public ZCItems() {
		
	}
	
	public static void load(ZombieCraftMod pMod) {
		CoroUtilItem.setUnlocalizedNameAndTexture(barricade = (new ItemBarricade(Material.wood)).setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":barricade");
		CoroUtilItem.setUnlocalizedNameAndTexture(editTool = (new ItemEditTool(0)).setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":editTool");
		
        //Items \\
		CoroUtilItem.setUnlocalizedNameAndTexture(itemSword = (new ItemSwordZC(EnumToolMaterial.IRON))/*.setIconCoord(2, 4)*/.setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":swordIronZC");
        itemDEagle = (new ItemGunDEagle()).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":guns/itemGunDeagle").setCreativeTab(ZombieCraftMod.tabBlock);
        itemAk47 = (new ItemGunAk47()).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":guns/itemGunAk47").setCreativeTab(ZombieCraftMod.tabBlock);
        itemShotgun = (new ItemGunShotgun()).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":guns/itemGunShotgun").setCreativeTab(ZombieCraftMod.tabBlock);
        itemM4 = (new ItemGunM4()).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":guns/itemGunM4").setCreativeTab(ZombieCraftMod.tabBlock);
        itemSniper = (new ItemGunSniper()).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":guns/itemGunSniper").setCreativeTab(ZombieCraftMod.tabBlock);
        itemFlamethrower = (new ItemGunFlamethrower()).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":guns/itemGunFlamethrower").setCreativeTab(ZombieCraftMod.tabBlock);
        
        itemM1911 = (new ItemGunM1911()).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":guns/itemGunM1911").setCreativeTab(ZombieCraftMod.tabBlock);
        itemRifle = (new ItemGunRifle()).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":guns/itemGunRifle").setCreativeTab(ZombieCraftMod.tabBlock);
        itemUzi = (new ItemGunUzi()).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":guns/itemGunUzi").setCreativeTab(ZombieCraftMod.tabBlock);
        itemRaygun = (new ItemGunRaygun()).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":guns/itemGunRaygun").setCreativeTab(ZombieCraftMod.tabBlock);
        itemRPG = (new ItemGunRPG()).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":guns/itemGunRPG").setCreativeTab(ZombieCraftMod.tabBlock);
        itemChickenGun = (new ItemGunChickenGun()).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":guns/itemGunChickenGun").setCreativeTab(ZombieCraftMod.tabBlock);
        
        //itemGrenade = (new ItemGrenade(itemGrenadeID)).setIconIndex(itemGrenadeTexID).setUnlocalizedNameAndTexture("itemGrenade").setCreativeTab(ZombieCraftMod.tabBlock);
        //itemGrenadeStun = (new ItemGrenadeStun(itemGrenadeStunID)).setIconIndex(itemGrenadeStunTexID).setUnlocalizedNameAndTexture("itemGrenadeStun").setCreativeTab(ZombieCraftMod.tabBlock);
        
        CoroUtilItem.setUnlocalizedNameAndTexture(itemPerkSpeed = (new ItemAbility(abilityIndexID++))/*.setIconCoord(15, 14)*/.setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":itemPerkSpeed");
        CoroUtilItem.setUnlocalizedNameAndTexture(itemPerkExStatic = (new ItemAbility(abilityIndexID++))/*.setIconCoord(15, 14)*/.setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":itemPerkExStatic");
        CoroUtilItem.setUnlocalizedNameAndTexture(itemPerkJugg = (new ItemAbility(abilityIndexID++))/*.setIconCoord(15, 14)*/.setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":itemPerkJugg");
        CoroUtilItem.setUnlocalizedNameAndTexture(itemPerkCharge = (new ItemAbility(abilityIndexID++))/*.setIconCoord(15, 14)*/.setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":itemPerkCharge");
        CoroUtilItem.setUnlocalizedNameAndTexture(itemPerkComrade = (new ItemAbility(abilityIndexID++))/*.setIconCoord(15, 14)*/.setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":itemPerkComrade");
        
        CoroUtilItem.setUnlocalizedNameAndTexture(itemPickupDoublePoints = (new ItemAbility(abilityIndexID++, true)).setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":doublepoints");
        CoroUtilItem.setUnlocalizedNameAndTexture(itemPickupInstaKill = (new ItemAbility(abilityIndexID++, true)).setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":instakill");
        CoroUtilItem.setUnlocalizedNameAndTexture(itemPickupMaxAmmo = (new ItemAbility(abilityIndexID++, true)).setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":maxammo");
        CoroUtilItem.setUnlocalizedNameAndTexture(itemPickupNuke = (new ItemAbility(abilityIndexID++, true)).setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":nuke");
        
        CoroUtilItem.setUnlocalizedNameAndTexture(barricadePlaceable = (new ItemBarricadePlaceable(Material.wood))/*.setIconCoord(11, 2)*/.setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":barricadePlaceable");
        //itemPlacerTower = (new ItemPlacerTower(ConfigIDs.ID_I_PLACERTOWER)).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":placerTower").setCreativeTab(ZombieCraftMod.tabBlock);
        //itemPlacerWall = (new ItemPlacerWall(ConfigIDs.ID_I_PLACERWALL)).setUnlocalizedNameAndTexture(ZombieCraftMod.modID + ":placerWall").setCreativeTab(ZombieCraftMod.tabBlock);
        
        LanguageRegistry.addName(itemSword, "Sword");
        LanguageRegistry.addName(itemDEagle, "Desert Eagle");
        LanguageRegistry.addName(itemAk47, "AK 47");
        LanguageRegistry.addName(itemShotgun, "Shotgun");
        LanguageRegistry.addName(itemM4, "M4");
        LanguageRegistry.addName(itemSniper, "Sniper Rifle");
        LanguageRegistry.addName(itemFlamethrower, "Flamethrower");
        
        LanguageRegistry.addName(itemM1911, "M1911");
        LanguageRegistry.addName(itemRifle, "Rifle");
        LanguageRegistry.addName(itemUzi, "Uzi");
        LanguageRegistry.addName(itemRaygun, "Raygun");
        LanguageRegistry.addName(itemRPG, "RPG");
        LanguageRegistry.addName(itemChickenGun, "Chicken Gun");
        
        //LanguageRegistry.addName(itemGrenade, "Grenade");
        //LanguageRegistry.addName(itemGrenadeStun, "Stun Grenade");
        
        LanguageRegistry.addName(itemPerkSpeed, "Speed Cola");
        LanguageRegistry.addName(itemPerkExStatic, "ExStatic");
        LanguageRegistry.addName(itemPerkJugg, "Juggernog");
        LanguageRegistry.addName(itemPerkCharge, "Charge");
        LanguageRegistry.addName(itemPerkComrade, "Comrade");
        LanguageRegistry.addName(itemPickupDoublePoints, "Double Points");
        LanguageRegistry.addName(itemPickupInstaKill, "Insta Kill");
        LanguageRegistry.addName(itemPickupMaxAmmo, "Refill Ammo");
        LanguageRegistry.addName(itemPickupNuke, "Nuke");
        
        LanguageRegistry.addName(barricade,"Barricade");
        LanguageRegistry.addName(barricadePlaceable,"Placeable Barricade");
    	LanguageRegistry.addName(editTool,"ZC Editor Tool");
    	//LanguageRegistry.addName(itemPlacerTower,"Tower Placer");
    	//LanguageRegistry.addName(itemPlacerWall,"Wall Placer");
        //Items //
		
		//buildTool = (new ItemBuildTool(z_ItemIDStart++, 0)).setIconCoord(5, 5).setUnlocalizedNameAndTexture("buildTool").setCreativeTab(ZombieCraftMod.tabBlock);
	}
}
