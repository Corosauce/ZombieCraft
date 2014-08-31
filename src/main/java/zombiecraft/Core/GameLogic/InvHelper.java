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
	
	public static int getOptimalBuySlotOld(EntityPlayer player, InventoryPlayer inventory, ItemStack item) {
		
		Item buyItem = null;
		if (item != null) {
			buyItem = item.getItem();
		}
		
    	/*System.out.print("buyItem: ");
    	System.out.println(buyItem);
    	
    	System.out.print("buyBlock: ");
    	System.out.println(buyBlock);*/
		
		inventoryRef = inventory;
    	
    	//if (buyItem == mod_ZombieCraft.frienddrink || buyItem == mod_ZombieCraft.jugg || buyItem == mod_ZombieCraft.speed || buyItem instanceof ItemAbility) {
    		//return 8; //doesnt matter, item to get auto used
    	//}
    	
    	ItemStack tStack = null;
    	Item itemInSlot = null;
    	
		/*if (buyBlock == mod_ZombieCraft.betty || buyItem instanceof ZCSdkItemGrenadeStun) {
			tStack = inventory.getStackInSlot(2);
    		if (tStack == null) {
    			return 2;
    		}
    		tStack = inventory.getStackInSlot(3);
    		if (tStack == null) {
    			return 3;
    		}
    		tStack = inventory.getStackInSlot(2);
    		if (tStack != null) {
    			if (tStack.stackSize < 4) { 
    				return 2;
    			}
    		}
    		tStack = inventory.getStackInSlot(3);
    		if (tStack != null) {
    			if (tStack.stackSize < 4) { 
    				return 3;
    			}
    		}
    		return -1;
    	}*/
		
		
		
		if (buyItem instanceof ItemSword/* || buyItem instanceof ItemLSword*/) {
			tStack = inventory.getStackInSlot(0);
    		if (tStack == null || inventory.currentItem == 0) {
    			return 0;
    		}
    		tStack = inventory.getStackInSlot(1);
    		if (tStack == null || inventory.currentItem == 1) {
    			return 1;
    		}
    		
    		return -1;
    	}
		
		if (buyItem instanceof ItemGun) {
			
			tStack = inventory.getStackInSlot(0);
			if (tStack != null) {
				if (tStack.getItem() instanceof ItemGun) {
					firstGun = (ItemGun)tStack.getItem();
				}
			}
			tStack = inventory.getStackInSlot(1);
			if (tStack != null) {
				if (tStack.getItem() instanceof ItemGun) {
					secondGun = (ItemGun)tStack.getItem();
				}
			}
		}
		
		//
		tStack = inventory.getStackInSlot(0);
		if (tStack == null || emptyOrDiffGun(player, inventory,buyItem,0)) {
			return 0;
		}
		//if (((SdkItemGun)itemInSlot).requiredBullet.shiftedIndex)
		tStack = inventory.getStackInSlot(1);
		if (tStack == null || emptyOrDiffGun(player, inventory,buyItem,1)) {
			return 1;
		}
		
		//now do a full check
		/*tStack = inventory.getStackInSlot(0);
		if (tStack == null || emptyOrDiffGun(inventory,buyItem,0)) {
			return 0;
		}*/
		
			
		
		/*tStack = inventory.getStackInSlot(inventory.currentItem);
		if (tStack != null) {
    		itemInSlot = tStack.getItem();
    		if (!(itemInSlot.shiftedIndex == itemtype.shiftedIndex)) {
    			//tryID = mc.thePlayer.inventory.currentItem;
    		}
		}*/
		
		if (buyItem instanceof ItemGun) {
			tStack = inventory.getStackInSlot(0);
			if (tStack != null) {
				if (tStack.getItem() instanceof ItemGun) {
					firstGun = (ItemGun)tStack.getItem();
				}
			}
			tStack = inventory.getStackInSlot(1);
			if (tStack != null) {
				if (tStack.getItem() instanceof ItemGun) {
					secondGun = (ItemGun)tStack.getItem();
				}
			}
			
	    	//find gun without ammo
	    	for (int j = 2+27; j < 9+27; j++) {
	    		tStack = inventory.getStackInSlot(j);
	    		if (tStack != null) {
		    		itemInSlot = tStack.getItem();
		    		
		    		if (itemInSlot == null && (firstGun == buyItem || secondGun == buyItem)) {
		    			return j-27;
		    		}
		    		
		    		if (firstGun == buyItem) {
		    			/*if (itemInSlot.shiftedIndex == ((ItemGun)buyItem).requiredBullet.shiftedIndex) {
		    				continue;
		    			}*/
		    		}
		    		
		    		if (secondGun == buyItem) {
		    			/*if (itemInSlot.shiftedIndex == ((ItemGun)buyItem).requiredBullet.shiftedIndex) {
		    				continue;
		    			}*/
		    		}
		    		
		    		
		    		
		    		/*if (itemInSlot instanceof SdkItemGun)
		    		{
		    			int bulletID = ((SdkItemGun)itemInSlot).requiredBullet.shiftedIndex;
			    		if (tStack != null) {
			    			//if this gun has no ammo (above it)
			    			if (getSlotAboveItem(inventory, bulletID, j) == -1) {
			    				return j;
			    			}
			    		}
		    		}*/
	    		} else {
	    			if ((firstGun == buyItem || secondGun == buyItem)) {
		    			return j-27;
		    		}
	    		}
	    	}
		}
		
		if (inventory.currentItem < 2) {
			return inventory.currentItem;
		}
    	
    	//find empty slot instead
    	/*for (int j = 0; j < 9; j++) {
    		tStack = inventory.getStackInSlot(j);
    		if (tStack == null) {
    			return j;
    		}
    	}*/
    	
    	return -1;
		
    }
    
    public static boolean emptyOrDiffGun(EntityPlayer player, InventoryPlayer inventory, Item buyItem, int slot) {
    	
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
        			/*ItemStack ammoStack = inventory.getStackInSlot();
	        		Item ammoInSlot = ammoStack.getItem();
	        		if (ammoStack != null && ammoInSlot != null) {
		        		if (ammoInSlot.shiftedIndex == ((SdkItemGun)gunInSlot).requiredBullet.shiftedIndex) {
		        			return false;
		        		}
	        		} else {
	        			return true;
	        		}*/
        		} else { return true; }
    		} else {
    			return true;
    		}
    		
    		
    	} else {
    		return false;
    	}
    	
    	//return false;
    }
    
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
