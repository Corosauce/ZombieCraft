package zombiecraft.Core.Items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zombiecraft.Core.Entities.Projectiles.EntityGrenade;

public class ItemGrenade extends ItemGun
{
    public ItemGrenade(int var1)
    {
        super(var1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        --var1.stackSize;
        var2.playSoundAtEntity(var3, "zc.gun.grunt", 1.0F, 1.0F / (itemRand.nextFloat() * 0.1F + 0.95F));

        if (!var2.isRemote)
        {
            var2.spawnEntityInWorld(new EntityGrenade(var2, var3));
        }

        return var1;
    }
}
