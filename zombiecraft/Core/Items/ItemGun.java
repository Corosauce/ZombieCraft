package zombiecraft.Core.Items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import zombiecraft.Core.EnumAmmo;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Entities.BaseEntAI;
import zombiecraft.Core.Entities.Projectiles.EntityBullet;
import zombiecraft.Forge.ZCClientTicks;
import zombiecraft.Forge.ZombieCraftMod;
import CoroAI.c_CoroAIUtil;
import CoroAI.componentAI.ICoroAI;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	//public int reloadDelay = 0;
	public int reloadDelayClient = 0;
	//public int fireDelay = 0;
	public int fireDelayClient = 0;
	//public int clipAmount = -1;
	public int clipAmountClient = -1;
	
	public static int tempReturnVal;
	
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
        
        
        
        //clipAmount = clipAmountClient = magSize;
        
        setFull3D();
    }
    
    public Item setUnlocalizedNameAndTexture(String nameTex) {
    	this.setUnlocalizedName(nameTex);
    	this.setTextureName(nameTex);
    	return this;
    }
    
    public void checkNBTStack(ItemStack stack) {
    	if (stack.stackTagCompound == null) {
    		stack.stackTagCompound = new NBTTagCompound();
    	}
    }

    public EntityBullet getBulletEntity(World var1, Entity var2, float var3, float var4, float var5, float var6, float var7)
    {
        return new EntityBullet(var1, var2, this, var3, var4, var5, var6, var7);
    }
    
    public int getFireDelay(ItemStack stack, World var2, EntityPlayer var3) {
    	if (var3.username.contains("fakePlayer")) {
    		BaseEntAI entAI = (BaseEntAI)c_CoroAIUtil.playerToCompAILookup.get(var3.username);
    		
    		if (entAI != null) {
    			return entAI.curCooldown_FireGun;
    		} else {
    			System.out.println("fail gun code, isRemote: " + var2.isRemote);
    			return 0;
    		}
    	} else {
	    	if (var2.isRemote) {
	    		return fireDelayClient;
	    	} else {
	    		//new code, use nbt from itemstack for server only
	    		return stack.stackTagCompound.getInteger("fireDelay");
	    	}
    	}
    }
    
	public int getReloadDelay(ItemStack stack, World var2, EntityPlayer var3) {
		if (var3.username.contains("fakePlayer")) {
			BaseEntAI entAI = (BaseEntAI)c_CoroAIUtil.playerToCompAILookup.get(var3.username);
    		
    		if (entAI != null) {
    			return entAI.curCooldown_Reload;
    		} else {
    			System.out.println("fail gun code, isRemote: " + var2.isRemote);
    			return 0;
    		}
    	} else {
	    	if (var2.isRemote) {
	    		return reloadDelayClient;
	    	} else {
	    		//new code, use nbt from itemstack for server only
	    		return stack.stackTagCompound.getInteger("reloadDelay");
	    	}
    	}
	}
	
	public int getClipAmount(ItemStack stack, World var2, EntityPlayer var3) {
		if (var3.username.contains("fakePlayer")) {
			BaseEntAI entAI = (BaseEntAI)c_CoroAIUtil.playerToCompAILookup.get(var3.username);
    		
    		if (entAI != null) {
    			return entAI.curClipAmount;
    		} else {
    			System.out.println("fail gun code, isRemote: " + var2.isRemote);
    			return 0;
    		}
    	} else {
	    	if (var2.isRemote) {
	    		return clipAmountClient;
	    	} else {
	    		//new code, use nbt from itemstack for server only
	    		//specific to clip, if !hasKey, do init aka set to -1
	    		if (stack.stackTagCompound.hasKey("clipAmount")) {
	    			return stack.stackTagCompound.getInteger("clipAmount");
	    		} else return -1;
	    	}
    	}
	}
	
	public void setFireDelay(ItemStack stack, World var2, EntityPlayer var3, int val) {
    	if (var3.username.contains("fakePlayer")) {
    		BaseEntAI entAI = (BaseEntAI)c_CoroAIUtil.playerToCompAILookup.get(var3.username);
    		
    		if (entAI != null) {
    			entAI.curCooldown_FireGun = val;
    		} else {
    			System.out.println("fail gun code, isRemote: " + var2.isRemote);
    			return;
    		}
    	} else {
	    	if (var2.isRemote) {
	    		fireDelayClient = val;
	    	} else {
	    		//new code, use nbt from itemstack for server only
	    		stack.stackTagCompound.setInteger("fireDelay", val);
	    	}
    	}
    }
    
	public void setReloadDelay(ItemStack stack, World var2, EntityPlayer var3, int val) {
		if (var3.username.contains("fakePlayer")) {
			BaseEntAI entAI = (BaseEntAI)c_CoroAIUtil.playerToCompAILookup.get(var3.username);
    		
    		if (entAI != null) {
    			entAI.curCooldown_Reload = val;
    		} else {
    			System.out.println("fail gun code, isRemote: " + var2.isRemote);
    			return;
    		}
    	} else {
	    	if (var2.isRemote) {
	    		reloadDelayClient = val;
	    	} else {
	    		//new code, use nbt from itemstack for server only
	    		stack.stackTagCompound.setInteger("reloadDelay", val);
	    	}
    	}
	}
	
	public void setClipAmount(ItemStack stack, World var2, EntityPlayer var3, int val) {
		if (var3.username.contains("fakePlayer")) {
			BaseEntAI entAI = (BaseEntAI)c_CoroAIUtil.playerToCompAILookup.get(var3.username);
    		
    		if (entAI != null) {
    			entAI.curClipAmount = val;
    		} else {
    			System.out.println("fail gun code, isRemote: " + var2.isRemote);
    			return;
    		}
    	} else {
	    	if (var2.isRemote) {
	    		clipAmountClient = val;
	    	} else {
	    		//new code, use nbt from itemstack for server only
	    		stack.stackTagCompound.setInteger("clipAmount", val);
	    	}
    	}
	}
    
    public int tryItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
    	
    	
    	
    	checkNBTStack(var1);
    	
    	//init fix - doesnt work when buying
    	if (this.getClipAmount(var1, var2, var3) == -1) this.setClipAmount(var1, var2, var3, magSize);
    	
    	//sync clip count server to client if it goes out of sync 
    	if (var2.isRemote/* && false*/) {
    		int serverClipCount = -1;
    		if (var1.stackTagCompound.hasKey("clipAmount")) {
    			serverClipCount = var1.stackTagCompound.getInteger("clipAmount");
    		}
    		if (serverClipCount != -1 && Math.abs(this.getClipAmount(var1, var2, var3) - serverClipCount) > 1) {
    			System.out.println("clipamount desync > 1, fixing: " + this.getClipAmount(var1, var2, var3) + " - " + serverClipCount);
    			this.setClipAmount(var1, var2, var3, serverClipCount);
    		}
    		int serverReloadDelay = var1.stackTagCompound.getInteger("reloadDelay");
    		if (Math.abs(this.getReloadDelay(var1, var2, var3) - serverReloadDelay) > 1) {
    			System.out.println("reloadDelay desync > 1, fixing");
    			this.setReloadDelay(var1, var2, var3, serverReloadDelay);
    		}
    	}
    	
    	int curFireDelay = this.getFireDelay(var1, var2, var3);
    	int curReloadDelay = this.getReloadDelay(var1, var2, var3);
    	int curClipAmount = this.getClipAmount(var1, var2, var3);
    	
    	//new anti mutli reload methods
    	/*boolean needsReload = var1.stackTagCompound.getBoolean("needsReload");
    	
    	System.out.println("what: " + needsReload + " - " + curReloadDelay);
    	
    	//enforce a reload before they can use it
    	if (needsReload && curReloadDelay <= 0) {
    		System.out.println("triggering a forced reload for " + this);
    		curReloadDelay = reloadTime;
    	}*/
    	
    	//MAIN GUN LOGIC START \\
    	if (curFireDelay > 0 || curReloadDelay > 0) return -3;
    	
    	int useBulletResult = ZCUtil.useItemInInventory(var3, var3.username, this);
    	
    	if (var3.capabilities.isCreativeMode/* || (var3.username == "fakePlayer")*/) {
    		if (curClipAmount == 0) {
    			curClipAmount = magSize;
    		}
    		useBulletResult = 1;
        }
    	
    	//If fired
    	if (useBulletResult > 0) {
    		
    		curClipAmount--;
	        
    		//If needs reload
	        if (curClipAmount <= 0) {
	        	curReloadDelay = reloadTime;
	        	curClipAmount = this.magSize;
	        	int ammoCount = ZCUtil.getAmmoData(var3.username, ammoType.ordinal());
	        	if (ammoCount == 0) {
	        		curClipAmount = -1;
	        	} else {
		        	if (ammoCount < this.magSize) curClipAmount = ammoCount;
		        	if (!var2.isRemote && ammoCount > 0) var2.playSoundAtEntity(var3, ZombieCraftMod.modID + ":" + "zc.gun.reload", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
	        	}
	        }
    		
	        if (!var2.isRemote) {
	        	
	            //System.out.println("ammo: " + var10);
	        	fireGun(var2, var3);
	        	var2.playSoundToNearExcept(var3, ZombieCraftMod.modID + ":" + firingSound, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
	        	//var2.playSoundToNearExcept(var3, firingSound, (double)var3.posX, (double)var3.posY, (double)var3.posZ, 1F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
	        } else {
	        	var2.playSound(var3.posX, var3.posY, var3.posZ, ZombieCraftMod.modID + ":" + firingSound, 1F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F), false);
	        	recoil();
	        	if (var3 instanceof EntityPlayer) ((EntityPlayer)var3).swingProgressInt = 5;
	        }
	        
    	} else {
    		if (!var2.isRemote) {
        		var2.playSoundAtEntity(var3, ZombieCraftMod.modID + ":" + "zc.gun.gunempty", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
    		}
    		curClipAmount = -1;
    	}
    	curFireDelay = useDelay;
    	if (var2.isRemote) fixDelay();
    	//MAIN GUN LOGIC END //
    	
    	//INSTANCE HANDLING
    	
    	this.setFireDelay(var1, var2, var3, curFireDelay);
    	this.setReloadDelay(var1, var2, var3, curReloadDelay);
    	this.setClipAmount(var1, var2, var3, curClipAmount);
    	
    	return useBulletResult;
    }
    
    public void fireGun(World world, EntityPlayer entP) {
    	for (int i = 0; i < this.numBullets; i++) {
        	EntityBullet ent = getBulletEntity(world, entP, 0, 0, 0, 0, 0);
        	
        	world.spawnEntityInWorld(ent);
    	}
    }
    
    public long lastTime = 0;
    
    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    	
    	EntityPlayer var3 = (EntityPlayer)par3Entity;
    	
    	checkNBTStack(par1ItemStack);
    	
    	//INSTANCE HANDLING
    	if (var3 == null || var3.username.contains("fakePlayer")) {
    		//cooldown handled properly in c_PlayerProxy
    	} else {
	    	if (par2World.isRemote) {
	    		//prevents multiples of the same item causing speed increases, afaik this doesnt happen on server side
	    		if (lastTime != par2World.getWorldTime()) {
	    			lastTime = par2World.getWorldTime();
	    			if (fireDelayClient > 0) fireDelayClient--;
		    		if (reloadDelayClient > 0) reloadDelayClient--;
	    		}
	    		
	    	} else {
	    		int tempFireDelay = par1ItemStack.stackTagCompound.getInteger("fireDelay");
	    		if (tempFireDelay > 0) par1ItemStack.stackTagCompound.setInteger("fireDelay", --tempFireDelay);
	    		int tempReloadDelay = par1ItemStack.stackTagCompound.getInteger("reloadDelay");
	    		if (tempReloadDelay > 0) {
	    			//if held, tick reload, else reset it since its an incomplete reload cycle for non held weapon
	    			if (par5) {
	    				par1ItemStack.stackTagCompound.setInteger("reloadDelay", --tempReloadDelay);
	    			} else {
	    				par1ItemStack.stackTagCompound.setInteger("reloadDelay", reloadTime);
	    			}
	    			//mark reload cycle completed
	    			/*if (tempReloadDelay == 0) {
	    				System.out.println("mark reload complete for " + this);
	    				par1ItemStack.stackTagCompound.setBoolean("needsReload", false);
	    			} else {
	    				
	    			}*/
	    		}
	    		/*if (fireDelay > 0) fireDelay--;
	    		if (reloadDelay > 0) reloadDelay--;*/
	    	}
    	}
    	
    	if (!par2World.isRemote) {
    		//this wasnt the best design, better would be to just check above: if active weapon then tick reload, else, reset it to max if wasnt at 0
    		//reload resetting for weapon switches
    		/*int lastSlotUsed = var3.getEntityData().getInteger("lastSlotUsed");
    		if (lastSlotUsed != var3.inventory.currentItem) {
    			ItemStack prevItem = var3.inventory.getStackInSlot(lastSlotUsed);
    			if (prevItem != null && prevItem.stackTagCompound != null) {
    				int prevGunReloadTime = prevItem.stackTagCompound.getInteger("reloadDelay");
    				if (prevGunReloadTime > 0) {
    					System.out.println("mark reload needed for " + this);
    					prevItem.stackTagCompound.setBoolean("needsReload", true);
    				}
    			}
    		}
    		var3.getEntityData().setInteger("lastSlotUsed", var3.inventory.currentItem);*/
    	} else {
    		
    		Minecraft mc = FMLClientHandler.instance().getClient();
    		
    		//mc.thePlayer.renderArmPitch -= 90F;
    		//mc.thePlayer.prevRenderArmPitch = 300F;
    		
    		
    		if (this instanceof ItemGunSniper && ZCClientTicks.iMan != null) {
    			if (par5) {
    				setZoom(1F + (ZCClientTicks.iMan.zoomState * 2F));
	    		} else {
	    			setZoom(1F);
	    			ZCClientTicks.iMan.zoomState = 0;
	    		}
    		}
    		
    		if (reloadDelayClient > 0) {
    			
    			ZCUtil.setPrivateValueBoth(ItemRenderer.class, mc.entityRenderer.itemRenderer, ZCUtil.field_obf_equippedProgress, ZCUtil.field_mcp_equippedProgress, 0.1F);
    		} else {
    			
    		}
    	}
    	
    	
    }
    
    @SideOnly(Side.CLIENT)
    public void setZoom(float val) {
    	ZCUtil.setPrivateValueBoth(EntityRenderer.class, FMLClientHandler.instance().getClient().entityRenderer, ZCUtil.field_obf_cameraZoom, ZCUtil.field_mcp_cameraZoom, val);
    }

    public int tryItemRightClick2(ItemStack var1, World var2, EntityPlayer var3) {
    	int nFireDelay = 0;
    	//if (!var2.isRemote) {
	    	if (var1.stackTagCompound.hasKey("fireDelay")) {
	    		nFireDelay = var1.stackTagCompound.getInteger("fireDelay");
	    		
	    		//i think server is overwriting the client side nbt 1-2 ticks after it changes on server, this is a click spam fix for fast firing weapons
	    		if (useDelay > 4 && nFireDelay > 0/* && (var2.isRemote && nFireDelay > 1)*/) {
	    			//System.out.println("nFireDelay:" + nFireDelay);
	    			return -3;
	    		}
	    	}
    	/*} else {
    		if (fireDelayClient > 0) {
    			return -2;
    		}
    	}*/
    	
    	int nReloadDelay = 0;
    	if (var1.stackTagCompound.hasKey("reloadDelay")) {
    		nReloadDelay = var1.stackTagCompound.getInteger("reloadDelay");
    		
    		if (nReloadDelay > 0) {
    			return -3;
    		}
    	}
    	if (!var2.isRemote) {
    		//System.out.println("server");
    	} else {
    		//System.out.println("client");
    	}
    	/*if (var2.isRemote) {
    		if (reloadDelayClient != 0 || fireDelayClient != 0) {
    			return var1;
    		}
    	} else {
    		if (reloadDelay != 0 || fireDelay != 0) {
	    		
	    		//fixDelay();
	    		return var1;
	    	}
    	}*/
    	
        //var2.playSoundAtEntity(var3, "random.pop", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
    	int var10 = 0;

        var10 = ZCUtil.useItemInInventory(var3, var3.username, this);
        
        //System.out.println("var3.username: " + var3.username);
        
        if (var3.capabilities.isCreativeMode/* || (var3.username == "fakePlayer")*/) {
        	var10 = 1;
        }
        
        if (var10 > 0) {
        	
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
	            
		        
	                
		        var2.playSoundAtEntity(var3, ZombieCraftMod.modID + ":" + firingSound, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
		        
		        
		        
		        if (var10 == 2) {
		        	//reloadDelay = reloadTime;
		        	var1.stackTagCompound.setInteger("reloadDelay", reloadTime);
		        	var2.playSoundAtEntity(var3, ZombieCraftMod.modID + ":" + "zc.gun.reload", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
		        	
		        }
	
	        } else {
	        	if (var10 == 2) {
	        		//reloadDelayClient = reloadTime;
		        	var1.stackTagCompound.setInteger("reloadDelay", reloadTime);
	        	}
	        	//if (var3 instanceof EntityPlayer) ((EntityPlayer)var3).swingItem();
	        	if (var3 instanceof EntityPlayer) ((EntityPlayer)var3).swingProgressInt = 5;
	        }
	        
	        
        	
        } else {
        	if (!var2.isRemote) {
        		var2.playSoundAtEntity(var3, ZombieCraftMod.modID + ":" + "zc.gun.gunempty", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
        		//var2.playSoundAtEntity(var3, "sdkzc.round_over", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
        	}
        }
        
        if (var2.isRemote) {
        	fireDelayClient = useDelay;
        	//var1.stackTagCompound.setInteger("fireDelay", useDelay);
	        fixDelay();
	        //recoil();
        } else {
        	//fireDelay = useDelay;
        	var1.stackTagCompound.setInteger("fireDelay", useDelay);
        	var1.stackTagCompound.setInteger("recoil", 1);
        }
    	return var10;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
    	tryItemRightClick(var1, var2, var3);
        
        return var1;
    }
    
    @SideOnly(Side.CLIENT)
    public void fixDelay() {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	
    	ZCUtil.setPrivateValueBoth(Minecraft.class, mc, ZCUtil.field_obf_rightClickDelayTimer, ZCUtil.field_mcp_rightClickDelayTimer, useDelay);
    	ZCUtil.setPrivateValueBoth(ItemRenderer.class, mc.entityRenderer.itemRenderer, ZCUtil.field_obf_equippedProgress, ZCUtil.field_mcp_equippedProgress, 0.85F);
    	
        //rightClickDelayTimer
    }
    
    @SideOnly(Side.CLIENT)
    public void recoil() {
    	ZCClientTicks.camMan.recoil(recoil * 1);
    	Minecraft mc = FMLClientHandler.instance().getClient();
		
		mc.thePlayer.renderArmPitch -= recoil * 40F;
		mc.thePlayer.renderArmYaw += recoil * 5F;
    }
    
    public void reload() {
    	
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
    
    //@Override
    public void onUpdate2(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    	
    	int nReloadDelay = 0;
    	
    	if (par1ItemStack.stackTagCompound == null) {
    		par1ItemStack.stackTagCompound = new NBTTagCompound();
    	}
    	
    	
    	
    	if (!par2World.isRemote) {
    		
    		
    		if (par1ItemStack.stackTagCompound.hasKey("fireDelay")) {
        		int nFireDelay = par1ItemStack.stackTagCompound.getInteger("fireDelay");
        		if (nFireDelay > 0) {
        			/*if (!par2World.isRemote) */par1ItemStack.stackTagCompound.setInteger("fireDelay", --nFireDelay);
        			/*if (!par2World.isRemote) {
        				System.out.println("server dec nFireDelay:" + nFireDelay);
        			} else {
        				System.out.println("client dec nFireDelay:" + nFireDelay);
        			}*/
        		}
        	}
        	if (par1ItemStack.stackTagCompound.hasKey("reloadDelay")) {
        		nReloadDelay = par1ItemStack.stackTagCompound.getInteger("reloadDelay");
        		if (nReloadDelay > 0) {
        			par1ItemStack.stackTagCompound.setInteger("reloadDelay", --nReloadDelay);
        		}
        	}
    		
    		//if (reloadDelay > 0) reloadDelay--;
    		//if (fireDelay > 0) fireDelay--;
    	} else {
    		
    		int delay = par1ItemStack.stackTagCompound.getInteger("fireDelay");
    		
    		if (delay > 0 && delay == useDelay - 1) {
    			//par1ItemStack.stackTagCompound.setInteger("recoil", 0);
    			//System.out.println(par1ItemStack.stackTagCompound.getInteger("fireDelay"));
    			recoil();
    		}
    		
    		//var1.stackTagCompound.setInteger("recoil", 1);
    		//if (reloadDelayClient > 0) reloadDelayClient--;
    		if (fireDelayClient > 0) fireDelayClient--;
    		
    		nReloadDelay = par1ItemStack.stackTagCompound.getInteger("reloadDelay");
    		
    		if (nReloadDelay > 0) {
    			Minecraft mc = FMLClientHandler.instance().getClient();
    			ZCUtil.setPrivateValueBoth(ItemRenderer.class, mc.entityRenderer.itemRenderer, ZCUtil.field_obf_equippedProgress, ZCUtil.field_mcp_equippedProgress, 0.4F);
    		} else {
    			
    		}
    	}
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
    	player.worldObj.playSoundAtEntity(player, ZombieCraftMod.modID + ":" + "zc.meleehit", 1.0F, 1.0F);
    	return false;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        return false;
    }
}
