package zombiecraft.Core.Items;

import zombiecraft.Core.EnumAmmo;

public class ItemGunSniper extends ItemGun
{
    public ItemGunSniper(int var1)
    {
        super(var1);
        this.firingSound = "zc.gun.sniper";
        //this.requiredBullet = mod_ZCSdkGuns.itemBulletHeavy;
        this.numBullets = 1;
        this.damage = 30;
        this.muzzleVelocity = 8.0F;
        this.spread = 0.0F;
        this.useDelay = 20;
        this.recoil = 8.0F;
        ammoType = EnumAmmo.SNIPER;
        hitCount = 50;
        //this.soundRangeFactor = 8.0F;
    }
}
