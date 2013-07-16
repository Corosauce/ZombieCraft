package zombiecraft.Client.Entities;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;

import zombiecraft.Core.Entities.BaseEntAI;
import zombiecraft.Core.Entities.Zombie;
import zombiecraft.Core.GameLogic.ZCGame;
import CoroAI.entity.c_EnhAI;
import build.render.Overlays;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZCComrade extends RenderBiped
{
    public RenderZCComrade()
    {
        super(new ModelBiped(0.0F), 0.5F);
    }
    
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
    	
    	doRenderComrade((c_EnhAI) par1Entity, par2, par4, par6, par8, par9);
    	
    	super.doRender(par1Entity, par2, par4, par6, par8, par9);
    	
		
    	boolean debug = MinecraftServer.getServer() != null && ZCGame.instance().mapMan != null && MinecraftServer.getServer().isSinglePlayer() && ZCGame.instance().mapMan.editMode;
    	
    	if (debug) renderDebug((BaseEntAI)par1Entity, par2, par4, par6, par8, par9);
    	
    }
    
    public void doRenderComrade(c_EnhAI par1EntityPlayer, double par2, double par4, double par6, float par8, float par9)
    {
    	
    	//renderEquippedItems(par1EntityPlayer, par9);
    	
        /*float var10 = 1.0F;
        GL11.glColor3f(var10, var10, var10);
        ItemStack var11 = par1EntityPlayer.getCurrentEquippedItem();
        this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = var11 != null ? 1 : 0;

        if (var11 != null && par1EntityPlayer.getItemInUseCount() > 0)
        {
            EnumAction var12 = var11.getItemUseAction();

            if (var12 == EnumAction.block)
            {
                this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 3;
            }
            else if (var12 == EnumAction.bow)
            {
                this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = true;
            }
        }

        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = par1EntityPlayer.isSneaking();
        double var14 = par4 - (double)par1EntityPlayer.yOffset;

        if (par1EntityPlayer.isSneaking() && !(par1EntityPlayer instanceof EntityPlayerSP))
        {
            var14 -= 0.125D;
        }

        super.doRenderLiving(par1EntityPlayer, par2, var14, par6, par8, par9);
        this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = false;
        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
        this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 0;*/
    }
    
    public void renderDebug(BaseEntAI par1Entity, double par2, double par4, double par6, float par8, float par9) {
    	
	    Entity sEnt = MinecraftServer.getServer().worldServerForDimension(ZCGame.ZCDimensionID).getEntityByID(par1Entity.entityId);
		
		if (sEnt != null && sEnt instanceof Zombie) {
			Zombie z = (Zombie)sEnt;
			PathEntity pe = ((EntityLiving)sEnt).getNavigator().getPath();
			
			if (pe != null && pe.getCurrentPathLength() > 1) {
				for (int i = 0; i < pe.getCurrentPathLength() - 1; i++) {
					Vec3 vec = pe.getVectorFromIndex(par1Entity, i);
					Vec3 vec2 = pe.getVectorFromIndex(par1Entity, i+1);
					Overlays.renderLineFromToBlock(vec.xCoord, vec.yCoord+0.5F, vec.zCoord, vec2.xCoord, vec2.yCoord+0.5F, vec2.zCoord, 0x00FF00);
				}
			}
		
		
			if (pe != null/*par1Entity.currentAction != null*/) {
				//if () {
					this.renderLivingLabel(par1Entity, String.valueOf(pe.getCurrentPathIndex() + " - " + z.job.getJobClass().state), par2, par4, par6, 999);
				//}
			} else {
				//this.renderLivingLabel(par1Entity, "wat", par2, par4, par6, 999);
			}
		}
		
    }
}
