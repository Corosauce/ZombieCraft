package zombiecraft.Core.Items;

import zombiecraft.Core.EnumAmmo;
import zombiecraft.Core.Entities.Projectiles.EntityBullet;
import zombiecraft.Core.Entities.Projectiles.EntityBulletFlame;
import net.minecraft.src.*;

public class ItemGunFlamethrower extends ItemGun
{
    public ItemGunFlamethrower(int var1)
    {
        super(var1);
        this.firingSound = "zc.gun.flamethrower";
        //this.requiredBullet = mod_ZCSdkGuns.itemOil;
        this.numBullets = 1;
        this.damage = 5;
        this.muzzleVelocity = 0.75F;
        this.spread = 0.0F;
        this.useDelay = 1;
        this.recoil = 0.0F;
        magSize = 64;
        ammoType = EnumAmmo.FUEL;
        //this.soundDelay = 12;
        //this.soundRangeFactor = 2.0F;
    }
    
    public EntityBullet getBulletEntity(World var1, Entity var2, float var3, float var4, float var5, float var6, float var7)
    {
        return new EntityBulletFlame(var1, var2, this, var3, var4, var5, var6, var7);
    }
}
