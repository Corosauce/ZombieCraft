package zombiecraft.Core.Items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import zombiecraft.Core.GameLogic.ZCGame;

public class ItemBuildTool extends Item {
    
    private boolean tmp;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	
	//0 = spawn linking, 1 = level bound setting
	public int mode = 0;
	
	public int subMode = 0;
	
	public int x1 = 0;
	public int y1 = 0;
	public int z1 = 0;
	public int x2 = 0;
	public int y2 = 0;
	public int z2 = 0;

    public ItemBuildTool(int par1, int parMode)
    {
        super(par1);
        this.maxStackSize = 1;
        mode = parMode;
        xCoord = 0;
		yCoord = 0;
		zCoord = 0;
		tmp = true;
    }
    
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	int i = (int)par3EntityPlayer.posX;
    	int j = (int)par3EntityPlayer.posY-1;
    	int k = (int)par3EntityPlayer.posZ;
    	
    	
    	
    	if (mode == 0) {
    		doPlaceLoad(par3EntityPlayer, i, j, k);
    	} else if (mode == 1) {
    		
    	} else if (mode == 2) {
    		
    	}
        return par1ItemStack;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int i, int j, int k, int par7)
    {
    	if (mode == 0) {
    		doPlaceLoad(par2EntityPlayer, i, j, k);
    	} else if (mode == 1) {
    		
    		
    		
    	} else {
    	
    		//onItemRightClick(par1ItemStack, par3World, par2EntityPlayer);
    	}
    	return true;
    }
    
    public void doPlaceLoad(EntityPlayer par2EntityPlayer, int i, int j, int k) {
    	ZCGame.instance().setLevelSize(i, j, k, i, j, k);
    	//ZCGame.instance.loadLevel(mod_ZombieCraft.player);
    	ZCGame.instance().regenerateLevel(par2EntityPlayer);
    }
    
    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    	mode = 0;//ZCGame.instance.mapMan.editToolMode;
    	if (ZCGame.instance().mapMan.editMode && par5) {
    		ZCGame.instance().showEditToolMode();
    	}
    }
}
