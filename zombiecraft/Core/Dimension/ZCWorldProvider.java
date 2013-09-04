package zombiecraft.Core.Dimension;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import zombiecraft.Core.GameLogic.ZCGame;
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
        return false;
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
        int var3 = this.worldObj.getFirstUncoveredBlock(par1, par2);
        return var3 == 0 ? false : Block.blocksList[var3].blockMaterial.blocksMovement();
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
