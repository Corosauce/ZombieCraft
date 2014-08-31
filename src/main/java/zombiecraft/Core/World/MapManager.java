package zombiecraft.Core.World;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import zombiecraft.Core.EnumGameMode;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.config.ConfigMisc;
import CoroUtil.util.CoroUtilBlock;
import build.BuildServerTicks;
import build.world.BuildJob;
import build.world.BuildManager;

/* I guess this class is for map specific stuff I felt I didn't want to clutter ZCGame with */
public class MapManager {
	
	public ZCGame zcGame;
	public Level zcLevel;
	//public BuildManager buildMan;
	public int levelBuildID = -1;
	
	public String curLevel = "";
	public float curBuildPercent = -1;
	/*public int levelStartX = 0;
	public int levelStartY = 96;
	public int levelStartZ = 0;*/
	
	//Important states
	public boolean doorNoClip = false;
	public boolean editMode = false;
	public EnumGameMode gameMode = EnumGameMode.CLOSED;
	public boolean unsavedChanges = false;
	public boolean infoOverlay = true;
	
	public int editToolMode = 0;
	
	public boolean shouldEntitiesReset = false;
	
	public boolean wasBuildActive = false;
	
	public MapManager(ZCGame game) {
		zcGame = game;
		zcLevel = zcGame.zcLevel;
		//buildMan = new BuildManager();
		curLevel = "Nacht";
		
		
		//updateEditState();
	}
	
	public void tick() {
		
		if (buildActive()) {
			shouldEntitiesReset = true;
			wasBuildActive = true;
		} else {
			shouldEntitiesReset = false;
			if (wasBuildActive) {
				wasBuildActive = false;
				zcGame.wMan.levelRegeneratedCallback();
			}
		}
		
		if (!zcGame.gameActive) {
			//System.out.println(getLobbyPlayers().size());
		} else {
			//if (zcLevel != null && zcLevel.buildData != null) {
				
			//}
			if (zcLevel != null) zcLevel.tick();
		}
		
		if (zcGame.lobbyActive) {
			World world = DimensionManager.getWorld(ZCGame.ZCDimensionID);
			if (world != null) {
				if (world.getPlayerEntityByName(zcGame.lobbyLeader) == null) {
					zcGame.lobbyActive = false;
					zcGame.lobbyLeader = "";
				}
			}
		}
		
		//buildMan.updateTick();
		
	}
	
	public boolean buildActive() {
		if (BuildServerTicks.buildMan.isBuildActive(this.zcLevel.buildData)) {
			return true;
		}
		return false;
	}
	
	public void buildStart(EntityPlayer ent) {
		if (!buildActive()) {
			if (ent != null) {
				zcLevel.buildData.dim = zcGame.activeZCDimension;
				zcGame.setActiveDimension(ent.dimension);
			}
			zcGame.wMan.removeExtraEntities();
			if (ent != null) {
				clearStartArea(ent);
			}
			
			zcLevel.buildJob = new BuildJob(BuildServerTicks.buildMan.nextBuildID++, zcLevel.buildData);
			BuildServerTicks.buildMan.addBuild(zcLevel.buildJob);
		}
	}
	
	public void clearStartArea(EntityPlayer ent) {
		int r = ConfigMisc.chunkClearRadius;
		System.out.println("starting ZC area clear");
		if (r > 0) {
			for (int x = -r; x <= r; x++) {
				for (int z = -r; z <= r; z++) {
					for (int xx = 0; xx < 16; xx++) {
						for (int zz = 0; zz < 16; zz++) {
							for (int y = Level.lobby_coord_minY-1; y > 51; y--) {
								ent.worldObj.setBlock((x*16)+xx, y, (z*16)+zz, Blocks.air);
							}
						}
					}
				}
			}
		}
		System.out.println("finished ZC area clear");
	}
	
	public void toggleNoClip() {
		doorNoClip = !doorNoClip;
	}
	
	public void toggleEditMode() {
		editMode = !editMode;
		updateEditState();
	}
	
	public void setToolEditMode(int mode) {
		editToolMode = mode;
	}
	
	public void updateEditState() {
		if (!editMode) {
			//zcGame.wMan.stopGame();
		}
		ZCUtil.setBlocksMineable(editMode);
	}
	
	public void saveLevel() {
		checkFolder(zcGame.getMapSaveFolderPath());
		zcLevel.buildData.dim = zcGame.activeZCDimension;
		zcLevel.buildData.file = zcGame.getMapSaveFolderPath() + File.separator + curLevel;
		zcLevel.writeNBT();
	}
	
	public void checkFolder(String path) {
		File theDir = new File(path);

		if (!theDir.exists()) {
			System.out.println("creating directory: " + path);
			boolean result = theDir.mkdir();  
			if(result){    
				System.out.println("DIR created");  
		    }
		}
	}
	
	public void loadLevel() {
		System.out.println("Loading Level: " + new File(zcGame.getMapSaveFolderPath() + this.curLevel).getAbsolutePath());
		zcLevel.readNBT(zcGame.getMapSaveFolderPath() + this.curLevel);
	}
	
	public boolean safeToStart() {
		if (unsavedChanges) return false;
		
		if (this.editMode) return false;
		
		return true;
	}
	
	public void setMapName(String name) {
		if (name == "") name = "NewWorld";
		this.curLevel = name;
		this.zcLevel.levelName = name;
	}
	
	public void setPlayerSpawn(int x, int y, int z) {
		zcLevel.player_spawnX = x - zcLevel.buildData.map_coord_minX;
		zcLevel.player_spawnY = y - zcLevel.buildData.map_coord_minY;
		zcLevel.player_spawnZ = z - zcLevel.buildData.map_coord_minZ;
		
		zcLevel.player_spawnX_world = zcLevel.buildData.map_coord_minX + zcLevel.player_spawnX;
		zcLevel.player_spawnY_world = zcLevel.buildData.map_coord_minY + zcLevel.player_spawnY;
		zcLevel.player_spawnZ_world = zcLevel.buildData.map_coord_minZ + zcLevel.player_spawnZ;
		
		//if (zcLevel.player_spawnY == 0) zcLevel.player_spawnY = 999;
	}
	
	public void movePlayerToSpawn(EntityPlayer player) {
		
		//System.out.println("player tele X: " + (zcLevel.player_spawnX + zcLevel.map_coord_minX));
		if (zcLevel.player_spawnY != 999) {
			int randRange = 2;
			this.zcGame.teleportPlayer(player, zcLevel.player_spawnX + zcLevel.buildData.map_coord_minX - (randRange/2) + (player.worldObj.rand.nextFloat() * randRange)
					, zcLevel.player_spawnY+0.5F + zcLevel.buildData.map_coord_minY, zcLevel.player_spawnZ + zcLevel.buildData.map_coord_minZ - (randRange/2) + (player.worldObj.rand.nextFloat() * randRange));
		} else {
			movePlayerToLobby(player);
		}
		
	}
	
	public void buildLobbyIfMissing(World world) {
		
		
		
		int lobbyBuildID = -67;
		
		if (!BuildServerTicks.buildMan.isBuildActive(lobbyBuildID)) {
			if (CoroUtilBlock.isAir(world.getBlock(zcLevel.lobby_coord_minX, zcLevel.lobby_coord_minY, zcLevel.lobby_coord_minZ)) || 
					CoroUtilBlock.isAir(world.getBlock(zcLevel.lobby_coord_maxX-1, zcLevel.lobby_coord_minY, zcLevel.lobby_coord_minZ)) || 
					CoroUtilBlock.isAir(world.getBlock(zcLevel.lobby_coord_maxX-1, zcLevel.lobby_coord_minY, zcLevel.lobby_coord_maxZ-1)) || 
					CoroUtilBlock.isAir(world.getBlock(zcLevel.lobby_coord_minX, zcLevel.lobby_coord_minY, zcLevel.lobby_coord_maxZ-1))) {
				System.out.println("lobby missing: generating");
				BuildJob bj = new BuildJob(lobbyBuildID, zcLevel.lobby_coord_minX, zcLevel.lobby_coord_minY, zcLevel.lobby_coord_minZ, zcGame.getSaveFolderPath() + "Lobby");
				bj.build.dim = ZCGame.ZCDimensionID;
				BuildServerTicks.buildMan.addBuild(bj);
				
			}
		}
	}
	
	public void movePlayerToLobby(EntityPlayer player) {
		
		//if lobby not generated
		buildLobbyIfMissing(player.worldObj);
		
		if (player.getDistance(zcLevel.lobby_coord_playerX, zcLevel.lobby_coord_playerY, zcLevel.lobby_coord_playerZ) > 8) {
			int randRange = 2;
			this.zcGame.teleportPlayer(player, zcLevel.lobby_coord_playerX - (randRange/2) + (player.worldObj.rand.nextFloat() * randRange), zcLevel.lobby_coord_playerY + 1.5D, zcLevel.lobby_coord_playerZ - (randRange/2) + (player.worldObj.rand.nextFloat() * randRange));
			
			player.motionY = 0.8F;
		}
	}
	
	public boolean removeForRebuild(TileEntity tEnt) {
		if (shouldEntitiesReset) {
			return !isInsideLevel(tEnt);
		} else {
			return false;
		}
		
	}
	
	public boolean isInsideLevel(TileEntity tEnt) {
		if (tEnt.xCoord >= zcLevel.buildData.map_coord_minX && tEnt.xCoord < zcLevel.buildData.map_coord_minX + zcLevel.buildData.map_sizeX) {
			if (tEnt.yCoord >= zcLevel.buildData.map_coord_minY && tEnt.yCoord < zcLevel.buildData.map_coord_minY + zcLevel.buildData.map_sizeY) {
				if (tEnt.zCoord >= zcLevel.buildData.map_coord_minZ && tEnt.zCoord < zcLevel.buildData.map_coord_minZ + zcLevel.buildData.map_sizeZ) {
					return true;
				}
			}
		}
		return false;
	}
}
