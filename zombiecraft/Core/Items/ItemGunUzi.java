package zombiecraft.Core.Items;

import zombiecraft.Core.EnumAmmo;

public class ItemGunUzi extends ItemGun
{
    

	public ItemGunUzi(int var1)
    {
        super(var1);
        this.firingSound = "zc.gun.uzi";
        //this.requiredBullet = ZombieCraftMod.itemBulletLight;
        this.numBullets = 1;
        this.damage = 3;
        //this.muzzleVelocity = 4.0F;
        this.muzzleVelocity = 4F;
        this.spread = 5F;
        this.useDelay = 1;
        this.recoil = 2.0F;
        magSize = 72;
        ammoType = EnumAmmo.UZI;
    }
}
