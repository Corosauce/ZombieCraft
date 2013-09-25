package zombiecraft.Client.gui.elements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextFieldZC extends GuiTextField {

	public String name;
	
	public GuiTextFieldZC(String parName, FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5) {
		super(par1FontRenderer, par2, par3, par4, par5);
		name = parName;
		setMaxStringLength(128);
	}

}
