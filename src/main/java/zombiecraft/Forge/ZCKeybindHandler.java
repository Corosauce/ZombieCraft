package zombiecraft.Forge;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ZCKeybindHandler {

	//might need to revise how we manage multiple keys at once, for now only allow 1 hold down and let go before switching(?)
	
	public static KeyBinding consoleKey = new KeyBinding("ZC_Menu", Keyboard.KEY_GRAVE, "key.categories.gameplay");
	public static KeyBinding useKey = new KeyBinding("ZC_Use", Keyboard.KEY_E, "key.categories.gameplay");
	public static KeyBinding reloadKey = new KeyBinding("ZC_Reload", Keyboard.KEY_R, "key.categories.gameplay");
	public static KeyBinding chargeKey = new KeyBinding("ZC_Charge", Keyboard.KEY_LCONTROL, "key.categories.gameplay");
	public static KeyBinding cameraKey = new KeyBinding("ZC_Camera", Keyboard.KEY_SEMICOLON, "key.categories.gameplay");
	public static KeyBinding zoomKey = new KeyBinding("ZC_Zoom", Keyboard.KEY_Z, "key.categories.gameplay");

	public static boolean keyDown = false;
	public static KeyBinding keyDownBinding = null;
	
    public ZCKeybindHandler() {
            //the first value is an array of KeyBindings, the second is whether or not the call
            //keyDown should repeat as long as the key is down
            //super(new KeyBinding[]{consoleKey, useKey, reloadKey, chargeKey, cameraKey, zoomKey}, new boolean[]{false, false, false, false, false, false});
    }
    
    public static void tickClient() {
    	
    	if (keyDownBinding != null) {
    		if (!keyDownBinding.getIsKeyPressed()) {
    			ZCClientTicks.iMan.keyEvent(keyDownBinding, true);
    			keyDown = false;
    			keyDownBinding = null;
    		} else {
    			return;
    		}
    	}
    	
    	boolean wasKeyDown = keyDown;
    	
    	if (consoleKey.getIsKeyPressed()) {
    		setKeyDown(consoleKey);
    	} else if (useKey.getIsKeyPressed()) {
    		setKeyDown(useKey);
    	} else if (reloadKey.getIsKeyPressed()) {
    		setKeyDown(reloadKey);
    	} else if (chargeKey.getIsKeyPressed()) {
    		setKeyDown(chargeKey);
    	} else if (cameraKey.getIsKeyPressed()) {
    		setKeyDown(cameraKey);
    	} else if (zoomKey.getIsKeyPressed()) {
    		setKeyDown(zoomKey);
    	}
    	
    	if (keyDownBinding != null) {
	    	if (wasKeyDown != keyDown) {
	    		ZCClientTicks.iMan.keyEvent(keyDownBinding, false);
	    	}
    	}
    }
    
    public static void setKeyDown(KeyBinding parKey) {
    	keyDownBinding = parKey;
    	keyDown = true;
    }

    /*@Override
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
    }*/
}
