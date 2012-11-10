package zombiecraft.Core.Items;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import zombiecraft.Core.EnumAmmo;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Entities.EntityBullet;
import zombiecraft.Core.GameLogic.ZCGame;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumOptions;
import net.minecraft.src.Frustrum;
import net.minecraft.src.Item;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagByte;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

public class ItemGun extends Item
{
    //Main gun configurations
	public EnumAmmo ammoType;
	public int damage;
	public float spread;
	public float muzzleVelocity;	
	public int numBullets;
	public int magSize;
	public int useDelay;
	public int reloadTime;
	public String firingSound;
	public int hitCount;
	
	//unused
	public float soundRangeFactor;
	public float recoil;
	
	//internal counters
	public int reloadDelay = 0;
	public int reloadDelayClient = 0;
	public int fireDelay = 0;
	public int fireDelayClient = 0;
	
    public ItemGun(int var1)
    {
        super(var1);
        this.maxStackSize = 64;
        this.setMaxDamage(190);
        magSize = 16;
        ammoType = EnumAmmo.PISTOL;
        soundRangeFactor = 1F;
        reloadTime = 50;
        hitCount = 1;
        
        setFull3D();
    }

    public EntityBullet getBulletEntity(World var1, Entity var2, float var3, float var4, float var5, float var6, float var7)
    {
        return new EntityBullet(var1, var2, this, var3, var4, var5, var6, var7);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
    	if (var2.isRemote) {
    		if (reloadDelayClient != 0 || fireDelayClient != 0) {
    			return var1;
    		}
    	} else {
    		if (reloadDelay != 0 || fireDelay != 0) {
	    		
	    		//fixDelay();
	    		return var1;
	    	}
    	}
    	
        //var2.playSoundAtEntity(var3, "random.pop", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
    	int var10 = 0;

        var10 = ZCUtil.useItemInInventory(var3, this);
        
        if (var10 > 0 || var3.capabilities.isCreativeMode) {
        	
	        if (!var2.isRemote) {
	        	
	            //System.out.println("ammo: " + var10);
	        	for (int i = 0; i < this.numBullets; i++) {
		        	EntityBullet ent = getBulletEntity(var2, var3, 0, 0, 0, 0, 0);
		        	
		        	var2.spawnEntityInWorld(ent);
	        	}
	        	
		        //ent.setPosition(var3.posX, var3.posY + var3.getEyeHeight() - 0.3F, var3.posZ);
		        
		        /*float adjust = 110F;
		        float dist = 0.0F;
		        
		        ent.posX -= (double)(MathHelper.sin((ent.rotationYaw + adjust)  * (float)Math.PI / 180.0F) * dist);
	        	//ent.posY += 0.2D;
	            ent.posZ -= (double)(MathHelper.cos((ent.rotationYaw + adjust) * (float)Math.PI / 180.0F) * dist);*/
		        //ent.fixAim();
		        
	            //ent.setPosition(ent.posX, ent.posY, ent.posZ);
	            
	            /*ent.motionX *= 0.05F;
	            ent.motionY *= 0.05F;
	            ent.motionZ *= 0.05F;*/
	            
		        
	                
		        var2.playSoundAtEntity(var3, firingSound, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
		        
		        
		        
		        if (var10 == 2) {
		        	reloadDelay = reloadTime;
		        	var2.playSoundAtEntity(var3, "sdk.reload", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
		        	
		        }
	
	        } else {
	        	if (var10 == 2) {
	        		reloadDelayClient = reloadTime;
	        	}
	        	//if (var3 instanceof EntityPlayer) ((EntityPlayer)var3).swingItem();
	        	if (var3 instanceof EntityPlayer) ((EntityPlayer)var3).field_82173_br = 5;
	        }
	        
	        
        	
        } else {
        	if (!var2.isRemote) {
        		var2.playSoundAtEntity(var3, "sdk.gunempty", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
        		//var2.playSoundAtEntity(var3, "sdkzc.round_over", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
        	}
        }
        
        if (var2.isRemote) {
        	fireDelayClient = useDelay;
	        fixDelay();
        } else {
        	fireDelay = useDelay;
        }
        
        return var1;
    }
    
    @SideOnly(Side.CLIENT)
    public void fixDelay() {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	
    	ZCUtil.setPrivateValueBoth(Minecraft.class, mc, ZCUtil.field_obf_rightClickDelayTimer, ZCUtil.field_mcp_rightClickDelayTimer, useDelay);
    	ZCUtil.setPrivateValueBoth(ItemRenderer.class, mc.entityRenderer.itemRenderer, ZCUtil.field_obf_equippedProgress, ZCUtil.field_mcp_equippedProgress, 0.95F);
    	
        //rightClickDelayTimer
    }
    
    @Override
    public void onUsingItemTick(ItemStack stack, EntityPlayer player, int count) 
    {
    	
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) 
    {
        return true;
    }
    
    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    	
    	
    	
    	if (!par2World.isRemote) {
    		if (reloadDelay > 0) reloadDelay--;
    		if (fireDelay > 0) fireDelay--;
    	} else {
    		if (reloadDelayClient > 0) reloadDelayClient--;
    		if (fireDelayClient > 0) fireDelayClient--;
    		
    		if (fireDelay == 0) {
	    		//if (par3Entity instanceof EntityPlayer) ((EntityPlayer)par3Entity).swingItem();
	    		//if (par3Entity instanceof EntityPlayer) ((EntityPlayer)par3Entity).swingProgressInt = 4;
    		} else {
    			//if (par3Entity instanceof EntityPlayer) ((EntityPlayer)par3Entity).swingItem();
	    		//if (par3Entity instanceof EntityPlayer) ((EntityPlayer)par3Entity).swingProgressInt = 5;
    		}
    		
    		if (reloadDelayClient > 0) {
    			Minecraft mc = FMLClientHandler.instance().getClient();
    			ZCUtil.setPrivateValueBoth(ItemRenderer.class, mc.entityRenderer.itemRenderer, ZCUtil.field_obf_equippedProgress, ZCUtil.field_mcp_equippedProgress, 0.4F);
    		} else {
    			
    		}
    	}
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        return false;
    }
}
