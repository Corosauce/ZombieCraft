package zombiecraft.Server;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet0KeepAlive;
import net.minecraft.src.Packet101CloseWindow;
import net.minecraft.src.Packet102WindowClick;
import net.minecraft.src.Packet103SetSlot;
import net.minecraft.src.Packet106Transaction;
import net.minecraft.src.Packet107CreativeSetSlot;
import net.minecraft.src.Packet108EnchantItem;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet130UpdateSign;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.Packet16BlockItemSwitch;
import net.minecraft.src.Packet18Animation;
import net.minecraft.src.Packet19EntityAction;
import net.minecraft.src.Packet255KickDisconnect;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.Packet53BlockChange;
import net.minecraft.src.Packet7UseEntity;
import net.minecraft.src.Packet9Respawn;
import net.minecraft.src.Slot;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.WorldServer;

public class NetServerHandler
{
	
    public NetServerHandler()
    {
    	
    }

    /**
     * Adds the packet to the underlying network manager's send queue.
     */
    public void sendPacket(Packet par1Packet)
    {
    	if (true) return;
        //this.netManager.addToSendQueue(par1Packet);
    }
}
