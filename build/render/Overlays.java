package build.render;

import org.lwjgl.opengl.GL11;

import build.world.Build;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.MathHelper;
import net.minecraft.src.PathPoint;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Tessellator;

@SideOnly(Side.CLIENT)
public class Overlays {

	
	public static void renderBuildOutline(Build buildData) {
		float x = buildData.map_coord_minX;// + 0.5F;
		float y = buildData.map_coord_minY;// + 0.5F;
		float z = buildData.map_coord_minZ;// + 0.5F;
		float x1 = buildData.map_sizeX + x - 1F;
		float y1 = buildData.map_sizeY + y - 1F;
		float z1 = buildData.map_sizeZ + z - 1F;
		renderBuildOutline(x, y, z, x1, y1, z1);
	}
	
	public static void renderBuildOutline(float x, float y, float z, float x1, float y1, float z1) {
		
		x += 0.5F;
		y += 0.5F;
		z += 0.5F;
		x1 += 0.5F;
		y1 += 0.5F;
		z1 += 0.5F;
		
		//x += (mod_ZombieCraft.worldRef.rand.nextFloat() * 0.3F);
		
		int color = 0xFF0000;
		
		//cross intersects
		renderLineFromToBlock(x, y, z, x1, y1, z1, color);
		renderLineFromToBlock(x1, y, z1, x, y1, z, color);
		renderLineFromToBlock(x, y, z1, x1, y1, z, color);
		renderLineFromToBlock(x1, y, z, x, y1, z1, color);
		
		//corner 0
		renderLineFromToBlock(x, y, z, x, y, z1, color);
		renderLineFromToBlock(x, y, z, x, y1, z, color);
		renderLineFromToBlock(x, y, z, x1, y, z, color);
		
		//corner 1
		renderLineFromToBlock(x1, y, z, x1, y1, z, color);
		renderLineFromToBlock(x1, y, z, x1, y, z1, color);
		
		//corner 2
		renderLineFromToBlock(x1, y, z1, x1, y1, z1, color);
		renderLineFromToBlock(x1, y, z1, x, y, z1, color);
		
		//corner 3
		renderLineFromToBlock(x, y, z1, x, y1, z1, color);
		//renderLineFromToBlock(x1, y, z1, x, y, z1);
		
		//top parts
		renderLineFromToBlock(x, y1, z, x1, y1, z, color);
		renderLineFromToBlock(x1, y1, z, x1, y1, z1, color);
		renderLineFromToBlock(x1, y1, z1, x, y1, z1, color);
		renderLineFromToBlock(x, y1, z1, x, y1, z, color);
		
		//renderLineFromToBlock(x1, y, z, x1, y1, z1);
		
	}

	public static void renderLine(PathPoint ppx, PathPoint ppx2, double d, double d1, double d2, float f, float f1, int stringColor) {
		renderLineFromToBlock(ppx.xCoord, ppx.yCoord, ppx.zCoord, ppx2.xCoord, ppx2.yCoord, ppx2.zCoord, stringColor);
	}
	
	public static void renderLineFromToBlockCenter(double x1, double y1, double z1, double x2, double y2, double z2, int stringColor) {
		renderLineFromToBlock(x1+0.5D, y1+0.5D, z1+0.5D, x2+0.5D, y2+0.5D, z2+0.5D, 0D, 0D, 0D, 0F, 0F, stringColor);
	}
	
	public static void renderLineFromToBlock(double x1, double y1, double z1, double x2, double y2, double z2, int stringColor) {
		renderLineFromToBlock(x1, y1, z1, x2, y2, z2, 0D, 0D, 0D, 0F, 0F, stringColor);
	}
	
	public static void renderLineFromToBlock(double x1, double y1, double z1, double x2, double y2, double z2, double d, double d1, double d2, float f, float f1, int stringColor) {
	    Tessellator tessellator = Tessellator.instance;
	    RenderManager rm = RenderManager.instance;
	    
	    float castProgress = 1.0F;
	
	    float f10 = 0F;
	    double d4 = MathHelper.sin(f10);
	    double d6 = MathHelper.cos(f10);
	
	    double pirateX = x1;
	    double pirateY = y1;
	    double pirateZ = z1;
	    double entX = x2;
	    double entY = y2;
	    double entZ = z2;
	
	    double fishX = castProgress*(entX - pirateX);
	    double fishY = castProgress*(entY - pirateY);
	    double fishZ = castProgress*(entZ - pirateZ);
	    GL11.glDisable(3553);
	    GL11.glDisable(2896);
	    tessellator.startDrawing(3);
	    //int stringColor = 0x888888;
	    //if (((EntityNode)entitypirate).render) {
	    	//stringColor = 0x880000;
	    //} else {
	    	//stringColor = 0xEF4034;
		//}
	    tessellator.setColorOpaque_I(stringColor);
	    int steps = 16;
	
	    for (int i = 0; i <= steps; ++i) {
	        float f4 = i/(float)steps;
	        tessellator.addVertex(
	            pirateX - rm.renderPosX + fishX * f4,//(f4 * f4 + f4) * 0.5D + 0.25D,
	            pirateY - rm.renderPosY + fishY * f4,//(f4 * f4 + f4) * 0.5D + 0.25D,
	            pirateZ - rm.renderPosZ + fishZ * f4);
	    }
	    
	    tessellator.draw();
	    GL11.glEnable(2896);
	    GL11.glEnable(3553);
	}
	
}
