package zombiecraft.Forge;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.Potion;

public class ZCPotionSpeed extends Potion
{
    public ZCPotionSpeed(int par1, boolean par2, int par3)
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
        
        par1EntityLiving.motionX *= 1.4F;
        par1EntityLiving.motionZ *= 1.4F;
        
        System.out.println("hmm");
    }

}
