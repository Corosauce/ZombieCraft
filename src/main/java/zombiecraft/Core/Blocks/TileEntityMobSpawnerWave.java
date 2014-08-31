package zombiecraft.Core.Blocks;

import java.util.List;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import zombiecraft.Core.Entities.BaseEntAI;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.World.LevelConfig;
import zombiecraft.Forge.ZCServerTicks;
import CoroUtil.tile.ITilePacket;
import CoroUtil.tile.TileHandler;
import CoroUtil.util.CoroUtilBlock;
import CoroUtil.util.CoroUtilNBT;
import build.SchematicData;
import build.world.Build;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;



public class TileEntityMobSpawnerWave extends TileEntity implements SchematicData, ITilePacket
{
    /** The stored delay before a new spawn. */
    public int delay = -1;
    
    public int useDelayFixed = 200;
    public int useDelayRand = 600;
    public boolean useDelayFirstSpawn = true;

    /**
     * The string ID of the mobs being spawned from this spawner. Defaults to pig, apparently.
     */
    public String mobID = ""; //loaded from config
    public String fixPrefix = "ZombieCraftMod.";
    public boolean customSpawn = false;
    public String customSpawnMob = "";
    public double yaw;
    public double yaw2 = 0.0D;
    
    //Modes used for activation conditions
    public boolean act_Watch = false;
    public boolean act_Proximity = false;
    public boolean act_Wave = false;
    
    //Activate method 0
    public int watchX;
    public int watchY;
    public int watchZ;
    public boolean watchBroken;
    
    //Activate method 1
    public int proxActRange = 32;
    
    //Activate method 2
    public int waveMin;
    public int waveMax;

    //GUI/Edit stuff
    public NBTTagCompound nbtInfoClient = new NBTTagCompound();
	public NBTTagCompound nbtInfoServer = new NBTTagCompound(); //possibly unneeded because actual values are in tile entity, or use this nbt and write it out entirely, new tiles would have better use for it but sync existing vars to this
    
	public static int CMD_SAVE = 1;
	
	public String CMD_GUIMODE_STR_PROXIMITY = "proximity";
	public String CMD_GUIMODE_STR_WAVE = "wave";
	public String CMD_GUIMODE_STR_WATCH = "watch";
	
	//main gui fields
	public int CMD_BOOL_PROXIMITY = 2;
	public int CMD_BOOL_WAVE = 3;
	public int CMD_BOOL_WATCH = 4;
	public int CMD_BOOL_CUSTOMSPAWNING = 8;
	public int CMD_BOOL_USEDELAYFIRSTSPAWN = 9;
	
	public int CMD_CONF_PROXIMITY = 5;
	public int CMD_CONF_WAVE = 6;
	public int CMD_CONF_WATCH = 7;
	
	public String CMD_BOOL_CUSTOMSPAWNING_STR = "useCustomSpawn";
	public String CMD_BOOL_ACTPROX = "act_Proximity";
	public String CMD_BOOL_ACTWATCH = "act_Watch";
	public String CMD_BOOL_ACTWAVE = "act_Wave";
	public String CMD_BOOL_USEDELAYFIRSTSPAWN_STR = "useDelayFirstSpawn";
	
	public String nbtStrProxDist = "proximity_dist";
	public String nbtStrCustomSpawn = "customSpawn";
	public String nbtStrWatchX = "watchX";
	public String nbtStrWatchY = "watchY";
	public String nbtStrWatchZ = "watchZ";
	public String nbtStrWaveMin = "waveMin";
	public String nbtStrWaveMax = "waveMax";
	public String nbtStrSpawnDelay = "spawnDelay";
	public String nbtStrSpawnDelayRand = "spawnDelayRand";
	
	
    public TileEntityMobSpawnerWave()
    {
        this.delay = 20;
        watchX = xCoord;
		watchY = yCoord+1;
		watchZ = zCoord;
		watchBroken = false;
		
		NBTTagCompound tileData = new NBTTagCompound();
    	setDefaults(tileData);
    	nbtInfoServer.setTag("tileData", tileData);
    	
    	updateReferences(tileData);
    }
    
	public void setDefaults(NBTTagCompound tileData) {
		tileData.setString(nbtStrProxDist, "32");
    	tileData.setString(nbtStrWatchX, "0");
    	tileData.setString(nbtStrWatchY, "0");
    	tileData.setString(nbtStrWatchZ, "0");
    	tileData.setString(nbtStrWaveMin, "0");
    	tileData.setString(nbtStrWaveMax, "999");
    	tileData.setString(nbtStrSpawnDelay, "600");
    	tileData.setString(nbtStrSpawnDelayRand, "200");
    	tileData.setString(nbtStrCustomSpawn, customSpawnMob);
    	
    	tileData.setBoolean(CMD_BOOL_ACTPROX, false);
    	tileData.setBoolean(CMD_BOOL_ACTWAVE, false);
    	tileData.setBoolean(CMD_BOOL_ACTWATCH, false);
    	tileData.setBoolean(CMD_BOOL_CUSTOMSPAWNING_STR, false);
    	tileData.setBoolean(CMD_BOOL_USEDELAYFIRSTSPAWN_STR, true);
    	
    }
    
    public void updateReferences(NBTTagCompound tileData) {
    	//NBTTagCompound tileData = nbt.getCompoundTag("tileData");

    	if (tileData.hasKey(CMD_BOOL_ACTPROX)) act_Proximity = tileData.getBoolean(CMD_BOOL_ACTPROX);
    	if (tileData.hasKey(CMD_BOOL_ACTWATCH)) act_Watch = tileData.getBoolean(CMD_BOOL_ACTWATCH);
    	if (tileData.hasKey(CMD_BOOL_ACTWAVE)) act_Wave = tileData.getBoolean(CMD_BOOL_ACTWAVE);
    	if (tileData.hasKey(CMD_BOOL_CUSTOMSPAWNING_STR)) customSpawn = tileData.getBoolean(CMD_BOOL_CUSTOMSPAWNING_STR);
    	if (tileData.hasKey(CMD_BOOL_USEDELAYFIRSTSPAWN_STR)) useDelayFirstSpawn = tileData.getBoolean(CMD_BOOL_USEDELAYFIRSTSPAWN_STR);
    	
    	if (tileData.hasKey(nbtStrProxDist)) proxActRange = Integer.valueOf(tileData.getString(nbtStrProxDist));
    	if (tileData.hasKey(nbtStrWatchX)) watchX = Integer.valueOf(tileData.getString(nbtStrWatchX));
    	if (tileData.hasKey(nbtStrWatchY)) watchY = Integer.valueOf(tileData.getString(nbtStrWatchY));
    	if (tileData.hasKey(nbtStrWatchZ)) watchZ = Integer.valueOf(tileData.getString(nbtStrWatchZ));
    	if (tileData.hasKey(nbtStrWaveMin)) waveMin = Integer.valueOf(tileData.getString(nbtStrWaveMin));
    	if (tileData.hasKey(nbtStrWaveMax)) waveMax = Integer.valueOf(tileData.getString(nbtStrWaveMax));
    	if (tileData.hasKey(nbtStrCustomSpawn)) customSpawnMob = tileData.getString(nbtStrCustomSpawn);
    	if (tileData.hasKey(nbtStrSpawnDelay)) useDelayFixed = Integer.valueOf(tileData.getString(nbtStrSpawnDelay));
    	if (tileData.hasKey(nbtStrSpawnDelayRand)) useDelayRand = Integer.valueOf(tileData.getString(nbtStrSpawnDelayRand));
    	//System.out.println("side: " + FMLCommonHandler.instance().getEffectiveSide() + " - useDelayRand: " + useDelayRand);
    	if (useDelayRand == 0 && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
    		int catchme = 0;
    	}
    }
	
	public void updateServerNBTForSync() {
		nbtInfoServer.setBoolean("markUpdated", true);
		
		NBTTagCompound tileData = new NBTTagCompound();
		
		tileData.setBoolean(CMD_BOOL_ACTPROX, act_Proximity);
		tileData.setBoolean(CMD_BOOL_ACTWATCH, act_Watch);
		tileData.setBoolean(CMD_BOOL_ACTWAVE, act_Wave);
		tileData.setBoolean(CMD_BOOL_CUSTOMSPAWNING_STR, customSpawn);
		tileData.setBoolean(CMD_BOOL_USEDELAYFIRSTSPAWN_STR, useDelayFirstSpawn);
		
		tileData.setString(nbtStrProxDist, String.valueOf(proxActRange));
		tileData.setString(nbtStrWatchX, String.valueOf(this.watchX));
		tileData.setString(nbtStrWatchY, String.valueOf(this.watchY));
		tileData.setString(nbtStrWatchZ, String.valueOf(this.watchZ));
		tileData.setString(nbtStrWaveMin, String.valueOf(this.waveMin));
		tileData.setString(nbtStrWaveMax, String.valueOf(this.waveMax));
		tileData.setString(nbtStrSpawnDelay, String.valueOf(this.useDelayFixed));
		tileData.setString(nbtStrSpawnDelayRand, String.valueOf(this.useDelayRand));
		tileData.setString(nbtStrCustomSpawn, customSpawnMob);
		
		nbtInfoServer.setTag("tileData", tileData);
	}

    public String getMobID()
    {
        return this.mobID;
    }
    
    public void setWatchCoords(int x, int y, int z) {
    	watchX = x;
    	watchY = y;
    	watchZ = z;
    	act_Watch = true;
    	sync();
    }
    
    public boolean isActive() {
    	
    	//non optional conditions
    	if (ZCServerTicks.zcGame.gameActive
		&& ZCServerTicks.zcGame.wMan.wave_StartDelay == 0
		&& ZCServerTicks.zcGame.wMan.wave_Kills + ZCServerTicks.zcGame.wMan.wave_Invaders.size() < ZCServerTicks.zcGame.wMan.wave_MaxKills) {
    	
    		boolean isActive = true;
    		
	    	if (act_Watch && !watchBroken) isActive = false;
	    	
	    	if (act_Proximity && !anyPlayerInRange()) isActive = false;
	    	
	    	if (act_Wave && (ZCServerTicks.zcGame.wMan.wave_Stage < waveMin || ZCServerTicks.zcGame.wMan.wave_Stage > waveMax)) isActive = false;
	    	
	    	return isActive;
    	}
    	
    	return false;
    }

    /**
     * Returns true if there is a player in range (using World.getClosestPlayer)
     */
    public boolean anyPlayerInRange()
    {
        return this.worldObj.getClosestPlayer((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D, proxActRange+1) != null;
    }
    
    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
    	//this was crashing game
    	//ZCGame.wMan.winCountdown = 20;
    	
    	//temp
    	//mobID = "ZombieCraftMod.EntityZCImp";
    	
    	if (worldObj.isRemote) return;
    	
    	if (!this.worldObj.isRemote)
        {
    		//ServerTickHandler.sendPacketToAll(this.getDescriptionPacket());
	    	if (ZCGame.instance().mapMan.removeForRebuild(this)) {
	    		worldObj.removeTileEntity(xCoord, yCoord, zCoord);		
	    		worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air);
	    	}
        }
    	
    	//act_Watch = true;
    	//this.worldObj.setBlockWithNotify(this.xCoord, yCoord, zCoord, 0);
    	//mobID = "EntityZCZombie";
        this.yaw2 = this.yaw;
        /*if (!watchBroken) */if (CoroUtilBlock.isAir(this.worldObj.getBlock(watchX, watchY, watchZ))) { watchBroken = true; } else { watchBroken = false; }

        if (isActive())
        {
        	
        	
            double var1 = (double)((float)this.xCoord + this.worldObj.rand.nextFloat());
            double var3 = (double)((float)this.yCoord + this.worldObj.rand.nextFloat());
            double var5 = (double)((float)this.zCoord + this.worldObj.rand.nextFloat());
            this.worldObj.spawnParticle("smoke", var1, var3, var5, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle("flame", var1, var3, var5, 0.0D, 0.0D, 0.0D);

            for (this.yaw += (double)(1000.0F / ((float)this.delay + 200.0F)); this.yaw > 360.0D; this.yaw2 -= 360.0D)
            {
                this.yaw -= 360.0D;
            }

            if (!this.worldObj.isRemote)
            {
                if (this.delay == -1)
                {
                    this.updateDelay();
                }

                if (this.delay > 0)
                {
                    --this.delay;
                    return;
                }
                
                int var7 = 3;
                
                int plCount = ZCGame.instance().getPlayerCount();
                
                if (plCount > 1) {
                	var7 *= plCount * 0.8F;
                }

                for (int var8 = 0; var8 < var7; ++var8)
                {
                	
                	EntityLiving var9 = null;
                	
                	if (customSpawn) {
                		boolean failed = false;
                		var9 = ((EntityLiving)EntityList.createEntityByName(customSpawnMob, this.worldObj));
                		
                		if (var9 == null) {
                			var9 = ((EntityLiving)EntityList.createEntityByName(fixPrefix + customSpawnMob, this.worldObj));
                			if (var9 == null) {
                				failed = true;
                			}
                		}
                		
                		if (failed) {
                			System.out.println("ZC SPAWN ERROR: FAILED TO SPAWN CUSTOM MOB: " + customSpawnMob);
                		}
                	} else {
                		mobID = LevelConfig.get(LevelConfig.nbtStrWaveDefaultMobSpawned);
	                	String mob = mobID;
	                	
	                	if (ZCServerTicks.zcGame.wMan.wave_Stage % 10 == 0 && this.worldObj.rand.nextInt(60) == 0) {
	                		mob = "ZombieCraftMod.EntityZCImp";
	                	}
	                	
	                	var9 = ((EntityLiving)EntityList.createEntityByName(mob, this.worldObj));
	                    
	                    if (var9 == null) var9 = ((EntityLiving)EntityList.createEntityByName(fixPrefix + mob, this.worldObj));
                	}

                    if (var9 == null)
                    {
                        return;
                    }

                    int var10 = this.worldObj.getEntitiesWithinAABB(var9.getClass(), AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).expand(8.0D, 4.0D, 8.0D)).size();

                    if (var10 >= 6)
                    {
                        this.updateDelay();
                        return;
                    }

                    if (var9 != null)
                    {
                        double var11 = (double)this.xCoord + (this.worldObj.rand.nextDouble() - this.worldObj.rand.nextDouble()) * 1.5D;
                        double var13 = (double)(this.yCoord + this.worldObj.rand.nextInt(3) - 1);
                        double var15 = (double)this.zCoord + (this.worldObj.rand.nextDouble() - this.worldObj.rand.nextDouble()) * 1.5D;
                        var9.setLocationAndAngles(var11, var13, var15, this.worldObj.rand.nextFloat() * 360.0F, 0.0F);

                        if (var9.getCanSpawnHere())
                        {
                        	if (var9 instanceof BaseEntAI) {
                        		ZCServerTicks.zcGame.spawnWaveEntity((BaseEntAI)var9);
                        	} else {
                        		this.worldObj.spawnEntityInWorld(var9);
                        		this.worldObj.playAuxSFX(2004, this.xCoord, this.yCoord, this.zCoord, 0);
                                var9.spawnExplosionParticle();
                        	}
                            this.updateDelay();
                        }
                    }
                }
            }

            super.updateEntity();
        } else {
        	if (useDelayFirstSpawn) {
        		this.updateDelay();
        	} else {
        		delay = 1;
        	}
        }
    }

    /**
     * Sets the delay before a new spawn (base delay of 200 + random number up to 600).
     */
    private void updateDelay()
    {
    	
        this.delay = useDelayFixed + this.worldObj.rand.nextInt(useDelayRand);
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound, Build build)
    {
        super.readFromNBT(par1NBTTagCompound);
        //this.mobID = par1NBTTagCompound.getString("EntityId");
        this.delay = par1NBTTagCompound.getShort("Delay");
        
		if (build != null) {
			try {
			watchX = build.map_coord_minX + par1NBTTagCompound.getInteger(nbtStrWatchX);
			watchY = build.map_coord_minY + par1NBTTagCompound.getInteger(nbtStrWatchY);
			watchZ = build.map_coord_minZ + par1NBTTagCompound.getInteger(nbtStrWatchZ);
			} catch (Exception ex) {
				//System.out.println("catching cast crash for spawner wave, switching datatype to Short");
				watchX = build.map_coord_minX + par1NBTTagCompound.getShort(nbtStrWatchX);
				watchY = build.map_coord_minY + par1NBTTagCompound.getShort(nbtStrWatchY);
				watchZ = build.map_coord_minZ + par1NBTTagCompound.getShort(nbtStrWatchZ);
			}
		} else {
			try {
				watchX = par1NBTTagCompound.getInteger(nbtStrWatchX);
				watchY = par1NBTTagCompound.getInteger(nbtStrWatchY);
				watchZ = par1NBTTagCompound.getInteger(nbtStrWatchZ);
			} catch (Exception ex) {
				//System.out.println("catching cast crash for spawner wave, switching datatype to Short");
				watchX = par1NBTTagCompound.getShort(nbtStrWatchX);
				watchY = par1NBTTagCompound.getShort(nbtStrWatchY);
				watchZ = par1NBTTagCompound.getShort(nbtStrWatchZ);
			}
		}
		if (par1NBTTagCompound.hasKey(CMD_BOOL_ACTPROX)) act_Proximity = par1NBTTagCompound.getBoolean(CMD_BOOL_ACTPROX);
		if (par1NBTTagCompound.hasKey(CMD_BOOL_ACTWATCH)) act_Watch = par1NBTTagCompound.getBoolean(CMD_BOOL_ACTWATCH);
		if (par1NBTTagCompound.hasKey(CMD_BOOL_ACTWAVE)) act_Wave = par1NBTTagCompound.getBoolean(CMD_BOOL_ACTWAVE);
		if (par1NBTTagCompound.hasKey(CMD_BOOL_CUSTOMSPAWNING_STR)) customSpawn = par1NBTTagCompound.getBoolean(CMD_BOOL_CUSTOMSPAWNING_STR);
		if (par1NBTTagCompound.hasKey(nbtStrCustomSpawn)) customSpawnMob = par1NBTTagCompound.getString(nbtStrCustomSpawn);
		if (par1NBTTagCompound.hasKey(nbtStrProxDist)) proxActRange = par1NBTTagCompound.getInteger(nbtStrProxDist);
		if (par1NBTTagCompound.hasKey(nbtStrWaveMin)) waveMin = par1NBTTagCompound.getInteger(nbtStrWaveMin);
		if (par1NBTTagCompound.hasKey(nbtStrWaveMax)) waveMax = par1NBTTagCompound.getInteger(nbtStrWaveMax);
		if (par1NBTTagCompound.hasKey(nbtStrSpawnDelay)) useDelayFixed = par1NBTTagCompound.getInteger(nbtStrSpawnDelay);
		if (par1NBTTagCompound.hasKey(nbtStrSpawnDelayRand)) {
			useDelayRand = par1NBTTagCompound.getInteger(nbtStrSpawnDelayRand);
		}
		if (par1NBTTagCompound.hasKey(CMD_BOOL_USEDELAYFIRSTSPAWN_STR)) useDelayFirstSpawn = par1NBTTagCompound.getBoolean(CMD_BOOL_USEDELAYFIRSTSPAWN_STR);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
    	readFromNBT(par1NBTTagCompound, null);
    	sync();
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound, Build build)
    {
        super.writeToNBT(par1NBTTagCompound);
        //par1NBTTagCompound.setString("EntityId", this.mobID);
        par1NBTTagCompound.setShort("Delay", (short)this.delay);
        if (build != null) {
	        par1NBTTagCompound.setInteger(nbtStrWatchX, (short)(this.watchX - build.map_coord_minX));
	        par1NBTTagCompound.setInteger(nbtStrWatchY, (short)(this.watchY - build.map_coord_minY));
	        par1NBTTagCompound.setInteger(nbtStrWatchZ, (short)(this.watchZ - build.map_coord_minZ));
        	
        } else {
        	par1NBTTagCompound.setInteger(nbtStrWatchX, (short)this.watchX);
            par1NBTTagCompound.setInteger(nbtStrWatchY, (short)this.watchY);
            par1NBTTagCompound.setInteger(nbtStrWatchZ, (short)this.watchZ);
        }
        par1NBTTagCompound.setBoolean(CMD_BOOL_ACTPROX, act_Proximity);
        par1NBTTagCompound.setBoolean(CMD_BOOL_ACTWATCH, act_Watch);
        par1NBTTagCompound.setBoolean(CMD_BOOL_ACTWAVE, act_Wave);
        par1NBTTagCompound.setString(nbtStrCustomSpawn, customSpawnMob);
        par1NBTTagCompound.setBoolean(CMD_BOOL_CUSTOMSPAWNING_STR, customSpawn);
        par1NBTTagCompound.setInteger(nbtStrProxDist, proxActRange);
        par1NBTTagCompound.setInteger(nbtStrWaveMin, waveMin);
        par1NBTTagCompound.setInteger(nbtStrWaveMax, waveMax);
        par1NBTTagCompound.setInteger(nbtStrSpawnDelay, useDelayFixed);
        par1NBTTagCompound.setInteger(nbtStrSpawnDelayRand, useDelayRand);
        par1NBTTagCompound.setBoolean(CMD_BOOL_USEDELAYFIRSTSPAWN_STR, useDelayFirstSpawn);
    }
    
    public String getData(String name) {
    	if (worldObj.isRemote) {
    		return nbtInfoClient.getCompoundTag("tileData").getString(name);
    	} else {
    		return nbtInfoServer.getCompoundTag("tileData").getString(name);
    	}
	}
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    	writeToNBT(par1NBTTagCompound, null);
    }
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    	updateServerNBTForSync();
    	nbtInfoClient = pkt.func_148857_g();
    	updateReferences(nbtInfoClient.getCompoundTag("tileData"));
    	//this.readFromNBT();
    }
    
    //this might actually be needed for this tile entity, since it has client side visual things to show and this automatic syncing way could help
    @Override
    public Packet getDescriptionPacket()
    {
    	updateServerNBTForSync();
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtInfoServer);
    }

	@Override
	public TileHandler getTileHandler() {
		return null;
	}

	@Override
	public void handleClientSentNBT(String parUsername, NBTTagCompound par1nbtTagCompound) {
		if (!ZCGame.instance().isOp(parUsername)) return;
		
		NBTTagCompound nbtPartialClientData = par1nbtTagCompound.getCompoundTag("tileData");
    	NBTTagCompound tempCopyOfServerNBT = nbtInfoServer.getCompoundTag("tileData");
    	tempCopyOfServerNBT = CoroUtilNBT.copyOntoNBT(nbtPartialClientData, tempCopyOfServerNBT);
    	nbtInfoServer.setTag("tileData", tempCopyOfServerNBT);
    	
    	//assumes data doesnt come in when sync request
		if (par1nbtTagCompound.hasKey("sync")) {
			sync();
		} else {
			updateReferences(nbtInfoServer.getCompoundTag("tileData"));
		}
		
		
		/*if (!ZCGame.instance().isOp(parUsername)) return;
		
		if (par1nbtTagCompound.hasKey("cmdID")) {
			int cmdID = par1nbtTagCompound.getInteger("cmdID");
			String guiCur = par1nbtTagCompound.getString("guiCur");
			EntityPlayerMP entP = (EntityPlayerMP)worldObj.getPlayerEntityByName(par1nbtTagCompound.getString("username"));
			
			//issue with gui side sending packets without the data in it, due to it requiring the element to be used to have it set, work out a better solution instead of hasKey check...
			if (guiCur.equals("main")) {
				if (par1nbtTagCompound.hasKey(nbtStrCustomSpawn)) customSpawnMob = par1nbtTagCompound.getString(nbtStrCustomSpawn);
				if (par1nbtTagCompound.hasKey("cmdBool_" + CMD_BOOL_CUSTOMSPAWNING)) customSpawn = par1nbtTagCompound.getBoolean("cmdBool_" + CMD_BOOL_CUSTOMSPAWNING);
			} else if (guiCur.equals(this.CMD_GUIMODE_STR_PROXIMITY)) {
				if (par1nbtTagCompound.hasKey("cmdBool_" + CMD_BOOL_PROXIMITY)) act_Proximity = par1nbtTagCompound.getBoolean("cmdBool_" + CMD_BOOL_PROXIMITY);
				if (par1nbtTagCompound.hasKey(nbtStrProxDist)) proxActRange = Integer.valueOf("0" + par1nbtTagCompound.getString(nbtStrProxDist));
			} else if (guiCur.equals(this.CMD_GUIMODE_STR_WATCH)) {
				if (par1nbtTagCompound.hasKey("cmdBool_" + CMD_BOOL_WATCH)) act_Watch = par1nbtTagCompound.getBoolean("cmdBool_" + CMD_BOOL_WATCH);
				if (par1nbtTagCompound.hasKey(nbtStrWatchX)) watchX = Integer.valueOf("0" + par1nbtTagCompound.getString(nbtStrWatchX));
				if (par1nbtTagCompound.hasKey(nbtStrWatchY)) watchY = Integer.valueOf("0" + par1nbtTagCompound.getString(nbtStrWatchY));
				if (par1nbtTagCompound.hasKey(nbtStrWatchZ)) watchZ = Integer.valueOf("0" + par1nbtTagCompound.getString(nbtStrWatchZ));
			} else if (guiCur.equals(this.CMD_GUIMODE_STR_WAVE)) {
				if (par1nbtTagCompound.hasKey("cmdBool_" + CMD_BOOL_WAVE)) act_Wave = par1nbtTagCompound.getBoolean("cmdBool_" + CMD_BOOL_WAVE);
			}
			
			if (par1nbtTagCompound.hasKey("sync")) {
				sync();
			}
			
			//System.out.println("act_Watch! " + act_Watch);
			
			if (cmdID == CMD_SAVE) {
				
			}
		}*/
	}
	
	@Override
	public void validate() {
		super.validate();
		
	}
	
	public void sync() {
    	MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(getDescriptionPacket());
    }

	@Override
	public void handleServerSentDataWatcherList(List parList) {
		
	}

	@Override
	public void handleClientSentDataWatcherList(String parUsername, List parList) {
		
	}
}
