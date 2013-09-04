package zombiecraft.Core.Entities;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import zombiecraft.Core.EnumDifficulty;
import zombiecraft.Core.ZCBlocks;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Blocks.BlockBarrier;
import zombiecraft.Core.Blocks.BlockTower;
import zombiecraft.Core.Blocks.BlockWallPlaceable;
import zombiecraft.Core.Entities.Projectiles.EntityBullet;
import zombiecraft.Core.GameLogic.WaveManager;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.Items.ItemGun;
import zombiecraft.Forge.ZCServerTicks;
import CoroAI.c_CoroAIUtil;
import CoroAI.c_IEnhAI;
import CoroAI.entity.EnumDiploType;
import CoroAI.entity.JobBase;
import CoroAI.entity.JobProtect;
import CoroAI.entity.c_EnhAI;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseEntAI extends c_EnhAI
{
	
	//public int team;
	
	public boolean initWaveSettings = true;

	public int curBlockDamage;

	public boolean isBreaking;
	
	public boolean wasHeadshot;
	
	public BaseEntAI(World par1World, double x, double y, double z) {
		this(par1World);
		this.setPosition(x, y, z);
	}
	
    public BaseEntAI(World par1World)
    {
        super(par1World);
        
        //team = 0;
        maxReach_Melee = 2.0F;
        
        //no reload time to let guns handle it?
        cooldown_Ranged = 3;
        
        //System.out.println("REMOVING SERVERMODE CHECK BaseEntAI constructor");
        if (ZCServerTicks.zcGame.serverMode) {
        	this.serverMode = true;
        	
        	maxReach_Melee = 1.3F;
        }
        
        //System.out.println("maxReach_Melee: " + maxReach_Melee);
        
        entityCollisionReduction = 0.9F;
        
        this.setExp(1);
        //mod_ZombieCraft.zcGame.addTasks(tasks, targetTasks, this);
        
    }
    
    @Override
    public void entityInit() {
    	super.entityInit();
    	this.dataWatcher.addObject(24, Integer.valueOf(0));
    }
    
    public void dropItems() {
    	
    }
    
    @Override
	public boolean attackEntityFrom(DamageSource damagesource, int i) {
    	
    	if (dipl_team == EnumDiploType.COMRADE) {
	    	if (damagesource.getEntity() instanceof EntityPlayer && fakePlayer != null) {
	    		EntityPlayer entP = (EntityPlayer)damagesource.getEntity();
	    		ItemStack is = entP.getCurrentEquippedItem();
	    		if (!entP.username.contains("fakePlayer") && is != null && is.getItem() instanceof ItemGun) {
	    			ItemGun ig = (ItemGun)is.getItem();
	    			
	    			this.inventory.currentItem = slot_Ranged;
	    			this.inventory.setInventorySlotContents(slot_Ranged, is.copy());
	    			
	    			
	    			int ammoID = ig.ammoType.ordinal();
	    			int giveAmount = ZCUtil.getAmmoData(entP.username, ammoID);
					
	    			//give all your ammo of that gun to him
					ZCUtil.setAmmoData(fakePlayer.username, ammoID, giveAmount + ZCUtil.getAmmoData(fakePlayer.username, ammoID));
					ZCUtil.setAmmoData(entP.username, ammoID, 0);
					entP.inventory.setInventorySlotContents(entP.inventory.currentItem, null);

					syncClientItems();
					
					return false;
	    		}
	    	}
    	}
    	
    	return super.attackEntityFrom(damagesource, i);
    }
    
    @Override
    public boolean customRightClick(World world, ItemStack stack, EntityPlayer player) {
    	ItemStack is = this.getCurrentEquippedItem();
    	if (is != null && is.getItem() instanceof ItemGun) {
    		int returnVal = ((ItemGun)is.getItem()).tryItemRightClick(stack, world, player);
    		
    		//System.out.println("firing state: " + returnVal);
    		
    		if (returnVal > 0) {
    			if (returnVal == 1) {
    				//normal fire
    				swingItem();
    			} else if (returnVal == 2) {
    				//reload
    			}
    		}
    	}
    	return true;
    }
    
    public float aimVal = 0.8F;
    public float recoilVal = 0.9F;
    public float reloadVal = 0.5F;
    
    public int holsterDelay = 0;
    
    @Override
    public void updateSwing() {
    	
    	if (holsterDelay > 0) holsterDelay--;
    	
    	ItemStack is = this.getCurrentEquippedItem();
    	if (is != null && is.getItem() instanceof ItemGun) {
    		if(swingArm) {
	            swingTick++;
	
	            
	            if(swingTick >= 5) {
	                swingTick = 0;
	                swingArm = false;
	            } else {
	            	holsterDelay = 60;
	            }
	            
	            //swingProgress = recoilVal;
	            
	        } else {
	            swingTick = 0;
	        }
    		
    		//this depended on item stack synced nbt, now broken reload animation
    		//if (is.stackTagCompound.hasKey("reloadDelay")) {
        		int nReloadDelay = this.curCooldown_Reload;//is.stackTagCompound.getInteger("reloadDelay");
        		
        		if (nReloadDelay > 0) {
        			if (holsterDelay > 10) holsterDelay = 10;
        		}
    		//}
	
    		if (holsterDelay > 10) {
    			swingProgress = 0.36F - (swingTick * 0.04F);
    		} else {
    			swingProgress = holsterDelay * 0.01F;
    		}
    		prevSwingProgress = swingProgress;
    	} else {
	    	if(swingArm) {
	            swingTick++;
	
	            
	            if(swingTick == 8) {
	                swingTick = 0;
	                swingArm = false;
	            }
	        } else {
	            swingTick = 0;
	        }
	
	        swingProgress = (float)swingTick / 8F;
    	}
    }
    
    @Override
    public boolean isBreaking() {
		return isBreaking;
	}
    
    @Override
    public void setPrjOwner(int offset) {
    	super.setPrjOwner(offset);
    	
    	Entity ent = (Entity)worldObj.loadedEntityList.get(worldObj.loadedEntityList.size()-offset);
    	if (ent instanceof EntityBullet) {
    		((EntityBullet) ent).owner = this;
    	}
    }
    
    public void leftClickItem(Entity var1) {
    	ZCGame.instance().pvpFixPre();
    	super.leftClickItem(var1);
    	ZCGame.instance().pvpFixPost();
    }
    
    protected void updateAITasks()
    {
        ++this.entityAge;
        //Profiler.startSection("checkDespawn");
        this.despawnEntity();
        //Profiler.endSection();
        //Profiler.startSection("sensing");
        //this.func_48090_aM().func_48481_a();
        //Profiler.endSection();
        //Profiler.startSection("targetSelector");
        //this.targetTasks.onUpdateTasks();
        //Profiler.endSection();
        //Profiler.startSection("goalSelector");
        //this.tasks.onUpdateTasks();
        //Profiler.endSection();
        //Profiler.startSection("navigation");
        
        //entityCollisionReduction = 0.6F;
        
        //these 2 methods must stick together in this order
        checkPathfindLock();
        this.getNavigator().onUpdateNavigation();
        
        updateAI();
        
        //float oldSpeed = getAIMoveSpeed();
        
        
        if (!this.getNavigator().noPath()) {
        	
        	//something to help aiming while pathfinding
        	try {
	            double pposX = Double.valueOf(c_CoroAIUtil.getPrivateValueSRGMCP(EntityMoveHelper.class, this.getMoveHelper(), "field_75646_b", "posX").toString());
	            double pposY = Double.valueOf(c_CoroAIUtil.getPrivateValueSRGMCP(EntityMoveHelper.class, this.getMoveHelper(), "field_75647_c", "posY").toString());
	            double pposZ = Double.valueOf(c_CoroAIUtil.getPrivateValueSRGMCP(EntityMoveHelper.class, this.getMoveHelper(), "field_75644_d", "posZ").toString());
	            
	            for (int i = 0; i < 6; i++) {
	        		this.getMoveHelper().onUpdateMoveHelper();
	    	        this.getMoveHelper().setMoveTo(pposX, pposY, pposZ, getMoveHelper().getSpeed());
	        	}
        	} catch (Exception ex) {
        		System.out.println("REFLECTION FAIL");
        		ex.printStackTrace();
        	}
        	
        	
        }
        
        this.getMoveHelper().onUpdateMoveHelper();
        this.getLookHelper().onUpdateLook();
        this.getJumpHelper().doJump();
        
        
        
        //Profiler.endSection();
        //Profiler.startSection("mob tick");
        
        //this.func_48097_s_();
        //Profiler.endSection();
        //Profiler.startSection("controls");
        
        //Profiler.endSection();
        isBreaking = false;
    }
    
    @Override
    public void applyEntityCollision(Entity par1Entity)
    {
    	float randFactor = rand.nextFloat();
    	
        if (par1Entity.riddenByEntity != this && par1Entity.ridingEntity != this)
        {
            double var2 = par1Entity.posX - this.posX;
            double var4 = par1Entity.posZ - this.posZ;
            double var6 = MathHelper.abs_max(var2, var4);

            if (var6 >= 0.01D)
            {
                var6 = (double)MathHelper.sqrt_double(var6);
                var2 /= var6;
                var4 /= var6;
                double var8 = 1.0D / var6;

                if (var8 > 1.0D)
                {
                    var8 = 1.0D;
                }

                var2 *= var8;
                var4 *= var8;
                var2 *= 0.05D;
                var4 *= 0.05D;
                
                var2 *= randFactor;
                var4 *= randFactor;
                
                var2 *= (double)(1.0F - this.entityCollisionReduction);
                var4 *= (double)(1.0F - this.entityCollisionReduction);
                if (this.rand.nextInt(3) == 0) {
                	this.addVelocity(-var2, 0.0D, -var4);
                }
                par1Entity.addVelocity(var2, 0.0D, var4);
            }
        }
    }
    
    @Override
    public void updateAI() {
    	super.updateAI();
    }
    
    public float getMoveSpeed() {
    	return (float) this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e();
    }
    
    @Override
    public boolean isValidLightLevel() {
    	return true;
    }
    
    @Override
    public boolean getCanSpawnHere()
    {
        return /*this.isValidLightLevel() && */super.getCanSpawnHere();
    }
    
    @Override
    public float getBlockPathWeight(int par1, int par2, int par3)
    {
        return 1.0F;//0.5F - this.worldObj.getLightBrightness(par1, par2, par3);
    }
    
    @Override
    public boolean canDespawn() {
    	return false;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void particleHeadShot() {
    	for (int i = 0; i < 10; i++) {
			Random rand = new Random();
        	//par1Entity.worldObj.spawnParticle("reddust", par1Entity.posX, par1Entity.posY + 1.68F, par1Entity.posZ, 0, 0, 0/*rand.nextFloat() - 0.5F, rand.nextFloat() - 0.5F, rand.nextFloat() - 0.5F*/);
        	
        	EntityFX var21 = new EntityReddustFX(worldObj, posX, posY + 1.68F, posZ, 0, 0, 0) { public void onUpdate() { super.onUpdate(); this.motionY -= 0.05F; }; };
        	
        	float range = 0.3F;
        	
        	var21.motionX += ((rand.nextFloat() * range) - (0.5F * range));
        	var21.motionY += ((rand.nextFloat() * range) - (0.5F * range));
        	var21.motionZ += ((rand.nextFloat() * range) - (0.5F * range));
        	
        		
        	ZCUtil.setPrivateValueBoth(EntityFX.class, var21, ZCUtil.field_obf_particleGravity, ZCUtil.field_mcp_particleGravity, 2F);
        	FMLClientHandler.instance().getClient().effectRenderer.addEffect((EntityFX)var21);
		}
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
    
    	//entityCollisionReduction = 0.9F;
    	
    	if (worldObj.isRemote && getDataWatcher().getWatchableObjectInt(24) == 1) {
    		if (!wasHeadshot) {
    			particleHeadShot();
    		}
    		wasHeadshot = true;
    	} else {
    		wasHeadshot = false;
    	}
    	
    	//cooldown_Ranged = 3;

    	if (this.initWaveSettings && !(this instanceof BaseEntAI_Ally)) {
    		if (ZCServerTicks.zcGame.wMan != null && ZCServerTicks.zcGame.wMan.wave_Stage > 0) {
    			initWaveSettings = false;
	    		BaseEntAI ent = this;
	            WaveManager wMan = ZCServerTicks.zcGame.wMan;
	            
	            //ent.setHealth(15);
	            
	            //Wave based difficulty adjustments
	    		//ent.setExp((int)(ent.getExp() + (ent.getExp() * (wMan.amp_Exp * wMan.wave_Stage))));
	    		ent.setHealth((int)(ent.func_110143_aJ() + (ent.func_110143_aJ() * (wMan.amp_Health * wMan.wave_Stage))));
	    		ent.lungeFactor += ent.lungeFactor * (wMan.amp_Lunge * wMan.wave_Stage);
	    		
	    		setMoveSpeed(0.23F);
	    		
	    		//random variation
	    		ent.setMoveSpeed(this.getMoveSpeed() + (0.1F * worldObj.rand.nextFloat()));
	    		
	    		if (!worldObj.isRemote) {
	    			//System.out.println("speed was: " + this.getMoveSpeed());
	    		}
	    		
	    		ent.setMoveSpeed(this.getMoveSpeed() + (0.002F * wMan.wave_Stage));
	    		if (ent.getMoveSpeed() > wMan.amp_Speed_Max) ent.setMoveSpeed(wMan.amp_Speed_Max);
	    		if (ent.lungeFactor > wMan.amp_Lunge_Max) ent.lungeFactor = wMan.amp_Lunge_Max;
	    		
	    		//System.out.println("ent.lungeFactor " + ent.lungeFactor);
	    		if (true) {
	    			//System.out.println("ent.getHealth() " + ent.getHealth());
	    			//System.out.println("ent.lungeFactor " + ent.lungeFactor);
	    			//System.out.println("ent.moveSpeed " + ent.getMoveSpeed());
	    		}
	    		//System.out.println("ent.getHealth() " + ent.getHealth());
    		}
    	}
    	
    	
    	
        super.onLivingUpdate();
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "";
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return 0;
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected void func_48085_j_(int par1)
    {
        
    }
    
    @Override
	public void setDead() {
    	
    	JobBase jb = job.getPrimaryJobClass();
		if (jb instanceof JobProtect) {
			String name = ((JobProtect) jb).playerName;
			ZCGame.instance().check(name);
			int count = (Integer)ZCGame.instance().getData(name, zombiecraft.Core.DataTypes.comrades);
			if (count > 0) {
				ZCGame.instance().setData(name, zombiecraft.Core.DataTypes.comrades, count - 1);
			}
		}
    	
		super.setDead();
    }
    
    @Override
	public int overrideBlockPathOffset(c_IEnhAI ent, int id, int meta, int x, int y, int z) {
    	c_EnhAI entAI = (c_EnhAI)ent;
    	
		if (id == ZCBlocks.barricadePlaceable.blockID) {
			if (entAI.dipl_team == EnumDiploType.COMRADE) {
				return -2;
			}
			if (entAI.worldObj.rand.nextInt(10) != 0) {
				return 1;
			}
			return -2;
		} else if (Block.blocksList[id] instanceof BlockHalfSlab) {
			return 0;
		} else if (Block.blocksList[id] instanceof BlockBarrier) {
			return -2;
		} else if (Block.blocksList[id] instanceof BlockWallPlaceable) {
			return -2;
		} else if (Block.blocksList[id] instanceof BlockTower) {
			return -2;
		}
		return -66;
    }
    
    @Override
    public void attackEntityWithNothing(Entity var1) {
    	
    	int damage = 1;
    	
    	if (ZCGame.instance().wMan.difficulty == EnumDifficulty.MEDIUM) {
    		damage = 2;
    	} else if (ZCGame.instance().wMan.difficulty == EnumDifficulty.HARD) {
    		damage = 3;
    	}
    	
    	if (var1 instanceof EntityPlayer) {
    		EntityPlayer entP = (EntityPlayer)var1;
    		
    		if ((Integer)ZCGame.instance().getData(entP, zombiecraft.Core.DataTypes.juggTime) > 0) {
    			damage /= 2;
    		}
    	}
    	
    	if (var1 instanceof EntityLivingBase) {
	    	
    		DamageSource ds = DamageSource.causeMobDamage((EntityLivingBase)var1);
    		ds.damageType = "zc.zombie";
    		
    		var1.attackEntityFrom(ds, damage);
	    	//super.attackEntityWithNothing(var1);
	    	var1.velocityChanged = false;
	    	
	    	
    	}
    }

	public void headshot() {
		// TODO Auto-generated method stub
		//System.out.println("headshot");
		if (getDataWatcher().getWatchableObjectInt(24) == 0) {
			worldObj.playSoundAtEntity(this, "zc.headshot", 0.6F, 1.0F);
		}
		getDataWatcher().updateObject(24, Integer.valueOf(1));
	}
    
}
