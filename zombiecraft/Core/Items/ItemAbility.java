package zombiecraft.Core.Items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import zombiecraft.Core.Buyables;
import zombiecraft.Core.DataTypes;
import zombiecraft.Core.PacketTypes;
import zombiecraft.Core.ZCItems;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Entities.Comrade;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZombieCraftMod;
import CoroAI.entity.EnumJob;

public class ItemAbility extends Item {
	
	public int abilityID;
	boolean useOnPickup;
	//public int potionLength;
    
	public ItemAbility(int par1, int parPotionID)
    {
        this(par1, parPotionID, false);
    }
	
    public ItemAbility(int par1, int parPotionID, boolean parUseOnPickup)
    {
        super(par1);
        this.maxStackSize = 1;
        abilityID = parPotionID;
        useOnPickup = parUseOnPickup;
        //potionLength = length;
    }
    
    public Icon getIconFromDamage(int par1) {
    	return useOnPickup ? this.itemIcon : Item.potion.getIconFromDamage(0);
    }
    
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	if (!par2World.isRemote) {
    		ZCGame.instance().check(par3EntityPlayer);
    		if (abilityID == ((ItemAbility)ZCItems.itemPerkCharge).abilityID) {
    			
    		} else if (abilityID == ((ItemAbility)ZCItems.itemPerkComrade).abilityID) {
    			int count = (Integer)ZCUtil.getData(par3EntityPlayer, DataTypes.comrades);
    			if (count < Buyables.perkMaxComrades) {
    				ZCUtil.setData(par3EntityPlayer, DataTypes.comrades, count + 1);
    				
    				Comrade comrade = new Comrade(par2World);
    				double x = par2World.rand.nextFloat() - 0.5F;
        			double z = par2World.rand.nextFloat() - 0.5F;
        			comrade.setPosition(par3EntityPlayer.posX + x, par3EntityPlayer.posY, par3EntityPlayer.posZ + z);
        			par2World.spawnEntityInWorld(comrade);
        			comrade.agent.spawnedOrNBTReloadedInit();
    			} else {
    				//redeem points
    				ZCGame.instance().givePoints(par3EntityPlayer, Buyables.perkCostComrades, true);
    				ZCGame.instance().updateInfo(par3EntityPlayer, PacketTypes.PLAYER_POINTS, new int[] {(Integer)ZCGame.instance().getData(par3EntityPlayer, DataTypes.zcPoints)});
    			}
    			//System.out.println("spawn comrade: " + par3EntityPlayer.rotationYaw);
    			
    			//BaseEntAI comrade = new Imp(par2World);
    			
    			
    			//comrade.initJobAndStates(EnumJob.INVADER);
    		} else if (abilityID == ((ItemAbility)ZCItems.itemPerkExStatic).abilityID) {
    			ZCGame.instance().setData(par3EntityPlayer, DataTypes.exStaticCooldown, 0); //0 sets to enabled
    		} else if (abilityID == ((ItemAbility)ZCItems.itemPerkJugg).abilityID) {
    			ZCGame.instance().setData(par3EntityPlayer, DataTypes.juggTime, Buyables.perkLengthJugg);
    		} else if (abilityID == ((ItemAbility)ZCItems.itemPickupDoublePoints).abilityID) {
    			ZCGame.instance().setData(par3EntityPlayer, DataTypes.doublePointsTime, Buyables.pickupLengthDoublePoints);
    			par2World.playSoundAtEntity(par3EntityPlayer, ZombieCraftMod.modID + ":zc.doublepoints", 1.0F, 1.0F);
    		} else if (abilityID == ((ItemAbility)ZCItems.itemPickupInstaKill).abilityID) {
    			ZCGame.instance().setData(par3EntityPlayer, DataTypes.instaKillTime, Buyables.pickupLengthInstaKill);
    			par2World.playSoundAtEntity(par3EntityPlayer, ZombieCraftMod.modID + ":zc.instakill", 1.0F, 1.0F);
    		} else if (abilityID == ((ItemAbility)ZCItems.itemPickupMaxAmmo).abilityID) {
    			ZCGame.instance().refillAmmo(par3EntityPlayer);
    			ZCGame.instance().syncPlayer(par3EntityPlayer);
    			par2World.playSoundAtEntity(par3EntityPlayer, ZombieCraftMod.modID + ":" + "zc.ammo", 1.0F, 1.0F);
    		} else if (abilityID == ((ItemAbility)ZCItems.itemPickupNuke).abilityID) {
    			ZCGame.instance().nukeInvaders(par3EntityPlayer);
    			par2World.playSoundAtEntity(par3EntityPlayer, ZombieCraftMod.modID + ":zc.nuke", 1.0F, 1.0F);
    		} else {
    			//par3EntityPlayer.addPotionEffect(new PotionEffect(potionID, potionLength, 1));
    		}
    		
    		//par2World.playSoundAtEntity(par3EntityPlayer, "zc.doublepointsc", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
    	} else {
    		
    		//item perks are bought, handle on transact confirm packet
    		//pickups might get called here since they are server side picked up
    		
    	}
    	
        return null;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int i, int j, int k, int par7)
    {
    	return true;
    }
    
    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    	if (useOnPickup && par3Entity instanceof EntityPlayer) {
    		EntityPlayer entP = (EntityPlayer)par3Entity;
    		entP.inventory.mainInventory[par4] = this.onItemRightClick(par1ItemStack, par2World, entP);
    	}
    }
}
