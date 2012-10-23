package zombiecraft.Core.Items;

import zombiecraft.Core.EnumAmmo;
import zombiecraft.Core.Entities.EntityBullet;
import net.minecraft.src.*;

public class ItemGunSniper extends ItemGun
{
    public ItemGunSniper(int var1)
    {
        super(var1);
        this.firingSound = "sdk.sniper";
        //this.requiredBullet = mod_ZCSdkGuns.itemBulletHeavy;
        this.numBullets = 1;
        this.damage = 12;
        this.muzzleVelocity = 8.0F;
        this.spread = 0.0F;
        this.useDelay = 20;
        this.recoil = 8.0F;
        ammoType = EnumAmmo.RIFLE;
        //this.soundRangeFactor = 8.0F;
    }
}
