package zombiecraft.Core.Items;

import zombiecraft.Core.ZCBlocks;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZombieCraftMod;
import net.minecraft.src.*;

public class ItemPerk extends Item {
	
	public int potionID;
	public int potionLength;
    
	public ItemPerk(int par1, int parPotionID)
    {
        this(par1, parPotionID, 30 * 20);
    }
	
    public ItemPerk(int par1, int parPotionID, int length)
    {
        super(par1);
        this.maxStackSize = 1;
        potionID = parPotionID;
        potionLength = length;
    }
    
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	if (!par2World.isRemote) {
    		par3EntityPlayer.addPotionEffect(new PotionEffect(potionID, potionLength, 1));
    	}
    	
        return par1ItemStack;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int i, int j, int k, int par7)
    {
    	return true;
    }
    
    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    	
    }
}
