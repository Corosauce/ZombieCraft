package zombiecraft.Core.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import zombiecraft.Core.Buyables;
import zombiecraft.Core.GameLogic.ZCGame;
import CoroAI.ITilePacket;
import CoroAI.tile.ITileInteraction;
import CoroAI.tile.TileHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityMysteryBox extends TileEntity implements ITilePacket, ITileInteraction
{

	//Design plan
	
	//Configs - dont forget, ultimate map config should have a default/customized list to base each mystery boxes defaults / link to list off of
	//items to randomize
	//prices? wat? no, uses maps configured prices
	//randomize time
	//cooldown between usage? nah
	
	//ZC 2 had multiple mystery plates usable beside eachother
	//- sync config between mysteryboxes barrier connection break style
	
	public TileHandler tileHandler;
	
	//configuration vars
	
	//item string list..... needs mapping?
	public List<ItemStack> items = new ArrayList<ItemStack>();
	
	//runtime vars
	public boolean cycling = false;
	public int timeRandomizeCur = 0;
	public int timeRandomizeMax = 80;
	public int itemToRenderIndex = 0;
	public int ticksPerCycleCur = 1;
	public int ticksPerCycleMax = 5;
	public int purchaseChanceTimeoutCur = 0;
	public int purchaseChanceTimeoutMax = 80;
	//client side only
	public ItemStack itemToRenderStack = null;
	
    public TileEntityMysteryBox() {
    	for (int i = 0; i < Buyables.items.size(); i++) {
    		items.add(Buyables.getBuyItem(i));
    	}
    	tileHandler = new TileHandler(this);
    	tileHandler.addObject("renderItemStack", (ItemStack)Buyables.getBuyItem(0));
    	tileHandler.addObject("cycling", Integer.valueOf(0));
    	tileHandler.addObject("purchaseTimeout", Integer.valueOf(purchaseChanceTimeoutCur));
    }
    
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (!worldObj.isRemote) {
			if (cycling) {
				if (timeRandomizeCur > 0) timeRandomizeCur--;
				
				if (worldObj.getWorldTime() % ticksPerCycleCur == 0) {
					itemToRenderIndex++;
					if (itemToRenderIndex >= items.size()) itemToRenderIndex = 0;
					//System.out.println("itemToRenderIndex: " + itemToRenderIndex);
					tileHandler.updateObject("renderItemStack", items.get(itemToRenderIndex));
				}
				
				if (timeRandomizeCur > 40 && timeRandomizeCur % 10 == 0) {
					ticksPerCycleCur++;
					if (ticksPerCycleCur >= ticksPerCycleMax) ticksPerCycleCur = ticksPerCycleMax;
				}
				
				if (timeRandomizeCur == 0) {
					cyclingStop();
				}
			}
			tileHandler.updateObject("cycling", cycling ? 1 : 0);

			if (purchaseChanceTimeoutCur > 0) {
				purchaseChanceTimeoutCur--;
				if (purchaseChanceTimeoutCur <= 0) {
					//naturally resets
				}
			}
			tileHandler.updateObject("purchaseTimeout", purchaseChanceTimeoutCur);
		} else {
			itemToRenderStack = (ItemStack)tileHandler.getObject("renderItemStack");
			cycling = ((Integer)tileHandler.getObject("cycling")) == 1;
			purchaseChanceTimeoutCur = ((Integer)tileHandler.getObject("purchaseTimeout"));
		}
		
		tileHandler.tickUpdate();
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
	public void handleClientSentNBT(NBTTagCompound par1nbtTagCompound) {
		
	}
	
	@Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		setClientNBT(pkt.customParam1);
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
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, ZCGame.instance().nbtInfoServer);
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
		tileHandler.handleServerSentDataWatcherList(parList);
	}

	@Override
	public void handleClientSentDataWatcherList(List parList) {
		//unneeded
	}

	@Override
	public TileHandler getTileHandler() {
		return tileHandler;
	}

	@Override
	public void clickedLeft() {
		if (!worldObj.isRemote) {
			if (canPurchase()) {
				callbackPurchase();
			} else {
				if (!cycling) {
					cyclingStart();
				}
			}
		}
	}

	@Override
	public void clickedRight() {
		if (!worldObj.isRemote) {
			if (canPurchase()) {
				callbackPurchase();
			} else {
				if (!cycling) {
					cyclingStart();
				}
			}
		}
	}
	
	public void cyclingStart() {
		Random rand = new Random();
		cycling = true;
		timeRandomizeCur = timeRandomizeMax;
		itemToRenderIndex = rand.nextInt(items.size()); //this is where the random is, the end result can be predicted if you are trained enough
		ticksPerCycleCur = 1;
		purchaseChanceTimeoutCur = 0;
	}
	
	public void cyclingStop() {
		cycling = false;
		
		//start purchase chance
		purchaseChanceTimeoutCur = purchaseChanceTimeoutMax;
	}
	
	public boolean canPurchase() {
		if (purchaseChanceTimeoutCur > 0) return true;
		return false;
	}
	
	public void callbackPurchase() {
		System.out.println("bought!");
		purchaseChanceTimeoutCur = 0;
	}
}
