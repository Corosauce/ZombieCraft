package zombiecraft.Core.Items;

import zombiecraft.Core.EnumAmmo;

public class ItemGunM4 extends ItemGun
{
    public ItemGunM4()
    {
        super();
        this.firingSound = "zc.gun.m4_s";
        this.numBullets = 1;
        //this.burstShots = 2;
        this.damage = 7;
        this.muzzleVelocity = 4.0F;
        this.spread = 0.5F;
        this.useDelay = 3;
        this.recoil = 2F;
        magSize = 48;
        hitCount = 1;
        ammoType = EnumAmmo.AUTORIFLE;
    }
}
