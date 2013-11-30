package zombiecraft.Client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.StringTranslate;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMainMenuZC extends GuiMainMenu
{
	
    public GuiMainMenuZC()
    {
        super();
    }
    
    @Override
    public void initGui()
    {
        super.initGui();

        int var4 = this.height / 4 + 04;
        
        this.buttonList.add(new GuiButton(66, this.width / 2 - 100, var4, 80, 20, "ZombieCraft"));
        
        
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
    	super.actionPerformed(par1GuiButton);
        if (par1GuiButton.id == 66)
        {
            this.mc.displayGuiScreen(new GuiSelectZCMap(this));
        }
    }
    
    @Override
    public void drawScreen(int par1, int par2, float par3) {
    	super.drawScreen(par1, par2, par3);
    	
    	Minecraft mc = FMLClientHandler.instance().getClient();

        GameSettings options = mc.gameSettings;
        
        boolean conflict = false;
        for (int first = 0; first < options.keyBindings.length && !conflict; first++) {
	        for (int second = 0; second < options.keyBindings.length; second++) {
	            if (second != first && (options.keyBindings[second].keyDescription.contains("ZC_") || options.keyBindings[first].keyDescription.contains("ZC_")) && options.keyBindings[second].keyCode == options.keyBindings[first].keyCode)
	            {
	                conflict = true;
	                break;
	            }
	        }
        }
        
        if (conflict) {
        	String fix = "KEY CONFLICT DETECTED! CHECK CONTROLS!";
        	mc.fontRenderer.drawStringWithShadow(fix, this.width / 2 - mc.fontRenderer.getStringWidth(fix) / 2, this.height - 60, 0xFF0000);
        }
    }

}
