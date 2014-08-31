package zombiecraft.Core.Blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import zombiecraft.Client.gui.InventoryWrapper;
import zombiecraft.Core.Buyables;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.World.LevelConfig;
import zombiecraft.Forge.ZombieCraftMod;
import CoroUtil.tile.ITileInteraction;
import CoroUtil.tile.ITilePacket;
import CoroUtil.tile.TileHandler;
import CoroUtil.util.CoroUtilNBT;
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
	//this is to be repurposed as the list used for runtime
	//public List<ItemStack> items = new ArrayList<ItemStack>();
	public InventoryWrapper invHandler = new InventoryWrapper();
	
	//runtime vars
	public boolean cycling = false;
	public int timeRandomizeCur = 0;
	public int timeRandomizeMax;
	public int itemToRenderIndex = 0;
	public int ticksPerCycleCur = 1;
	public int ticksPerCycleMax = 5;
	public int purchaseChanceTimeoutCur = 0;
	public int purchaseChanceTimeoutMax;
	//client side only
	public ItemStack itemToRenderStack = null;
	
	//GUI/Edit stuff
    public NBTTagCompound nbtInfoClient = new NBTTagCompound();
	public NBTTagCompound nbtInfoServer = new NBTTagCompound();
	
	//GUI Elements
	public String nbtStrTimeoutPurchase = "nbtStrTimeoutPurchase";
	public String nbtStrTimeoutRandomize = "nbtStrTimeoutRandomize";
	public String nbtStrInventory = "invItems";
	public int maxInvSlots = 9*4;
	
    public TileEntityMysteryBox() {
    	
    	tileHandler = new TileHandler(this);
    	tileHandler.addObject("renderItemStack", (ItemStack)Buyables.getBuyItem(0));
    	tileHandler.addObject("cycling", Integer.valueOf(0));
    	tileHandler.addObject("purchaseTimeout", Integer.valueOf(purchaseChanceTimeoutCur));
    	
    	//defaults
    	NBTTagCompound tileData = new NBTTagCompound();
    	setDefaults(tileData);
    	nbtInfoServer.setTag("tileData", tileData);
    	updateReferences();
    	//sync(); //needed? getdescpacket might be auto called for whom needs it
    	
    }
    
    public void setDefaults(NBTTagCompound tileData) {
    	tileData.setString(nbtStrTimeoutPurchase, "100");
    	tileData.setString(nbtStrTimeoutRandomize, "80");
    	
    	ItemStack[] defStacks = LevelConfig.getMysteryBoxDefaultItems(maxInvSlots);
    	
    	invHandler.invInitData(defStacks);
    	
    	tileData.setTag(nbtStrInventory, invHandler.invWriteTagList());
    }
    
    public void updateReferences() {
    	NBTTagCompound tileData = nbtInfoServer.getCompoundTag("tileData");
    	
    	purchaseChanceTimeoutMax = Integer.valueOf(tileData.getString(nbtStrTimeoutPurchase));
    	timeRandomizeMax = Integer.valueOf(tileData.getString(nbtStrTimeoutRandomize));
    	
    	//invHandler.invInitData(tileData.getTagList(nbtStrInventory));
    }
    
    public String getData(String name) {
    	if (worldObj.isRemote) {
    		return nbtInfoClient.getCompoundTag("tileData").getString(name);
    	} else {
    		return nbtInfoServer.getCompoundTag("tileData").getString(name);
    	}
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
					if (itemToRenderIndex >= invHandler.getSizeInventory()) itemToRenderIndex = 0;
					int bailCur = 0;
					while (bailCur++ < 100 && invHandler.getStackInSlot(itemToRenderIndex) == null) {
						itemToRenderIndex++;
						if (itemToRenderIndex >= invHandler.getSizeInventory()) itemToRenderIndex = 0;
					}
					
					//System.out.println("itemToRenderIndex: " + itemToRenderIndex);
					tileHandler.updateObject("renderItemStack", invHandler.getStackInSlot(itemToRenderIndex));
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
        nbtInfoServer.setTag("tileData", CoroUtilNBT.copyOntoNBT(nbtInfoServer.getCompoundTag("tileData"), tagCompound.getCompoundTag("tileData")));
        invHandler.invInitData(tagCompound.getTagList(nbtStrInventory), maxInvSlots);
        //readFromNBTPacket(tagCompound);
        updateReferences();
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setTag("tileData", nbtInfoServer.getCompoundTag("tileData"));
        tagCompound.setTag(nbtStrInventory, invHandler.invWriteTagList());
        //writeToNBTPacket(tagCompound);
    }

	@Override
	public void handleClientSentNBT(String parUsername, NBTTagCompound par1nbtTagCompound) {
		if (!ZCGame.instance().isOp(parUsername)) return;
		
		NBTTagCompound nbtPartialClientData = par1nbtTagCompound.getCompoundTag("tileData");
    	NBTTagCompound tempCopyOfServerNBT = nbtInfoServer.getCompoundTag("tileData");
    	tempCopyOfServerNBT = CoroUtilNBT.copyOntoNBT(nbtPartialClientData, tempCopyOfServerNBT);
    	nbtInfoServer.setTag("tileData", tempCopyOfServerNBT);
    	
    	//nbtInfoServer.setTag(nbtStrInventory, par2NBTBase)
    	
    	//assumes data doesnt come in when sync request
		if (par1nbtTagCompound.hasKey("sync")) {
			sync();
		} else {
			updateReferences();
		}
	}
	
	public void sync() {
    	NBTTagCompound nbtForClient = nbtInfoServer;//.getCompoundTag("tileData");
    	if (nbtForClient == null) nbtForClient = new NBTTagCompound();
    	nbtForClient.setBoolean("markUpdated", true);
    	MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtForClient));
    }
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		setClientNBT(pkt.func_148857_g());
    	//this.readFromNBTPacket(pkt.customParam1);
    }
	
	//this one reads from both saving to disk nbt and network nbt
	@SideOnly(Side.CLIENT)
	public void setClientNBT(NBTTagCompound nbt) {
		nbtInfoClient = nbt;
		//ZCGame.nbtInfoSessionClient = nbt;
	}
    
	//pretty sure this isnt needed here
    /*@Override
    public Packet getDescriptionPacket()
    {
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, ZCGame.instance().nbtInfoServer);
    }*/
    
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
	public void handleClientSentDataWatcherList(String parUsername, List parList) {
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
	public void clickedRight(EntityPlayer player, int face, float localVecX, float localVecY, float localVecZ) {
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
		itemToRenderIndex = rand.nextInt(invHandler.getSizeInventory()); //this is where the random is, the end result can be predicted if you are trained enough
		ticksPerCycleCur = 1;
		purchaseChanceTimeoutCur = 0;
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, ZombieCraftMod.modID + ":zc.mysterybox", 1, 1);
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
