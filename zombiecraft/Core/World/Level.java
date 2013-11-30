package zombiecraft.Core.World;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import zombiecraft.Core.Camera.CameraPoint;
import zombiecraft.Core.GameLogic.ZCGame;
import build.world.Build;

/* This class should mainly be for stuff thats used on top of the buildmod schematic spec */
public class Level {
	
	public ZCGame zcGame;
	
	public List<EntityPlayer> playersInGame; //could be moved to ZCGame
	public List<String> playersInGame_Names; //could be moved to ZCGame
	
	//NBT Loaded stuff
	public String levelName = "";
	public Build buildData;
	
	//lets reuse this for the statically set lobby room coordinates - adjust these for the min x y z corner of lobby room for generation
	public static int lobby_coord_minX = -36;
	public static int lobby_coord_minY = 98;
	public static int lobby_coord_minZ = 26;
	
	public static int lobby_coord_playerX = -32;
	public static int lobby_coord_playerY = 100;
	public static int lobby_coord_playerZ = 30;
	
	public static int lobby_coord_maxX = lobby_coord_minX + 7;
	public static int lobby_coord_maxY = lobby_coord_minY + 5;
	public static int lobby_coord_maxZ = lobby_coord_minZ + 9;
	
	
	
	//calculated from min + size
	/*public int map_coord_maxX = 0;
	public int map_coord_maxY = 0;
	public int map_coord_maxZ = 0;*/
	
	public boolean lobby_distCheck = true;
	public int lobby_scanSize = 16;
	
	public int lobby_tileEntX = 0;
	public int lobby_tileEntY = 0;
	public int lobby_tileEntZ = 0;
	
	public int player_spawnX = 0;
	public int player_spawnY = 999;
	public int player_spawnZ = 0;
	
	public int player_spawnX_world = 0;
	public int player_spawnY_world = 999;
	public int player_spawnZ_world = 0;
	
	public List<CameraPoint> camPoints;
	
	public String texturePack;
	
	public Level(ZCGame game) {
		zcGame = game;
		
		playersInGame = new LinkedList();
		playersInGame_Names = new LinkedList();
		camPoints = new LinkedList();
		
		texturePack = "default";
		
		//SO FUCKING TEMP
		/*CameraPoint cp = new CameraPoint();
		cp.posX = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnX_world + 50;
		cp.posY = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnY_world;
		cp.posZ = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnZ_world;
		camPoints.add(cp);
		
		cp = new CameraPoint();
		cp.posX = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnX_world + 50;
		cp.posY = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnY_world;
		cp.posZ = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnZ_world + 50;
		camPoints.add(cp);
		
		cp = new CameraPoint();
		cp.posX = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnX_world;
		cp.posY = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnY_world;
		cp.posZ = ZCClientTicks.zcGame.mapMan.zcLevel.player_spawnZ_world;
		camPoints.add(cp);*/
		
	}
	
	public void tick() {
		if (!zcGame.gameActive) {
			//System.out.println(getLobbyPlayers().size());
		} else {
			
			
		}
	}
	
	public void newGame() {
		lobby_distCheck = false;
		resetActiveData();
	}
	
	public void newGameFrom(EntityPlayer player, int x, int y, int z) {
		if (player != null) { 
			zcGame.setActiveDimension(player.dimension);
		} else zcGame.setActiveDimension(ZCGame.ZCDimensionID);
		lobby_distCheck = true;
		lobby_tileEntX = x; lobby_tileEntY = y; lobby_tileEntZ = z;
		resetActiveData();
	}
	
	public void resetActiveData() {
		playersInGame = this.getLobbyPlayers();
		System.out.println("Level.getLobbyPlayers size: " + playersInGame.size());
	}
	
	public void readNBT(String filename) {
		
		
		
		if (buildData == null) {
			if (zcGame.gameData == null) {
				buildData = new Build(0, ZCGame.ZCWorldHeight, 0, filename);
			} else {
				buildData = new Build(zcGame.gameData.getInteger("map_coord_minX"), zcGame.gameData.getInteger("map_coord_minY"), zcGame.gameData.getInteger("map_coord_minZ"), filename);
			}
		} else {
			buildData = new Build(this.buildData.map_coord_minX, this.buildData.map_coord_minY, this.buildData.map_coord_minZ, filename);
		}
		buildData.dim = ZCGame.ZCDimensionID;
		//Build reads its data, then we add ours to it
		buildData.readNBT(filename);
		
		levelName = buildData.file;
		
		NBTTagCompound nbttagcompound = buildData.levelData;
		
		if (nbttagcompound != null) {
		
			player_spawnX = nbttagcompound.getInteger("player_spawnX");
			player_spawnY = nbttagcompound.getInteger("player_spawnY");
			player_spawnZ = nbttagcompound.getInteger("player_spawnZ");
			
			player_spawnX_world = buildData.map_coord_minX + player_spawnX;
			player_spawnY_world = buildData.map_coord_minY + player_spawnY;
			player_spawnZ_world = buildData.map_coord_minZ + player_spawnZ;
			
			//Camera points
			NBTTagList tag = nbttagcompound.getTagList("camPoints");
			if (tag != null) {
				for (int var21 = 0; var21 < tag.tagCount(); ++var21)
	            {
	                NBTTagCompound var20 = (NBTTagCompound)tag.tagAt(var21);
	                
	                CameraPoint cp = new CameraPoint();
	                cp.readFromNBT(var20, buildData);
	                this.camPoints.add(cp);
	            }
			}
			
			String texPack = nbttagcompound.getString("texturePack");
    		
    		if (texPack.length() > 0) {
    			//System.out.println("try load tex pac: " + texPack);
    			//if (zcGame.trySetTexturePack(texPack)) {
    				texturePack = texPack;
    			//} else {
    				//System.out.println("failed to load texturepack");
    			//}
    		}
    		
    		LevelConfig.loadNBT(nbttagcompound.getCompoundTag("mapConfig"));
			
		} else {
			System.out.println("Error loading level build data, non existing schematic file");
		}
		
		if (player_spawnY == 0) player_spawnY = 999;
	}
	
	public void writeNBT() {
		
		//We set our data, then we have Build set its and it auto writes out
		if (buildData.levelData == null) {
			System.out.println("New NBT Data Object");
			buildData.levelData = new NBTTagCompound();
		}
		
		//new
		buildData.newFormat = true;
		
		//Extra data
		buildData.levelData.setString("levelName", levelName);
		buildData.levelData.setInteger("player_spawnX", player_spawnX);
		buildData.levelData.setInteger("player_spawnY", player_spawnY);
		buildData.levelData.setInteger("player_spawnZ", player_spawnZ);
		
		//Camera points
		//buildData.levelData.setFloat("campoint_count", camPoints.size());
		NBTTagList tag = new NBTTagList();
		for (int i = 0; i < camPoints.size(); i++) {
			NBTTagCompound nbtCamPoint = new NBTTagCompound();
			camPoints.get(i).writeToNBT(nbtCamPoint, buildData);
			tag.appendTag(nbtCamPoint);
		}	
		buildData.levelData.setTag("camPoints", tag);
		
		if (texturePack != null && texturePack.length() > 0) buildData.levelData.setString("texturePack", texturePack);
		
		buildData.levelData.setCompoundTag("mapConfig", LevelConfig.saveNBT());
		
		buildData.scanWriteNBT();
	}
	
	public List<EntityPlayer> getLobbyPlayers() {
		List<EntityPlayer> players = zcGame.getPlayers();
		List<EntityPlayer> playersInLobby = new LinkedList();
		
		playersInGame = new LinkedList();
		playersInGame_Names = new LinkedList();
		
		for(int i = 0; i < players.size(); i++) {
			EntityPlayer player = players.get(i); 
			
			//force always add
			playersInLobby.add(player);
			playersInGame_Names.add(player.username);
			
			/*if (lobby_distCheck) {
				System.out.println("lobby: " + lobby_tileEntX + "," + lobby_tileEntY + "," + lobby_tileEntZ);
				if (player.getDistance(lobby_tileEntX, lobby_tileEntY, lobby_tileEntZ) < lobby_scanSize) {
					playersInLobby.add(player);
					playersInGame_Names.add(player.username);
				}
			} else {
				if (player.posX > lobby_coord_minX &&
				player.posY > lobby_coord_minY && 
				player.posZ > lobby_coord_minZ && 
				player.posX < lobby_coord_maxX && 
				player.posY < lobby_coord_maxY && 
				player.posZ < lobby_coord_maxZ) {
					playersInLobby.add(player);
					playersInGame_Names.add(player.username);
				}
			}*/
		}
		
		return playersInLobby;
	}
	
	public void initNewGame() {
		
	}
}
