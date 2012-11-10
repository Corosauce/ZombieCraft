package zombiecraft.Core.Dimension;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.EntityBat;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.SpawnListEntry;

public class ZCBiomeGen extends BiomeGenBase
{
	public static final ZCBiomeGen base = (ZCBiomeGen) (new ZCBiomeGen(76)).setBiomeName("ZCRealm");
	
    public ZCBiomeGen(int par1)
    {
        super(par1);
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        
        this.spawnableCreatureList.add(new SpawnListEntry(EntityBat.class, 100, 10, 10));
    }
}
