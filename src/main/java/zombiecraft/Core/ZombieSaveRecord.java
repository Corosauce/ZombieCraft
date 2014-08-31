package zombiecraft.Core;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.zip.ZipFile;

public class ZombieSaveRecord
    implements Serializable, Comparable
{
	public String text;
    public String subText;	
    public String extraInfo;
    public String zipname;
    public File worldFile;
	public int type; //0 = folder, 1 = zip, 2 = schematic
	
	//public int tex = 0;

    public ZombieSaveRecord(File f1, String s2, int parType)
    {
		zipname = s2;
		worldFile = new File(f1, s2);
		type = parType;
		load();
    }

    public boolean func_22015_a()
    {
        return true;
    }

    public String getSubText()
    {
        return subText;
    }

    public String getExtraInfo()
    {
        return extraInfo;
    }

    public String getText()
    {
        return text;
    }
	
	public String getZipname()
    {
        return zipname;
    }

    public void setText(String s)
    {
        text = s;
    }
	
	public void setZipname(String s)
    {
        zipname = s;
    }

    public void setSubText(String s)
    {
        subText = s;
    }   
	
	public void setSubText2(String s)
    {
        extraInfo = s;
    }
	
	public void load()
	{
		
		subText = "\u00A7" + '2' + "Folder Map";
        if (type == 1) {
        	subText = "\u00A7" + '4' + "Packaged Map";
        } else if (type == 2) { subText = "\u00A7" + 'e' + "Schematic File"; }
		
		InputStream inputstream = null;
	
		try
        {
			if (type == 0) {
				File file = new File(worldFile.getPath() + "/" + "info.txt");
				//System.out.println(worldFile.getPath() + "/" + "map.png");
				//tex = FMLClientHandler.instance().getClient().renderEngine.getTexture(worldFile.getPath() + "/" + "map.png");
				inputstream = new FileInputStream(file);
				readInfo(inputstream);
			} else if (type == 1) {
				ZipFile zipfile = new ZipFile(worldFile);
	            try
	            {
	            	//tex = FMLClientHandler.instance().getClient().renderEngine.getTexture(worldFile.getPath() + "/" + "map.png");
	                inputstream = zipfile.getInputStream(zipfile.getEntry("info.txt"));
	                readInfo(inputstream);
	            }
	            catch(Exception exception) { }
	            try
	            {
	                zipfile.close();
	            }
	            catch(Exception exception5) { }
			} else if (type == 2) {
				text = worldFile.getName().substring(0, worldFile.getName().length() - 10);
			}
		
		}
        catch(Exception exception2)
        {
            exception2.printStackTrace();
        }
        finally
        {
            try
            {
                inputstream.close();
            }
            catch(Exception exception4) { }
            
        }
	}
	
	public void readInfo(InputStream is) {
		
		try {
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(is));
	        text = bufferedreader.readLine();
	        
	        String buffer, result = "";
	        while ((buffer = bufferedreader.readLine()) != null) {
	        	result = result + buffer + "\u00A7f\n";
	        }
	        extraInfo = result;
	        bufferedreader.close();
	        is.close();
		}
        catch(Exception exception2)
        {
            exception2.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(Exception exception4) { }
        }
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return type-((ZombieSaveRecord)o).type;
	}
}
