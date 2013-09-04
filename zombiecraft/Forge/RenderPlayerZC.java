package zombiecraft.Forge;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Items.ItemGun;

public class RenderPlayerZC extends RenderPlayer {

	@Override
	public void func_130009_a(AbstractClientPlayer par1EntityPlayer, double par2, double par4, double par6, float par8, float par9) {
		
		super.func_130009_a(par1EntityPlayer, par2, par4, par6, par8, par9);
	}
	
	@Override
	public void func_130000_a(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9) {
		
		super.func_130000_a(par1EntityLivingBase, par2, par4, par6, par8, par9);
	}
	
	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2) {
		ModelBiped model = (ModelBiped)ZCUtil.getPrivateValueBoth(RenderLiving.class, this, "i", "mainModel");
		
		//aiming and arm sockets at head
		if (model != null) {
			if (par1EntityLivingBase.getHeldItem() != null && par1EntityLivingBase.getHeldItem().getItem() instanceof ItemGun) {
				model.aimedBow = true;
			}
			//model.bipedRightArm.rotationPointY = 0F;
			//model.bipedLeftArm.rotationPointY = 0F;
			
			//if (model.aimedBow)
	        //{
				
			
			//this doesnt work, needs model override post hook for set rotation angles in model biped
			
				/*float par3 = 500F;
				
	            float var8 = 0.0F;
	            float var9 = 0.0F;
	            model.bipedRightArm.rotateAngleZ = 0.0F;
	            model.bipedLeftArm.rotateAngleZ = 0.0F;
	            model.bipedRightArm.rotateAngleY = -(0.1F - var8 * 0.6F) + model.bipedHead.rotateAngleY;
	            model.bipedLeftArm.rotateAngleY = 0.1F - var8 * 0.6F + model.bipedHead.rotateAngleY + 0.4F;
	            model.bipedRightArm.rotateAngleX = -((float)Math.PI / 2F) + model.bipedHead.rotateAngleX;
	            model.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F) + model.bipedHead.rotateAngleX;
	            model.bipedRightArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
	            model.bipedLeftArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
	            model.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
	            model.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
	            model.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
	            model.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;*/
	        //}
			
	        
	            
		}
		super.preRenderCallback(par1EntityLivingBase, par2);
	}
}
