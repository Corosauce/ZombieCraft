package zombiecraft.Forge;

import net.minecraft.src.*;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Render;
import net.minecraft.src.RenderManager;
import net.minecraftforge.client.IItemRenderer;

public class GunRenderer implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		// TODO Auto-generated method stub

		
		
		if (data.length > 1 && data[1] instanceof EntityPlayer) {
			
			//System.out.println("what: " + data);
			
			EntityPlayer entP = (EntityPlayer)data[1];
			EntityLiving entL = new EntityChicken(entP.worldObj);
			entL.setPosition(entP.posX, entP.posY, entP.posZ);
			Render var10 = null;
			var10 = RenderManager.instance.getEntityRenderObject(entL);
			float partialTick = 1F;
			var10.doRender(entL, 0, 0, 0, entL.rotationYaw, partialTick);
			
			//this causes recursive loop, need to remake own item render code :/
			//RenderManager.instance.itemRenderer.renderItem(entP, entP.getCurrentEquippedItem(), 0);
		}
		
	}

	
}
