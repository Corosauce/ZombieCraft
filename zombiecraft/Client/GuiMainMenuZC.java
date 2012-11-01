package zombiecraft.Client;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.FMLCommonHandler;

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

        StringTranslate var2 = StringTranslate.getInstance();
        int var4 = this.height / 4 + 24;
        
        this.controlList.add(new GuiButton(66, this.width / 2 - 100, var4, 80, 20, var2.translateKey("ZombieCraft")));
        
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

}
