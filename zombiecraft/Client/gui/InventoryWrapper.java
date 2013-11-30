package zombiecraft.Client.gui;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryWrapper implements IInventory {

	public ItemStack[] invList;
	
	public void invInitData(NBTTagList stacks, int bufferSize) {
		//System.out.println("invInitData tag");
		invList = new ItemStack[bufferSize];
		for (int i = 0; i < stacks.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) stacks.tagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < invList.length) {
            	invList[slot] = ItemStack.loadItemStackFromNBT(tag);
            	//System.out.println(invList[slot]);
            }
		}
	}
	
	public void invInitData(ItemStack[] stacks) {
		//System.out.println("invInitData stack");
		invList = stacks;
	}
	
	public NBTTagList invWriteTagList() {
		//System.out.println("invWriteTagList");
		NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < invList.length; i++) {
            ItemStack stack = invList[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
		return itemList;
	}
	
	@Override
	public int getSizeInventory() {
		return invList.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i < invList.length) {
			return invList[i];
		} else {
			return null;
		}
	}

	@Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
		/*System.out.println("-----");
		System.out.println("side: " + FMLCommonHandler.instance().getEffectiveSide());
		System.out.println("inv slot " + slot + " set: " + stack);*/
        invList[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                stack.stackSize = getInventoryStackLimit();
        }              
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
                if (stack.stackSize <= amt) {
                        setInventorySlotContents(slot, null);
                } else {
                        stack = stack.splitStack(amt);
                        if (stack.stackSize == 0) {
                                setInventorySlotContents(slot, null);
                        }
                }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
                setInventorySlotContents(slot, null);
        }
        return stack;
    }
   
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

	@Override
	public String getInvName() {
		return "Inventory Wrapper";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public void onInventoryChanged() {
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

}
