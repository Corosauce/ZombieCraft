package zombiecraft.Client.gui.tiles;

import net.minecraft.entity.player.InventoryPlayer;
import zombiecraft.Core.Blocks.TileEntityMysteryBox;

public class ContainerTileMysteryBox extends ContainerTileBase {

	protected TileEntityMysteryBox tileEntity;

    public ContainerTileMysteryBox (InventoryPlayer inventoryPlayer, TileEntityMysteryBox te){
    	super(inventoryPlayer);
        tileEntity = te;
    }
}
