package build.world;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import zombiecraft.Core.GameLogic.ZCGame;

/*import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.GameLogic.ZCGame;*/
import net.minecraft.src.Block;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
//import net.minecraft.src.mod_ZombieCraft;

public class BuildJob {

	public int id = 0;

	public Build build;
	
	public int offset = 0;
	public int build_rate = 1500;
	public int build_rand = 20;
	
	public int timeout = 0;

	public int pass = 0;
	public List<Integer> blockIDsSkipFirstPass = new LinkedList();
	
	public boolean build_active = false;
	public int build_currentTick = 0;
	public int build_loopTickX = 0;
	public int build_loopTickY = 0;
	public int build_loopTickZ = 0;
	public int build_startX = 0;
	public int build_startY = 0;
	public int build_startZ = 0;
    
    public int curLayerCount = 0;
    public int curLayerCountMax = 0;
    
    public boolean doRandomBuild = false;
	
	//REFACTOR THESE NAMES TO THE ABOVE COMMENTED OUT ONES ONCE COMPILING
	/*public int map_sizeX = 0;
	public int map_sizeY = 0;
	public int map_sizeZ = 0;
	public int map_coord_minX = 0;
	public int map_coord_minY = 0;
	public int map_coord_minZ = 0;*/
	
	
	
	public BuildJob(int parID, int x, int y, int z, String parFile) {
		id = parID;
		
		build_startX = x;
		build_startY = y;
		build_startZ = z;
		
		build = new Build(x, y, z, parFile);
	}
	
	public BuildJob(int parID, int x, int y, int z, Build parBuild) {
		id = parID;
		
		build_startX = x;
		build_startY = y;
		build_startZ = z;
		
		build = parBuild;
	}
	
	public BuildJob(int parID, Build parBuild) {
		id = parID;
		
		//wat
		build_startX = parBuild.map_coord_minX;
		build_startY = parBuild.map_coord_minY;
		build_startZ = parBuild.map_coord_minZ;
		
		if (parBuild.map_coord_minY == ZCGame.ZCWorldHeight) {
			//System.out.println("offset adjust");
			if (false) build_startY = parBuild.map_coord_minY + parBuild.map_surfaceOffset;
		}
		
		build = parBuild;
	}
	
	public void load() {
		
	}
	
	public void start() {
		
	}
	
	public void updateTick() {
		
	}
	
	public void newBuild(int x, int y, int z) {
		//InputStream is;// = this.getClass().getResourceAsStream("N:\\dev\\Game Dev\\minecraft\\modding\\mcp50 fishymerge\\jars\\" + file);
		
		//is = new FileInputStream(file);
		
		//int x = (int)mc.thePlayer.posX+5;
		//int y = (int)mc.thePlayer.posY-1;
		//int z = (int)mc.thePlayer.posZ+5;
		
		blockIDsSkipFirstPass = new LinkedList();
		blockIDsSkipFirstPass.add(Block.tnt.blockID);
		blockIDsSkipFirstPass.add(Block.redstoneRepeaterActive.blockID);
		blockIDsSkipFirstPass.add(Block.redstoneRepeaterIdle.blockID);
		blockIDsSkipFirstPass.add(Block.redstoneWire.blockID);
		blockIDsSkipFirstPass.add(Block.torchRedstoneActive.blockID);
		blockIDsSkipFirstPass.add(Block.torchRedstoneIdle.blockID);
		blockIDsSkipFirstPass.add(Block.torchWood.blockID);
		
		int offset = 0;
		
		y += offset;
		
		//if (tryBuild(x, y, z, curBuild)) {
			build_active = true;
		//}
		
		build_currentTick = 0;
    	build_loopTickX = 0;
    	build_loopTickY = 0;
    	build_loopTickZ = 0;
    	pass = 0;
		
		build_startX = x;// - (build.map_sizeX / 2);
    	build_startY = y;
    	build_startZ = z;// - (build.map_sizeZ / 2);
	}
	
	public void buildStart() {
    	resetBuildState();
    	newBuild(build_startX, build_startY, build_startZ);
    	//build_startX = this.build.map_coord_minX;
    	//build_startY = this.build.map_coord_minY;
    	//build_startZ = this.build.map_coord_minZ;
    	
    	//shouldEntitiesReset = true;
    	System.out.println("Level build starting");
    }
    
    public void buildComplete() {
    	//shouldEntitiesReset = false;
    	build_active = false;
		doRandomBuild = true;
		//mod_ZombieCraft.worldRef.editingBlocks = false;
		//spawnLevelEntities();
		
		System.out.println("Level build complete");
    }
    
    
    
    public void resetBuildState() {
    	build.build_blockPlaced = new boolean
        [build.map_sizeX]
        [build.map_sizeY]
        [build.map_sizeZ];
		
		for (int xx = 0; xx < build.map_sizeX; xx++) {
			for (int yy = 0; yy < build.map_sizeY; yy++) {
				for (int zz = 0; zz < build.map_sizeZ; zz++) {
					int index = yy * build.map_sizeX * build.map_sizeZ + zz * build.map_sizeX + xx;
					build.build_blockPlaced[xx][yy][zz] = false;
					
				}
			}
		}
		
		build.curTick = 0;
		build.maxTicks = build.map_sizeX * build.map_sizeY * build.map_sizeZ * 3; //3 build passes
    }
}
