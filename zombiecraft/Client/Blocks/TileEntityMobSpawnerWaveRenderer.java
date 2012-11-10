package zombiecraft.Client.Blocks;

import net.minecraft.src.*;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

import build.render.Overlays;

import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Client.*;

public class TileEntityMobSpawnerWaveRenderer extends TileEntitySpecialRenderer
{
    /**
     * Hash map of the entities that the mob spawner has rendered/rendering spinning inside of them
     */
    private Map entityHashMap = new HashMap();

    public void renderTileEntityMobSpawner(TileEntityMobSpawnerWave par1TileEntityMobSpawner, double par2, double par4, double par6, float par8)
    {
    	
    	if (ZCGame.instance() != null && ZCGame.instance().mapMan != null && ZCGame.instance().mapMan.editMode && ZCGame.instance().mapMan.infoOverlay) {
	    	renderLivingLabel((par1TileEntityMobSpawner.act_Watch ? "Watch | " : "") + (par1TileEntityMobSpawner.act_Proximity ? "Proximity" : ""), par2+0.5F, par4, par6+0.5F, 0);
	    	Overlays.renderLineFromToBlockCenter(par1TileEntityMobSpawner.xCoord, par1TileEntityMobSpawner.yCoord, par1TileEntityMobSpawner.zCoord, par1TileEntityMobSpawner.watchX, par1TileEntityMobSpawner.watchY, par1TileEntityMobSpawner.watchZ, 0x00AA00);
    	}
    	
    	if (true) return;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2 + 0.5F, (float)par4, (float)par6 + 0.5F);
        Entity var9 = (Entity)this.entityHashMap.get(par1TileEntityMobSpawner.getMobID());

        if (var9 == null)
        {
            var9 = EntityList.createEntityByName(par1TileEntityMobSpawner.getMobID(), (World)null);
            this.entityHashMap.put(par1TileEntityMobSpawner.getMobID(), var9);
        }

        if (var9 != null)
        {
            var9.setWorld(par1TileEntityMobSpawner.worldObj);
            float var10 = 0.4375F;
            GL11.glTranslatef(0.0F, 0.4F, 0.0F);
            GL11.glRotatef((float)(par1TileEntityMobSpawner.yaw2 + (par1TileEntityMobSpawner.yaw - par1TileEntityMobSpawner.yaw2) * (double)par8) * 10.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.4F, 0.0F);
            GL11.glScalef(var10, var10, var10);
            var9.setLocationAndAngles(par2, par4, par6, 0.0F, 0.0F);
            RenderManager.instance.renderEntityWithPosYaw(var9, 0.0D, 0.0D, 0.0D, 0.0F, par8);
        }

        GL11.glPopMatrix();
    }

    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        this.renderTileEntityMobSpawner((TileEntityMobSpawnerWave)par1TileEntity, par2, par4, par6, par8);
    }
    
    protected void renderLivingLabel(String par2Str, double par3, double par5, double par7, int par9)
    {
        //float var10 = par1EntityLiving.getDistanceToEntity(this.renderManager.livingPlayer);

    	
    	
        //if (var10 <= (float)par9)
        //{
            FontRenderer var11 = RenderManager.instance.getFontRenderer();
            float var12 = 1.6F;
            float var13 = 0.016666668F * var12;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + 2.3F, (float)par7);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-var13, -var13, var13);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator var14 = Tessellator.instance;
            byte var15 = 0;

            if (par2Str.equals("deadmau5"))
            {
                var15 = -10;
            }

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            var14.startDrawingQuads();
            int var16 = var11.getStringWidth(par2Str) / 2;
            var14.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            var14.addVertex((double)(-var16 - 1), (double)(-1 + var15), 0.0D);
            var14.addVertex((double)(-var16 - 1), (double)(8 + var15), 0.0D);
            var14.addVertex((double)(var16 + 1), (double)(8 + var15), 0.0D);
            var14.addVertex((double)(var16 + 1), (double)(-1 + var15), 0.0D);
            var14.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, var15, 553648127);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, var15, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        //}
    }
}
