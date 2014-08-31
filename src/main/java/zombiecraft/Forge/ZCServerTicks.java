package zombiecraft.Forge;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import zombiecraft.Core.PacketTypes;
import zombiecraft.Server.ZCGameMP;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class ZCServerTicks
{
	public static ZCGameMP zcGame;
	
	public static World worldRef = null;

	public static long lastWorldTime;

	public static World lastWorld;
	
	//public static int zcDim;

    public static void onTickInGame()
    {
    	
    	//if (zcGame != null && ((ZCGameMP)zcGame).mc == null) {
    		((ZCGameMP)zcGame).mc = MinecraftServer.getServer();
    	//}
    	
    	if (((ZCGameMP)zcGame).mc.worldServerForDimension(((ZCGameMP)zcGame).activeZCDimension) != lastWorld || worldRef == null) {
    		if (((ZCGameMP)zcGame).mc != null) {
    			worldRef = ((ZCGameMP)zcGame).mc.worldServerForDimension(((ZCGameMP)zcGame).activeZCDimension);
    			lastWorld = worldRef;
    			zcGame.wMan.stopGame();
    			
    			((ServerCommandManager)FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager()).registerCommand(new CommandTeleportZC());
    			((ServerCommandManager)FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager()).registerCommand(new CommandPoints());
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
        
        
        //ByteArrayOutputStream bos = new ByteArrayOutputStream(80);
    	//DataOutputStream outputStream = new DataOutputStream(bos);
    	
    	ByteBuf outputStream = Unpooled.buffer();
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
    			ByteBufUtils.writeUTF8String(outputStream, "");
    		} else {
    			ByteBufUtils.writeUTF8String(outputStream, dataString[0]);
    			//outputStream.writeUTF(dataString[0]);
    		}
    		
    	} catch (Exception ex) {
    		System.out.println("server packet write fail");
    		//ex.printStackTrace();
    	}
    	
    	/*Packet250CustomPayload packet = new Packet250CustomPayload();
    	packet.channel = channel;
    	packet.data = bos.toByteArray();
    	packet.length = bos.size();*/
    	
    	FMLProxyPacket packet = new FMLProxyPacket(outputStream, ZombieCraftMod.eventChannelName);
    	
    	if (player != null && player instanceof EntityPlayerMP) {
    		ZombieCraftMod.eventChannel.sendTo(packet, (EntityPlayerMP)player);
    		//((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
        } else {
        	ZombieCraftMod.eventChannel.sendToAll(packet);
        	//MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(packet);
        }
    	
    	//PacketDispatcher.sendPacketToPlayer(packet, player);
    	
    	
    }
    
    /*public void sendPacketToAll(Packet packet) {
    	ModLoaderMp.sendPacketToAll(ModLoaderMp.getModInstance(mod_ZombieCraft.class), packet);
    }*/
    
    //shouldnt use, for tile entities replace with worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
