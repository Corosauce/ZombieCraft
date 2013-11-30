package zombiecraft.Core.Items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPickup extends Item {
	
	public int pickupID;
    
	public ItemPickup(int par1, int parPotionID)
    {
		super(par1);
        this.maxStackSize = 1;
        pickupID = parPotionID;
    }
    
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	
    	//System.out.println("Pickup used");
    	
    	if (!par2World.isRemote) {
    		if (pickupID == 0) {
    			//nuke
    		} else if (pickupID == 1) {
    			//insta kill
    		} else if (pickupID == 2) {
    			//double points
    		} else if (pickupID == 3) {
    			//refil ammo
    		}
    	} else {
    		if (pickupID == 28) {
    			//ZCUtil.setData(par3EntityPlayer, DataTypes.hasCharge, 1);
    		}
    	}
    	
        return par1ItemStack;
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
    	//Automatic usage on first tick after pickup (assumed space in inventory)
    	if (par3Entity instanceof EntityPlayer && !((EntityPlayer) par3Entity).capabilities.isCreativeMode) {
    		onItemRightClick(par1ItemStack, par2World, (EntityPlayer)par3Entity);
    	} else {
    		System.out.println("doesnt support non player use");
    	}
    }
}
