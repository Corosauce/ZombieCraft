package zombiecraft.Core.Blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMobSpawnerOpen extends BlockMobSpawnerWave {

	public BlockMobSpawnerOpen(int par1) {
		super(par1);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityMobSpawnerWave();
    }

}
