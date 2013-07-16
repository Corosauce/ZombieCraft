package zombiecraft.Core.Items;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import zombiecraft.Core.EnumAmmo;
import zombiecraft.Core.Entities.Projectiles.EntityBullet;
import zombiecraft.Core.Entities.Projectiles.EntityBulletMissle;

public class ItemGunRPG extends ItemGun
{
    

	public ItemGunRPG(int var1)
    {
        super(var1);
        this.firingSound = "zc.gun.rpg";
        //this.requiredBullet = ZombieCraftMod.itemBulletLight;
        this.numBullets = 1;
        this.damage = 100;
        //this.muzzleVelocity = 4.0F;
        this.muzzleVelocity = 4F;
        this.spread = 1F;
        this.useDelay = 40;
        this.recoil = 2.0F;
        //magSize = 8;
        ammoType = EnumAmmo.ROCKET;
    }
    
    public EntityBullet getBulletEntity(World var1, Entity var2, float var3, float var4, float var5, float var6, float var7)
    {
        return new EntityBulletMissle(var1, var2, this, var3, var4, var5, var6, var7);
    }
	
	
}
