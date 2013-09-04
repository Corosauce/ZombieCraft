package zombiecraft.Core.Blocks;

import java.util.List;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZCServerTicks;
import CoroAI.ITilePacket;
import CoroAI.entity.c_EnhAI;
import CoroAI.tile.TileHandler;
import build.SchematicData;
import build.world.Build;



public class TileEntityMobSpawnerWave extends TileEntity implements SchematicData, ITilePacket
{
    /** The stored delay before a new spawn. */
    public int delay = -1;
    
    public int useDelayFixed = 200;
    public int useDelayRand = 600;

    /**
     * The string ID of the mobs being spawned from this spawner. Defaults to pig, apparently.
     */
    public String mobID = "ZombieCraftMod.EntityZCZombie";
    public String fixPrefix = "ZombieCraftMod.";
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
    public float proxActRange = 32F;
    
    //Activate method 2
    public int waveMin = 0;
    public int waveMax = 999;

    //GUI/Edit stuff
    public NBTTagCompound nbtInfoClient = new NBTTagCompound();
	public NBTTagCompound nbtInfoServer = new NBTTagCompound(); //possibly unneeded because actual values are in tile entity, or use this nbt and write it out entirely, new tiles would have better use for it but sync existing vars to this
    
	public static int CMD_SAVE = 1;
	
    public TileEntityMobSpawnerWave()
    {
        this.delay = 20;
        watchX = xCoord;
		watchY = yCoord+1;
		watchZ = zCoord;
		watchBroken = false;
    }

    public String getMobID()
    {
        return this.mobID;
    }

    public void setMobID(String par1Str)
    {
        this.mobID = par1Str;
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
        return this.worldObj.getClosestPlayer((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D, proxActRange) != null;
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
    	
    	if (!this.worldObj.isRemote)
        {
    		//ServerTickHandler.sendPacketToAll(this.getDescriptionPacket());
	    	if (ZCGame.instance().mapMan.removeForRebuild(this)) {
	    		worldObj.removeBlockTileEntity(xCoord, yCoord, zCoord);		
	    		worldObj.setBlock(xCoord, yCoord, zCoord, 0);
	    	}
        }
    	
    	//act_Watch = true;
    	//this.worldObj.setBlockWithNotify(this.xCoord, yCoord, zCoord, 0);
    	//mobID = "EntityZCZombie";
        this.yaw2 = this.yaw;
        /*if (!watchBroken) */if (this.worldObj.getBlockId(watchX, watchY, watchZ) == 0) { watchBroken = true; } else { watchBroken = false; }

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
                	
                	String mob = mobID;
                	
                	if (ZCServerTicks.zcGame.wMan.wave_Stage % 10 == 0 && this.worldObj.rand.nextInt(60) == 0) {
                		mob = "ZombieCraftMod.EntityZCImp";
                	}
                	
                	EntityLiving var9 = ((EntityLiving)EntityList.createEntityByName(mob, this.worldObj));
                    
                    if (var9 == null) var9 = ((EntityLiving)EntityList.createEntityByName(fixPrefix + mob, this.worldObj));

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
                        	if (var9 instanceof c_EnhAI) {
                        		ZCServerTicks.zcGame.spawnWaveEntity((c_EnhAI)var9);
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
        	this.updateDelay();
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
        this.mobID = par1NBTTagCompound.getString("EntityId");
        this.delay = par1NBTTagCompound.getShort("Delay");
        
		if (build != null) {
			watchX = build.map_coord_minX + par1NBTTagCompound.getShort("watchX");
			watchY = build.map_coord_minY + par1NBTTagCompound.getShort("watchY");
			watchZ = build.map_coord_minZ + par1NBTTagCompound.getShort("watchZ");
		} else {
			watchX = par1NBTTagCompound.getShort("watchX");
			watchY = par1NBTTagCompound.getShort("watchY");
			watchZ = par1NBTTagCompound.getShort("watchZ");
		}
		act_Proximity = par1NBTTagCompound.getBoolean("act_Proximity");
		act_Watch = par1NBTTagCompound.getBoolean("act_Watch");
		act_Wave = par1NBTTagCompound.getBoolean("act_Wave");
		
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
    	readFromNBT(par1NBTTagCompound, null);
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound, Build build)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setString("EntityId", this.mobID);
        par1NBTTagCompound.setShort("Delay", (short)this.delay);
        if (build != null) {
	        par1NBTTagCompound.setShort("watchX", (short)(this.watchX - build.map_coord_minX));
	        par1NBTTagCompound.setShort("watchY", (short)(this.watchY - build.map_coord_minY));
	        par1NBTTagCompound.setShort("watchZ", (short)(this.watchZ - build.map_coord_minZ));
        } else {
        	par1NBTTagCompound.setShort("watchX", (short)this.watchX);
            par1NBTTagCompound.setShort("watchY", (short)this.watchY);
            par1NBTTagCompound.setShort("watchZ", (short)this.watchZ);
        }
        par1NBTTagCompound.setBoolean("act_Proximity", act_Proximity);
        par1NBTTagCompound.setBoolean("act_Watch", act_Watch);
        par1NBTTagCompound.setBoolean("act_Wave", act_Wave);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    	writeToNBT(par1NBTTagCompound, null);
    }
    
    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
    	nbtInfoClient = pkt.customParam1;
    	//this.readFromNBT();
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, var1);
    }

	@Override
	public TileHandler getTileHandler() {
		return null;
	}

	@Override
	public void handleClientSentNBT(NBTTagCompound par1nbtTagCompound) {
		//parse, dont do this
		//nbtInfoServer = par1nbtTagCompound;
		if (par1nbtTagCompound.hasKey("sync")) {
			sync();
		} else if (par1nbtTagCompound.hasKey("cmdID")) {
			int cmdID = par1nbtTagCompound.getInteger("cmdID");
			EntityPlayerMP entP = (EntityPlayerMP)worldObj.getPlayerEntityByName(par1nbtTagCompound.getString("username"));
			
			System.out.println("cmd! " + cmdID);
			
			if (cmdID == CMD_SAVE) {
				
			}
		}
	}
	
	public void sync() {
		
	}

	@Override
	public void handleServerSentDataWatcherList(List parList) {
		
	}

	@Override
	public void handleClientSentDataWatcherList(List parList) {
		
	}
}
