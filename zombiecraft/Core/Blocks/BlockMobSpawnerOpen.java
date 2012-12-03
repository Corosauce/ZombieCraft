package zombiecraft.Core.Blocks;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockMobSpawnerOpen extends BlockMobSpawnerWave {

	public BlockMobSpawnerOpen(int par1, int par2) {
		super(par1, par2);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityMobSpawnerWave();
    }

}
