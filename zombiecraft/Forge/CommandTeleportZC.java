package zombiecraft.Forge;

import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;

public class CommandTeleportZC extends CommandBase {

	@Override
	public String getCommandName() {
		return "zc";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		EntityPlayer player = getCommandSenderAsPlayer(var1);
		if(player instanceof EntityPlayerMP)
		{
			ZombieCraftMod.teleportPlayerToggle((EntityPlayerMP)player);
		}
		else
		{
			System.out.println("Not EntityPlayerMP");
		}
	}

}
