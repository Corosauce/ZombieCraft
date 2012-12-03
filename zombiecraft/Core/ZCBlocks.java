package zombiecraft.Core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import zombiecraft.Core.Blocks.BlockBarricade;
import zombiecraft.Core.Blocks.BlockBarricadePlaceable;
import zombiecraft.Core.Blocks.BlockBarrier;
import zombiecraft.Core.Blocks.BlockBetty;
import zombiecraft.Core.Blocks.BlockMobSpawnerWave;
import zombiecraft.Core.Blocks.BlockPurchasePlate;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.Blocks.TileEntityPurchasePlate;
import zombiecraft.Forge.ZombieCraftMod;

import net.minecraft.src.*;

public class ZCBlocks {
	
	//Do not allow for reconfig, schematics will break, eventually you will have to shift this up to somewhere below 4096 and convert old
	public static int z_BlockIDStart = 190;
	
	public static Block b_mobSpawnerWave;
    public static Block b_buyBlock;
    
    public static Block barricadeS0;
	public static Block barricadeS1;
	public static Block barricadeS2;
	public static Block barricadeS3;
	public static Block barricadeS4;
	public static Block barricadeS5;
	
	public static Block barrier;
	
	public static Block barricadePlaceable;
	
	public static Block betty;
	public static int bettyTexID = 0;
	
	public ZCBlocks() {
		
	}
	
	//ALWAYS ADD IDS TO END!!!! THINK OF THE SCHEMATICS!
	public static void load(ZombieCraftMod pMod) {
		b_mobSpawnerWave  = (new BlockMobSpawnerWave(z_BlockIDStart++, 65)).setBlockName("z_spawnblock").setCreativeTab(ZombieCraftMod.tabBlock);
    	b_buyBlock = (new BlockPurchasePlate(z_BlockIDStart++, 65, EnumMobType.players, Material.circuits)).setBlockName("z_purchaseBlock").setCreativeTab(ZombieCraftMod.tabBlock);
    	
    	int stateToBlockID[] = {z_BlockIDStart++,z_BlockIDStart++,z_BlockIDStart++,z_BlockIDStart++,z_BlockIDStart++,z_BlockIDStart++};
    	barricadeS0 = (new BlockBarricade(stateToBlockID, ZCItems.barricadeTopTexIDs, Material.circuits, 0)).setBlockName("barricadeBroken");
		barricadeS1 = (new BlockBarricade(stateToBlockID, ZCItems.barricadeTopTexIDs, Material.circuits, 1)).setBlockName("barricadeS1");
		barricadeS2 = (new BlockBarricade(stateToBlockID, ZCItems.barricadeTopTexIDs, Material.circuits, 2)).setBlockName("barricadeS2");
		barricadeS3 = (new BlockBarricade(stateToBlockID, ZCItems.barricadeTopTexIDs, Material.circuits, 3)).setBlockName("barricadeS3");
		barricadeS4 = (new BlockBarricade(stateToBlockID, ZCItems.barricadeTopTexIDs, Material.circuits, 4)).setBlockName("barricadeS4");
		barricadeS5 = (new BlockBarricade(stateToBlockID, ZCItems.barricadeTopTexIDs, Material.circuits, 5)).setBlockName("barricade");
		//barricadeS5 = Block.doorWood;//(new BlockDoor(121, Material.wood, 5)).setHardness(3F).setStepSound(Block.soundWoodFootstep).setBlockName("doorWood");
		barrier = (new BlockBarrier(z_BlockIDStart++, 1)).setBlockName("barrier").setCreativeTab(ZombieCraftMod.tabBlock);
		
		barricadePlaceable = (new BlockBarricadePlaceable(z_BlockIDStart++, ZCItems.barricadeTopTexIDs)).setHardness(2.0F).setResistance(5.0F).setBlockName("barricadePlacable");
		
		betty = (new BlockBetty(z_BlockIDStart++, bettyTexID, Material.plants)).setHardness(2.0F).setResistance(5.0F).setBlockName("betty").setCreativeTab(ZombieCraftMod.tabBlock);
		
    	ModLoader.registerBlock(b_mobSpawnerWave);
    	ModLoader.registerBlock(b_buyBlock);
    	
    	ModLoader.registerBlock(barricadeS5);
    	ModLoader.registerBlock(barrier);
    	ModLoader.registerBlock(barricadePlaceable);
    	ModLoader.registerBlock(betty);
    	
        GameRegistry.registerTileEntity(TileEntityMobSpawnerWave.class, "z_tspawnblock");
        GameRegistry.registerTileEntity(TileEntityPurchasePlate.class, "z_tpurchaseblock");
        ModLoader.addName(ZCBlocks.b_mobSpawnerWave, "SpawnBlockWave");
        ModLoader.addName(ZCBlocks.b_buyBlock, "PurchaseBlock");
        
        
    	//ModLoader.addName(ZCItems.buildTool,"ZC Build Tool");
    	ModLoader.addName(ZCBlocks.barrier,"ZC Barrier");
    	ModLoader.addName(ZCBlocks.barricadePlaceable,"Placable Barricade");
    	ModLoader.addName(ZCBlocks.betty,"Bouncing Betty");
	}
	
}
