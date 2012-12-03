package zombiecraft.Forge;

import zombiecraft.Core.Buyables;
import net.minecraft.src.*;

public class ZCPotionJugg extends Potion
{
	
	public int tickCount = 0;
	
    public ZCPotionJugg(int par1, boolean par2, int par3)
    {
        super(par1, par2, par3);
    }

    /**
     * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
     */
    public boolean isInstant()
    {
        return false;
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    public boolean isReady(int par1, int par2)
    {
        return par1 >= 1;
    }
    
    @Override
    public void performEffect(EntityLiving par1EntityLiving, int par2)
    {
        super.performEffect(par1EntityLiving, par2);
        
        if (!par1EntityLiving.worldObj.isRemote) {
	        if (par1EntityLiving instanceof EntityPlayer) {
	        	
	        	tickCount++;
	        	EntityPlayer ent = (EntityPlayer)par1EntityLiving;
	        	
	        	System.out.println(tickCount);
	        	
	        	if (tickCount + 20 < Buyables.perkLengthJugg) {
		        	if (ent.inventory.armorInventory[2] == null) {
		        		ent.inventory.armorInventory[2] = new ItemStack(Item.plateSteel);
		        	}
	        	} else {
	        		ent.inventory.armorInventory[2] = null;
	        		tickCount = 0;
	        	}
	        }
        }
        
        
    }

}
