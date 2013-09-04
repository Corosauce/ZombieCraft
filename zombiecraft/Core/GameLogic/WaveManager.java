package zombiecraft.Core.GameLogic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import zombiecraft.Core.EnumDifficulty;
import zombiecraft.Core.PacketTypes;
import zombiecraft.Forge.ZCClientTicks;
import zombiecraft.Forge.ZCServerTicks;
import CoroAI.entity.c_EnhAI;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class WaveManager {
	
	public ZCGame zcGame;
	
    public boolean zombHUD;
    public boolean showBuyMenu;
    public boolean showBuyMenu2;
    public Item itemToBuy;
    public Block blockToBuy;
    public int zombieKills;
    public int totalZombieKills;
    public int spawnerCount;
    public int waveSpawnCount;
    public int waveSpawnMax;
    public int debrisX;
    public int debrisY;
    public int debrisZ;
    
    public boolean devMode;
    public boolean buyItem;
    public boolean instaKill;
    public int weapCount;
    public boolean showTrapUse;
    public long trapTimer;
    public int weapCost;
    public boolean showNoPower = false;
    public boolean powerMenu = false;
    public boolean newGUI = false;
    public boolean showSwitchTrap = false;
    public boolean showSwitchPower = false;
    public boolean doneWaveInit = false;
    public boolean newWave;
    public EnumDifficulty difficulty = EnumDifficulty.MEDIUM;
	
	public long wave_StartDelay = 0;
	public int wave_Stage = 0;
	public int wave_Kills = 0;
	public int wave_Invaders_Count_Last = 0;
	public int wave_MaxKills = 0;
	public ArrayList<c_EnhAI> wave_Invaders = new ArrayList<c_EnhAI>();
	public int wave_Invaders_Count = 0;
	
	public int killMaxBase = 10;
	public int killMaxMultiplier = 3;
	
	public float amp_KillMax = 0.5F;
	public float amp_Exp = 0.1F;
	public float amp_Health = 0.05F;
	public float amp_Lunge = 0.005F;
	public float amp_Lunge_Max = 1.25F;
	public float amp_Speed_Max = 0.40F;
	
	@Deprecated
	public float expToPointsFactor = 20F;
	
	public long lastWorldTime;
	
	public static boolean levelNeedsRegen = false;
	public static boolean waitingToStart = false;
	//public boolean gameOver = false;
	public int levelPrepDelay = 0;
	
	public int winCountdown = 100;
	
	public WaveManager(ZCGame game) {
		zcGame = game;
		
	}
	
	public boolean startGameFromPlayer(EntityPlayer player) {
		if (zcGame.mapMan.safeToStart()) {
			zcGame.zcLevel.newGameFrom(player, (int)player.posX, (int)player.posY, (int)player.posZ);
			prepGame();
		} else {
			System.out.println("Not safe to start!");
			return false;
		}
		return true;
	}
	
	public boolean startGameFromPlayerSpawn(EntityPlayer player) {
		if (zcGame.mapMan.safeToStart() || ZCGame.autoload) {
			zcGame.zcLevel.newGameFrom(player, this.zcGame.zcLevel.player_spawnX_world, this.zcGame.zcLevel.player_spawnY_world, this.zcGame.zcLevel.player_spawnZ_world);
			prepGame();
		} else {
			System.out.println("Not safe to start!");
			return false;
		}
		return true;
	}
	
	public boolean startGameFromLobby() {
		if (zcGame.mapMan.safeToStart()) {
			zcGame.zcLevel.newGame();
			prepGame();
		} else {
			System.out.println("Not safe to start!");
			return false;
		}
		return true;
	}
	
	public void prepGame() {
		//zcGame.gameActive = true;
		killInvaders();
		if (levelNeedsRegen) {
			levelNeedsRegen = false;
			
			zcGame.movePlayersToLobby();
			
			if (ZCGame.autostart) {
				waitingToStart = true;
			}
			if (ZCGame.autoload) {
				
				System.out.println("FIX ME I ONLY WORK FOR SCHEMATIC AUTOLOADING - aka not folders or zips?");
				ZCGame.instance().mapMan.curLevel = ZCGame.curLevelOverride;
				
			}
			ZCGame.instance().mapMan.loadLevel();
			ZCGame.instance().zcLevel.buildData.map_coord_minY = ZCGame.ZCWorldHeight;
			ZCGame.instance().mapMan.buildStart(null);
		} else {
			
			zcGame.gameActive = true;
			zcGame.zcLevel.playersInGame = zcGame.getPlayers(zcGame.activeZCDimension);
			zcGame.resetPlayers();
			//startGameFromPlayer(null);//fix for new autostart
			setWaveAndStart(1);
		}
	}
	
	public void levelRegeneratedCallback() {
		
		ZCGame.autoload = false;
		
		
		
		if (waitingToStart) {
			if (ZCGame.autostart) {
				zcGame.zcLevel.playersInGame = zcGame.getPlayers(zcGame.activeZCDimension);//fix for new autostart
				ZCGame.autostart = false;
			}
			
			waitingToStart = false;
			zcGame.gameActive = true;
			zcGame.resetPlayers();
			setWaveAndStart(1);
		}
	}
	
	public void removeExtraEntities() {
		World world = zcGame.getWorld();
		
		for (int i = 0; i < world.loadedEntityList.size(); i++) {
			Entity ent = (Entity)world.loadedEntityList.get(i);
			
			if (ent instanceof EntityPlayer) {
				continue;
			}
			
			ent.setDead();
		}
	}
	
	public void setWave(int stage) {
		wave_Stage = stage;
		if (wave_Stage < 1) wave_Stage = 1;
	}
	
	public void setWaveAndStart(int stage) {
		setWave(stage);
		prepWaveStart();
	}
	
	public void nextWave() {
		wave_Stage++;
	}
	
	public void endWave() {
		//zcGame.playSoundEffect("zc.round_over", null, 2F, 1.0F);
		nextWave();
		prepWaveStart();
	}
	
	public void gameOverWin() {
		gameOver();
		zcGame.updateInfoRun(null, PacketTypes.GAME_END, new int[] {1}, null);
		if (zcGame.debug) System.out.print("Game Over Win");
	}
	
	public void gameOverLose() {
		gameOver();
		zcGame.updateInfoRun(null, PacketTypes.GAME_END, new int[] {0}, null);
		if (zcGame.debug) System.out.print("Game Over Lose");
	}
	
	public void gameOver() {
		levelNeedsRegen = true;
		zcGame.gameActive = false;
		levelPrepDelay = 100; //5 seconds
	}
	
	//gameover style ending
	public void endGame() {
		
	}
	
	public void stopGame() {
		zcGame.gameActive = false;
		wave_Stage = 0;
		killInvaders();
	}
	
	public void killInvaders() {
		killInvaders(true);
	}
	
	public void killInvaders(boolean instaRemove) {
		for (int i = 0; i < wave_Invaders.size(); i++) {
			Entity ent = (Entity)wave_Invaders.get(i);
			if (ent != null) {
				if (instaRemove) {
					ent.setDead();
				} else if (ent instanceof EntityLivingBase) {
					((EntityLivingBase)ent).attackEntityFrom(DamageSource.onFire, 999999); //insta kill the more animated way
					//((EntityLivingBase)ent).setEntityHealth(0);
				}
			}
		}
		wave_Invaders.clear();
	}
	
	public void prepWaveStart() {
		winCountdown = 200;
		wave_StartDelay = 100;
		wave_Kills = 0;
		wave_Invaders.clear();
		
		wave_StartDelay += 10 * wave_Stage;
		if (wave_StartDelay > 250) wave_StartDelay = 250;
		
		int plCount = zcGame.getPlayerCount();
		
		//wave_MaxKills = (int)(killMaxBase + (killMaxBase * (1 + ((plCount-1) * 0.7)) * (amp_KillMax * wave_Stage)));//(int)(killMaxBase + (wave_Stage * amp_KillMax));
		
		//wave_MaxKills = (int)(killMaxBase + (killMaxMultiplier * 1.3 * plCount * wave_Stage));
		
		wave_MaxKills = (int)(killMaxBase + (killMaxMultiplier * wave_Stage));
		
		if (plCount > 1) {
			wave_MaxKills *= plCount * 0.8F;
		}
		
		zcGame.updateInfo(null, PacketTypes.INFO_WAVE, new int[] {wave_Stage, (int)wave_StartDelay, wave_MaxKills, 0});
		
		System.out.println("wave_Stage = " + wave_Stage + " | wave_MaxKills: " + wave_MaxKills + " | plCount: " + plCount + " | wave_StartDelay: " + wave_StartDelay);
	}
	
	public void stop() {
		zcGame.gameActive = false;
	}
	
	public void togglePause() {
		zcGame.gameActive = !zcGame.gameActive;
	}
	
	public boolean cond_WaveEnd() {
		if (wave_Kills >= wave_MaxKills && wave_Invaders_Count == 0) {
			return true;
		}
		return false;
	}
	
	public boolean cond_Win() {
		if (winCountdown == 0) {
			return true;
		}
		return false;
	}
	
	public boolean cond_Lose() {
		boolean allDead = true;
		List<EntityPlayer> players = zcGame.getPlayers();
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).func_110143_aJ() > 0) {
				allDead = false;
			}
		}
		if (allDead) {
			int sdfsdf = 0;
		}
		return allDead;
	}
	
	public void tick() {
		
		World world = null;
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			world = ZCServerTicks.worldRef;
		} else {
			world = ZCClientTicks.worldRef;
		}
		
		if (lastWorldTime == world.getWorldTime()) return;
		lastWorldTime = world.getWorldTime();
		
		if (winCountdown > 0) winCountdown--;
		
		if (!zcGame.isRemote()) {
		
			zcGame.updateWaveInfo();
			
			wave_Invaders_Count = wave_Invaders.size();
			
			if (zcGame.gameActive) {
				if (wave_StartDelay > 0) {
					wave_StartDelay--;
					//System.out.println(wave_StartDelay);
					//If last tick before new wave start
					if (wave_StartDelay == 0) {
						//Init stuff?
					}
				} else {
					if (cond_WaveEnd()) endWave();
					
					/*if (cond_Win()) {
						gameOverWin();
					}*/
					
					if (cond_Lose()) gameOverLose();
					
					
					//must use wave_Invaders.size(), so it can adjust if mob dies while it iterates
					for(int i = 0; i < wave_Invaders.size(); i++) {
						if (wave_Invaders.get(i).isDead) {
							wave_Kills++;
							wave_Invaders.remove(i);
							//System.out.println("wave_Kills = " + wave_Kills);
						} else {
							zcGame.entTick(wave_Invaders.get(i));
						}
					}
				}
			} else {
				if (levelPrepDelay > 0) levelPrepDelay--;
				
				if (!waitingToStart && levelNeedsRegen && levelPrepDelay == 0) {
					//prepGame();
					startGameFromPlayerSpawn(null);
					//zcGame.zcLevel.newGameFrom(this.zcGame.zcLevel.player_spawnX_world, this.zcGame.zcLevel.player_spawnY_world, this.zcGame.zcLevel.player_spawnZ_world);
				}
				
			}
		}
	}
	
}
