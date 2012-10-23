package zombiecraft.Forge;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.*;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class ZCKeybindHandler extends KeyHandler {

	static KeyBinding consoleKey = new KeyBinding("ZC_Menu", Keyboard.KEY_GRAVE);
	static KeyBinding useKey = new KeyBinding("ZC_Use", Keyboard.KEY_E);
	static KeyBinding reloadKey = new KeyBinding("ZC_Reload", Keyboard.KEY_R);

    public ZCKeybindHandler() {
            //the first value is an array of KeyBindings, the second is whether or not the call
            //keyDown should repeat as long as the key is down
            super(new KeyBinding[]{consoleKey, useKey, reloadKey}, new boolean[]{false, false, false});
    }

    @Override
    public String getLabel() {
            return "ZC Key bindings";
    }

    @Override
    public void keyDown(EnumSet<TickType> types, KeyBinding kb,
                    boolean tickEnd, boolean isRepeat) {
            //do whatever
    	if (ZCClientTicks.iMan != null) {
    		ZCClientTicks.iMan.keyEvent(kb, false);
		}
    }

    @Override
    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
            //do whatever
    	//System.out.println(FMLCommonHandler.instance().getEffectiveSide());
    	//System.out.println(kb.keyDescription);
    	
    	if (ZCClientTicks.iMan != null) {
    		ZCClientTicks.iMan.keyEvent(kb, true);
		}
    }

    @Override
    public EnumSet<TickType> ticks() {
            return EnumSet.of(TickType.CLIENT);
            //I am unsure if any different TickTypes have any different effects.
    }
}
