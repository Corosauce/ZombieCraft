package zombiecraft.Core;

import net.minecraft.block.Block;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import zombiecraft.Core.Blocks.BlockBarricade;
import zombiecraft.Core.Blocks.BlockBarricadePlaceable;
import zombiecraft.Core.Blocks.BlockBarrier;
import zombiecraft.Core.Blocks.BlockBetty;
import zombiecraft.Core.Blocks.BlockMobSpawnerWave;
import zombiecraft.Core.Blocks.BlockMysteryBox;
import zombiecraft.Core.Blocks.BlockPacketTester;
import zombiecraft.Core.Blocks.BlockPurchasePlate;
import zombiecraft.Core.Blocks.BlockSession;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.Blocks.TileEntityMysteryBox;
import zombiecraft.Core.Blocks.TileEntityPurchasePlate;
import zombiecraft.Core.Blocks.TileEntitySession;
import zombiecraft.Core.config.ConfigIDs;
import zombiecraft.Forge.ZombieCraftMod;
import CoroAI.util.CoroUtilBlock;
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
	
	public static Block barrier;
	
	public static Block barricadePlaceable;
	
	public static Block betty;
	public static Block packetTester;
	public static Block session;
	public static Block tower;
	public static Block wall;
	public static Block mysteryBox;
	//public static int bettyTexID = 0;
	
	public static Icon barricadeTopTexIDs[] = new Icon[7];// = new int[] { 0, 0, 0, 0, 0, 0, 0 };
	
	public ZCBlocks() {
		
	}
	
	//ALWAYS ADD IDS TO END!!!! THINK OF THE SCHEMATICS!
	public static void load(ZombieCraftMod pMod) {
		b_mobSpawnerWave  = (new BlockMobSpawnerWave(ConfigIDs.ID_B_WAVEMOBSPAWNER)).setUnlocalizedName("z_spawnblock").setCreativeTab(ZombieCraftMod.tabBlock);
		setUnlocalizedNameAndTexture(b_buyBlock = (new BlockPurchasePlate(ConfigIDs.ID_B_PURCHASEPLATE, EnumMobType.players, Material.circuits)).setCreativeTab(ZombieCraftMod.tabBlock), "z_purchaseBlock");
    	
    	int stateToBlockID[] = {ConfigIDs.ID_B_BARRICADE0, ConfigIDs.ID_B_BARRICADE1, ConfigIDs.ID_B_BARRICADE2, ConfigIDs.ID_B_BARRICADE3, ConfigIDs.ID_B_BARRICADE4, ConfigIDs.ID_B_BARRICADE5};
    	barricadeS0 = (new BlockBarricade(stateToBlockID, Material.circuits, 0)).setUnlocalizedName("barricadeBroken");
		barricadeS1 = (new BlockBarricade(stateToBlockID, Material.circuits, 1)).setUnlocalizedName("barricadeS1");
		barricadeS2 = (new BlockBarricade(stateToBlockID, Material.circuits, 2)).setUnlocalizedName("barricadeS2");
		barricadeS3 = (new BlockBarricade(stateToBlockID, Material.circuits, 3)).setUnlocalizedName("barricadeS3");
		barricadeS4 = (new BlockBarricade(stateToBlockID, Material.circuits, 4)).setUnlocalizedName("barricadeS4");
		barricadeS5 = (new BlockBarricade(stateToBlockID, Material.circuits, 5)).setUnlocalizedName("barricade");
		//barricadeS5 = Block.doorWood;//(new BlockDoor(121, Material.wood, 5)).setHardness(3F).setStepSound(Block.soundWoodFootstep).setBlockName("doorWood");
		barrier = (new BlockBarrier(ConfigIDs.ID_B_BARRIER)).setUnlocalizedName("barrier").setCreativeTab(ZombieCraftMod.tabBlock);
		
		barricadePlaceable = (new BlockBarricadePlaceable(ConfigIDs.ID_B_BARRICADEPLACEABLE)).setHardness(2.0F).setResistance(5.0F).setUnlocalizedName("barricadePlacable");
		
		setUnlocalizedNameAndTexture(betty = (new BlockBetty(ConfigIDs.ID_B_BETTY, Material.plants)).setHardness(2.0F).setResistance(5.0F).setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":betty");
		
		packetTester = (new BlockPacketTester(ConfigIDs.ID_B_PACKETTESTER)).setHardness(2F).setStepSound(Block.soundLadderFootstep).setUnlocalizedName(ZombieCraftMod.modID + ":packetTester").setCreativeTab(ZombieCraftMod.tabBlock);
		setUnlocalizedNameAndTexture(session = (new BlockSession(ConfigIDs.ID_B_SESSION)).setHardness(2F).setStepSound(Block.soundLadderFootstep).setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":session");
		setUnlocalizedNameAndTexture(mysteryBox = (new BlockMysteryBox(ConfigIDs.ID_B_MYSTERYBOX)).setHardness(2F).setStepSound(Block.soundLadderFootstep).setCreativeTab(ZombieCraftMod.tabBlock), ZombieCraftMod.modID + ":mysterybox");
		//tower = (new BlockTower(ConfigIDs.ID_B_TOWER)).setHardness(2F).setStepSound(Block.soundLadderFootstep).setUnlocalizedName("ZombieCraft:tower").setCreativeTab(ZombieCraftMod.tabBlock);
        
		//wall = (new BlockWallPlaceable(ConfigIDs.ID_B_WALL)).setUnlocalizedName("ZombieCraft:wall").setCreativeTab(ZombieCraftMod.tabBlock);
		
		//these names cant be updated until you are scanning in the updated schematics, seems nbt wont restore if block name changed?
		
        addBlock(b_mobSpawnerWave, TileEntityMobSpawnerWave.class, "SpawnBlockWave");
        addBlock(b_buyBlock, TileEntityPurchasePlate.class, "PurchaseBlock");
        addBlock(barricadeS5, "Barricade");
        addBlock(barrier, "ZC Barrier");
        addBlock(barricadePlaceable, "Placeable Barricade");
        addBlock(betty, "Bouncing Betty");
        addBlock(session, TileEntitySession.class, "Game Session Manager");
        addBlock(mysteryBox, TileEntityMysteryBox.class, "Mystery Box");
        //addBlock(packetTester, TileEntityPacketTester.class, "PacketTester");
        //addBlock(tower, TileEntityTower.class, "Tower");
        //addBlock(wall, "Wall");
        
        GameRegistry.addRecipe(new ItemStack(session, 1), new Object[] {"III", "ICI", "III", 'I', Item.ingotIron, 'C', Item.fireballCharge});
	}
	
	public static Block setUnlocalizedNameAndTexture(Block block, String nameTex) {
		block.setUnlocalizedName(nameTex);
		block.setTextureName(nameTex);
    	return block;
    }
	
	/*public static void addBlock(Block block, Class tEnt, String name) {
		addBlock(block, tEnt, name, false);
	}*/
	
	public static void addBlock(Block block, Class tEnt, String name) {
		addBlock(block, name);
		GameRegistry.registerTileEntity(tEnt, name); //is it ok to use same name here? packet tester doesnt care so far
	}
	
	public static void addBlock(Block block, String name) {
		GameRegistry.registerBlock(block, name);
		LanguageRegistry.addName(block, name);
	}
	
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
