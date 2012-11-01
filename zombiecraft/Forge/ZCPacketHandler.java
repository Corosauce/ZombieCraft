package zombiecraft.Forge;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.Blocks.*;

import net.minecraft.src.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ZCPacketHandler implements IPacketHandler
{
    public ZCPacketHandler()
    {
    }

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
    	
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
    	
    	
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.data));

        try
        {
        
	        if ("Data".equals(packet.channel))
	        {
	            
	            	int what = 0;
	                //float val = dis.readFloat();
	                
	        } else if  ("TileEnt".equals(packet.channel)) {
	            
	        	int pt = dis.readInt();
	        	
	        	int x = dis.readInt();
	        	int y = dis.readInt();
	        	int z = dis.readInt();
	        	
	        	TileEntity tEnt = ZCGame.instance().getTileEntity(x, y, z);
	        	
	        	if (side == Side.SERVER) {
	        		
	        	} else {
	        		//System.out.println("tEnt " + tEnt);
		    		if (tEnt != null && tEnt instanceof TileEntityPurchasePlate) {
		    			((TileEntityPurchasePlate)tEnt).itemIndex = dis.readInt();
		    		}
	        	}
	        	
	        	
	    		
	        	
	        	
	        	//ZCGame.instance().handleTileEntityPacket(x, y, z, 0, var5, var6, var7)handleTileEntityPacket()
	            	
	        } else if  ("Input".equals(packet.channel)) {
	        	if (side == Side.SERVER) {
	        		// We are on the server side.
	        		//EntityPlayerMP entP = (EntityPlayerMP) player;
	        	} else if (side == Side.CLIENT) {
	        		//EntityClientPlayerMP entP = (EntityClientPlayerMP) player;
	        		// We are on the client side.
	        	} else {
	        		// We are on the Bukkit server. Bukkit is a server mod used for security.
	        	}
	        	
	        	EntityPlayer entP = (EntityPlayer) player;
	        	
	        	if (entP != null) {
		        	boolean mouseDown = false;
		        	try {
		        		mouseDown = dis.readBoolean();
		        	} catch (Exception ex) {
		        		ex.printStackTrace();
		        	}
		        	
		        	System.out.println(entP.username + " - " + mouseDown);
	        	}
	        } else if  ("MLMP".equals(packet.channel)) {
	        	
	        	EntityPlayer entP = (EntityPlayer) player;
	        	
	        	PacketMLMP packetMLMP = new PacketMLMP();
	        	
	        	packetMLMP.packetType = dis.readInt();
	        	
	        	//System.out.println("packetType: " + packetMLMP.packetType);
	        	
        		for (int i = 0; i < 20; i++) {
        			
        			packetMLMP.dataInt[i] = dis.readInt();
        			//System.out.println(i + " - " + packetMLMP.dataInt[i]);
        		}
        		try {
        			packetMLMP.dataString[0] = dis.readUTF();
        			//System.out.println("level string: " + packetMLMP.dataString[0]);
        		}
    			catch (Exception ex)
    	        {
    				//ex.printStackTrace();
    				//System.out.println(packetMLMP.packetType);
    	        	//System.out.println("no string error");
    	        }
	        	
	        	if (side == Side.SERVER) {
	        		ZCServerTicks.zcGame.handlePacket(entP, packetMLMP);
	        	} else if (side == Side.CLIENT) {
	        		ZCClientTicks.zcGame.handlePacket(entP, packetMLMP);
	        	}
	        	
	        }
        }
        catch (Exception ex)
        {
        	System.out.println("packet read fail");
            //ex.printStackTrace();
        }
    }
}
