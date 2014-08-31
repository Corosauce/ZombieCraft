package zombiecraft.Client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerSession extends Container {

	//protected TileEntitySession tileEntity;
	
	public int lastEUAmount = 0;
	public int lastShootTicksToCharge = 0;
	public int lastShootTicksBetweenShots = 0;
	public int lastShootRange = 0;
	public boolean lastMeleeRightClick = false;

    public ContainerSession (InventoryPlayer inventoryPlayer) {
    	
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;//tileEntity.isUseableByPlayer(player);
    }
    
    //Overrides for handling custom data syncing
    
    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
        	//this.tileEntity.powerEU = par2*1000;
        }
    }
}
