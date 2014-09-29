package zombiecraft.Forge;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import zombiecraft.Client.ZCGameSP;
import zombiecraft.Client.GameLogic.InterfaceManager;
import zombiecraft.Client.GameLogic.InterfaceManagerMP;
import zombiecraft.Client.gui.GuiMainMenuZC;
import zombiecraft.Core.Camera.CameraManager;
import build.world.Build;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class ZCClientTicks
{
	
	public static ZCGameSP zcGame;
	public static InterfaceManager iMan;
	//public static ZCSoundManager sMan = new ZCSoundManager();
	
	public static Minecraft mc = null;
	public static World worldRef = null;
	public static EntityPlayer player = null;
	public static World lastWorld;
	
	public static CameraManager camMan = new CameraManager();

    public static void onRenderTick()
    {
    	
    	if (mc == null) mc = FMLClientHandler.instance().getClient();
    	if (worldRef == null) worldRef = FMLClientHandler.instance().getClient().theWorld;
        if (player == null) player = FMLClientHandler.instance().getClient().thePlayer;
    	
        if (mc.currentScreen instanceof GuiMainMenu && !(mc.currentScreen instanceof GuiMainMenuZC)) {
        	mc.displayGuiScreen(new GuiMainMenuZC());
        }
        
        if (worldRef == null || player == null || zcGame == null || zcGame.mapMan == null) {
            return;
        }
        
        if(timeout > 0 && msg != null) {
            ScaledResolution var8 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            int var4 = var8.getScaledWidth();
            int var10 = var8.getScaledHeight();
            int var6 = mc.fontRenderer.getStringWidth(msg);
            mc.fontRenderer.drawStringWithShadow(msg, 3, 105, 16777215);
            
        }
        
    	//inits done in here so we can properly tell if were in SSP mode or SMP mode
		if (ZCClientTicks.iMan == null) {
			/*if (!mc.isMultiplayerWorld()) {
				ZombieCraftMod.iMan = new InterfaceManagerSP(mc, this);
				System.out.println("new InterfaceManagerSP");
	    	} else {*/
	    		ZCClientTicks.iMan = new InterfaceManagerMP(mc, zcGame);
	    		System.out.println("new InterfaceManagerMP");
	    	//}
		} else {
			ZCClientTicks.iMan.tick();
		}
		
		if (lastWorld != FMLClientHandler.instance().getClient().theWorld) {
            //worldSaver = null;
			worldRef = FMLClientHandler.instance().getClient().theWorld;
            lastWorld = worldRef;
            System.out.println("Resetting ZombieCraft");
            iMan.resetGunBinds();
            iMan = null; //auto resets in zcgamesp when ready
            zcGame.resetWaveManager = true;//zcGame.wMan = null;
            zcGame.levelHasInit = false;
            zcGame.resetWorldHook();
            //getFXLayers();
        }
		
		if (camMan != null) {
			camMan.renderTick();
		}
    }
    
    public static void onTickInGame() {
		// TODO Auto-generated method stub
		

        if (worldRef == null || player == null) {
            return;
        }
        
        if (timeout > 0) --timeout;
        
        if (zcGame == null && worldRef != null) {
        	zcGame = new ZCGameSP(false);
        	zcGame.mc = mc;
        	
        	if (zcGame.zcLevel.buildData == null) {
    			if (zcGame.gameData == null) {
    				zcGame.zcLevel.buildData = new Build(0, 0, 0, zcGame.mapMan.curLevel);
    			}
        	}
        	
        }
        
        if (zcGame != null) {
        	zcGame.tick();
        }
        
        if (camMan != null) {
        	camMan.gameTick();
        }
        
        //System.out.println("ZOMBIECRAFT SOUND MANAGER OFF");
        /*if (sMan != null) {
        	sMan.tick();
        }*/
        
        //zcGame.trySetTexturePack("ZombieCraft - 16bit");
        
        //System.out.println(this.mc.texturePackList);
    	
	}
    
    public static int timeout;
    public static String msg;
    public static int color;
    public static int defaultColor = 16777215;
    public static boolean ingui;
    
    public static void displayMessage(String var0, int var1) {
        msg = var0;
        timeout = 25;
        color = var1;
    }

    public static void displayMessage(String var0) {
        displayMessage(var0, defaultColor);
    }
    
    public static void sendPacket(int packetType, int[] dataInt) {
    	sendPacket(packetType, dataInt, new String[0]);
    }
    
    public static void sendPacket(int packetType, int[] dataInt, String[] dataString) {
    	//PacketMLMP packetmlmp = new PacketMLMP();
    	//packetmlmp.packetType = packetType;
    	//packetmlmp.dataInt = dataInt; //size of 10
    	//packetmlmp.dataString = dataString; //this only ever had a size of 1
        //ModLoaderMp.sendPacket(ModLoaderMp.getModInstance(mod_ZombieCraft.class), packet);
        
        /*ByteArrayOutputStream bos = new ByteArrayOutputStream(80);
    	DataOutputStream outputStream = new DataOutputStream(bos);*/
    	
    	ByteBuf outputStream = Unpooled.buffer();
    	try {
    		
    		//outputStream.writeInt(Mouse.isButtonDown(0) ? 1 : 0);
    		//outputStream.writeBoolean(Mouse.isButtonDown(0));
    		
    		//new for 1.7.10
    		ByteBufUtils.writeUTF8String(outputStream, "MLMP");
    		
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
    			//outputStream.writeUTF("");
    		} else {
    			ByteBufUtils.writeUTF8String(outputStream, dataString[0]);
    			//outputStream.writeUTF(dataString[0]);
    			//System.out.println("durrrrrrr: " + dataString[0]);
    		}
    		
    	} catch (Exception ex) {
    		System.out.println("client side no string");
    		//ex.printStackTrace();
    	}
    	
    	FMLProxyPacket packet = new FMLProxyPacket(outputStream, ZombieCraftMod.eventChannelName);
    	
    	ZombieCraftMod.eventChannel.sendToServer(packet);
    	
    	/*Packet250CustomPayload packet = new Packet250CustomPayload();
    	packet.channel = "MLMP";
    	packet.data = bos.toByteArray();
    	packet.length = bos.size();
    	
    	
    	
    	FMLClientHandler.instance().getClient().thePlayer.sendQueue.addToSendQueue(packet);*/
        
	}
}
