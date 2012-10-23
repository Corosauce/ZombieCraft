package zombiecraft.Core.Camera;

import zombiecraft.Forge.ZCClientTicks;
import net.minecraft.src.*;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class MovementInputProxy extends MovementInput
{
    private GameSettings gameSettings;
    
    public float camForward;
    public float camStrafe;
    public boolean camUp;
    public boolean camDown;

    public MovementInputProxy(GameSettings par1GameSettings)
    {
        this.gameSettings = par1GameSettings;
    }

    public void updatePlayerMoveState()
    {
    	
    	this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;
    	
    	if (ZCClientTicks.camMan.camState == EnumCameraState.FREE) {
    		this.camForward = 0.0F;
            this.camStrafe = 0.0F;
            
            if (this.gameSettings.keyBindForward.pressed) this.camForward++;
            if (this.gameSettings.keyBindBack.pressed) this.camForward--;
            if (this.gameSettings.keyBindLeft.pressed) this.camStrafe++;
            if (this.gameSettings.keyBindRight.pressed) this.camStrafe--;
            this.camUp = this.gameSettings.keyBindJump.pressed;
            this.camDown = this.gameSettings.keyBindSneak.pressed;
            
    	} else {
    		if (this.gameSettings.keyBindForward.pressed)
            {
                ++this.moveForward;
            }

            if (this.gameSettings.keyBindBack.pressed)
            {
                --this.moveForward;
            }

            if (this.gameSettings.keyBindLeft.pressed)
            {
                ++this.moveStrafe;
            }

            if (this.gameSettings.keyBindRight.pressed)
            {
                --this.moveStrafe;
            }

            this.jump = this.gameSettings.keyBindJump.pressed;
            this.sneak = this.gameSettings.keyBindSneak.pressed;

            if (this.sneak)
            {
                this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
                this.moveForward = (float)((double)this.moveForward * 0.3D);
            }
    	}
    	
        

        
    }
}
