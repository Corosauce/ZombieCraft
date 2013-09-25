package zombiecraft.Client.gui;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Property;

import org.lwjgl.opengl.GL11;

import zombiecraft.Client.gui.elements.GuiButtonZC;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZCClientTicks;
import zombiecraft.Forge.ZombieCraftMod;

public class GuiEditorCP extends GuiScreen
{
    private int updateCounter2 = 0;
    private int updateCounter = 0;
    private int inventoryRows = 0;
    
    private int ySize;
    private int xSize;
    private int xOffset;
    
    private GuiTextField textboxWorldName;
    
    private static int G_EDITMODE = 0;
    private static int G_TOGGLEGAME = 1;
    private static int G_PREVSTAGE = 2;
    private static int G_NEXTSTAGE = 3;
    private static int G_DOORNOCLIP = 4;
    private static int G_TOOLMODE = 5;
    private static int G_SAVE = 6;
    private static int G_LOAD = 7;
    
    private static int G_CLOSE = 9;
    
    private static int G_TOOLMODE_LEVEL = 10;
    private static int G_TOOLMODE_LINK = 11;
    private static int G_TOOLMODE_SPAWN = 12;
    
    private static int G_LOBBYMENU = 13;
    private static int G_MAPCONFIG = 14;
    
    public ResourceLocation resGUI = new ResourceLocation(ZombieCraftMod.modID + ":textures/gui/editorCP.png");
    
    //container additions \\
    
    
    
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
    	
    	//inventoryRows = 5;
    	
        //int var4 = this.mc.renderEngine.getTexture("/mods/ZombieCraft/textures/textures/menus/editorCP.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //this.mc.renderEngine.bindTexture("/mods/ZombieCraft/textures/menus/editorCP.png");
        mc.func_110434_K().func_110577_a(resGUI);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2 + 50;
        
        
        
        this.drawTexturedModalRect(var5 + xOffset, var6, 0, 0, this.xSize, ySize);
        if (ZCGame.instance().mapMan.editMode) this.drawTexturedModalRect(-2, var6, 0, 0, this.xSize-76, ySize);
        //this.drawTexturedModalRect(var5, var6 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
        this.drawCenteredString(this.fontRenderer, "ZC Menu", ((this.width - this.xSize) / 2) + xOffset + 50, var6 - 10, 16777215);
        if (ZCGame.instance().mapMan.editMode) this.drawCenteredString(this.fontRenderer, "Level Info", 0 + 45, var6 - 10, 16777215);
    }
    
    // end container additions //
    
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void initGui()
    {
    	xSize = 176;
    	ySize = 200;
    	xOffset = (int)(this.width / 4 * 1.92);
    	
        this.updateCounter2 = 0;
        this.buttonList.clear();
        byte var1 = -16;
        
        //System.out.println(width);
    	//System.out.println(xSize);
        
        int startX = ((this.width - this.xSize) / 2) + xOffset + 6;
        int startY = (this.height - this.ySize) / 2 + 73;
        
        int div = 22;
        
        /*String original = new String("\u00A7");
        String roundTrip = "";
        try {
        	roundTrip = new String(original.getBytes("UTF8"), "UTF8");
        } catch (Exception ex) {
        	
        }*/
        
        this.buttonList.add(new GuiButton(G_EDITMODE, startX, startY + 0 + var1, 90, 20, (ZCGame.instance().mapMan.editMode ? "\u00A7" + '4' : "") + "Edit Mode"));
        this.buttonList.add(new GuiButton(G_TOGGLEGAME, startX, startY + div*1 + var1, 90, 20, (!ZCGame.instance().gameActive ? "Start Game" : "\u00A72Stop Game")));
        this.buttonList.add(new GuiButton(G_PREVSTAGE, startX, startY + div*2 + var1, 90, 20, "Prev Stage"));
        this.buttonList.add(new GuiButton(G_NEXTSTAGE, startX, startY + div*3 + var1, 90, 20, "Next Stage"));
        
        this.buttonList.add(new GuiButton(G_LOBBYMENU, startX, startY + div*5 + var1, 90, 20, "Lobby Menu"));
        
        
        this.buttonList.add(new GuiButton(G_CLOSE, startX, startY + div*6 + var1, 90, 20, "Close"));
        
        
        
        int startX2 = 4;//((this.width - this.xSize) / 2) - (xOffset/4*1);
        //int startY2 = (this.height - this.ySize) / 2 + 23;
        
        this.textboxWorldName = new GuiTextField(this.fontRenderer, startX2, startY + div*0 + var1, 90, 20);
        //this.textboxWorldName.setFocused(true);
        this.textboxWorldName.setText(ZCGame.instance().mapMan.curLevel);
	        
	    if (ZCGame.instance().mapMan.editMode) {
	        
	        this.buttonList.add(new GuiButton(G_SAVE, startX2, startY + div*1 + var1, 90, 20, "Save Level"));
	        this.buttonList.add(new GuiButton(G_LOAD, startX2, startY + div*2 + var1, 90, 20, "Load Level"));
	        this.buttonList.add(new GuiButton(99, startX2, startY + div*3 + var1, 90, 20, "Generate Level"));
	        
	        this.buttonList.add(new GuiButtonZC(G_TOOLMODE_LEVEL, startX2, startY + div*4 + var1, 20, 20, 1));
	        this.buttonList.add(new GuiButtonZC(G_TOOLMODE_LINK, startX2 + 23, startY + div*4 + var1, 20, 20, 2));
	        this.buttonList.add(new GuiButtonZC(G_TOOLMODE_SPAWN, startX2 + 46, startY + div*4 + var1, 20, 20, 3));
	        //this.buttonList.add(new GuiButton(G_TOOLMODE_SPAWN, startX2 + 70, startY + div*4 + var1, 20, 20, "S"));
	        
	        //this.buttonList.add(new GuiButton(G_TOOLMODE, startX2, startY + div*5 + var1, 90, 20, "Tool Mode: " + ZCGame.instance().mapMan.editToolMode));
	        this.buttonList.add(new GuiButton(G_MAPCONFIG, startX2, startY + div*5 + var1, 90, 20, "Map Config"));
	        
	        this.buttonList.add(new GuiButton(G_DOORNOCLIP, startX2, startY + div*6 + var1, 90, 20, (ZCGame.instance().mapMan.doorNoClip ? "\u00A74" : "") + "Door No-Clip"));
        }
	    
	    if (false) {
		    System.out.println("DEBUG CONFIG DUMP");
	    	
		    try {
		    	Iterator it = ZombieCraftMod.instance.preInitConfig.getCategoryNames().iterator();
			    while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			        Object obj = pairs.getValue();
			        Property prop = null;
			        
			        if (obj instanceof TreeMap) {
			        	
			        	Iterator it2 = ((TreeMap)obj).entrySet().iterator();
					    while (it.hasNext()) {
					        Map.Entry pairs2 = (Map.Entry)it.next();
					        Object obj2 = pairs.getValue();
					        int what = 0;
					    }
			        	
			        	prop = (Property)obj;
			        	
			        	System.out.println(prop.comment);
			        }
			        
			        if (obj instanceof Property) {
				        //Object val = pairs.getValue();
				        
			        } else {
			        	
			        }
			    }
		    } catch (Exception ex) {
		    	ex.printStackTrace();
		    }
	    }
        
    }
    
    protected void keyTyped(char par1, int par2)
    {
    	super.keyTyped(par1, par2);
    	if (!ZCGame.instance().mapMan.editMode) return;
    	
        if (this.textboxWorldName.isFocused())
        {
            this.textboxWorldName.textboxKeyTyped(par1, par2);
            ZCGame.instance().setMapName(ZCClientTicks.player, this.textboxWorldName.getText());
            //this.localizedNewWorldText = this.textboxWorldName.getText();
        }

        if (par1 == 13)
        {
        	//save on enter?, nah lets not
            //this.actionPerformed((GuiButton)this.buttonList.get(7));
        }

        //lock save button if no name
        ((GuiButton)this.buttonList.get(7)).enabled = this.textboxWorldName.getText().length() > 0;
        //this.makeUseableName();
        
    }
    
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        
        if (ZCGame.instance().mapMan.editMode) this.textboxWorldName.mouseClicked(par1, par2, par3);
        
	}

    protected void actionPerformed(GuiButton var1)
    {
    	
    	if (var1.id == 99) {
    		ZCGame.instance().regenerateLevel(ZCClientTicks.player);
    	}
    	
        if (var1.id == G_TOGGLEGAME)
        {
        	if (!ZCGame.instance().mapMan.editMode || ZCGame.instance().gameActive) {
        		ZCClientTicks.iMan.setStage(1, 1);
        	}
        }
        if (var1.id == G_LOBBYMENU)
        {
        	this.mc.displayGuiScreen(new GuiSession(mc.thePlayer.inventory));
        }
        if (var1.id == G_MAPCONFIG)
        {
        	this.mc.displayGuiScreen(new GuiMapConfig(mc.thePlayer.inventory));
        }
        if (var1.id == G_EDITMODE)
        {
            ZCClientTicks.iMan.toggleEditMode();
            ZCGame.instance().mapMan.editMode = !ZCGame.instance().mapMan.editMode; //this is here for client side prediction, the server is already sending in a packet for this
        }
        if (var1.id == G_SAVE)
        {
        	ZCGame.instance().saveLevel(ZCClientTicks.player);
        }
        if (var1.id == G_LOAD)
        {
        	ZCGame.instance().loadLevel(ZCClientTicks.player);
        }
        
        if (var1.id == G_TOOLMODE)
        {
        	ZCGame.instance().mapMan.editToolMode++;
        	
        	if (ZCGame.instance().mapMan.editToolMode >= 3) {
        		ZCGame.instance().mapMan.editToolMode = 0;
        	}
        	ZCGame.instance().mapMan.setToolEditMode(ZCGame.instance().mapMan.editToolMode);
        }
        if (var1.id == G_TOOLMODE_LEVEL)
        {
        	ZCGame.instance().mapMan.editToolMode = 1;
        }
        if (var1.id == G_TOOLMODE_LINK)
        {
        	ZCGame.instance().mapMan.editToolMode = 0;
        }
        if (var1.id == G_TOOLMODE_SPAWN)
        {
        	ZCGame.instance().mapMan.editToolMode = 2;
        }
        if (var1.id == G_DOORNOCLIP) {
        	ZCClientTicks.iMan.toggleNoClip();
        }
        
        if (var1.id == G_PREVSTAGE) {
        	ZCClientTicks.iMan.setStage(ZCClientTicks.zcGame.wMan.wave_Stage-1, 0);
        }
        
        if (var1.id == G_NEXTSTAGE) {
        	ZCClientTicks.iMan.setStage(ZCClientTicks.zcGame.wMan.wave_Stage+1, 0);
        }

        if (var1.id == G_CLOSE)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
        initGui();
    }
    
    public void updateStates() {
    	GuiButton var2 = null;
    	
    	if (!this.textboxWorldName.isFocused()) {
    		this.textboxWorldName.setText(ZCGame.instance().mapMan.curLevel);
    	}

        for (Iterator var1 = this.buttonList.iterator(); var1.hasNext();)
        {
            var2 = (GuiButton)var1.next();
            if (var2 instanceof GuiButtonZC) {
        		var2.enabled = false;
        		if (((GuiButtonZC) var2).id == this.G_TOOLMODE_LEVEL && ZCGame.instance().mapMan.editToolMode == 1) {
        			var2.enabled = true;
        		} else if (((GuiButtonZC) var2).id == this.G_TOOLMODE_LINK && ZCGame.instance().mapMan.editToolMode == 0) {
        			var2.enabled = true;
        		} else if (((GuiButtonZC) var2).id == this.G_TOOLMODE_SPAWN && ZCGame.instance().mapMan.editToolMode == 2) {
        			var2.enabled = true;
        		}
        	} else {
        		if (var2.id == this.G_EDITMODE) {
        			var2.displayString = (ZCGame.instance().mapMan.editMode ? "\u00A7" + '4' : "") + "Edit Mode";
        		} else if (var2.id == this.G_TOGGLEGAME) {
        			var2.displayString = (!ZCGame.instance().gameActive ? "Start Game" : "\u00A72Stop Game");
        		} else if (var2.id == this.G_DOORNOCLIP) {
        			var2.displayString = (!ZCGame.instance().mapMan.doorNoClip ? "Door No-Clip" : "\u00A74Door No-Clip");
        		}
        	}
        }
    }

    public void updateScreen()
    {
    	updateStates();
                
        super.updateScreen();
        ++this.updateCounter;
        if (ZCGame.instance().mapMan.editMode) this.textboxWorldName.updateCursorCounter();
    }

    public void drawScreen(int var1, int var2, float var3)
    {
        //this.drawDefaultBackground();
    	
    	drawGuiContainerBackgroundLayer(0,0,0);
    	
    	
    	if (ZCGame.instance().mapMan.editMode) this.textboxWorldName.drawTextBox();
    	
    	ZCClientTicks.iMan.showMainOverlay(false);
        //mod_Weather.weatherDbg();
        
        super.drawScreen(var1, var2, var3);
    }
}
