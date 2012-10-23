package zombiecraft.Core.World;

import java.util.LinkedList;
import java.util.List;

import build.BuildServerTicks;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import zombiecraft.Core.EnumGameMode;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.GameLogic.ZCGame;

public class MapManager {
	
	public ZCGame zcGame;
	public Level zcLevel;
	//public BuildManager buildMan;
	public int levelBuildID = -1;
	
	public String curLevel = "";
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
		} else {
			shouldEntitiesReset = false;
		}
		
		if (!zcGame.gameActive) {
			//System.out.println(getLobbyPlayers().size());
		} else {
			//if (zcLevel != null && zcLevel.buildData != null) {
				
			//}
			if (zcLevel != null) zcLevel.tick();
		}
		
		//buildMan.updateTick();
		
		/*if (buildActive()) {
			updateBuildProgress();
		}*/
	}
	
	public boolean buildActive() {
		if (BuildServerTicks.buildMan.isBuildActive(this.zcLevel.buildData)) {
			return true;
		}
		return false;
	}
	
	public void buildStart() {
		if (!buildActive()) {
			BuildServerTicks.buildMan.newBuild(zcLevel.buildData);
			
		}
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
		zcLevel.writeNBT();
	}
	
	public void loadLevel() {
		zcLevel.readNBT(this.curLevel);
	}
	
	public boolean safeToStart() {
		if (unsavedChanges) return false;
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
			this.zcGame.teleportPlayer(player, zcLevel.player_spawnX + zcLevel.buildData.map_coord_minX, zcLevel.player_spawnY+0.5F + zcLevel.buildData.map_coord_minY, zcLevel.player_spawnZ + zcLevel.buildData.map_coord_minZ);
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
		if (tEnt.xCoord >= zcLevel.buildData.map_coord_minX && tEnt.xCoord <= zcLevel.buildData.map_coord_minX + zcLevel.buildData.map_sizeX) {
			if (tEnt.yCoord >= zcLevel.buildData.map_coord_minY && tEnt.yCoord <= zcLevel.buildData.map_coord_minY + zcLevel.buildData.map_sizeY) {
				if (tEnt.zCoord >= zcLevel.buildData.map_coord_minZ && tEnt.zCoord <= zcLevel.buildData.map_coord_minZ + zcLevel.buildData.map_sizeZ) {
					return true;
				}
			}
		}
		return false;
	}
}
