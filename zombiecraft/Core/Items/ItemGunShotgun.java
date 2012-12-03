package zombiecraft.Core.Items;

import zombiecraft.Core.EnumAmmo;
import zombiecraft.Core.Entities.Projectiles.EntityBullet;
import net.minecraft.src.*;

public class ItemGunShotgun extends ItemGun
{
    public ItemGunShotgun(int var1)
    {
        super(var1);
        this.firingSound = "zc.gun.shotgun";
        this.numBullets = 12;
        reloadTime = 20 * (numBullets / 2);
        this.damage = 3;
        this.muzzleVelocity = 3.0F;
        this.spread = 14.0F;
        this.useDelay = 25;
        this.recoil = 8.0F;
        ammoType = EnumAmmo.SHELL;
    }
}
