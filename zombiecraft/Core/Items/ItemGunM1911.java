package zombiecraft.Core.Items;

import zombiecraft.Core.EnumAmmo;

public class ItemGunM1911 extends ItemGun
{
    public ItemGunM1911(int var1)
    {
        super(var1);
        this.firingSound = "zc.gun.m1911_s";
        //this.requiredBullet = mod_ZCSdkGuns.itemBulletMedium;
        this.numBullets = 1;
        this.damage = 5;
        this.muzzleVelocity = 4.0F;
        this.spread = 0.5F;
        this.useDelay = 5;
        this.recoil = 2.0F;
        ammoType = EnumAmmo.PISTOL;
    }
}
