package zombiecraft.Core.Items;

import zombiecraft.Core.EnumAmmo;
import zombiecraft.Core.Entities.EntityBullet;
import net.minecraft.src.*;

public class ItemGunShotgun extends ItemGun
{
    public ItemGunShotgun(int var1)
    {
        super(var1);
        this.firingSound = "sdk.shotgun";
        this.numBullets = 12;
        this.damage = 3;
        this.muzzleVelocity = 3.0F;
        this.spread = 14.0F;
        this.useDelay = 16;
        this.recoil = 8.0F;
        ammoType = EnumAmmo.SHELL;
    }
}
