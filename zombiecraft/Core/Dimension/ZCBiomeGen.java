package zombiecraft.Core.Dimension;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;

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
        //this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 100, 1, 10));
    }
}
