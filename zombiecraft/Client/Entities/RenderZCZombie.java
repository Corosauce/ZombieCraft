package zombiecraft.Client.Entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

import zombiecraft.Core.Entities.BaseEntAI;
import zombiecraft.Core.Entities.Zombie;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZombieCraftMod;
import build.render.Overlays;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZCZombie extends RenderBiped
{
    public RenderZCZombie()
    {
        super(new ModelZombieZC(), 0.5F);
    }

	@Override
	protected ResourceLocation func_110775_a(Entity entity) {
		return new ResourceLocation(ZombieCraftMod.modID + ":textures/entities/zombie.png");
	}
    
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
    	super.doRender(par1Entity, par2, par4, par6, par8, par9);
		
    	boolean debug = MinecraftServer.getServer() != null && ZCGame.instance().mapMan != null && MinecraftServer.getServer().isSinglePlayer() && ZCGame.instance().mapMan.editMode;
    	
    	if (debug) renderDebug((Zombie)par1Entity, par2, par4, par6, par8, par9);
    	
    }
    
    public ModelBase getMainModel() {
    	return modelBipedMain;
    }
    
    public void renderDebug(Zombie par1Entity, double par2, double par4, double par6, float par8, float par9) {
    	
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
		
		
			if (pe != null && z.agent.jobMan != null && z.agent.jobMan.getPrimaryJob() != null/*par1Entity.currentAction != null*/) {
				//if () {
					this.renderLivingLabel(par1Entity, String.valueOf(pe.getCurrentPathIndex() + " - " + z.agent.jobMan.getPrimaryJob().state), par2, par4, par6, 999);
				//}
			} else {
				//this.renderLivingLabel(par1Entity, "wat", par2, par4, par6, 999);
			}
		}
		
    }
    
    protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
    {
        GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);

        if (par1EntityLivingBase.deathTime > 0)
        {
            float var5 = ((float)par1EntityLivingBase.deathTime + par4 - 1.0F) / 20.0F * 1.6F;
            var5 = MathHelper.sqrt_float(var5);

            if (var5 > 1.0F)
            {
                var5 = 1.0F;
            }

            if (((BaseEntAI)par1EntityLivingBase).wasHeadshot) {
            	GL11.glRotatef(var5 * this.getDeathMaxRotation(par1EntityLivingBase), 1.0F, 0.0F, 0.0F);
            } else {
            	GL11.glRotatef(var5 * this.getDeathMaxRotation(par1EntityLivingBase), 0.0F, 0.0F, 1.0F);
            }
        }
    }
}
