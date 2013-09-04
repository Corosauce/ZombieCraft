package zombiecraft.Client.gui;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.src.ModLoader;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.WorldInfo;

import org.lwjgl.opengl.GL11;

import zombiecraft.Core.ZombieSaveRecord;
import zombiecraft.Core.GameLogic.WaveManager;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZCClientTicks;
import zombiecraft.Forge.ZCServerTicks;
import zombiecraft.Server.ZCGameMP;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSelectZCMap extends GuiScreen
{
    /** simple date formater */
    private final DateFormat dateFormatter = new SimpleDateFormat();

    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    protected GuiScreen parentScreen;

    /** The title string that is displayed in the top-center of the screen. */
    protected String screenTitle = "ZombieCraft Maps";

    /** True if a world has been selected. */
    private boolean selected = false;

    /** the currently selected world */
    private int selectedWorld;

    /** The save list for the world selection screen */
    private List saveList;
    private GuiMapSlot worldSlotContainer;

    /** E.g. World, Welt, Monde, Mundo */
    private String localizedWorldText;
    private String localizedMustConvertText;

    /**
     * The game mode text that is displayed with each world on the world selection list.
     */
    private String[] localizedGameModeText = new String[3];

    /** set to true if you arein the process of deleteing a world/save */
    private boolean deleting;

    /** the rename button in the world selection gui */
    private GuiButton buttonRename;

    /** the select button in the world selection gui */
    private GuiButton buttonSelect;

    /** the delete button in the world selection gui */
    private GuiButton buttonDelete;
    private GuiButton field_82316_w;
    
    public Minecraft mc;
    
    //public List worldList;

    public GuiSelectZCMap(GuiScreen par1GuiScreen)
    {
        this.parentScreen = par1GuiScreen;
        mc = FMLClientHandler.instance().getClient();
        saveList = new ArrayList();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        StringTranslate var1 = StringTranslate.getInstance();
        //this.screenTitle = var1.translateKey("selectWorld.title");
        this.localizedWorldText = var1.translateKey("selectWorld.world");
        this.localizedMustConvertText = var1.translateKey("selectWorld.conversion");
        this.localizedGameModeText[EnumGameType.SURVIVAL.getID()] = var1.translateKey("gameMode.survival");
        this.localizedGameModeText[EnumGameType.CREATIVE.getID()] = var1.translateKey("gameMode.creative");
        this.localizedGameModeText[EnumGameType.ADVENTURE.getID()] = var1.translateKey("gameMode.adventure");
        //this.loadSaves();
        this.loadList();
        this.worldSlotContainer = new GuiMapSlot(this);
        //this.worldSlotContainer.func_77207_a(40, 80, 30, 180);
        this.worldSlotContainer.registerScrollButtons(this.buttonList, 4, 5); //useless
        this.initButtons();
    }

    /**
     * loads the saves
     */
    private void loadSaves()
    {
    	try {
	        ISaveFormat var1 = this.mc.getSaveLoader();
	        this.saveList = var1.getSaveList();
	        Collections.sort(this.saveList);
	        
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	this.selectedWorld = -1;
    }

    /**
     * returns the file name of the specified save number
     */
    protected String getSaveFileName(int par1)
    {
        return ((SaveFormatComparator)this.saveList.get(par1)).getFileName();
    }

    /**
     * returns the name of the saved game
     */
    protected String getSaveName(int par1)
    {
        String var2 = ((SaveFormatComparator)this.saveList.get(par1)).getDisplayName();

        if (var2 == null || MathHelper.stringNullOrLengthZero(var2))
        {
            StringTranslate var3 = StringTranslate.getInstance();
            var2 = var3.translateKey("selectWorld.world") + " " + (par1 + 1);
        }

        return var2;
    }

    /**
     * intilize the buttons for this GUI
     */
    public void initButtons()
    {
        StringTranslate var1 = StringTranslate.getInstance();
        this.buttonList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 154 - 42, this.height - 28, 72, 20, var1.translateKey("Play Map")));
        this.buttonList.add(this.buttonSelect = new GuiButton(6, this.width / 2 - 154 + 42, this.height - 28, 72, 20, var1.translateKey("Edit Map")));
        //this.buttonList.add(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, var1.translateKey("selectWorld.create")));
        //this.buttonList.add(this.buttonDelete = new GuiButton(6, this.width / 2 - 154, this.height - 28, 72, 20, var1.translateKey("selectWorld.rename")));
        //this.buttonList.add(this.buttonRename = new GuiButton(2, this.width / 2 - 76, this.height - 28, 72, 20, var1.translateKey("selectWorld.delete")));
        //this.buttonList.add(this.field_82316_w = new GuiButton(7, this.width / 2 + 4, this.height - 28, 72, 20, var1.translateKey("selectWorld.recreate")));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 82 - 36 - 56, this.height - 28, 72, 20, var1.translateKey("gui.cancel")));
        //this.buttonSelect.enabled = false;
        //this.buttonRename.enabled = false;
        //this.buttonDelete.enabled = false;
        //this.field_82316_w.enabled = false;
    }
    
    public void loadList()
    {
		Minecraft mc = ModLoader.getMinecraftInstance();
        saveList.clear();
		checkFolder(ZCGame.getClientSidePath() + File.separator + ZCGame.getMapFolder() + File.separator);
		File zombieWorldDir = new File(ZCGame.getClientSidePath() + File.separator + ZCGame.getMapFolder() + File.separator);
		
		if(zombieWorldDir.exists() && zombieWorldDir.isDirectory())
        {
            File afile[] = zombieWorldDir.listFiles();
            File afile1[] = afile;
            int i = afile1.length;
            for(int j = 0; j < i; j++)
            {
                File file = afile1[j];
                
                try {
	                if (file.isDirectory()) {
	                	saveList.add(new ZombieSaveRecord(zombieWorldDir,file.getName(),0));
	                } else if (file.isFile() && file.getName().toLowerCase().endsWith(".zip")) {
	                	saveList.add(new ZombieSaveRecord(zombieWorldDir,file.getName(),1));
	                } else if (file.isFile() && file.getName().toLowerCase().endsWith(".schematic")) {
	                	saveList.add(new ZombieSaveRecord(zombieWorldDir,file.getName(),2));
		            }
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        }
       
        /*ZombieSaveRecord zombiesaverecord;
        for(Iterator iterator = saveList.iterator(); iterator.hasNext();)
        {
            zombiesaverecord = (ZombieSaveRecord)iterator.next();
            zombiesaverecord.load();
        }*/
        Collections.sort(this.saveList);
    }
    
    public void checkFolder(String path) {
		File theDir = new File(path);

		if (!theDir.exists()) {
			System.out.println("creating directory: " + path);
			boolean result = theDir.mkdir();  
			if(result){    
				System.out.println("DIR created");  
		    }
		}
	}

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 2)
            {
                String var2 = this.getSaveName(this.selectedWorld);

                if (var2 != null)
                {
                    this.deleting = true;
                    GuiYesNo var3 = getDeleteWorldScreen(this, var2, this.selectedWorld);
                    this.mc.displayGuiScreen(var3);
                }
            }
            else if (par1GuiButton.id == 1)
            {
                //this.loadWorld(this.selectedWorld);
            	
            	initZCWorld(this.selectedWorld, false);
            	
            }
            else if (par1GuiButton.id == 3)
            {
                this.mc.displayGuiScreen(new GuiCreateWorld(this));
            }
            else if (par1GuiButton.id == 6)
            {
                //this.mc.displayGuiScreen(new GuiRenameWorld(this, this.getSaveFileName(this.selectedWorld)));
            	initZCWorld(this.selectedWorld, true);
            }
            else if (par1GuiButton.id == 0)
            {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (par1GuiButton.id == 7)
            {
                GuiCreateWorld var5 = new GuiCreateWorld(this);
                ISaveHandler var6 = this.mc.getSaveLoader().getSaveLoader(this.getSaveFileName(this.selectedWorld), false);
                WorldInfo var4 = var6.loadWorldInfo();
                var6.flush();
                var5.func_82286_a(var4);
                this.mc.displayGuiScreen(var5);
            }
            else
            {
                this.worldSlotContainer.actionPerformed(par1GuiButton);
            }
        }
    }
    
    public void initZCWorld(int selectedMap, boolean edit) {
    	GuiCreateWorld var5 = new GuiCreateWorld(this);
        ISaveHandler var6 = this.mc.getSaveLoader().getSaveLoader("ZC", false);
        WorldInfo var4 = var6.loadWorldInfo();
        var6.flush();
        //var5.func_82286_a(var4);
        //ZCUtil.setPrivateValueBoth(WorldInfo.class, var4, "commandsAllowed", "commandsAllowed", true);
        //
        WorldSettings var66 = new WorldSettings((new Random()).nextLong(), edit ? EnumGameType.CREATIVE : EnumGameType.SURVIVAL, false, false, WorldType.DEFAULT);
        var66.func_82750_a(var5.generatorOptionsToUse);
        var66.enableCommands();
        //this.mc.displayGuiScreen(var5);
        
        ZCGame.autoload = true;
        WaveManager.levelNeedsRegen = true;
        WaveManager.waitingToStart = false;
        ZCGameMP.adjustedPlayer = false;
        
        if (!edit) {
        	ZCGame.autostart = true;
        } else {
        	ZCGame.autostart = false;
        }
        
        ZCServerTicks.zcGame.mapMan.editMode = edit;
        if (ZCClientTicks.zcGame != null && ZCClientTicks.zcGame.mapMan != null) ZCClientTicks.zcGame.mapMan.editMode = edit;
        
        ZombieSaveRecord zsr = (ZombieSaveRecord)saveList.get(selectedWorld);
        if (zsr != null && zsr.worldFile.getName().length() >= 10) {
        	ZCGame.curLevelOverride = zsr.worldFile.getName().substring(0, zsr.worldFile.getName().length() - 10);
        	System.out.println("Set to load: " + ZCGame.curLevelOverride);
        }
        
        this.mc.launchIntegratedServer("ZC", "ZC", var66);
    }

    /**
     * Gets the selected world.
     */
    public void loadWorld(int par1)
    {
        this.mc.displayGuiScreen((GuiScreen)null);

        if (!this.selected)
        {
            this.selected = true;
            String var2 = null;// = this.getSaveFileName(par1);

            if (var2 == null)
            {
                var2 = "World" + par1;
            }

            String var3 = this.getSaveName(par1);

            if (var3 == null)
            {
                var3 = "World" + par1;
            }

            this.mc.launchIntegratedServer(var2, var3, (WorldSettings)null);
        }
    }

    public void confirmClicked(boolean par1, int par2)
    {
        if (this.deleting)
        {
            this.deleting = false;

            if (par1)
            {
                ISaveFormat var3 = this.mc.getSaveLoader();
                var3.flushCache();
                var3.deleteWorldDirectory(this.getSaveFileName(par2));
                this.loadSaves();
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.worldSlotContainer.drawScreen(par1, par2, par3);
        //this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        
        fontRenderer.drawString(screenTitle, 12, 20, 16777215);
        
        ZombieSaveRecord zsr = (ZombieSaveRecord)saveList.get(selectedWorld);
        
        if (zsr != null) {
        	
        	//drawMapGraphic(zsr, Tessellator.instance, 200, 420, 40, 0);
        	
        	fontRenderer.drawString(zsr.text, 180 + 20, 20, 16777215);
        	
        	String str = zsr.getExtraInfo();
        	if (str != null) {
        		fontRenderer.drawSplitString(str, 180 + 20, 40, 220, 16777215);
        	}
        }
        
        super.drawScreen(par1, par2, par3);
    }
    
    //This method is a semi failed attempt at loading textures outside of mc root path, or more technically, outside texture pack path
    protected void drawMapGraphic(ZombieSaveRecord zsr, Tessellator tess, int left, int right, int bottom, int top)
    {
    	
    	int amountScrolled = 0;
    	//String path = new File(".").getAbsolutePath() + "/../src/minecraft/map.png";
    	//path = "/../map.png";
    	//System.out.println(path);
        
        
        File var2 = new File(zsr.worldFile, "/map.png");

        BufferedInputStream bis = null;
        try {
	        if (var2.exists())
	        {
	        	bis = new BufferedInputStream(new FileInputStream(var2));
	        }
        
        
	        //mc.renderEngine.singleIntBuffer.clear();
	        //GLAllocation.generateTextureNames(mc.renderEngine.singleIntBuffer);
	        int var3 = 9951;//mc.renderEngine.singleIntBuffer.get(0);
	        
	        BufferedImage bi = ImageIO.read(bis);
	        bis.close();
	        
	        //mc.renderEngine.setupTexture(bi, var3);
	        //GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3);
	        
	        //GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/gui/book.png"));
	        
	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3);
        
        } catch (Exception ex) {
        	//ex.printStackTrace();
        }
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float height = 110.0F;
        tess.startDrawingQuads();
        tess.setColorOpaque_I(0xFFFFFF);
        tess.addVertex((double)left,  (double)bottom, 0.0D);
        tess.addVertex((double)right, (double)bottom, 0.0D);
        tess.addVertex((double)right, (double)top,    0.0D);
        tess.addVertex((double)left,  (double)top,    0.0D);
        tess.draw();
    }

    /**
     * Gets a GuiYesNo screen with the warning, buttons, etc.
     */
    public static GuiYesNo getDeleteWorldScreen(GuiScreen par0GuiScreen, String par1Str, int par2)
    {
        StringTranslate var3 = StringTranslate.getInstance();
        String var4 = var3.translateKey("selectWorld.deleteQuestion");
        String var5 = "\'" + par1Str + "\' " + var3.translateKey("selectWorld.deleteWarning");
        String var6 = var3.translateKey("selectWorld.deleteButton");
        String var7 = var3.translateKey("gui.cancel");
        GuiYesNo var8 = new GuiYesNo(par0GuiScreen, var4, var5, var6, var7, par2);
        return var8;
    }

    static List getList(GuiSelectZCMap par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.saveList;
    }

    /**
     * called whenever an element in this gui is selected
     */
    static int onElementSelected(GuiSelectZCMap par0GuiSelectWorld, int par1)
    {
        return par0GuiSelectWorld.selectedWorld = par1;
    }

    /**
     * returns the world currently selected
     */
    static int getSelectedWorld(GuiSelectZCMap par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.selectedWorld;
    }

    /**
     * returns the select button
     */
    static GuiButton getSelectButton(GuiSelectZCMap par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.buttonSelect;
    }

    /**
     * returns the rename button
     */
    static GuiButton getRenameButton(GuiSelectZCMap par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.buttonRename;
    }

    /**
     * returns the delete button
     */
    static GuiButton getDeleteButton(GuiSelectZCMap par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.buttonDelete;
    }

    static GuiButton func_82312_f(GuiSelectZCMap par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.field_82316_w;
    }

    static String func_82313_g(GuiSelectZCMap par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.localizedWorldText;
    }

    static DateFormat func_82315_h(GuiSelectZCMap par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.dateFormatter;
    }

    static String func_82311_i(GuiSelectZCMap par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.localizedMustConvertText;
    }

    static String[] func_82314_j(GuiSelectZCMap par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.localizedGameModeText;
    }
}
