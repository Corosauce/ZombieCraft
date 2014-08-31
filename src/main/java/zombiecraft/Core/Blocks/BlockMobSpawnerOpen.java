package zombiecraft.Core.Blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMobSpawnerOpen extends BlockMobSpawnerWave {

	public BlockMobSpawnerOpen() {
		super();
	}
	
	@Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityMobSpawnerWave();
    }

}
