package zombiecraft.Client;

import java.io.File;

import net.minecraft.client.audio.SoundPool;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.src.ModLoader;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZCClientTicks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;




@SideOnly(Side.CLIENT)
public class ZCSoundManager {

	//keep ticksBeforeMusic high
	
	//load music into streaming channel
	
	//randomize between them somehow getting song lengths for continual play
	
	public int curSoundTicks;
	public int songLengthTicks;
	
	public boolean soundSwapped;
	
	public SoundPool soundPoolMusicBkp;
	public SoundPool zcSoundPool;
	
	public ZCSoundManager() {
		
		
	}
	
	public void tick() {
		
		//music swapper disabled until a proper way is found with new resourcepack system
		
		if (false && ModLoader.getMinecraftInstance().sndManager.sndSystem != null) {
		
			if (ZCClientTicks.mc.thePlayer != null && ZCClientTicks.mc.thePlayer.dimension == ZCGame.ZCDimensionID/*ZCClientTicks.zcGame.gameActive*/) {
				//ZCUtil.setPrivateValueBoth(SoundManager.class, ZCClientTicks.mc.sndManager, "j", "ticksBeforeMusic", SoundManager.MUSIC_INTERVAL);
				
				if (!soundSwapped) {
					soundSwapped = true;
					soundPoolMusicBkp = ZCClientTicks.mc.sndManager.soundPoolMusic;
					//FIRST ISSUE: this is the main issue for this code working, also pretty sure theres some bugs with music swapping, as i recal ZC music in overworld...
					//ZCClientTicks.mc.sndManager.soundPoolMusic = new SoundPool();
					addLevelMusic();
					zcSoundPool = ZCClientTicks.mc.sndManager.soundPoolMusic;
					
					ZCClientTicks.mc.sndManager.sndSystem.stop("BgMusic");
				}
				
				if (ModLoader.getMinecraftInstance().sndManager.sndSystem.playing("BgMusic")) {
					//System.out.println("not playing");
				} else {
					
					if (ZCClientTicks.mc.gameSettings.musicVolume != 0.0F) {
						//get random song
						SoundPoolEntry var1 = ZCClientTicks.mc.sndManager.soundPoolMusic.getRandomSound();
						
						
						
						//music playing code
						if (var1 != null) {
							System.out.println("start playing -> " + var1.func_110458_a());
			                //var1 = ModCompatibilityClient.audioModPickBackgroundMusic(ZCClientTicks.mc.sndManager, var1);
							ModLoader.getMinecraftInstance().sndManager.sndSystem.backgroundMusic("BgMusic", var1.func_110457_b(), var1.func_110458_a(), false);
							ModLoader.getMinecraftInstance().sndManager.sndSystem.setVolume("BgMusic", ZCClientTicks.mc.gameSettings.musicVolume);
							ModLoader.getMinecraftInstance().sndManager.sndSystem.play("BgMusic");
							//SoundManager.sndSystem.play("music.zc");
						} else {
							//System.out.println("FAIL TO PLAY RANDOM MUSIC");
						}
					} else {
						
					}
				}
				
			} else {
				if (soundSwapped) {
					soundSwapped = false;
					//SECOND ISSUE: the final modifier
					//ZCClientTicks.mc.sndManager.soundPoolMusic = soundPoolMusicBkp;
				}
			}
		}
	}
	
	public void addLevelMusic() {
		
		String path = "mod/music/zc/";
		
		//THIRD ISSUE: need new install method copy from other mods
		/*tryInstallSound(path + "Bent and Broken.ogg");
		tryInstallSound(path + "Return of Lazarus.ogg");
		tryInstallSound(path + "The House of Leaves.ogg");*/
		
	}
	
	public void playRandom() {
		
	}
	
	public void playSong(String str) {
		
	}
	
}
