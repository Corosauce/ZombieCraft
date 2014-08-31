package zombiecraft.Client.gui;

import java.util.Iterator;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZCClientTicks;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGameOverZC extends GuiScreen
{
    /**
     * The cooldown timer for the buttons, increases every tick and enables all buttons when reaching 20.
     */
    private int cooldownTimer;

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
    	
    	
    	
        this.buttonList.clear();

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 48, StatCollector.translateToLocal("deathScreen.spectate")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72, StatCollector.translateToLocal("deathScreen.respawn")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, StatCollector.translateToLocal("deathScreen.titleScreen")));

        ((GuiButton)this.buttonList.get(0)).enabled = false;
        ((GuiButton)this.buttonList.get(1)).enabled = false;
        ((GuiButton)this.buttonList.get(2)).enabled = false;
        
        if (/*ZCGame.instance().wMan.waveSpawnMax == ZCGame.instance().wMan.waveSpawnCount && */ZCGame.instance().gameActive)
        {
        	((GuiButton)this.buttonList.get(1)).enabled = false;
        }
        
        

        /*GuiButton var2;

        for (Iterator var1 = this.buttonList.iterator(); var1.hasNext(); var2.enabled = false)
        {
            var2 = (GuiButton)var1.next();
        }*/
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2) {}

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
    	
    	ZCClientTicks.zcGame.waitingToSpawn = true;
    	
        switch (par1GuiButton.id)
        {
        	case 0:
        		this.mc.thePlayer.setHealth(1);
        		//((EntityClientPlayerMP)mc.thePlayer).sendQueue.addToSendQueue(new Packet11PlayerPosition(mc.thePlayer.motionX, -999D, -999D, mc.thePlayer.motionZ, true));
        		//this.mc.thePlayer.posY = 256; //wont work client side derp
        		//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        		this.mc.displayGuiScreen((GuiScreen)null);
        		
        		ZCClientTicks.camMan.spectate(mc.thePlayer);
        		ZCClientTicks.camMan.spectateNext();
        		
                break;
            case 1:
                this.mc.thePlayer.respawnPlayer();
                this.mc.displayGuiScreen((GuiScreen)null);
                break;
            case 2:
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld((WorldClient)null);
                this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        boolean var4 = false;//this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
        String var5 = var4 ? StatCollector.translateToLocal("deathScreen.title.hardcore") : StatCollector.translateToLocal("deathScreen.title");
        this.drawCenteredString(FMLClientHandler.instance().getClient().fontRenderer, var5, this.width / 2 / 2, 30, 16777215);
        GL11.glPopMatrix();

        if (var4)
        {
            this.drawCenteredString(FMLClientHandler.instance().getClient().fontRenderer, StatCollector.translateToLocal("deathScreen.hardcoreInfo"), this.width / 2, 144, 16777215);
        }

        this.drawCenteredString(FMLClientHandler.instance().getClient().fontRenderer, StatCollector.translateToLocal("deathScreen.score") + ": \u00a7e" + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
        super.drawScreen(par1, par2, par3);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.cooldownTimer;
        GuiButton var2;

        if (this.cooldownTimer >= 40)
        {
        	
        	ZCClientTicks.zcGame.waitingToSpawn = true;
        	
            for (Iterator var1 = this.buttonList.iterator(); var1.hasNext();)
            {
            	
                var2 = (GuiButton)var1.next();
                if (var2.id == 1) {
                	if (!ZCGame.instance().gameActive || ZCGame.instance().wMan.wave_StartDelay > 0) {
                		var2.enabled = true;
                	}
            	} else {
            		var2.enabled = true;
            	}
            }
        }
    }
}
