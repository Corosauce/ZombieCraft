package zombiecraft.Forge;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.EnumSet;

import zombiecraft.Client.ZCGameSP;
import zombiecraft.Client.GameLogic.InterfaceManager;
import zombiecraft.Core.PacketTypes;
import zombiecraft.Server.ZCGameMP;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ContainerPlayer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.ServerCommandManager;
import net.minecraft.src.Slot;
import net.minecraft.src.World;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ZCServerTicks implements ITickHandler
{
	public static ZCGameMP zcGame;
	
	public static World worldRef = null;

	public long lastWorldTime;

	public World lastWorld;
	
	//public static int zcDim;
	
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
    	if (type.equals(EnumSet.of(TickType.SERVER)))
        {
            onTickInGame();
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        
    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.SERVER);
    }

    @Override
    public String getLabel()
    {
        return null;
    }

    public void onTickInGame()
    {
    	
    	if (zcGame != null && ((ZCGameMP)zcGame).mc == null) {
    		((ZCGameMP)zcGame).mc = MinecraftServer.getServer();
    	}
    	
    	if (((ZCGameMP)zcGame).mc.worldServerForDimension(((ZCGameMP)zcGame).activeZCDimension) != lastWorld || worldRef == null) {
    		if (((ZCGameMP)zcGame).mc != null) {
    			worldRef = ((ZCGameMP)zcGame).mc.worldServerForDimension(((ZCGameMP)zcGame).activeZCDimension);
    			lastWorld = worldRef;
    			
    			((ServerCommandManager)FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager()).registerCommand(new CommandTeleportZC());
    		}
    	} else {
    		if (worldRef != null) {
	    		if (worldRef.getWorldTime() != lastWorldTime) {
	    			lastWorldTime = worldRef.getWorldTime(); 
	    			if (zcGame != null) zcGame.tick();
	    		}
    		}
    	}
    	
    }
    
  //PACKETS!
    public static void sendBuyPromptPacket(EntityPlayer player, int itemIndex) {
    	sendPacket(player, PacketTypes.MENU_BUY_PROMPT, new int[] {itemIndex});

	}
    
    public static void sendBuyTimeoutPacket(EntityPlayer player, int itemIndex) {
        sendPacket(player, PacketTypes.MENU_BUY_TIMEOUT, new int[] {0});
	}
    
    public static void sendPacket(EntityPlayer player, int packetType, int[] dataInt) {
    	sendPacket(player, packetType, dataInt, new String[0]);
    }
    
    public static void sendPacket(EntityPlayer player, int packetType, int[] dataInt, String[] dataString) {
    	sendPacket(player, packetType, dataInt, dataString, "MLMP");
    }
    
    public static void sendPacket(EntityPlayer player, int packetType, int[] dataInt, String[] dataString, String channel) {
    	/*Packet230ModLoader packet = new Packet230ModLoader();
        packet.packetType = packetType;
        packet.dataInt = dataInt;
        packet.dataString = dataString;*/
        
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream(80);
    	DataOutputStream outputStream = new DataOutputStream(bos);
    	try {
    		
    		//outputStream.writeInt(Mouse.isButtonDown(0) ? 1 : 0);
    		//outputStream.writeBoolean(Mouse.isButtonDown(0));
    		
    		outputStream.writeInt(packetType);
    		int i = 0;
    		for (i = 0; i < 20; i++) {
    			if (dataInt == null || i >= dataInt.length) {
    				outputStream.writeInt(0);
    			} else {
    				outputStream.writeInt(dataInt[i]);
    			}
    		}
    		if (dataString == null || dataString.length == 0) {
    			outputStream.writeUTF("");
    		} else {
    			outputStream.writeUTF(dataString[0]);
    		}
    		
    	} catch (Exception ex) {
    		System.out.println("server packet write fail");
    		//ex.printStackTrace();
    	}
    	
    	Packet250CustomPayload packet = new Packet250CustomPayload();
    	packet.channel = channel;
    	packet.data = bos.toByteArray();
    	packet.length = bos.size();
    	
    	if (player != null && player instanceof EntityPlayerMP) {
    		((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
        } else {
        	MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(packet);
        }
    	
    	
    	
    	
    }
    
    /*public void sendPacketToAll(Packet packet) {
    	ModLoaderMp.sendPacketToAll(ModLoaderMp.getModInstance(mod_ZombieCraft.class), packet);
    }*/
    
    public static void sendPacketToAll(Packet var0)
    {
        if (var0 != null)
        {
        	MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(var0);
            /*for (int var1 = 0; var1 < ModLoader.getMinecraftServerInstance().configManager.playerEntities.size(); ++var1)
            {
                ((EntityPlayerMP)ModLoader.getMinecraftServerInstance().configManager.playerEntities.get(var1)).playerNetServerHandler.sendPacket(var0);
            }*/
        }
    }
}
