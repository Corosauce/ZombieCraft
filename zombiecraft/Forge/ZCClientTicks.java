package zombiecraft.Forge;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.EnumSet;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import build.world.Build;

import zombiecraft.Client.ZCGameSP;
import zombiecraft.Client.GameLogic.InterfaceManager;
import zombiecraft.Client.GameLogic.InterfaceManagerMP;
import zombiecraft.Core.Camera.CameraManager;
import zombiecraft.Core.GameLogic.*;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;

public class ZCClientTicks implements ITickHandler
{
	
	public static ZCGameSP zcGame;
	public static InterfaceManager iMan;
	
	public static Minecraft mc = null;
	public static World worldRef = null;
	public static EntityPlayer player = null;
	public static World lastWorld;
	
	public static CameraManager camMan = new CameraManager();
	
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {}

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        if (type.equals(EnumSet.of(TickType.RENDER)))
        {
            onRenderTick();
        }
        else if (type.equals(EnumSet.of(TickType.CLIENT)))
        {
            GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;

            if (guiscreen != null)
            {
                onTickInGUI(guiscreen);
            }
            else
            {
                
            }
            
            onTickInGame();
        }
    }

	@Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.RENDER, TickType.CLIENT);
        // In my testing only RENDER, CLIENT, & PLAYER did anything on the client side.
        // Read 'cpw.mods.fml.common.TickType.java' for a full list and description of available types
    }

    @Override
    public String getLabel()
    {
        return null;
    }

    public void onRenderTick()
    {
    	
    	if (mc == null) mc = ModLoader.getMinecraftInstance();
    	if (worldRef == null) worldRef = ModLoader.getMinecraftInstance().theWorld;
        if (player == null) player = ModLoader.getMinecraftInstance().thePlayer;
    	
        if (worldRef == null || player == null || zcGame == null || zcGame.mapMan == null) {
            return;
        }
        
        
        
        if(timeout > 0 && msg != null) {
            ScaledResolution var8 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
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
		
		if (lastWorld != ModLoader.getMinecraftInstance().theWorld) {
            //worldSaver = null;
			worldRef = ModLoader.getMinecraftInstance().theWorld;
            lastWorld = worldRef;
            System.out.println("Resetting ZombieCraft");
            iMan.resetGunBinds();
            iMan = null; //auto resets in zcgamesp when ready
            zcGame.resetWaveManager = true;//zcGame.wMan = null;
            zcGame.levelHasInit = false;
            zcGame.resetWorldHook();
            //getFXLayers();
        }
		
		if (this.camMan != null) {
			camMan.renderTick();
		}
    }

    public void onTickInGUI(GuiScreen guiscreen)
    {
        //TODO: Your Code Here
    }
    
    public void onTickInGame() {
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
    			//System.out.println("durrrrrrr: " + dataString[0]);
    		}
    		
    	} catch (Exception ex) {
    		System.out.println("client side no string");
    		//ex.printStackTrace();
    	}
    	
    	Packet250CustomPayload packet = new Packet250CustomPayload();
    	packet.channel = "MLMP";
    	packet.data = bos.toByteArray();
    	packet.length = bos.size();
    	
    	
    	
    	FMLClientHandler.instance().getClient().thePlayer.sendQueue.addToSendQueue(packet);
        
	}
}
