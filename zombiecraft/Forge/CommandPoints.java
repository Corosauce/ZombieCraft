package zombiecraft.Forge;

import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;

public class CommandPoints extends CommandBase {

	@Override
	public String getCommandName() {
		return "points";
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
