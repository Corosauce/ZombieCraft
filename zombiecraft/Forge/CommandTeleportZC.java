package zombiecraft.Forge;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import zombiecraft.Core.GameLogic.ZCGame;

public class CommandTeleportZC extends CommandBase {

	@Override
	public String getCommandName() {
		return "zc";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		
		if (var2 != null && var2.length > 0) {
			EntityPlayer ent = ZCGame.instance().getWorld().getPlayerEntityByName(var2[0]);
			
			if (ent != null) {
				ZombieCraftMod.teleportPlayerToggle((EntityPlayerMP)ent);
			}
		} else {
			if(var1 instanceof EntityPlayerMP)
			{
				EntityPlayer player = getCommandSenderAsPlayer(var1);
				ZombieCraftMod.teleportPlayerToggle((EntityPlayerMP)player);
			}
		}
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true/*par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName())*/;
    }

}
