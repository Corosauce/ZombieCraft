package zombiecraft.Core.Camera;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import zombiecraft.Forge.ZCClientTicks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
            
            if (this.gameSettings.keyBindForward.getIsKeyPressed()) this.camForward++;
            if (this.gameSettings.keyBindBack.getIsKeyPressed()) this.camForward--;
            if (this.gameSettings.keyBindLeft.getIsKeyPressed()) this.camStrafe++;
            if (this.gameSettings.keyBindRight.getIsKeyPressed()) this.camStrafe--;
            this.camUp = this.gameSettings.keyBindJump.getIsKeyPressed();
            this.camDown = this.gameSettings.keyBindSneak.getIsKeyPressed();
            
    	} else {
    		if (this.gameSettings.keyBindForward.getIsKeyPressed())
            {
                ++this.moveForward;
            }

            if (this.gameSettings.keyBindBack.getIsKeyPressed())
            {
                --this.moveForward;
            }

            if (this.gameSettings.keyBindLeft.getIsKeyPressed())
            {
                ++this.moveStrafe;
            }

            if (this.gameSettings.keyBindRight.getIsKeyPressed())
            {
                --this.moveStrafe;
            }

            this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
            this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();

            if (this.sneak)
            {
                this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
                this.moveForward = (float)((double)this.moveForward * 0.3D);
            }
    	}
    	
        

        
    }
}
