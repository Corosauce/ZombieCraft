package build.world;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import zombiecraft.Core.GameLogic.ZCGame;

import build.SchematicData;

import cpw.mods.fml.common.FMLCommonHandler;

/*import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.GameLogic.ZCGame;*/
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
/*import net.minecraft.src.mod_ZombieCraft;*/

public class Build {

	//public int id = 0;
	public String file = "";

	//Basic placement data
	/*public int startX = 0;
	public int startY = 0;
	public int startZ = 0;
	
	public int sizeX = 0;
	public int sizeY = 0;
	public int sizeZ = 0;*/
	
	//visual data
	public int curTick = 0;
	public int maxTicks = 0;
	
	//world data
	public NBTTagCompound levelData = null;
	public NBTTagList tileEntities;
	public NBTTagList entities;
	public int build_blockIDArr[][][];
	public int build_blockMetaArr[][][];
	public boolean build_blockPlaced[][][];
	
	//REFACTOR THESE NAMES TO THE ABOVE COMMENTED OUT ONES ONCE COMPILING
	public int map_sizeX = 0;
	public int map_sizeY = 0;
	public int map_sizeZ = 0;
	public int map_coord_minX = 0;
	public int map_coord_minY = 0;
	public int map_coord_minZ = 0;
	public int map_surfaceOffset = 0;
	
	public int dim = 0;
	
	public Build(int x, int y, int z, String parFile) {
		//id = parID;
		file = parFile;
		
		map_coord_minX = x;
		map_coord_minY = y;
		map_coord_minZ = z;
		
		readNBT(file);
	}
	
	public void load() {
		
	}
	
	public void start() {
		
	}
	
	public void updateTick() {
		
	}
	
	public void setCornerPosition(int x, int y, int z) {
		map_coord_minX = x;
		map_coord_minY = y;
		map_coord_minZ = z;
	}
	
	public void recalculateLevelSize(int x1, int y1, int z1, int x2, int y2, int z2) {
		recalculateLevelSize(x1, y1, z1, x2, y2, z2, false);
	}
	
	public void recalculateLevelSize(int x1, int y1, int z1, int x2, int y2, int z2, boolean sizeUp) {
		
		map_sizeX = 0;
		map_sizeY = 0;
		map_sizeZ = 0;
		map_coord_minX = x1;
		map_coord_minY = y1;
		map_coord_minZ = z1;
		
		if (x1 > x2) map_coord_minX = x2;
		if (y1 > y2) map_coord_minY = y2;
		if (z1 > z2) map_coord_minZ = z2;
		
		if (x1 - x2 >= 0) {
			map_sizeX = x1 - x2;
		} else {
			map_sizeX = x2 - x1;
		}
		
		if (y1 - y2 >= 0) {
			map_sizeY = y1 - y2;
		} else {
			map_sizeY = y2 - y1;
		}
		
		if (z1 - z2 >= 0) {
			map_sizeZ = z1 - z2;
		} else {
			map_sizeZ = z2 - z1;
		}
		
		//map_coord_minY--;
		//map_sizeY+=2;
		if (sizeUp) {
			map_sizeX++;
			map_sizeY++;
			map_sizeZ++;
		}
		
		//System.out.println("Size: " + map_sizeX + "," + map_sizeY + "," + map_sizeZ);
		//System.out.println("map_coord_min: " + map_coord_minX + "," + map_coord_minY + "," + map_coord_minZ);
		
	}
	
	public void readNBT(String level) {
		
		
		
		levelData = null;
		
		try {
			
	    	NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(level + ".schematic"));
	    	
	    	levelData = nbttagcompound;
	    	
			byte metadata[] = nbttagcompound.getByteArray("Data");
			byte blockids[] = nbttagcompound.getByteArray("Blocks");
			tileEntities = nbttagcompound.getTagList("TileEntities");
			entities = nbttagcompound.getTagList("Entities");
			
			
			
			int sizeX = map_sizeX = nbttagcompound.getShort("Width");
			int sizeZ = map_sizeZ = nbttagcompound.getShort("Length");
			int sizeY = map_sizeY = nbttagcompound.getShort("Height");
			
			map_surfaceOffset = nbttagcompound.getShort("surfaceOffset");
			
			int bPosX = 0;
			int bPosY = 0;
			int bPosZ = 0;
			
			build_blockIDArr = new int
            [sizeX]
            [sizeY]
            [sizeZ];
			
			build_blockMetaArr = new int
            [sizeX]
            [sizeY]
            [sizeZ];
			
			build_blockPlaced = new boolean
            [sizeX]
            [sizeY]
            [sizeZ];
			
			for (int xx = 0; xx < sizeX; xx++) {
				for (int yy = 0; yy < sizeY; yy++) {
					for (int zz = 0; zz < sizeZ; zz++) {
						int index = yy * sizeX * sizeZ + zz * sizeX + xx;
						build_blockIDArr[xx][yy][zz] = blockids[index];
						build_blockMetaArr[xx][yy][zz] = metadata[index];
						build_blockPlaced[xx][yy][zz] = false;
						
					}
				}
			}
			
			file = level;
			//ZCGame.instance.mapMan.curLevel = level;
			
		} catch (Exception ex) {
			//notification off until generic build copy paste interface is supported for server
			//ex.printStackTrace();
		}
		
	}
	
	public void resetData() {
		build_blockIDArr = new int
        [map_sizeX]
        [map_sizeY]
        [map_sizeZ];
		
		build_blockMetaArr = new int
        [map_sizeX]
        [map_sizeY]
        [map_sizeZ];
		
		build_blockPlaced = new boolean
        [map_sizeX]
        [map_sizeY]
        [map_sizeZ];
	}
	
	public void scanLevelToData() {
		
		resetData();
		
		if (false) map_surfaceOffset = map_coord_minY - ZCGame.ZCWorldHeight;
		
		for (int xx = 0; xx < map_sizeX; xx++) {
			for (int yy = 0; yy < map_sizeY; yy++) {
				for (int zz = 0; zz < map_sizeZ; zz++) {
					int index = yy * map_sizeX * map_sizeZ + zz * map_sizeX + xx;
					
					World worldRef = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dim);
					
					build_blockIDArr[xx][yy][zz] = worldRef.getBlockId(map_coord_minX+xx, map_coord_minY+yy, map_coord_minZ+zz);
					build_blockMetaArr[xx][yy][zz] = worldRef.getBlockMetadata(map_coord_minX+xx, map_coord_minY+yy, map_coord_minZ+zz);
					build_blockPlaced[xx][yy][zz] = false;
					
					//check for tile entity to write out!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					
				}
			}
		}
	}
	
	public void scanWriteNBT() {
		scanLevelToData();
		writeNBT();
	}
	
	public void writeNBT() {
		//scans over level area and updates all data needed
		
		try {
			if (levelData == null) {
				System.out.println("New NBT Data Object");
				levelData = new NBTTagCompound();
			}
				
			byte metadata[] = new byte[map_sizeX * map_sizeY * map_sizeZ];// = levelData.getByteArray("Data");
			byte blockids[] = new byte[map_sizeX * map_sizeY * map_sizeZ];// = levelData.getByteArray("Blocks");
			//tileEntities = nbttagcompound.getTagList("TileEntities");
			//entities = nbttagcompound.getTagList("Entities");
			
			NBTTagList var16 = new NBTTagList();
			
			for (int xx = 0; xx < map_sizeX; xx++) {
				for (int yy = 0; yy < map_sizeY; yy++) {
					for (int zz = 0; zz < map_sizeZ; zz++) {
						int index = yy * map_sizeX * map_sizeZ + zz * map_sizeX + xx;
						
						blockids[index] = (byte)build_blockIDArr[xx][yy][zz];
						metadata[index] = (byte)build_blockMetaArr[xx][yy][zz];
						
						World worldRef = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dim);
						
						TileEntity tEnt = worldRef.getBlockTileEntity(map_coord_minX+xx,map_coord_minY+yy,map_coord_minZ+zz);
						if (tEnt != null) {
							NBTTagCompound var10 = new NBTTagCompound();
							
							if (tEnt instanceof SchematicData) {
								((SchematicData)tEnt).writeToNBT(var10, this);
							} else {
								tEnt.writeToNBT(var10);
							}
							
							//adjust coords to be relative to the schematic file
							var10.setInteger("x", xx);
							var10.setInteger("y", yy);
							var10.setInteger("z", zz);
							
							var16.appendTag(var10);
						}
						//check for tile entity to write out!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						
					}
				}
			}
	
	        levelData.setTag("TileEntities", var16);
			
			//somehow get all entities within bounds
			//loadedentity list bounds check
			
			levelData.setByteArray("Blocks", blockids);
			levelData.setByteArray("Data", metadata);
			
			levelData.setShort("Width", (short)map_sizeX);
			levelData.setShort("Height", (short)map_sizeY);
			levelData.setShort("Length", (short)map_sizeZ);
			
			levelData.setShort("surfaceOffset", (short)map_surfaceOffset);
			
			saveLevelData(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void saveLevelData(String level) {
		
		try {
			
			if (levelData != null) {
				
				FileOutputStream fos = new FileOutputStream(level + ".schematic");
				
		    	CompressedStreamTools.writeCompressed(levelData, fos);
		    	
		    	fos.close();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public boolean fillBuildData(Build parBuild) {
		
		FileInputStream fis = null;
		
    	try {
	    	InputStream is = new FileInputStream(parBuild.file);
			
	    	fis = new FileInputStream(parBuild.file);
	    	
	    	NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fis);
	    	
			byte metadata[] = nbttagcompound.getByteArray("Data");
			byte blockids[] = nbttagcompound.getByteArray("Blocks");
			
			
			
			if (fis != null) {
    			fis.close();
    		}
			
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	} finally {
    		
    		
    	}
    	return true;
    }
}
