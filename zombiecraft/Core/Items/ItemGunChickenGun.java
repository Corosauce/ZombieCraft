package zombiecraft.Core.Items;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import zombiecraft.Core.EnumAmmo;
import zombiecraft.Core.Entities.EntityChickenDropless;

public class ItemGunChickenGun extends ItemGun
{
    

	public ItemGunChickenGun(int var1)
    {
        super(var1);
        this.firingSound = "mob.chickenhurt";
        //this.requiredBullet = ZombieCraftMod.itemBulletLight;
        this.numBullets = 1;
        this.damage = 8;
        //this.muzzleVelocity = 4.0F;
        this.muzzleVelocity = 4F;
        this.spread = 1F;
        this.useDelay = 15;
        this.recoil = 2.0F;
        //magSize = 8;
        ammoType = EnumAmmo.CHICKEN;
    }
	
	@Override
	public void fireGun(World world, EntityPlayer entP) {
    	for (int i = 0; i < this.numBullets; i++) {
    		
    		EntityChicken entitychicken = new EntityChickenDropless(world);
	        entitychicken.setLocationAndAngles(entP.posX, entP.posY+0.8, entP.posZ, entP.rotationYaw, 0.0F);
	        
	        float f = 1.4F;
	        entitychicken.motionX = -MathHelper.sin((entP.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((entP.rotationPitch / 180F) * 3.141593F) * f;
	        entitychicken.motionZ = MathHelper.cos((entP.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((entP.rotationPitch / 180F) * 3.141593F) * f;
	        entitychicken.motionY = -MathHelper.sin((entP.rotationPitch / 180F) * 3.141593F) * f;
    		
        	world.spawnEntityInWorld(entitychicken);
    	}
    }
}
