package build.world;

import java.util.LinkedList;
import java.util.List;

import build.SchematicData;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
/*import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.GameLogic.ZCGame;*/

//This class works nice and fast, unless the generated thing is hovering in the air. Lighting calculations going overboard perhaps
public class BuildManager {

	public List<BuildJob> activeBuilds;
	public List<String> buildNames;
	public int nextBuildID = 0;
	
	//Building related below
	
	//global settings
	public int offset = 0;
	public int build_rate = 100000;
	public int build_rand = 20;
	
	public BuildManager() {
		activeBuilds = new LinkedList();
		buildNames = new LinkedList();
	}
	
	public void updateTick() {
		for (int i = 0; i < activeBuilds.size(); i++) {
			BuildJob bj = activeBuilds.get(i);
			if (bj != null) {
				if (bj.build_active) {
					bj.updateTick();
					updateBuildProgress(bj);
				} else {
					activeBuilds.remove(bj);
				}
			}
		}
	}
	
	public boolean isBuildActive(int id) {
		for (int i = 0; i < activeBuilds.size(); i++) {
			BuildJob bj = activeBuilds.get(i);
			if (bj.id == id) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isBuildActive(Build build) {
		for (int i = 0; i < activeBuilds.size(); i++) {
			BuildJob bj = activeBuilds.get(i);
			if (bj.build == build) {
				return true;
			}
		}
		
		return false;
	}
	
	public int newBuild(int x, int y, int z, String name) {
		int buildID = nextBuildID++;
		
		BuildJob newBuild = new BuildJob(buildID, x, y, z, name);
		addBuild(newBuild);
		
		return buildID;
	}
	
	public int newBuild(Build build) {
		int buildID = nextBuildID++;
		
		BuildJob newBuild = new BuildJob(buildID, build);
		addBuild(newBuild);
		
		return buildID;
	}
	
	public void addBuild(BuildJob buildJob) {
		activeBuilds.add(buildJob);
		buildJob.buildStart();
		//return -1;
	}
	
	public void addBuild(BuildJob build, int x, int y, int z) {
		activeBuilds.add(build);
		build.buildStart();
		//return -1;
	}
	
    //Actual building functions
	
	public void updateBuildProgress(BuildJob buildJob) {
		
		Build build = buildJob.build;
		
		buildJob.build_currentTick++;
		
		World worldRef = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(buildJob.build.dim);
		
		if (buildJob.timeout > 0) {
			buildJob.timeout--;
			Build b = buildJob.build;
			
			worldRef.markBlockRangeForRenderUpdate(buildJob.build_startX, buildJob.build_startY, buildJob.build_startZ, buildJob.build_startX+b.map_sizeX, buildJob.build_startY+b.map_sizeY, buildJob.build_startZ+b.map_sizeZ);
			//worldRef.markBlocksDirty(b.map_coord_minX, b.map_coord_minY, b.map_coord_minZ, b.map_coord_minX+b.map_sizeX, b.map_coord_minY+b.map_sizeY, b.map_coord_minZ+b.map_sizeZ);
			return;
		}
    	
    	build_rate = 100000;
    	
    	build_rate = 25000;
    	
    	build_rate = 1000;
    	
    	//World worldRef = mod_ZombieCraft.worldRef;
    	
    	
    	boolean replaceAir = false;
    	
    	int loopCount;
    	int id = 0;
    	worldRef.editingBlocks = true;
    	buildJob.curLayerCountMax = build.map_sizeX * build.map_sizeZ;
    	//System.out.println("rand?: " + doRandomBuild + " build layer " + curLayerCount + " / " + curLayerCountMax + " | " + ((float)curLayerCount / (float)curLayerCountMax));
    	
    	buildJob.doRandomBuild = false;
    	//build_rate = 50;
    	//build_rand = 20;
    	
    	try {
	    	if (buildJob.doRandomBuild) {
	    		if (worldRef.rand.nextInt(build_rand) != 0) return;
	    		boolean first = true;
	    		int tryCount = 0;
	    		while ((first || id == 0) && tryCount < 300) {
	    			tryCount++;
	    			first = false;
	    			buildJob.build_loopTickX = worldRef.rand.nextInt(build.map_sizeX);
	    			buildJob.build_loopTickZ = worldRef.rand.nextInt(build.map_sizeZ);
		    		
		    		//stop random when 60% built
		    		if (((float)buildJob.curLayerCount / (float)buildJob.curLayerCountMax) > 0.9F) {
		    			buildJob.doRandomBuild = false;
		    			buildJob.build_loopTickX = 0;
		    			buildJob.build_loopTickZ = 0;
		    			return;
		    		}
		    		
		    		
		    		
		    		if (!build.build_blockPlaced[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ]) {
			    		
				    	//try {
				    		id = build.build_blockIDArr[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ];
			
				    		//damn you mcedit
				    		if (id < 0) id += 256;
				    		
				    		int xx = buildJob.build_startX+buildJob.build_loopTickX;
				    		int yy = buildJob.build_startY+buildJob.build_loopTickY;
				    		int zz = buildJob.build_startZ+buildJob.build_loopTickZ;
				    		
				    		
				    		//if (id != 0) {
				    			//worldRef.setBlockAndMetadata(xx, yy, zz, 0, 0);
				    			//worldRef.removeBlockTileEntity(xx, yy, zz);
				    			//worldRef.setBlockAndMetadata(build_startX+build_loopTickX, build_startY+build_loopTickY, build_startZ+build_loopTickZ, 0, 0);
				    			
				    			worldRef.setBlockAndMetadata(xx, yy, zz, id, build.build_blockMetaArr[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ]);
				    			//worldRef.markBlockNeedsUpdate(xx, yy, zz);
				    			build.build_blockPlaced[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ] = true;
				    			buildJob.curLayerCount++;
				    			if (id != 0) {
				    				//buildParticles(xx,yy,zz);
				    			}
				    		//}
				    	/*} catch (Exception ex) {
				    		ex.printStackTrace();
				    		worldRef.editingBlocks = false;
				    		build_active = false;
				    		return;
				    	}*/
			    	}
	    		}
	    	} else {
	    		
	    		
	    		
		    	for (loopCount = 0; loopCount < build_rate; loopCount++) {
		    		
		    	
			    	
			    	if (buildJob.build_loopTickX >= build.map_sizeX) {
			    		buildJob.build_loopTickX = 0;
			    		buildJob.build_loopTickZ++;
			    	}
			    	if (buildJob.build_loopTickZ >= build.map_sizeZ) {
			    		buildJob.build_loopTickZ = 0;
			    		buildJob.build_loopTickY++;
			    		buildJob.curLayerCount = 0;
			    		buildJob.doRandomBuild = true;
			    		
			    	}
			    	
			    	if (buildJob.build_loopTickY >= build.map_sizeY) {
			    		//done
			    		if (buildJob.pass == 2) {
			    			buildComplete(buildJob);
				    		buildJob.buildComplete();
			    		} else {
			    			buildJob.pass++;
			    			buildJob.build_loopTickX = 0;
			    			buildJob.build_loopTickZ = 0;
			    			buildJob.build_loopTickY = 0;
			    			
			    			buildJob.timeout = 20;
			    			
			    		} 
			    		worldRef.editingBlocks = false;
			    		//worldRef.editingBlocks = true;
			    		return;
			    	}
			    	
			    	build.curTick++;// = buildJob.build_loopTickX + ((buildJob.build_loopTickY + 1) * (buildJob.build_loopTickZ + 1));
			    	
			    	//float percent = ((float)build.curTick + 1) / ((float)build.maxTicks) * 100F;
					//System.out.println("build percent: " + percent);
			    	
			    	if (buildJob.pass == 0) {
			    		int xx = buildJob.build_startX+buildJob.build_loopTickX;
			    		int yy = buildJob.build_startY+buildJob.build_loopTickY;
			    		int zz = buildJob.build_startZ+buildJob.build_loopTickZ;
			    		
			    		id = build.build_blockIDArr[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ];
			    		
			    		
			    		
			    		//if (id != 0) {
			    		//if (worldRef.getBlockId(xx, yy, zz) != 0) { // its quicker to set then to check, mostly
			    			//worldRef.removeBlockTileEntity(xx, yy, zz);		
			    			worldRef.setBlockAndMetadata(xx, yy, zz, 0, 0);
			    		//}
	    				
			    	} else {
				    	if (!build.build_blockPlaced[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ]) {
				    		
					    	//try {
					    		id = build.build_blockIDArr[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ];
					    		int meta = build.build_blockMetaArr[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ];
					    		//damn you mcedit ... ?
					    		if (id < 0) id += 256;
					    		
					    		int xx = buildJob.build_startX+buildJob.build_loopTickX;
					    		int yy = buildJob.build_startY+buildJob.build_loopTickY;
					    		int zz = buildJob.build_startZ+buildJob.build_loopTickZ;
					    		
					    		//if (id != worldRef.getBlockId(par1, par2, par3))
					    		boolean skip = false;
					    		//pass 1 should be structure pass, pass 2 is redstone pass
					    		if (buildJob.pass == 1 && buildJob.blockIDsSkipFirstPass.contains(id)) {
					    			skip = true;
					    		}
					    		//System.out.println(buildJob.build_loopTickX + ", " + buildJob.build_loopTickY + ", " + buildJob.build_loopTickZ + " - " + id);
					    		if (!skip && ((replaceAir || id != 0)/* && (id != worldRef.getBlockId(xx, yy, zz) || meta != worldRef.getBlockMetadata(xx, yy, zz))*/) ) {
					    			//if (worldRef.getBlockTileEntity(xx, yy, zz) != null/* || (id != 0 && Block.blocksList[id].blockID == Block.chest.blockID)*/) {
					    				//worldRef.removeBlockTileEntity(xx, yy, zz);		
					    				//worldRef.setBlockAndMetadata(xx, yy, zz, 0, 0);
					    				//break;
					    			//}
					    			//}
					    			//worldRef.setBlockAndMetadata(build_startX+build_loopTickX, build_startY+build_loopTickY, build_startZ+build_loopTickZ, 0, 0);
					    			
					    			worldRef.setBlockAndMetadata(xx, yy, zz, id, meta);
					    			//worldRef.setBlock(xx, yy, zz, id);
					    			//worldRef.setBlockMetadata(xx, yy, zz, meta);
					    			
					    			
					    			build.build_blockPlaced[buildJob.build_loopTickX][buildJob.build_loopTickY][buildJob.build_loopTickZ] = true;
					    			buildJob.curLayerCount++;
					    			if (id != 0) {
					    				//buildParticles(xx,yy,zz);
					    			}
					    		} else {
					    			loopCount--;
					    		}
					    	/*} catch (Exception ex) {
					    		ex.printStackTrace();
					    		worldRef.editingBlocks = false;
					    		build_active = false;
					    		return;
					    	}*/
				    	}
			    	}
			    	buildJob.build_loopTickX++;
			    	//if (id == 0) loopCount--;
		    	}
	    	}
    	} catch (Exception ex) {
    		buildJob.build_active = false;
    		ex.printStackTrace();
    	}
    	worldRef.editingBlocks = false;
    }
	
	public void buildComplete(BuildJob buildJob) {
		spawnLevelEntities(buildJob);
		
		
		//ZCGame.instance.wMan.levelRegeneratedCallback();
	}
	
	public void spawnLevelEntities(BuildJob buildJob) {
		//NBTTagList var14 = entities.getTagList("Entities");

		Build build = buildJob.build;
		
		World worldRef = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(build.dim);
		
		List ents = worldRef.loadedEntityList;
		
		for (int i = 0; i < ents.size(); i++) {
			Entity ent = (Entity)ents.get(i);
			
			//Kill all items, a lazy solution for level cleanup for now
			if (ent instanceof EntityItem) {
				ent.setDead();
			}
		}
		
        if (build.entities != null)
        {
            for (int var17 = 0; var17 < build.entities.tagCount(); ++var17)
            {
                NBTTagCompound var16 = (NBTTagCompound)build.entities.tagAt(var17);
                Entity var18 = EntityList.createEntityFromNBT(var16, worldRef);
                /*var5.hasEntities = true;

                if (var18 != null)
                {
                    var5.addEntity(var18);
                }*/
            }
        }

        //NBTTagList var15 = par2NBTTagCompound.getTagList("TileEntities");

        if (build.tileEntities != null)
        {
            for (int var21 = 0; var21 < build.tileEntities.tagCount(); ++var21)
            {
                NBTTagCompound var20 = (NBTTagCompound)build.tileEntities.tagAt(var21);
                TileEntity var13 = TileEntity.createAndLoadEntity(var20);
                
                if (var13 instanceof SchematicData) {
                	((SchematicData)var13).readFromNBT(var20, build);
                }
                
                if (var13 != null) {
	                var13.xCoord = build.map_coord_minX+var13.xCoord;
	                var13.yCoord = buildJob.build_startY+var13.yCoord;
	                var13.zCoord = build.map_coord_minZ+var13.zCoord;
	
	                try {
	                	Packet packet = var13.getDescriptionPacket();
	                	if (packet != null) {
	                		MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(packet);
	                	}
	                } catch (Exception ex) {
	                	ex.printStackTrace();
	                }
	                
	                worldRef.removeBlockTileEntity(var13.xCoord, var13.yCoord, var13.zCoord);
	                worldRef.setBlockTileEntity(var13.xCoord, var13.yCoord, var13.zCoord, var13);
	                
	                worldRef.loadedTileEntityList.add(var13);
                }
                
                /*if (var13 != null)
                {
                    var5.addTileEntity(var13);
                }*/
            }
        }
	}
	
	
}
