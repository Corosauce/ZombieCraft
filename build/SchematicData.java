package build;

import build.world.Build;
import net.minecraft.src.NBTTagCompound;

public interface SchematicData {
	abstract void readFromNBT(NBTTagCompound par1NBTTagCompound, Build build);
	abstract void writeToNBT(NBTTagCompound par1NBTTagCompound, Build build);
}
