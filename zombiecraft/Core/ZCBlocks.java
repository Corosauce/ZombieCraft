package zombiecraft.Core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import zombiecraft.Core.Blocks.BlockBarricade;
import zombiecraft.Core.Blocks.BlockBarrier;
import zombiecraft.Core.Blocks.BlockMobSpawnerWave;
import zombiecraft.Core.Blocks.BlockPurchasePlate;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.Blocks.TileEntityPurchasePlate;
import zombiecraft.Forge.ZombieCraftMod;

import net.minecraft.src.*;

public class ZCBlocks {
	
	@MLProp public static int z_BlockIDStart = 190;
	
	public static Block b_mobSpawnerWave;
    public static Block b_buyBlock;
    
    public static Block barricadeS0;
	public static Block barricadeS1;
	public static Block barricadeS2;
	public static Block barricadeS3;
	public static Block barricadeS4;
	public static Block barricadeS5;
	
	public static Block barrier;
	
	public ZCBlocks() {
		
	}
	
	public static void load() {
		b_mobSpawnerWave  = (new BlockMobSpawnerWave(z_BlockIDStart++, 65)).setBlockName("z_spawnblock").setCreativeTab(ZombieCraftMod.tabBlock);
    	b_buyBlock = (new BlockPurchasePlate(z_BlockIDStart++, 65, EnumMobType.players, Material.wood)).setBlockName("z_purchaseBlock").setCreativeTab(ZombieCraftMod.tabBlock);
    	
    	int stateToBlockID[] = {z_BlockIDStart++,z_BlockIDStart++,z_BlockIDStart++,z_BlockIDStart++,z_BlockIDStart++,z_BlockIDStart++};
    	
    	
    	
    	barricadeS0 = (new BlockBarricade(stateToBlockID, ZombieCraftMod.barricadeTopTexIDs, Material.circuits, 0)).setBlockName("barricadeBroken");
		barricadeS1 = (new BlockBarricade(stateToBlockID, ZombieCraftMod.barricadeTopTexIDs, Material.circuits, 1)).setBlockName("barricadeS1");
		barricadeS2 = (new BlockBarricade(stateToBlockID, ZombieCraftMod.barricadeTopTexIDs, Material.circuits, 2)).setBlockName("barricadeS2");
		barricadeS3 = (new BlockBarricade(stateToBlockID, ZombieCraftMod.barricadeTopTexIDs, Material.circuits, 3)).setBlockName("barricadeS3");
		barricadeS4 = (new BlockBarricade(stateToBlockID, ZombieCraftMod.barricadeTopTexIDs, Material.circuits, 4)).setBlockName("barricadeS4");
		barricadeS5 = (new BlockBarricade(stateToBlockID, ZombieCraftMod.barricadeTopTexIDs, Material.circuits, 5)).setBlockName("barricade");
		//barricadeS5 = Block.doorWood;//(new BlockDoor(121, Material.wood, 5)).setHardness(3F).setStepSound(Block.soundWoodFootstep).setBlockName("doorWood");
		barrier = (new BlockBarrier(z_BlockIDStart++, 1)).setBlockName("barrier").setCreativeTab(ZombieCraftMod.tabBlock);
		
    	ModLoader.registerBlock(b_mobSpawnerWave);
    	ModLoader.registerBlock(b_buyBlock);
    	
    	ModLoader.registerBlock(barricadeS5);
    	ModLoader.registerBlock(barrier);
    	
    	ModLoader.addRecipe(new ItemStack(b_buyBlock, 1), new Object[] {"   ", "   ", "XXX", 'X', Block.dirt});
    	
    	//System.out.println(Block.blocksList[191].blockID);
    	
	}
	
}
