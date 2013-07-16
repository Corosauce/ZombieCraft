package zombiecraft.Forge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SoundLoader {
	
	class ModFilter implements FilenameFilter {
	    public boolean accept(File dir, String name) {
	        return (name.startsWith("ZC 3") && name.endsWith(".zip"));
	    }
	}
	
	@ForgeSubscribe
    public void onSound(SoundLoadEvent event) {
		
		try {
		File modsPath = new File(Minecraft.getMinecraft().getAppDir("") + File.separator + ".minecraft" + File.separator + "mods");
		
		File[] files = modsPath.listFiles(new ModFilter());
		
		if (files != null && files.length > 0) {
			InputStream theFile = new FileInputStream(files[0]);
			ZipInputStream stream = new ZipInputStream(theFile);
			
			ZipEntry ze = stream.getNextEntry();
			
			while (ze != null) {
				String path = ze.getName().substring(ze.getName().indexOf('/', ze.getName().indexOf('/')+1)+1);;
				if (ze.getName().contains("resources/sound/") && ze.getName().contains(".")) {
					registerSound(event.manager, path);
				} else if (ze.getName().contains("resources/music/") && ze.getName().contains(".")) {
					registerMusic(event.manager, path);
				} else if (ze.getName().contains("resources/streaming/") && ze.getName().contains(".")) {
					registerStreaming(event.manager, path);
				}
				ze = stream.getNextEntry();
			}
		} else {
			System.out.println("WARNING: Couldn't find mods zip file for installing sounds.");
		}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
    
    private void registerSound(SoundManager manager, String path) {
        try {
            URL filePath = SoundLoader.class.getResource("/resources/sound/" + path);
            if (filePath != null) {
                manager.soundPoolSounds.addSound(path, filePath);
            } else {
                throw new FileNotFoundException();
            }
        } catch (Exception ex) {
            System.out.println(String.format("Warning: unable to load sound file %s", path));
        }
    }
    
    private void registerStreaming(SoundManager manager, String path) {
        try {
            URL filePath = SoundLoader.class.getResource("/resources/streaming/" + path);
            if (filePath != null) {
                manager.soundPoolStreaming.addSound(path, filePath);
            } else {
                throw new FileNotFoundException();
            }
        } catch (Exception ex) {
            System.out.println(String.format("Warning: unable to load streaming file %s"));
        }
    }
    
    private void registerMusic(SoundManager manager, String path) {
        try {
            URL filePath = SoundLoader.class.getResource("/resources/music/" + path);
            if (filePath != null) {
                manager.soundPoolMusic.addSound(path, filePath);
            } else {
                throw new FileNotFoundException();
            }
        } catch (Exception ex) {
            System.out.println(String.format("Warning: unable to load music file %s"));
        }
    }

}
