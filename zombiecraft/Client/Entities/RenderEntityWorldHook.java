package zombiecraft.Client.Entities;



import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderEntityWorldHook extends Render {

   public static final boolean renderLine = true;
   public static final boolean renderBobber = false;
   public static float yoffset = 0.4F;
   public static float caughtOffset = 0.8F;
   public static int stringColor = 8947848;


   public void doRenderNode(Entity var1, double var2, double var4, double var6, float var8, float var9) {
	   //if (ZCGame.instance().mapMan.editMode) ZCGame.instance().renderInWorldOverlay();
   }

   public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      this.shadowSize = 1.0F;
      GL11.glPushMatrix();
      this.doRenderNode(var1, var2, var4, var6, var8, var9);
      this.shadowSize = 0.0F;
      GL11.glPopMatrix();
   }

   public void renderClouds(float var1, Entity var2) {
      
   }

	@Override
	protected ResourceLocation func_110775_a(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
