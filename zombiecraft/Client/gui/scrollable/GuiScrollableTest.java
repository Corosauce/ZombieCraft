package zombiecraft.Client.gui.scrollable;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.src.ModLoader;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.EnumGameType;
import zombiecraft.Core.ZombieSaveRecord;
import zombiecraft.Core.GameLogic.ZCGame;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiScrollableTest extends GuiScreen implements IScrollingGUI
{
    /** simple date formater */
    private final DateFormat dateFormatter = new SimpleDateFormat();

    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    protected GuiScreen parentScreen;

    /** The title string that is displayed in the top-center of the screen. */
    protected String screenTitle = "Generic Scrollable Gui";

    /** True if a world has been selected. */
    private boolean selected = false;

    /** the currently selected world */
    private int selectedWorld;

    /** The save list for the world selection screen */
    public List<IScrollingElement> saveList;
    private GuiSlotImpl guiScrollable;

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

    public GuiScrollableTest(GuiScreen par1GuiScreen)
    {
        this.parentScreen = par1GuiScreen;
        mc = FMLClientHandler.instance().getClient();
        saveList = new ArrayList<IScrollingElement>();
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
        this.guiScrollable = new GuiSlotImpl(this, saveList, 180, this.height, 32, this.height - 32, 24);
        //this.worldSlotContainer.func_77207_a(40, 80, 30, 180);
        this.guiScrollable.registerScrollButtons(this.buttonList, 4, 5); //useless
        this.initButtons();
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
	                	saveList.add(new MapEntry(new ZombieSaveRecord(zombieWorldDir,file.getName(),0)));
	                } else if (file.isFile() && file.getName().toLowerCase().endsWith(".zip")) {
	                	saveList.add(new MapEntry(new ZombieSaveRecord(zombieWorldDir,file.getName(),1)));
	                } else if (file.isFile() && file.getName().toLowerCase().endsWith(".schematic")) {
	                	saveList.add(new MapEntry(new ZombieSaveRecord(zombieWorldDir,file.getName(),2)));
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
        //Collections.sort(this.saveList);
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
                
            }
            else
            {
                this.guiScrollable.actionPerformed(par1GuiButton);
            }
        }
    }

    public void confirmClicked(boolean par1, int par2)
    {
        
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.guiScrollable.drawScreen(par1, par2, par3);
        //this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        
        fontRenderer.drawString(screenTitle, 12, 20, 16777215);
        
        IScrollingElement entry = (IScrollingElement)saveList.get(selectedWorld);
        
        if (entry != null) {
        	
        	//drawMapGraphic(zsr, Tessellator.instance, 200, 420, 40, 0);
        	
        	fontRenderer.drawString(entry.getTitle(), 180 + 20, 20, 16777215);
        	
        	String str = entry.getExtraInfo();
        	if (str != null) {
        		fontRenderer.drawSplitString(str, 180 + 20, 40, 220, 16777215);
        	}
        }
        
        super.drawScreen(par1, par2, par3);
    }

    /**
     * called whenever an element in this gui is selected
     */
    public void onElementSelected(int par1)
    {
        selectedWorld = par1;
    }

    /**
     * returns the world currently selected
     */
    public int getSelectedElement()
    {
        return selectedWorld;
    }

    /**
     * returns the select button
     */
    public GuiButton getSelectButton()
    {
        return buttonSelect;
    }

	@Override
	public void drawBackground() {
	    this.drawDefaultBackground();
	}
}
