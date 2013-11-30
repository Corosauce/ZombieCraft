package zombiecraft.Client.Blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import zombiecraft.Core.towers.models.ModelTurret;

public class TileEntityTowerRenderer extends TileEntitySpecialRenderer
{
	public ModelTurret model;
	
    public static final ResourceLocation resTex = new ResourceLocation("/mods/ZombieCraft/textures/tileentities/map.png");
	
	public TileEntityTowerRenderer() {
		model = new ModelTurret();
		// TODO Auto-generated constructor stub
	}
	
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
    	GL11.glPushMatrix();
    	//model = new ModelTurret();
    	float var10 = 0.06F;
    	Minecraft.getMinecraft().getTextureManager().bindTexture(resTex);
    	//GL11.glEnable(GL11.GL_ALPHA_TEST);
    	GL11.glTranslatef((float)var2+0.5F, (float)var4+2.5F, (float)var6+0.5F);
    	GL11.glRotatef(180F, 0F, 0F, 1F);
    	//GL11.glEnable(GL12.GL_RESCALE_NORMAL); ????????????
    	GL11.glScalef(var10, var10, var10);
    	Tessellator tess = Tessellator.instance;
    	/*int i = (int)var2;
    	int j = (int)var4+2;
    	int k = (int)var6;
        float brightness = ZCBlocks.tower.getBlockBrightness(var1.worldObj, i, j+2, k);
        int skyLight = var1.worldObj.getLightBrightnessForSkyBlocks(i, j+2, k, 0);
        int modulousModifier = skyLight % 65536;
        int divModifier = skyLight / 65536;
        tess.setColorOpaque_F(brightness, brightness, brightness);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,  (float) modulousModifier,  divModifier);*/
    	/*GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);*/
        //GL11.glDisable(GL11.GL_LIGHTING);
        //tess.setBrightness(9999);/*setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.9F);*/
    	//GL11.glDisable(GL11.GL_BLEND);
    	float br = 0.3F;
    	GL11.glColor4f(br, br, br, 1F);
    	
    	/*tess.startDrawingQuads();
    	tess.draw();*/
    	model.render(null, (float)0, (float)0, (float)0, 0F, 0F, 1F);
    	
    	//GL11.glEnable(GL11.GL_BLEND);
    	GL11.glPopMatrix();
    }
    
}
