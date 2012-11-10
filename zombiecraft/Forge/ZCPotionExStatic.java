package zombiecraft.Forge;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Potion;

public class ZCPotionExStatic extends Potion
{
    public ZCPotionExStatic(int par1, boolean par2, int par3)
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
	        AxisAlignedBB var3 = par1EntityLiving.boundingBox.expand(1.0D, 2.0D, 1.0D);
	        List var4 = par1EntityLiving.worldObj.getEntitiesWithinAABB(EntityLiving.class, var3);
	
	        if (var4 != null && !var4.isEmpty())
	        {
	        	for (int i = 0; i < var4.size(); i++) {
	        		EntityLiving ent = (EntityLiving)var4.get(i);
	        		
	        		if (ent != par1EntityLiving) {
	        			ent.knockBack(par1EntityLiving, 0, par1EntityLiving.posX - ent.posX, par1EntityLiving.posZ - ent.posZ);
	        		}
	        	}
	        }
        }
        
        //par1EntityLiving.motionX *= 1.4F;
        //par1EntityLiving.motionZ *= 1.4F;
        
        //System.out.println("hmm");
    }

}
