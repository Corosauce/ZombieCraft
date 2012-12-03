package zombiecraft.Core.Items;

import zombiecraft.Core.EnumAmmo;
import zombiecraft.Core.Entities.Projectiles.EntityBullet;
import net.minecraft.src.*;

public class ItemGunDEagle extends ItemGun
{
    public ItemGunDEagle(int var1)
    {
        super(var1);
        this.firingSound = "zc.gun.deagle";
        //this.requiredBullet = mod_ZCSdkGuns.itemBulletMedium;
        this.numBullets = 1;
        this.damage = 10;
        this.muzzleVelocity = 4.0F;
        this.spread = 2.0F;
        this.useDelay = 8;
        this.recoil = 4.0F;
        ammoType = EnumAmmo.MAGNUM;
    }
}
