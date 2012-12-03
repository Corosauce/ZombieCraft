package build;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.EnumSet;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import build.enums.EnumBuildState;
import build.enums.EnumCopyState;
import build.render.Overlays;
import build.world.Build;
import build.world.BuildJob;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ContainerPlayer;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Slot;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;

public class BuildClientTicks implements ITickHandler
{
	//Player based fields
	public EnumBuildState buildState = EnumBuildState.NORMAL;
	public EnumCopyState copyState = EnumCopyState.NORMAL;
	public int key_Build = Keyboard.KEY_B;
	public int key_Copy = Keyboard.KEY_C;
	public Build clipboardData;
	
    public MovingObjectPosition extendedMouseOver = null;
    
    public static BuildClientTicks i = null;
    
    public BuildClientTicks() {
    	clipboardData = new Build(0, 0, 0, "build");
    }
	
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    	if (i == null) i = this;
    }

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
                onTickInGame();
            }
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
    
    public void worldRenderTick(float partialTicks)
    {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	
    	if (mc.renderViewEntity != null) extendedMouseOver = mc.renderViewEntity.rayTrace(10, partialTicks);
    	
        //TODO: Your Code Here
    	if (buildState == EnumBuildState.PLACE) {
    		Overlays.renderBuildOutline(clipboardData);
    	} else if (copyState == EnumCopyState.SETMAX) {
    		float x = (float)mc.thePlayer.posX;
    		float y = (float)mc.thePlayer.posY;
    		float z = (float)mc.thePlayer.posZ;
    		if (extendedMouseOver != null) {
    			x = extendedMouseOver.blockX;
    			y = extendedMouseOver.blockY;
    			z = extendedMouseOver.blockZ;
    		}
    		Overlays.renderBuildOutline(sx, sy, sz, x, y, z);
    	}
    }

    public void onRenderTick()
    {
    	
    	
    }

    public void onTickInGUI(GuiScreen guiscreen)
    {
        //TODO: Your Code Here
    }
    
    public boolean wasKeyDown = false;
    
    public void onTickInGame() {
		// TODO Auto-generated method stub
		
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	
    	if (mc.theWorld != null) {
    		clipboardData.dim = mc.theWorld.provider.dimensionId;
    	}
    	
    	if (buildState == EnumBuildState.PLACE) {
    		if (extendedMouseOver != null) {
    			clipboardData.setCornerPosition(extendedMouseOver.blockX - (clipboardData.map_sizeX / 2), extendedMouseOver.blockY+1, extendedMouseOver.blockZ - (clipboardData.map_sizeX / 2));
    		} else {
    			clipboardData.setCornerPosition((int)mc.thePlayer.posX - (clipboardData.map_sizeX / 2), (int)mc.thePlayer.posY, (int)mc.thePlayer.posZ - (clipboardData.map_sizeX / 2));
    		}
    	}
    	
    	//updateInput();
    	
	}
    
    public void updateInput() {
    	if (Keyboard.isKeyDown(key_Build)) {
			if (!wasKeyDown) {
				eventBuild();
			}
			wasKeyDown = true;
    	} else if (Keyboard.isKeyDown(key_Copy)) {
			if (!wasKeyDown) {
				eventCopy();
			}
			wasKeyDown = true;
		} else {
			wasKeyDown = false;
		}
    }
    
    int sx; int sy; int sz; int ex; int ey; int ez;
    
    public void eventCopy() {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	if (copyState == EnumCopyState.NORMAL) {
    		copyState = EnumCopyState.SETMIN;
    	} else if (copyState == EnumCopyState.SETMIN) {
    		copyState = EnumCopyState.SETMAX;
    		sx = (int)mc.thePlayer.posX;
    		sy = (int)(mc.thePlayer.posY - mc.thePlayer.yOffset);
    		sz = (int)mc.thePlayer.posZ;
    		if (extendedMouseOver != null) {
    			sx = extendedMouseOver.blockX;
    			sy = extendedMouseOver.blockY;
    			sz = extendedMouseOver.blockZ;
    		}
    	} else {
    		ex = (int)mc.thePlayer.posX;
    		ey = (int)(mc.thePlayer.posY - mc.thePlayer.yOffset);
    		ez = (int)mc.thePlayer.posZ;
    		if (extendedMouseOver != null) {
	    		ex = extendedMouseOver.blockX;
				ey = extendedMouseOver.blockY;
				ez = extendedMouseOver.blockZ;
    		}
			
			clipboardData.recalculateLevelSize(sx, sy, sz, ex, ey, ez);
			clipboardData.scanLevelToData();
			
    		copyState = EnumCopyState.NORMAL;
    	}
    	
    	System.out.println(copyState);
    }
    
    public void eventBuild() {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	if (buildState == EnumBuildState.NORMAL) {
    		buildState = EnumBuildState.PLACE;
    	} else if (buildState == EnumBuildState.PLACE) {
    		buildState = EnumBuildState.NORMAL;
    		BuildServerTicks.buildMan.addBuild(new BuildJob(0, clipboardData));
    	}
    }
}
