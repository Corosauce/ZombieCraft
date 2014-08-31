package zombiecraft.Forge;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class ZCBarricadePlaceableRenderer implements ISimpleBlockRenderingHandler
{
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
    	
    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
    	draw2CrossedSquares(block, world.getBlockMetadata(x, y, z), x, y, z, 1F);
        return true;
    }

    public boolean shouldRender3DInInventory()
    {
         return false;
    }

    public int getRenderId()
    {
         return 66;
    }
    
    public void draw2CrossedSquares(Block par1Block, int par2, double par3, double par5, double par7, float par9)
    {
        Tessellator var10 = Tessellator.instance;
        //int var11 = par1Block.getIcon(0, par2);

        /*if (this.overrideBlockTexture >= 0)
        {
            var11 = this.overrideBlockTexture;
        }*/
        
        //var11++;

        /*int var12 = (var11 & 15) << 4;
        int var13 = var11 & 240;
        double var14 = (double)((float)var12 / 256.0F);
        double var16 = (double)(((float)var12 + 15.99F) / 256.0F);
        double var18 = (double)((float)var13 / 256.0F);
        double var20 = (double)(((float)var13 + 15.99F) / 256.0F);*/
        
        IIcon icon = par1Block.getIcon(0, par2);//ZCBlocks.barricadeTopTexIDs[5];////Minecraft.getMinecraft().renderGlobal.globalRenderBlocks.getBlockIconFromSideAndMetadata(par1Block, 0, par2);

        double var14 = (double)icon.getMinU();
        double var16 = (double)icon.getMaxU();
        double var18 = (double)icon.getMinV();
        double var20 = (double)icon.getMaxV();
        
        double var22 = 0.45D * (double)par9;
        
        //double xzScale = 0.45F;
        double rand = 0.0F;
        
        double var24 = par3 + 0.5D - var22 + rand;
        double var26 = par3 + 0.5D + var22;
        double var28 = par7 + 0.5D - var22;
        double var30 = par7 + 0.5D + var22;
        
        
        
        //GL11.glRotatef(45.0F, 1.0F, 1.0F, 0.0F);
        
        var10.addVertexWithUV(var24, par5 + (double)par9, var28, var14, var18);
        var10.addVertexWithUV(var24, par5 + 0.0D, var28, var14, var20);
        var10.addVertexWithUV(var26, par5 + 0.0D, var30, var16, var20);
        var10.addVertexWithUV(var26, par5 + (double)par9, var30, var16, var18);
        var10.addVertexWithUV(var26, par5 + (double)par9, var30, var14, var18);
        var10.addVertexWithUV(var26, par5 + 0.0D, var30, var14, var20);
        var10.addVertexWithUV(var24, par5 + 0.0D, var28, var16, var20);
        var10.addVertexWithUV(var24, par5 + (double)par9, var28, var16, var18);
        var10.addVertexWithUV(var24, par5 + (double)par9, var30, var14, var18);
        var10.addVertexWithUV(var24, par5 + 0.0D, var30, var14, var20);
        var10.addVertexWithUV(var26, par5 + 0.0D, var28, var16, var20);
        var10.addVertexWithUV(var26, par5 + (double)par9, var28, var16, var18);
        var10.addVertexWithUV(var26, par5 + (double)par9, var28, var14, var18);
        var10.addVertexWithUV(var26, par5 + 0.0D, var28, var14, var20);
        var10.addVertexWithUV(var24, par5 + 0.0D, var30, var16, var20);
        var10.addVertexWithUV(var24, par5 + (double)par9, var30, var16, var18);
        
        par5 += 0.5F;
        var24 = par3 + 1D;
        var26 = par3 + 0.0D;
        var28 = par7 + 0.5D;
        var30 = par7 + 0.5D;
        
        var10.addVertexWithUV(var24, par5 + (double)par9, var28, var14, var18);
        var10.addVertexWithUV(var24, par5 + 0.0D, var28, var14, var20);
        var10.addVertexWithUV(var26, par5 + 0.0D, var30, var16, var20);
        var10.addVertexWithUV(var26, par5 + (double)par9, var30, var16, var18);
        var10.addVertexWithUV(var26, par5 + (double)par9, var30, var14, var18);
        var10.addVertexWithUV(var26, par5 + 0.0D, var30, var14, var20);
        var10.addVertexWithUV(var24, par5 + 0.0D, var28, var16, var20);
        var10.addVertexWithUV(var24, par5 + (double)par9, var28, var16, var18);
        
        var24 = par3 + 0.5D;
        var26 = par3 + 0.5D;
        var28 = par7 + 0.5D - var22;
        var30 = par7 + 0.5D + var22;
        
        var10.addVertexWithUV(var24, par5 + (double)par9, var28, var14, var18);
        var10.addVertexWithUV(var24, par5 + 0.0D, var28, var14, var20);
        var10.addVertexWithUV(var26, par5 + 0.0D, var30, var16, var20);
        var10.addVertexWithUV(var26, par5 + (double)par9, var30, var16, var18);
        var10.addVertexWithUV(var26, par5 + (double)par9, var30, var14, var18);
        var10.addVertexWithUV(var26, par5 + 0.0D, var30, var14, var20);
        var10.addVertexWithUV(var24, par5 + 0.0D, var28, var16, var20);
        var10.addVertexWithUV(var24, par5 + (double)par9, var28, var16, var18);
        
        /*var10.addVertexWithUV(var24, par5 + (double)par9, var30, var14, var18);
        var10.addVertexWithUV(var24, par5 + 0.0D, var30, var14, var20);
        var10.addVertexWithUV(var26, par5 + 0.0D, var28, var16, var20);
        var10.addVertexWithUV(var26, par5 + (double)par9, var28, var16, var18);
        var10.addVertexWithUV(var26, par5 + (double)par9, var28, var14, var18);
        var10.addVertexWithUV(var26, par5 + 0.0D, var28, var14, var20);
        var10.addVertexWithUV(var24, par5 + 0.0D, var30, var16, var20);
        var10.addVertexWithUV(var24, par5 + (double)par9, var30, var16, var18);*/
        
        //GL11.glRotatef(45.0F, -1.0F, -1.0F, 0.0F);
    }

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}
}
