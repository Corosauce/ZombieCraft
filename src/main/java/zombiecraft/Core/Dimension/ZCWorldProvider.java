package zombiecraft.Core.Dimension;

import CoroUtil.util.CoroUtilBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.World.LevelConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ZCWorldProvider extends WorldProvider
{
    /**
     * creates a new world chunk manager for WorldProvider
     */
	@Override
    public void registerWorldChunkManager()
    {
        this.worldChunkMgr = new ZCWorldChunkManager(ZCBiomeGen.base, 0.5F, 0.0F);
        this.dimensionId = ZCGame.ZCDimensionID;
        this.hasNoSky = false;
    }

    /**
     * Returns the chunk provider back for the world provider
     */
    @Override
    public IChunkProvider createChunkGenerator()
    {
        return new ZCChunkProvider(this.worldObj, this.worldObj.getSeed(), false);
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     */
    /*public float calculateCelestialAngle(long par1, float par3)
    {
        return 0.0F;
    }*/
    
    @SideOnly(Side.CLIENT)

    /**
     * Returns array with sunrise/sunset colors
     */
    @Override
    public float[] calcSunriseSunsetColors(float par1, float par2)
    {
        return null;
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * Return Vec3D with biome specific fog color
     */
    @Override
    public Vec3 getFogColor(float par1, float par2)
    {
    	//TEMP!!!!!!!!!
    	//generateLightBrightnessTable();
    	
    	
        int var3 = 10518688;
        
        var3 = 0x000000;
        
        float var4 = MathHelper.cos(par1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

        
        
        if (var4 < 0.0F)
        {
            var4 = 0.0F;
        }

        if (var4 > 1.0F)
        {
            var4 = 1.0F;
        }

        float var5 = (float)(var3 >> 16 & 255) / 255.0F;
        float var6 = (float)(var3 >> 8 & 255) / 255.0F;
        float var7 = (float)(var3 & 255) / 255.0F;
        var5 *= var4 * 0.0F + 0.15F;
        var6 *= var4 * 0.0F + 0.15F;
        var7 *= var4 * 0.0F + 0.15F;
        return Vec3.createVectorHelper((double)var5, (double)var6, (double)var7);
    }
    
    @Override
    public double getVoidFogYFactor()
    {
    	
    	//double adj = (/*FMLClientHandler.instance().getClient().thePlayer.getHealth()*/20 * 0.0005);
    	
    	//System.out.println(adj);
    	
    	//adj = 0.01D;
    	
    	
    	
        return 0D;//this.terrainType.voidFadeMagnitude();
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * Returns true if the given X,Z coordinate should show environmental fog.
     */
    @Override
    public boolean doesXZShowFog(int par1, int par2)
    {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isSkyColored()
    {
        return true;
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    @Override
    public boolean canRespawnHere()
    {
        return true;
    }
    
    public ChunkCoordinates getSpawnPoint()
    {
    	ZCGame zcGame = ZCGame.instance();
    	
    	if (zcGame != null && zcGame.zcLevel != null) {
    		//this code doesnt appear to return a very accurate result, so its not depended on, watchForPlayerRespawn still manages the final teleport
    		return new ChunkCoordinates(zcGame.zcLevel.lobby_coord_playerX, zcGame.zcLevel.lobby_coord_playerY, zcGame.zcLevel.lobby_coord_playerZ);
    	} else {
    		return super.getSpawnPoint();
    	}
        
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    @Override
    public boolean isSurfaceWorld()
    {
        return true;
    }
    @SideOnly(Side.CLIENT)

    /**
     * the y level at which clouds are rendered.
     */
    @Override
    public float getCloudHeight()
    {
        return 256.0F;
    }

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
    @Override
    public boolean canCoordinateBeSpawn(int par1, int par2)
    {
        Block var3 = this.worldObj.getTopBlock(par1, par2);
        return CoroUtilBlock.isAir(var3) ? false : var3.getMaterial().blocksMovement();
    }
    
    public void generateLightBrightnessTableInt() {
    	this.generateLightBrightnessTable();
    }
    
    @Override
    public float calculateCelestialAngle(long par1, float par3) {
    	long newVal = par1;
    	if (LevelConfig.nbtInfoClientMapConfig.getBoolean(LevelConfig.nbtStrCustomTimeUse)) {
    		String time = LevelConfig.nbtInfoClientMapConfig.getString(LevelConfig.nbtStrCustomTimeVal);
    		
    		if (time.equals("noon")) {
    			newVal = 6000;
    			//6000?
    		} else if (time.equals("midnight")) {
    			newVal = 18000;
    			//18000?
    		} else if (!time.equals("")) {
    			int val = Integer.valueOf(time);
    			if (val < 0) val = 0;
    			if (val > 24000) val = 24000;
    			newVal = val;
    			//parse number
    		} else {
    			return super.calculateCelestialAngle(par1, par3);
    		}
    	}
    	return super.calculateCelestialAngle(newVal, par3);
    }
    
    @Override
    protected void generateLightBrightnessTable()
    {
    	System.out.println("Generating ZC Dim brightness table");
    	String customLighting = LevelConfig.nbtInfoClientMapConfig.getString(LevelConfig.nbtStrCustomLightingMode);
    	if (!LevelConfig.nbtInfoClientMapConfig.getBoolean(LevelConfig.nbtStrCustomLightingUse) || customLighting.equals("")) {
    		//regular
    		super.generateLightBrightnessTable();
    		/*float f = 0.0F;

            for (int i = 0; i <= 15; ++i)
            {
                float f1 = 1.0F - (float)i / 15.0F;
                this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
                
                System.out.println(this.lightBrightnessTable[i]);
            }*/
    	} else if (customLighting.contains(",")) {
    		try {
	    		String strArray = LevelConfig.nbtInfoClientMapConfig.getString(LevelConfig.nbtStrCustomLightingMode);
	    		
	    		String strArrayVals[] = strArray.split(",");
	    		for (int i = 0; i <= 15; ++i)
	            {
	    			this.lightBrightnessTable[i] = Float.valueOf(strArrayVals[i]);
	            }
    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}
    	} else if (customLighting.equals("what")) {
	        float var1 = -0.05F;
	
	        worldObj.lastLightningBolt = 0;
	        
	        for (int var2 = 0; var2 <= 15; ++var2)
	        {
	            float var3 = /*1.0F - */(float)var2 / 15.0F;
	            
	            //normal
	            this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
	            
	            //nether
	            lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
	            
	            //negative light
	            lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F + var1) + var1;
	            
	            float wat = this.worldObj.getWorldTime();
	            
	            
	            
	            float smooth = this.worldObj.getWorldTime() % 200 / 60F;
	            
	            //System.out.println(smooth + " - " + wat);
	            
	            //awesome
	            //float smooth = System.currentTimeMillis() % 100000 / 10000F;
	            
	            
	            
	            //good control on darkening the torches
	            lightBrightnessTable[var2] = 0.0F + (float)Math.sin(var2 * 0.02F);
	            
	            //wacky
	            //lightBrightnessTable[var2] = -0.5F + (float)Math.sin(var2 * smooth);
	            
	            //System.out.println("FINAL: " + lightBrightnessTable[var2]);
	            
	            //lightBrightnessTable[var2] = (float)Math.cos(var2 * 0.11F);//var3 * 0.02F;
	            
	            //static negative light
	            //lightBrightnessTable[var2] = 0.0F;
	            
	            //(float) Math.sin(System.currentTimeMillis());
	        }
    	} else if (customLighting.equals("nether")) {
    		//nether way
            float f = 0.1F;

            for (int i = 0; i <= 15; ++i)
            {
                float f1 = 1.0F - (float)i / 15.0F;
                this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
            }
    	} else {
    		super.generateLightBrightnessTable();
    	}
    }

    /**
     * Gets the hard-coded portal location to use when entering this dimension.
     */
    @Override
    public ChunkCoordinates getEntrancePortalLocation()
    {
        return new ChunkCoordinates(-30, 100, 30);
    }

    @Override
    public int getAverageGroundLevel()
    {
        return ZCGame.ZCWorldHeight;
    }

    /**
     * Returns the dimension's name, e.g. "The End", "Nether", or "Overworld".
     */
    @Override
    public String getDimensionName()
    {
        return "ZombieCraft Realm";
    }
    
    //fail
    @Override
    public boolean canMineBlock(EntityPlayer player, int x, int y, int z)
    {
    	//return ZCUtil.areBlocksMineable;
        return worldObj.canMineBlockBody(player, x, y, z);
    }
}
