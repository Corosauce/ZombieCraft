package zombiecraft.Core.Items;

import zombiecraft.Core.EnumAmmo;
import zombiecraft.Core.Entities.Projectiles.EntityBullet;
import net.minecraft.src.*;

public class ItemGunAk47 extends ItemGun
{
    

	public ItemGunAk47(int var1)
    {
        super(var1);
        this.firingSound = "zc.gun.ak";
        //this.requiredBullet = ZombieCraftMod.itemBulletLight;
        this.numBullets = 1;
        this.damage = 5;
        //this.muzzleVelocity = 4.0F;
        this.muzzleVelocity = 4F;
        this.spread = 0.5F;
        this.useDelay = 2;
        this.recoil = 2.0F;
        magSize = 48;
        ammoType = EnumAmmo.AUTORIFLE;
    }
}
