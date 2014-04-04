package zombiecraft.Core.Blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import zombiecraft.Core.towers.TowerBase;
import zombiecraft.Core.towers.TowerMapping;
import CoroUtil.packet.PacketHelper;
import CoroUtil.tile.ITilePacket;
import CoroUtil.tile.TileHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityTower extends TileEntity implements ITilePacket
{
	public TileHandler tileHandler;
	
	public String towerType;
	public TowerBase tower;
	public NBTTagCompound nbtTower;
	
	public boolean firstTimeSync = true;
	public boolean hasLoadedNBT = false;
	public boolean waitingOnExternalInit = true;
	
    public TileEntityTower() {
    	tileHandler = new TileHandler(this);
    	tileHandler.addObject("testVal", Integer.valueOf(3));
    }
    
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (!worldObj.isRemote) {
    		if (hasLoadedNBT) {
				if (firstTimeSync && !waitingOnExternalInit) {
					firstTimeSync = false;
					
					initReady();
				}
				
				if (tower != null) tower.tickUpdate();
    		}
    	}
		
		//tick after code to insta send updates
		tileHandler.tickUpdate();
	}
	
	public void setTowerAndMarkInitReady(String parName) {
		towerType = parName;
		waitingOnExternalInit = false;
	}
    
    public void initReady() {
    	tower = TowerMapping.newTower(towerType);
    	if (nbtTower != null) tower.readFromNBT(nbtTower);
    	tower.init(this);
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        
        var1.setString("buildingNameType", towerType);
        
        NBTTagCompound tag = new NBTTagCompound();
        if (tower != null) {
        	tower.writeToNBT(tag);
        }
        var1.setCompoundTag("buildingData", tag);
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        towerType = var1.getString("buildingNameType");
        nbtTower = var1.getCompoundTag("buildingData");
        
        waitingOnExternalInit = false;
    }
	
	@SideOnly(Side.CLIENT)
	public void handleClientSending() {
		PacketHelper.sendClientPacket(PacketHelper.createPacketForTEntDWClient(this, "testVal", 9));
	}

	@Override
	public TileHandler getTileHandler() {
		return tileHandler;
	}

	@Override
	public void handleServerSentDataWatcherList(List parList) {
		tileHandler.handleServerSentDataWatcherList(parList);
	}
	
	@Override
	public void handleClientSentDataWatcherList(String parUsername, List parList) {
		//do sanity checking here with iteration usage, then manually update?
		
		//this new method works, note, it forces a server to all client in dim update even if the previous value the server had was the same, no biggie since its event based anyways? 
		tileHandler.tileDataWatcher.updateWatchedObjectsFromList(parList, true);
		//tileHandler.tileDataWatcher.setObjectWatched(-1);
		//tileHandler.handleServerSentDataWatcherList(parList);
	}

	@Override
	public void handleClientSentNBT(String parUsername, NBTTagCompound par1nbtTagCompound) {
		
	}
    
    public void readFromNBTPacket(NBTTagCompound tagCompound) {
    	//clTicksBuild = tagCompound.getInteger("ticksBuild");
    }
    
    public void writeToNBTPacket(NBTTagCompound tagCompound) {
    	//tagCompound.setInteger("ticksBuild", ticksBuildCur);
    }
    
    public void sync() {
    	MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayersInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
    }
	
	@Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
    	this.readFromNBTPacket(pkt.data);
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBTPacket(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, var1);
    }
    
	@Override
	public void validate()
	{
		super.validate();
		hasLoadedNBT = true;
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
		cleanup();
	}
	
	public void onBlockBroken()
	{
		cleanup();
	}
	
	public void cleanup() {
		
	}
	
	//@Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
        player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }
}
