package zombiecraft.Core;

import net.minecraft.block.Block;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.util.Icon;

import zombiecraft.Core.Blocks.BlockBarricade;
import zombiecraft.Core.Blocks.BlockBarricadePlaceable;
import zombiecraft.Core.Blocks.BlockBarrier;
import zombiecraft.Core.Blocks.BlockBetty;
import zombiecraft.Core.Blocks.BlockMobSpawnerWave;
import zombiecraft.Core.Blocks.BlockPurchasePlate;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.Blocks.TileEntityPurchasePlate;
import zombiecraft.Forge.ZombieCraftMod;
import cpw.mods.fml.common.registry.GameRegistry;

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
	//public static int bettyTexID = 0;
	
	public static Icon barricadeTopTexIDs[] = new Icon[7];// = new int[] { 0, 0, 0, 0, 0, 0, 0 };
	
	public ZCBlocks() {
		
	}
	
	//ALWAYS ADD IDS TO END!!!! THINK OF THE SCHEMATICS!
	public static void load(ZombieCraftMod pMod) {
		b_mobSpawnerWave  = (new BlockMobSpawnerWave(z_BlockIDStart++)).setUnlocalizedName("z_spawnblock").setCreativeTab(ZombieCraftMod.tabBlock);
    	b_buyBlock = (new BlockPurchasePlate(z_BlockIDStart++, EnumMobType.players, Material.circuits)).setUnlocalizedName("z_purchaseBlock").setCreativeTab(ZombieCraftMod.tabBlock);
    	
    	int stateToBlockID[] = {z_BlockIDStart++,z_BlockIDStart++,z_BlockIDStart++,z_BlockIDStart++,z_BlockIDStart++,z_BlockIDStart++};
    	barricadeS0 = (new BlockBarricade(stateToBlockID, Material.circuits, 0)).setUnlocalizedName("barricadeBroken");
		barricadeS1 = (new BlockBarricade(stateToBlockID, Material.circuits, 1)).setUnlocalizedName("barricadeS1");
		barricadeS2 = (new BlockBarricade(stateToBlockID, Material.circuits, 2)).setUnlocalizedName("barricadeS2");
		barricadeS3 = (new BlockBarricade(stateToBlockID, Material.circuits, 3)).setUnlocalizedName("barricadeS3");
		barricadeS4 = (new BlockBarricade(stateToBlockID, Material.circuits, 4)).setUnlocalizedName("barricadeS4");
		barricadeS5 = (new BlockBarricade(stateToBlockID, Material.circuits, 5)).setUnlocalizedName("barricade");
		//barricadeS5 = Block.doorWood;//(new BlockDoor(121, Material.wood, 5)).setHardness(3F).setStepSound(Block.soundWoodFootstep).setBlockName("doorWood");
		barrier = (new BlockBarrier(z_BlockIDStart++)).setUnlocalizedName("barrier").setCreativeTab(ZombieCraftMod.tabBlock);
		
		barricadePlaceable = (new BlockBarricadePlaceable(z_BlockIDStart++)).setHardness(2.0F).setResistance(5.0F).setUnlocalizedName("barricadePlacable");
		
		betty = (new BlockBetty(z_BlockIDStart++, Material.plants)).setHardness(2.0F).setResistance(5.0F).setUnlocalizedName("ZombieCraft:betty").setCreativeTab(ZombieCraftMod.tabBlock);
		
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
    	ModLoader.addName(ZCBlocks.barricadePlaceable,"Placeable Barricade");
    	ModLoader.addName(ZCBlocks.betty,"Bouncing Betty");
	}
	
}
