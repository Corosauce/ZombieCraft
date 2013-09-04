package zombiecraft.Forge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;
import CoroAI.c_CoroAIUtil;
import CoroAI.util.CoroUtilFile;

public class SoundLoader {
	
	class ModFilter implements FilenameFilter {
	    public boolean accept(File dir, String name) {
	        return (name.startsWith("ZC 3") && name.endsWith(".zip"));
	    }
	}
	
	@ForgeSubscribe
    public void onSound(SoundLoadEvent event) {
		
		//System.out.println("ZOMBIECRAFT MOD SOUND INSTALLING TEMP OFF");
        //if (true) return;
		
		//WHY DO THESE WORK AND ZIP ITERATOR DOESNT?!?!?!?!?
		//registerSound(event.manager, ZombieCraftMod.modID+":zc/ammo.wav");
		//registerSound(event.manager, ZombieCraftMod.modID+":zc/gun/deagle.ogg");
		//System.out.println("WAAAAAAAAAAAAAAAAAAAAAAAAT");
		//System.out.println(ZombieCraftMod.modID+":zc/gun/deagle.ogg");
		
		try {
			c_CoroAIUtil.check();
			File modsPath;
			//was going to do code for mcp sounds but they still arent loaded in
			//but instead, take ZC 3 zip, strip down to just resources folder, add into mcp/jars/mods - sounds will then work
			/*if (c_CoroAIUtil.runningMCP) {
				modsPath = new File("C:" + File.separator + "Users" + File.separator + "Corosus" + File.separator + "AppData" + File.separator + "Roaming" + File.separator + ".minecraft" + File.separator + "mods");
				modsPath = new File("C:\\Users\\Corosus\\AppData\\Roaming\\.minecraft\\mods");
			} else {*/
				modsPath = new File(CoroUtilFile.getSaveFolderPath() + File.separator + "mods");//new File(Minecraft.getMinecraft().getAppDir("") + File.separator + ".minecraft" + File.separator + "mods");
			//}
		
		
		
		
		File[] files = modsPath.listFiles(new ModFilter());
		
		if (files != null && files.length > 0) {
			InputStream theFile = new FileInputStream(files[0]);
			ZipInputStream stream = new ZipInputStream(theFile);
			
			ZipEntry ze = stream.getNextEntry();
			
			while (ze != null) {
				//strips out everything but whats after /sound/ or /music/ or /streaming/
				String path = ZombieCraftMod.modID + ":" + ze.getName().substring(ze.getName().indexOf('/', ze.getName().indexOf('/', ze.getName().indexOf('/')+1)+1)+1);
				//System.out.println(ze.getName());
				//System.out.println(path);
				if (ze.getName().contains("zombiecraft/sound/") && ze.getName().contains(".")) {
					registerSound(event.manager, path);
				} else if (ze.getName().contains("zombiecraft/music/") && ze.getName().contains(".")) {
					registerMusic(event.manager, path);
				} else if (ze.getName().contains("zombiecraft/streaming/") && ze.getName().contains(".")) {
					registerStreaming(event.manager, path);
				}
				ze = stream.getNextEntry();
			}
			
			theFile.close();
			stream.close();
		} else {
			System.out.println("ZC WARNING: Couldn't find mods zip file for installing sounds.");
		}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
    
    private void registerSound(SoundManager manager, String path) {
        try {
        	manager.addSound(path);
        } catch (Exception ex) {
            System.out.println(String.format("Warning: unable to load sound file %s", path));
        }
    }
    
    private void registerStreaming(SoundManager manager, String path) {
        try {
            manager.soundPoolStreaming.addSound(path);
        } catch (Exception ex) {
            System.out.println(String.format("Warning: unable to load streaming file %s", path));
        }
    }
    
    private void registerMusic(SoundManager manager, String path) {
        try {
            manager.soundPoolMusic.addSound(path);
        } catch (Exception ex) {
            System.out.println(String.format("Warning: unable to load music file %s", path));
        }
    }

}
