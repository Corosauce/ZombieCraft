package zombiecraft.Core.Items;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemSword;


public class ItemSwordZC extends ItemSword {

	public ItemSwordZC(int par1, EnumToolMaterial par2EnumToolMaterial) {
		super(par1, par2EnumToolMaterial);
		// TODO Auto-generated constructor stub
	}
	
	public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving)
    {
        //par1ItemStack.damageItem(1, par3EntityLiving);
        return true;
    }
	
	public int getDamageVsEntity(Entity par1Entity)
    {
		if (par1Entity instanceof EntityPlayer) return 0;
        return 5;
    }

}
