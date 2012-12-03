package build;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ServerCommandManager;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

public class BuildCommonProxy implements IGuiHandler
{
    public BuildMod mod;

    public BuildCommonProxy()
    {
    }

    public void init(BuildMod pMod)
    {
        mod = pMod;
        TickRegistry.registerTickHandler(new BuildServerTicks(), Side.SERVER);
        
        //pMod.itemEditTool = (ItemEditTool) (new ItemEditTool(5467, 1)).setIconIndex(8).setItemName("build tool").setCreativeTab(CreativeTabs.tabMisc);
        //ModLoader.addName(pMod.itemEditTool, "Build Tool");
    }

    public void registerRenderInformation()
    {
    }

    public void registerTileEntitySpecialRenderer()
    {
    }

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}
}
