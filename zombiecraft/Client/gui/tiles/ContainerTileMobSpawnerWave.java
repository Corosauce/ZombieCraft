package zombiecraft.Client.gui.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerTileMobSpawnerWave extends ContainerTileBase {

	protected TileEntityMobSpawnerWave tileEntity;

    public ContainerTileMobSpawnerWave (InventoryPlayer inventoryPlayer, TileEntityMobSpawnerWave te){
    	super(inventoryPlayer);
        tileEntity = te;
    }
}
