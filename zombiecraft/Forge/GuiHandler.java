package zombiecraft.Forge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import zombiecraft.Client.gui.ContainerPacketTester;
import zombiecraft.Client.gui.ContainerSession;
import zombiecraft.Client.gui.GuiPacketTester;
import zombiecraft.Client.gui.GuiSession;
import zombiecraft.Client.gui.tiles.ContainerTileMobSpawnerWave;
import zombiecraft.Client.gui.tiles.GuiTileMobSpawnerWave;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.Blocks.TileEntityPacketTester;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	//I was going to use this class for the session block, but given the abstractness of the information it is showing, it will be a client side only gui using synced data
	//Use this for gui/containers that actually use the tile entity data
	//also work out a generic way to watch and update variables / mark for watching
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if (tileEntity instanceof TileEntityMobSpawnerWave) {
			return new ContainerTileMobSpawnerWave(player.inventory, (TileEntityMobSpawnerWave) tileEntity);
		} else if (tileEntity instanceof TileEntityPacketTester){
            return new ContainerPacketTester(player.inventory, (TileEntityPacketTester) tileEntity);
		} else if (ID == 1) {
			return new ContainerSession(player.inventory);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if (tileEntity instanceof TileEntityMobSpawnerWave) {
			return new GuiTileMobSpawnerWave(player.inventory, (TileEntityMobSpawnerWave) tileEntity);
		} else if (tileEntity instanceof TileEntityPacketTester){
            return new GuiPacketTester(player.inventory, (TileEntityPacketTester) tileEntity);
		} else if (ID == 1) {
	        return new GuiSession(player.inventory);
		}
        return null;
	}

}
