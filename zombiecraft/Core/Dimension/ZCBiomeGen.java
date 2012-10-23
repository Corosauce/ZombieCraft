package zombiecraft.Core.Dimension;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.SpawnListEntry;

public class ZCBiomeGen extends BiomeGenBase
{
	public static final ZCBiomeGen base = (ZCBiomeGen) (new ZCBiomeGen(76)).setBiomeName("ZCRealm");
	
    public ZCBiomeGen(int par1)
    {
        super(par1);
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 100, 10, 10));
    }
}
