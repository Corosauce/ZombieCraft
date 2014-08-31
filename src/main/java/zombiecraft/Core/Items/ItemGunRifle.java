package zombiecraft.Core.Items;

import zombiecraft.Core.EnumAmmo;

public class ItemGunRifle extends ItemGun
{
    

	public ItemGunRifle()
    {
        super();
        this.firingSound = "zc.gun.rifle";
        //this.requiredBullet = ZombieCraftMod.itemBulletLight;
        this.numBullets = 1;
        this.damage = 12;
        //this.muzzleVelocity = 4.0F;
        this.muzzleVelocity = 4F;
        this.spread = 1F;
        this.useDelay = 10;
        this.recoil = 3.0F;
        //magSize = 8;
        ammoType = EnumAmmo.RIFLE;
    }
}
