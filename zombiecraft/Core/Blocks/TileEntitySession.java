package zombiecraft.Core.Blocks;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import zombiecraft.Core.GameLogic.ZCGame;
import CoroAI.ITilePacket;
import CoroAI.tile.TileHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntitySession extends TileEntity implements ITilePacket
{

	//dead class until visuals or something - session logic relocated to non tile entity
	
	
	
    public TileEntitySession() {
    }
    
	@Override
	public void updateEntity()
	{
		super.updateEntity();
	}
	
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        
        //readFromNBTPacket(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        
        //writeToNBTPacket(tagCompound);
    }

	@Override
	public void handleClientSentNBT(String parUsername, NBTTagCompound par1nbtTagCompound) {
		
	}
	
	
	
	@Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		setClientNBT(pkt.data);
    	//this.readFromNBTPacket(pkt.customParam1);
    }
	
	//redundant?
	@SideOnly(Side.CLIENT)
	public void setClientNBT(NBTTagCompound nbt) {
		//ZCGame.nbtInfoSessionClient = nbt;
	}
    
    @Override
    public Packet getDescriptionPacket()
    {
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, ZCGame.instance().nbtInfoServerSession);
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

	@Override
	public void handleServerSentDataWatcherList(List parList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleClientSentDataWatcherList(String parUsername, List parList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TileHandler getTileHandler() {
		// TODO Auto-generated method stub
		return null;
	}
}
