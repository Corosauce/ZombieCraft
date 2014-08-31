package zombiecraft.Client.gui.tiles;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import zombiecraft.Core.Blocks.TileEntityMysteryBox;

public class ContainerTileMysteryBox extends ContainerTileBase {

	protected TileEntityMysteryBox tileEntity;

    public ContainerTileMysteryBox (InventoryPlayer inventoryPlayer, TileEntityMysteryBox te){
    	super(inventoryPlayer);
        tileEntity = te;
        //addSlotToContainer(new Slot(te.invHandler, 0, 8, 17));
        
        int startX = 8;
        int startY = 80;
        
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
            	int slot = x + y * 9;
                addSlotToContainer(new Slot(te.invHandler, slot, startX + x * 18, startY + y * 18));
                //System.out.println("x + y * 3: " + slot);
            }
        }

        bindPlayerInventory(inventoryPlayer, startX, startY + (18*4) + 16);
    }
}
