package zombiecraft.Forge;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandPoints extends CommandBase {

	@Override
	public String getCommandName() {
		return "points";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		EntityPlayer player = getCommandSenderAsPlayer(var1);
		if(var1 instanceof EntityPlayerMP)
		{
			int points = 200;
			if (var2 != null && var2.length > 0) points = Integer.valueOf(var2[0]);
			ZCServerTicks.zcGame.givePoints(player, points);
		}
		else
		{
			System.out.println("Not EntityPlayerMP");
		}
	}

}
