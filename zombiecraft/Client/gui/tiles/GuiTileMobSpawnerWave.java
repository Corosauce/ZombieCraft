package zombiecraft.Client.gui.tiles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;

import CoroAI.tile.PacketHelper;

public class GuiTileMobSpawnerWave extends GuiTileBase {

	TileEntityMobSpawnerWave tEnt;
	
	
	
	public GuiTileMobSpawnerWave (InventoryPlayer inventoryPlayer,
			TileEntityMobSpawnerWave tileEntity) {
		super(new ContainerTileMobSpawnerWave(inventoryPlayer, tileEntity));
		tEnt = tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		super.drawGuiContainerForegroundLayer(param1, param2);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
	}
	
	@Override
    public void initGui()
    {
		super.initGui();
		
		NBTTagCompound data = new NBTTagCompound();
        data.setBoolean("sync", true);
        PacketHelper.sendClientPacket(PacketHelper.createPacketForTEntCommand(tEnt, data));
    }
	
	@Override
	protected void actionPerformed(GuiButton var1)
    {
		int val = 0;
		String username = "";
        if (mc.thePlayer != null) username = mc.thePlayer.username;
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("username", username);
        nbt.setInteger("cmdID", var1.id);
        
        PacketHelper.sendClientPacket(PacketHelper.createPacketForTEntCommand(tEnt, nbt));
    }
	
	public int sanitize(int val) {
		return sanitize(val, 0, 9999);
	}
	
	public int sanitize(int val, int min, int max) {
		if (val > max) val = max;
        if (val < min) val = min;
		return val;
	}

}
