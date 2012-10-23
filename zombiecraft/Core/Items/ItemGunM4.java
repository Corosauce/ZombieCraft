package zombiecraft.Core.Items;

import zombiecraft.Core.EnumAmmo;
import zombiecraft.Core.Entities.EntityBullet;
import net.minecraft.src.*;

public class ItemGunM4 extends ItemGun
{
    public ItemGunM4(int var1)
    {
        super(var1);
        this.firingSound = "sdk.m";
        this.numBullets = 1;
        //this.burstShots = 2;
        this.damage = 7;
        this.muzzleVelocity = 4.0F;
        this.spread = 0.5F;
        this.useDelay = 5;
        this.recoil = 1.0F;
        magSize = 48;
        ammoType = EnumAmmo.AUTORIFLE;
    }
}
