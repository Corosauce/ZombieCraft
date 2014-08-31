package zombiecraft.Core.Items;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import zombiecraft.Core.EnumAmmo;
import zombiecraft.Core.Entities.Projectiles.EntityBullet;
import zombiecraft.Core.Entities.Projectiles.EntityBulletLaser;

public class ItemGunRaygun extends ItemGun
{
    public ItemGunRaygun()
    {
        super();
        this.firingSound = "zc.gun.raygun";
        //this.requiredBullet = mod_ZCSdkGuns.itemOil;
        this.numBullets = 1;
        this.damage = 60;
        this.muzzleVelocity = 2F;
        this.spread = 0.0F;
        this.useDelay = 10;
        this.recoil = 1.0F;
        magSize = 32;
        ammoType = EnumAmmo.PLASMA;
        //this.soundDelay = 12;
        //this.soundRangeFactor = 2.0F;
    }
    
    public EntityBullet getBulletEntity(World var1, Entity var2, float var3, float var4, float var5, float var6, float var7)
    {
        return new EntityBulletLaser(var1, var2, this, var3, var4, var5, var6, var7);
    }
}
