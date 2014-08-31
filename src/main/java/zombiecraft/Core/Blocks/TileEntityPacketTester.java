package zombiecraft.Core.Blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import zombiecraft.Forge.ZombieCraftMod;
import CoroUtil.packet.PacketHelper;
import CoroUtil.tile.ITilePacket;
import CoroUtil.tile.TileHandler;
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
		ZombieCraftMod.eventChannel.sendToServer(PacketHelper.createPacketForTEntDWClient(this, "testVal", 9));
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
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    	this.readFromNBTPacket(pkt.func_148857_g());
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBTPacket(var1);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, var1);
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
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
        player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }
}
