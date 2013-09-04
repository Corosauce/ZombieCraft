package zombiecraft.Core.Blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import CoroAI.ITilePacket;
import CoroAI.tile.PacketHelper;
import CoroAI.tile.TileHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityPacketTester extends TileEntity implements ITilePacket
{
	public TileHandler tileHandler;
	
    public TileEntityPacketTester() {
    	tileHandler = new TileHandler(this);
    	tileHandler.addObject("testVal", Integer.valueOf(3));
    }
    
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (worldObj.getWorldTime() % 40 == 0) {
			if (worldObj.isRemote) {
				System.out.println(this + " client value: " + tileHandler.getObject("testVal"));
				//handleClientSending();
			} else {
				System.out.println(this + " server value: " + tileHandler.getObject("testVal"));
				//tileHandler.updateObject("testVal", 2/*worldObj.rand.nextInt(10)*/);
			}
		}
		
		//tick after code to insta send updates
		tileHandler.tickUpdate();
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
	public void handleClientSentDataWatcherList(List parList) {
		//do sanity checking here with iteration usage, then manually update?
		
		//this new method works, note, it forces a server to all client in dim update even if the previous value the server had was the same, no biggie since its event based anyways? 
		tileHandler.tileDataWatcher.updateWatchedObjectsFromList(parList, true);
		//tileHandler.tileDataWatcher.setObjectWatched(-1);
		//tileHandler.handleServerSentDataWatcherList(parList);
	}

	@Override
	public void handleClientSentNBT(NBTTagCompound par1nbtTagCompound) {
		
	}
	
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        
        readFromNBTPacket(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        
        writeToNBTPacket(tagCompound);
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
    	this.readFromNBTPacket(pkt.customParam1);
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
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
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
