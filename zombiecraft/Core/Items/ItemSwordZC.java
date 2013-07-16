package zombiecraft.Core.Items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.Icon;


public class ItemSwordZC extends ItemSword {

	public ItemSwordZC(int par1, EnumToolMaterial par2EnumToolMaterial) {
		super(par1, par2EnumToolMaterial);
		// TODO Auto-generated constructor stub
	}
	
	public Icon getIconFromDamage(int par1) {
    	return Item.swordIron.getIconFromDamage(0);
    }
	
	public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving)
    {
        //par1ItemStack.damageItem(1, par3EntityLiving);
		par2EntityLiving.worldObj.playSoundAtEntity(par2EntityLiving, "zc.meleehit", 1.0F, 1.0F);
        return true;
    }
	
	public int getDamageVsEntity(Entity par1Entity)
    {
		if (par1Entity instanceof EntityPlayer) return 0;
        return 10;
    }

}
