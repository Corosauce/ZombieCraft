package zombiecraft.Client.GameLogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import zombiecraft.Core.Buyables;
import zombiecraft.Core.CommandTypes;
import zombiecraft.Core.DataTypes;
import zombiecraft.Core.PacketTypes;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Client.*;
import zombiecraft.Forge.ZCClientTicks;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class InterfaceManagerMP extends InterfaceManager {
	
	public InterfaceManagerMP(Minecraft mc, ZCGame game) {
		super(mc, game);
		isRemote = true;
		if (mc.thePlayer != null) {
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY+1, mc.thePlayer.posZ);
		}
	}
	
	public void tick() {
		super.tick();
		
		if (holdingUse) {
			if (reBuyDelay == 0) {
				if ((Integer)zcGame.getData(mc.thePlayer, DataTypes.purchaseTimeout) > 0) {
					//buyMenuState = 0;
					zcGame.resetBuyState(mc.thePlayer, Buyables.barricadeRepairCooldown);
					ZCClientTicks.sendPacket(PacketTypes.COMMAND, new int[] {CommandTypes.USE});
					//resetBuyState(mc.thePlayer, Buyables.barricadeRepairCooldown);
					//ModLoaderMp.sendKey(ModLoaderMp.getModInstance(mod_ZombieCraft.class), CommandTypes.USE);
				}
			}
		}
		//debug
		/*if (Keyboard.isKeyDown(Keyboard.KEY_INSERT)) {
			ModLoaderMp.sendKey(ModLoaderMp.getModInstance(mod_ZombieCraft.class), Keyboard.KEY_INSERT);
			
		}*/
		
		
		
	}
	
	@Override
	public void toggleEditMode() {
		ZCClientTicks.sendPacket(PacketTypes.COMMAND, new int[] {CommandTypes.TOGGLE_EDIT});
	}
	
	@Override
	public void toggleNoClip() {
		ZCClientTicks.sendPacket(PacketTypes.COMMAND, new int[] {CommandTypes.TOGGLE_DOORNOCLIP});
	}
	
	@Override
	public void setStage(int wave, int reset) {
		ZCClientTicks.sendPacket(PacketTypes.COMMAND, new int[] {CommandTypes.SET_WAVE, wave, reset});
	}
	
	@Override
	public void keyEvent(KeyBinding key, boolean keyUp) {
		super.keyEvent(key, keyUp);
    	
	}
	
	public boolean startGame() {
		return false;
	}
	
	@Override
	public void setLevelSize(int x1, int y1, int z1, int x2, int y2, int z2) {
		ZCClientTicks.sendPacket(PacketTypes.COMMAND, new int[] {CommandTypes.SET_LEVELSIZE, x1, y1, z1, x2, y2, z2});
	}
	
}
