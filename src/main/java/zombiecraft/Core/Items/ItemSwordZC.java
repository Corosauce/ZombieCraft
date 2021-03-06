package zombiecraft.Core.Items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;


public class ItemSwordZC extends ItemSword {

	public ItemSwordZC(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial);
	}
	
	public IIcon getIconFromDamage(int par1) {
    	return Items.iron_sword.getIconFromDamage(0);
    }
	
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
        //par1ItemStack.damageItem(1, par3EntityLivingBase);
		par2EntityLivingBase.worldObj.playSoundAtEntity(par2EntityLivingBase, "zc.meleehit", 1.0F, 1.0F);
        return true;
    }
	
	public int getDamageVsEntity(Entity par1Entity)
    {
		if (par1Entity instanceof EntityPlayer) return 0;
        return 10;
    }

}
