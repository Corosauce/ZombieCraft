package zombiecraft.Core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import zombiecraft.Core.Items.*;
import zombiecraft.Forge.ZombieCraftMod;

import net.minecraft.src.*;

public class ZCItems {
	
	@MLProp public static int z_ItemIDStart = 5600;
	
	public static Item barricade;
	public static Item editTool;
	public static Item buildTool;
	
	public ZCItems() {
		
	}
	
	public static void load() {
		barricade = (new ItemBarricade(z_ItemIDStart++, Material.wood)).setIconCoord(11, 2).setItemName("barricade").setCreativeTab(ZombieCraftMod.tabBlock);
		editTool = (new ItemEditTool(z_ItemIDStart++, 0)).setIconCoord(5, 4).setItemName("editTool").setCreativeTab(ZombieCraftMod.tabBlock);
		//buildTool = (new ItemBuildTool(z_ItemIDStart++, 0)).setIconCoord(5, 5).setItemName("buildTool").setCreativeTab(ZombieCraftMod.tabBlock);
	}
}
