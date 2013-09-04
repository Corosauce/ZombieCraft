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

    public ContainerSession (InventoryPlayer inventoryPlayer){
        //tileEntity = te;

        //the Slot constructor takes the IInventory and the slot number in that it binds to
        //and the x-y coordinates it resides on-screen
        /*for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new Slot(tileEntity, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }*/
        
        int offsetX = 126;
        
        /*addSlotToContainer(new Slot(tileEntity, 0, 8, 17));
        addSlotToContainer(new Slot(tileEntity, 1, 8, 17 + (18 * 1)));
        addSlotToContainer(new Slot(tileEntity, 2, 8, 17 + (18 * 2)));
        addSlotToContainer(new Slot(tileEntity, 3, 8 + offsetX, 17 + (18 * 0)));
        addSlotToContainer(new Slot(tileEntity, 4, 8 + offsetX, 17 + (18 * 1)));
        addSlotToContainer(new Slot(tileEntity, 5, 8 + offsetX, 17 + (18 * 2)));*/

        //commonly used vanilla code that adds the player's inventory
        //bindPlayerInventory(inventoryPlayer);
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

            /*if (this.lastEUAmount != this.tileEntity.powerEU)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.powerEU/1000);
            }*/
            
            
            
        }

        //this.lastEUAmount = this.tileEntity.powerEU;
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
