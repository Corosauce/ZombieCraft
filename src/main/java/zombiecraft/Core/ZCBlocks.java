package zombiecraft.Core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import zombiecraft.Core.Blocks.BlockBarricade;
import zombiecraft.Core.Blocks.BlockBarricadePlaceable;
import zombiecraft.Core.Blocks.BlockBarrier;
import zombiecraft.Core.Blocks.BlockBetty;
import zombiecraft.Core.Blocks.BlockMobSpawnerWave;
import zombiecraft.Core.Blocks.BlockMysteryBox;
import zombiecraft.Core.Blocks.BlockPurchasePlate;
import zombiecraft.Core.Blocks.BlockSession;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.Blocks.TileEntityMysteryBox;
import zombiecraft.Core.Blocks.TileEntityPurchasePlate;
import zombiecraft.Core.Blocks.TileEntitySession;
import zombiecraft.Core.config.ConfigIDs;
import zombiecraft.Forge.ZombieCraftMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ZCBlocks {
	
	//Do not allow for reconfig, schematics will break, eventually you will have to shift this up to somewhere below 4096 and convert old
	//public static int z_BlockIDStart = 190;
	
	public static Block b_mobSpawnerWave;
    public static Block b_buyBlock;
    
    public static Block barricadeS0;
	public static Block barricadeS1;
	public static Block barricadeS2;
	public static Block barricadeS3;
	public static Block barricadeS4;
	public static Block barricadeS5;
	
	public static List<Block> barricadeStates = new ArrayList<Block>();
	
	public static Block barrier;
	
	public static Block barricadePlaceable;
	
	public static Block betty;
	public static Block packetTester;
	public static Block session;
	public static Block tower;
	public static Block wall;
	public static Block mysteryBox;
	//public static int bettyTexID = 0;
	
	public static IIcon barricadeTopTexIDs[] = new IIcon[7];// = new int[] { 0, 0, 0, 0, 0, 0, 0 };
	
	public ZCBlocks() {
		
	}
	
	//ALWAYS ADD IDS TO END!!!! THINK OF THE SCHEMATICS!
	public static void load(ZombieCraftMod pMod) {
		b_mobSpawnerWave = (new BlockMobSpawnerWave()).setCreativeTab(ZombieCraftMod.tabBlock);
		b_buyBlock = (new BlockPurchasePlate(/*EnumMobType.players, */Material.circuits)).setCreativeTab(ZombieCraftMod.tabBlock);
    	
    	//int stateToBlockID[] = {ConfigIDs.ID_B_BARRICADE0, ConfigIDs.ID_B_BARRICADE1, ConfigIDs.ID_B_BARRICADE2, ConfigIDs.ID_B_BARRICADE3, ConfigIDs.ID_B_BARRICADE4, ConfigIDs.ID_B_BARRICADE5};
    	addBlock(barricadeS0 = (new BlockBarricade(Material.circuits, 0)), "barricadeBroken", "barricadeBroken");
    	addBlock(barricadeS1 = (new BlockBarricade(Material.circuits, 1)), "barricadeS1", "barricadeS1");
    	addBlock(barricadeS2 = (new BlockBarricade(Material.circuits, 2)), "barricadeS2", "barricadeS2");
    	addBlock(barricadeS3 = (new BlockBarricade(Material.circuits, 3)), "barricadeS3", "barricadeS3");
    	addBlock(barricadeS4 = (new BlockBarricade(Material.circuits, 4)), "barricadeS4", "barricadeS4");
    	barricadeS5 = (new BlockBarricade(Material.circuits, 5));;
    	addBlock(barricadeS5, "Barricade", "Barricade");
    	
		//barricadeS5 = Block.doorWood;//(new BlockDoor(121, Material.wood, 5)).setHardness(3F).setStepSound(Block.soundWoodFootstep).setBlockName("doorWood");
		
		barricadeStates.add(barricadeS0);
		barricadeStates.add(barricadeS1);
		barricadeStates.add(barricadeS2);
		barricadeStates.add(barricadeS3);
		barricadeStates.add(barricadeS4);
		barricadeStates.add(barricadeS5);
		
		barrier = (new BlockBarrier(ConfigIDs.ID_B_BARRIER)).setCreativeTab(ZombieCraftMod.tabBlock);
		
		barricadePlaceable = (new BlockBarricadePlaceable()).setHardness(2.0F).setResistance(5.0F);
		
		betty = (new BlockBetty(Material.plants)).setHardness(2.0F).setResistance(5.0F).setCreativeTab(ZombieCraftMod.tabBlock);
		
		//packetTester = (new BlockPacketTester()).setHardness(2F).setUnlocalizedName(ZombieCraftMod.modID + ":packetTester").setCreativeTab(ZombieCraftMod.tabBlock);
		session = (new BlockSession()).setHardness(2F).setCreativeTab(ZombieCraftMod.tabBlock);
		mysteryBox = (new BlockMysteryBox()).setHardness(2F).setCreativeTab(ZombieCraftMod.tabBlock);
		//tower = (new BlockTower(ConfigIDs.ID_B_TOWER)).setHardness(2F).setStepSound(Block.soundLadderFootstep).setUnlocalizedName("ZombieCraft:tower").setCreativeTab(ZombieCraftMod.tabBlock);
        
		//wall = (new BlockWallPlaceable(ConfigIDs.ID_B_WALL)).setUnlocalizedName("ZombieCraft:wall").setCreativeTab(ZombieCraftMod.tabBlock);
		
		//these names cant be updated until you are scanning in the updated schematics, seems nbt wont restore if block name changed?
		
        addBlock(b_mobSpawnerWave, TileEntityMobSpawnerWave.class, "z_spawnblock", "SpawnBlock Wave");
        addBlock(b_buyBlock, TileEntityPurchasePlate.class, "z_purchaseBlock", "Purchase Block");
        //addBlock(barricadeS5, "Barricade", "Barricade");
        addBlock(barrier, "barrier", "ZC Barrier");
        addBlock(barricadePlaceable, "barricadePlacable", "Placeable Barricade");
        addBlock(betty, "betty", "Bouncing Betty");
        addBlock(session, TileEntitySession.class, "session", "Game Session Manager");
        addBlock(mysteryBox, TileEntityMysteryBox.class, "mysterybox", "Mystery Box");
        //addBlock(packetTester, TileEntityPacketTester.class, "PacketTester");
        //addBlock(tower, TileEntityTower.class, "Tower");
        //addBlock(wall, "Wall");
        
        GameRegistry.addRecipe(new ItemStack(session, 1), new Object[] {"III", "ICI", "III", 'I', Items.iron_ingot, 'C', Items.fire_charge});
	}
	
	public static void addBlock(Block block, Class tEnt, String unlocalizedName, String blockNameBase) {
		addBlock(block, unlocalizedName, blockNameBase);
		GameRegistry.registerTileEntity(tEnt, unlocalizedName);
	}
	
	public static void addBlock(Block parBlock, String unlocalizedName, String blockNameBase) {
		//vanilla calls
		GameRegistry.registerBlock(parBlock, unlocalizedName);
		parBlock.setBlockName(ZombieCraftMod.modID + ":" + unlocalizedName);
		parBlock.setBlockTextureName(ZombieCraftMod.modID + ":" + unlocalizedName);
		parBlock.setCreativeTab(CreativeTabs.tabMisc);
		LanguageRegistry.addName(parBlock, blockNameBase);
	}
	
	/*public static Block setUnlocalizedNameAndTexture(Block block, String nameTex) {
		block.setBlockName(nameTex);
		//block.setTextureName(nameTex);
    	return block;
    }*/
	
	/*public static void addBlock(Block block, Class tEnt, String name) {
		addBlock(block, tEnt, name, false);
	}*/
	
	/*public static void addBlock(Block block, Class tEnt, String name) {
		addBlock(block, name);
		GameRegistry.registerTileEntity(tEnt, name); //is it ok to use same name here? packet tester doesnt care so far
	}
	
	public static void addBlock(Block block, String name) {
		GameRegistry.registerBlock(block, name);
		LanguageRegistry.addName(block, name);
	}*/
	
	//temp method until we can update names
	/*public static void addBlock(Block block, String name, boolean useNullInternalName) {
		if (useNullInternalName) {
			GameRegistry.registerBlock(block);
		} else {
			GameRegistry.registerBlock(block, name);
		}
		LanguageRegistry.addName(block, name);
	}*/
}
