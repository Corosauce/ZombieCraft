package zombiecraft.Core.GameLogic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Items.ItemAbility;
import zombiecraft.Core.Items.ItemGun;

public class InvHelper {
	
	static ItemGun firstGun;
	static ItemGun secondGun;
	
	static InventoryPlayer inventoryRef;
	
	//returns: -1 = no go, -2 = dont give item, just use it (possible?) - use for guns too to just give ammo, rest = slot number to place in
	public static int getOptimalBuySlot(EntityPlayer player, InventoryPlayer inventory, ItemStack item) {
		inventoryRef = inventory;
		Item buyItem = null;
		if (item != null) {
			buyItem = item.getItem();
		}
		
		if (buyItem != null) {
			if (buyItem instanceof ItemGun) {
				int slot = getInventorySlotContainItem(item.getItem());
				
				if (slot > -1) {
					return -2;
				} else {
					int emtpySlot = getEmptySlot();
					
					if (emtpySlot > -1) {
						return emtpySlot;
					}
				}
			} else if (buyItem instanceof ItemAbility) {
				return -2;
			} else {
				return getEmptyOrStackableSlot(buyItem); //getItemStackLimit
			}
		}
		
		return -1;
	}
	
	private static int getEmptyOrStackableSlot(Item buyItem)
    {
    	
        for(int j = 0; j < 9; j++)
        {
            if(inventoryRef.mainInventory[j] == null || inventoryRef.mainInventory[j].stackSize < inventoryRef.mainInventory[j].getItem().getItemStackLimit())
            {
                return j;
            }
        }

        return -1;
    }
    
    /*public static boolean emptyOrDiffGun(EntityPlayer player, InventoryPlayer inventory, Item buyItem, int slot) {
    	
    	ItemStack gunStack = inventory.getStackInSlot(slot);
    	
		
		
    	if (gunStack == null) return true;
    	
    	if (gunStack.getItem() instanceof ItemGun) {
    		
    		if (gunStack != null) {
        		Item gunInSlot = gunStack.getItem();
        		if (gunInSlot != null) {
        			
        			//if diff gun and selected, and not in other gun slot
        			if (buyItem != gunInSlot && inventory.currentItem == slot && ((slot == 0 && buyItem != secondGun && secondGun != null) || (slot == 1 && buyItem != firstGun && firstGun != null))) {
        				return true;
        			}
        			
        			int foundAmmo = ZCUtil.getAmmoData(player, buyItem);//getInventorySlotContainItem(((SdkItemGun)gunInSlot).requiredBullet.shiftedIndex);
        			if (foundAmmo == -1) {
        				return true;
        			} else {
        				return false;
        			}
        		} else { return true; }
    		} else {
    			return true;
    		}
    		
    		
    	} else {
    		return false;
    	}
    	
    	//return false;
    }*/
    
   /*public static int getSlotAboveItem(InventoryPlayer inventory, int i, int curSlot)
    {
    	int j = curSlot;
    	
    	j+=9;
    	
    	//if (inventory.mainInventory[j] != null) System.out.println(inventory.mainInventory[j].itemID);
    	
    	if(inventory.mainInventory[j] != null && inventory.mainInventory[j].itemID == i)
        {
            return j;
        }
    	j+=9;
    	if (inventory.mainInventory[j] != null) System.out.println(inventory.mainInventory[j].itemID);
    	if(inventory.mainInventory[j] != null && inventory.mainInventory[j].itemID == i)
        {
            return j;
        }
    	j+=9;
    	if (inventory.mainInventory[j] != null) System.out.println(inventory.mainInventory[j].itemID);
    	if(inventory.mainInventory[j] != null && inventory.mainInventory[j].itemID == i)
        {
            return j;
        }
    	
        return -1;
    }*/
    
    /*public static int getSlotAboveUsedItem(InventoryPlayer inventory, int i)
    {
    	int j = inventory.currentItem;
    	
    	j+=9;
    	if(inventory.mainInventory[j] != null && inventory.mainInventory[j].itemID == i)
        {
            return j;
        }
    	j+=9;
    	if(inventory.mainInventory[j] != null && inventory.mainInventory[j].itemID == i)
        {
            return j;
        }
    	j+=9;
    	if(inventory.mainInventory[j] != null && inventory.mainInventory[j].itemID == i)
        {
            return j;
        }
    	
        return -1;
    }*/
    
    private static int getInventorySlotContainItem(Item i)
    {
    	
    	for(int j = 0; j < 9; j++)
        {
    		for (int k = 0; k < 4; k++) {
    			int ii = j + (k*9);
    			if(inventoryRef.mainInventory[ii] != null && inventoryRef.mainInventory[ii].getItem() == i)
	            {
	                return ii;
	            }
    		}
        }
    	
        /*for(int j = 0; j < mainInventory.length; j++)
        {
            if(mainInventory[j] != null && mainInventory[j].itemID == i)
            {
                return j;
            }
        }*/

        return -1;
    }
    
    private static int getEmptySlot()
    {
    	
    	/*for(int j = 0; j < 9; j++)
        {
    		for (int k = 0; k < 4; k++) {
    			int ii = j + (k*9);
    			if(inventoryRef.mainInventory[ii] == null)
	            {
	                return ii;
	            }
    		}
        }*/
    	
        for(int j = 0; j < 9; j++)
        {
            if(inventoryRef.mainInventory[j] == null)
            {
                return j;
            }
        }

        return -1;
    }
	
}
