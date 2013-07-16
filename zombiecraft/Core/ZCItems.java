package zombiecraft.Core;

import net.minecraft.block.material.Material;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.src.ModLoader;
import net.minecraft.util.Icon;

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
import zombiecraft.Forge.ZombieCraftMod;

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
    
    public static int itemIndexID;
    public static int abilityIndexID;
    
    //public static Potion zcPotionSpeed;
    //public static Potion zcPotionExStatic;
    //public static Potion zcPotionJugg;
	
	public ZCItems() {
		
	}
	
	public static void load(ZombieCraftMod pMod) {
		barricade = (new ItemBarricade(itemIndexID++, Material.wood))/*.setIconCoord(11, 2)*/.setUnlocalizedName("ZombieCraft:barricade").setCreativeTab(ZombieCraftMod.tabBlock);
		editTool = (new ItemEditTool(itemIndexID++, 0))/*.setIconCoord(5, 4)*/.setUnlocalizedName("ZombieCraft:editTool").setCreativeTab(ZombieCraftMod.tabBlock);
		
		//zcPotionSpeed = (new ZCPotionSpeed(abilityIndexID+0, false, 8171462)).setPotionName("potion.zc.speed");
		//zcPotionExStatic = (new ZCPotionExStatic(abilityIndexID+1, false, 8171462)).setPotionName("potion.zc.exstatic");
        //zcPotionJugg = (new ZCPotionJugg(abilityIndexID+2, false, 8171462)).setPotionName("potion.zc.jugg");
        //itemPerkCharge = (new ZCPotionJugg(28, false, 8171462)).setPotionName("potion.zc.charge");
        //itemPerkComrade = (new ZCPotionJugg(29, false, 8171462)).setPotionName("potion.zc.comrade");
        
        //Items \\
        itemSword = (new ItemSwordZC(itemIndexID++, EnumToolMaterial.IRON))/*.setIconCoord(2, 4)*/.setUnlocalizedName("ZombieCraft:swordIronZC").setCreativeTab(ZombieCraftMod.tabBlock);
        itemDEagle = (new ItemGunDEagle(itemIndexID++)).setUnlocalizedName("ZombieCraft:guns/itemGunDeagle").setCreativeTab(ZombieCraftMod.tabBlock);
        itemAk47 = (new ItemGunAk47(itemIndexID++)).setUnlocalizedName("ZombieCraft:guns/itemGunAk47").setCreativeTab(ZombieCraftMod.tabBlock);
        itemShotgun = (new ItemGunShotgun(itemIndexID++)).setUnlocalizedName("ZombieCraft:guns/itemGunShotgun").setCreativeTab(ZombieCraftMod.tabBlock);
        itemM4 = (new ItemGunM4(itemIndexID++)).setUnlocalizedName("ZombieCraft:guns/itemGunM4").setCreativeTab(ZombieCraftMod.tabBlock);
        itemSniper = (new ItemGunSniper(itemIndexID++)).setUnlocalizedName("ZombieCraft:guns/itemGunSniper").setCreativeTab(ZombieCraftMod.tabBlock);
        itemFlamethrower = (new ItemGunFlamethrower(itemIndexID++)).setUnlocalizedName("ZombieCraft:guns/itemGunFlamethrower").setCreativeTab(ZombieCraftMod.tabBlock);
        
        itemM1911 = (new ItemGunM1911(itemIndexID++)).setUnlocalizedName("ZombieCraft:guns/itemGunM1911").setCreativeTab(ZombieCraftMod.tabBlock);
        itemRifle = (new ItemGunRifle(itemIndexID++)).setUnlocalizedName("ZombieCraft:guns/itemGunRifle").setCreativeTab(ZombieCraftMod.tabBlock);
        itemUzi = (new ItemGunUzi(itemIndexID++)).setUnlocalizedName("ZombieCraft:guns/itemGunUzi").setCreativeTab(ZombieCraftMod.tabBlock);
        itemRaygun = (new ItemGunRaygun(itemIndexID++)).setUnlocalizedName("ZombieCraft:guns/itemGunRaygun").setCreativeTab(ZombieCraftMod.tabBlock);
        itemRPG = (new ItemGunRPG(itemIndexID++)).setUnlocalizedName("ZombieCraft:guns/itemGunRPG").setCreativeTab(ZombieCraftMod.tabBlock);
        itemChickenGun = (new ItemGunChickenGun(itemIndexID++)).setUnlocalizedName("ZombieCraft:guns/itemGunChickenGun").setCreativeTab(ZombieCraftMod.tabBlock);
        
        //itemGrenade = (new ItemGrenade(itemGrenadeID)).setIconIndex(itemGrenadeTexID).setUnlocalizedName("itemGrenade").setCreativeTab(ZombieCraftMod.tabBlock);
        //itemGrenadeStun = (new ItemGrenadeStun(itemGrenadeStunID)).setIconIndex(itemGrenadeStunTexID).setUnlocalizedName("itemGrenadeStun").setCreativeTab(ZombieCraftMod.tabBlock);
        
        itemPerkSpeed = (new ItemAbility(itemIndexID++, abilityIndexID++))/*.setIconCoord(15, 14)*/.setUnlocalizedName("ZombieCraft:itemPerkSpeed").setCreativeTab(ZombieCraftMod.tabBlock);
        itemPerkExStatic = (new ItemAbility(itemIndexID++, abilityIndexID++))/*.setIconCoord(15, 14)*/.setUnlocalizedName("ZombieCraft:itemPerkExStatic").setCreativeTab(ZombieCraftMod.tabBlock);
        itemPerkJugg = (new ItemAbility(itemIndexID++, abilityIndexID++))/*.setIconCoord(15, 14)*/.setUnlocalizedName("ZombieCraft:itemPerkJugg").setCreativeTab(ZombieCraftMod.tabBlock);
        itemPerkCharge = (new ItemAbility(itemIndexID++, abilityIndexID++))/*.setIconCoord(15, 14)*/.setUnlocalizedName("ZombieCraft:itemPerkCharge").setCreativeTab(ZombieCraftMod.tabBlock);
        itemPerkComrade = (new ItemAbility(itemIndexID++, abilityIndexID++))/*.setIconCoord(15, 14)*/.setUnlocalizedName("ZombieCraft:itemPerkComrade").setCreativeTab(ZombieCraftMod.tabBlock);
        
        itemPickupDoublePoints = (new ItemAbility(itemIndexID++, abilityIndexID++, true)).setUnlocalizedName("ZombieCraft:doublepoints").setCreativeTab(ZombieCraftMod.tabBlock);
        itemPickupInstaKill = (new ItemAbility(itemIndexID++, abilityIndexID++, true)).setUnlocalizedName("ZombieCraft:instakill").setCreativeTab(ZombieCraftMod.tabBlock);
        itemPickupMaxAmmo = (new ItemAbility(itemIndexID++, abilityIndexID++, true)).setUnlocalizedName("ZombieCraft:maxammo").setCreativeTab(ZombieCraftMod.tabBlock);
        itemPickupNuke = (new ItemAbility(itemIndexID++, abilityIndexID++, true)).setUnlocalizedName("ZombieCraft:nuke").setCreativeTab(ZombieCraftMod.tabBlock);
        
        
        barricadePlaceable = (new ItemBarricadePlaceable(itemIndexID++, Material.wood))/*.setIconCoord(11, 2)*/.setUnlocalizedName("ZombieCraft:barricadePlaceable").setCreativeTab(ZombieCraftMod.tabBlock);
        
        ModLoader.addName(itemSword, "Sword");
        ModLoader.addName(itemDEagle, "Desert Eagle");
        ModLoader.addName(itemAk47, "AK 47");
        ModLoader.addName(itemShotgun, "Shotgun");
        ModLoader.addName(itemM4, "M4");
        ModLoader.addName(itemSniper, "Sniper Rifle");
        ModLoader.addName(itemFlamethrower, "Flamethrower");
        
        ModLoader.addName(itemM1911, "M1911");
        ModLoader.addName(itemRifle, "Rifle");
        ModLoader.addName(itemUzi, "Uzi");
        ModLoader.addName(itemRaygun, "Raygun");
        ModLoader.addName(itemRPG, "RPG");
        ModLoader.addName(itemChickenGun, "Chicken Gun");
        
        //ModLoader.addName(itemGrenade, "Grenade");
        //ModLoader.addName(itemGrenadeStun, "Stun Grenade");
        
        ModLoader.addName(itemPerkSpeed, "Speed Cola");
        ModLoader.addName(itemPerkExStatic, "ExStatic");
        ModLoader.addName(itemPerkJugg, "Juggernog");
        ModLoader.addName(itemPerkCharge, "Charge");
        ModLoader.addName(itemPerkComrade, "Comrade");
        ModLoader.addName(itemPickupDoublePoints, "Double Points");
        ModLoader.addName(itemPickupInstaKill, "Insta Kill");
        ModLoader.addName(itemPickupMaxAmmo, "Refill Ammo");
        ModLoader.addName(itemPickupNuke, "Nuke");
        
        ModLoader.addName(ZCItems.barricade,"Barricade");
        ModLoader.addName(ZCItems.barricadePlaceable,"Placeable Barricade");
    	ModLoader.addName(ZCItems.editTool,"ZC Editor Tool");
        //Items //
		
		//buildTool = (new ItemBuildTool(z_ItemIDStart++, 0)).setIconCoord(5, 5).setUnlocalizedName("buildTool").setCreativeTab(ZombieCraftMod.tabBlock);
	}
}
