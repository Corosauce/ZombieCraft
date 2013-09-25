package zombiecraft.Core.Entities;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import zombiecraft.Core.EnumDifficulty;
import zombiecraft.Core.ZCBlocks;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Blocks.BlockBarricade;
import zombiecraft.Core.Blocks.BlockBarrier;
import zombiecraft.Core.Blocks.BlockTower;
import zombiecraft.Core.Blocks.BlockWallPlaceable;
import zombiecraft.Core.GameLogic.WaveManager;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.Items.ItemGun;
import zombiecraft.Core.World.LevelConfig;
import zombiecraft.Forge.ZCServerTicks;
import zombiecraft.Forge.ZombieCraftMod;
import CoroAI.componentAI.AIAgent;
import CoroAI.componentAI.IAdvPF;
import CoroAI.componentAI.ICoroAI;
import CoroAI.componentAI.jobSystem.JobBase;
import CoroAI.componentAI.jobSystem.JobProtect;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseEntAI extends EntityLiving implements ICoroAI, IAdvPF
{
	public boolean initWaveSettings = true;
	public int curBlockDamage;
	public boolean isBreaking;
	public boolean wasHeadshot;
    
    public float aimVal = 0.8F;
    public float recoilVal = 0.9F;
    public float reloadVal = 0.5F;
    
    public int holsterDelay = 0;
	
	//new AI gen
	public AIAgent agent;
	
	//added for gun handling
	public int curCooldown_FireGun;
	public int curCooldown_Reload;
	public int curClipAmount;
	
    public BaseEntAI(World par1World)
    {
        super(par1World);
        
        //team = 0;
        agent.maxReach_Melee = 2.0F;
        
        //no reload time to let guns handle it?
        //cooldown_Ranged = 3; moved to override
        
        //System.out.println("REMOVING SERVERMODE CHECK BaseEntAI constructor");
        if (ZCServerTicks.zcGame.serverMode) {
        	
        	agent.maxReach_Melee = 1.3F;
        }
        
        agent.PFRangePathing = 96;
        
        //System.out.println("maxReach_Melee: " + maxReach_Melee);
        
        //entityCollisionReduction = 0.9F;
        
        //this.setExp(1);
        //mod_ZombieCraft.zcGame.addTasks(tasks, targetTasks, this);
        
    }
    
    @Override
    protected void onDeathUpdate()
    {
        ++this.deathTime;

        if (this.deathTime == 20)
        {
            int i;

            if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && !this.isChild() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot"))
            {
                i = this.getExperiencePoints(this.attackingPlayer);

                while (i > 0)
                {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
                }
            }

            this.setDead();

            for (i = 0; i < 5; ++i)
            {
                double d0 = this.rand.nextGaussian() * 0.15D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.15D;
                this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.2F/* + (double)(this.rand.nextFloat() * this.height)*/, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
            }
        }
    }
    
    @Override
    protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
    	return 1;
    }
    
    @Override
    public void entityInit() {
    	super.entityInit();
    	checkAgent();
    	agent.entityInit();
    	this.dataWatcher.addObject(27, Integer.valueOf(0)); //headshot state
    }
    
    public void dropItems() {
    	
    }
    
    //needs reconsideration
    /*@Override
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
    }*/
    
    @Override
    public void swingItem()
    {
    	super.swingItem();
    	if (!worldObj.isRemote) {
    		agent.swingArm();
    	}
    }
    
    @Override
    protected void updateArmSwingProgress() {
    	
    	if (!worldObj.isRemote) return;
    	
    	if (holsterDelay > 0) holsterDelay--;
    	
    	//ItemStack is = this.getCurrentEquippedItem();
    	if (this.getCurrentItemOrArmor(0) != null && this.getCurrentItemOrArmor(0).getItem() instanceof ItemGun/*agent.useInv && agent.entInv.getCurrentEquippedItem() != null && agent.entInv.getCurrentEquippedItem().getItem() instanceof ItemGun*/) {
    		if(agent.swingArm) {
    			agent.swingTick++;
	
    			int recoilAnimTime = 5;
    			
    			ItemStack is = this.getCurrentItemOrArmor(0);
    	    	if (is != null && is.getItem() instanceof ItemGun) {
    	    		recoilAnimTime = ((ItemGun)is.getItem()).useDelay;
    	    	}
	            //System.out.println(recoilAnimTime);
	            if(agent.swingTick >= Math.max(2, Math.min(8, recoilAnimTime))) {
	            	agent.swingTick = 0;
	            	agent.swingArm = false;
	            } else {
	            	holsterDelay = 60;
	            }
	            
	            //swingProgress = recoilVal;
	            
	        } else {
	        	agent.swingTick = 0;
	        }
    		
    		//this depended on item stack synced nbt, now broken reload animation, definately still broken
    		//if (is.stackTagCompound.hasKey("reloadDelay")) {
    		int nReloadDelay = curCooldown_Reload;//is.stackTagCompound.getInteger("reloadDelay");
    		
    		if (nReloadDelay > 0) {
    			if (holsterDelay > 10) holsterDelay = 10;
    		}
    		//}
	
    		//System.out.println(agent.swingArm + " - agent.swingTick: " + agent.swingTick);
    		
    		if (holsterDelay > 10) {
    			swingProgress = 0.36F - (agent.swingTick * 0.04F);
    		} else {
    			swingProgress = holsterDelay * 0.01F;
    		}
    		prevSwingProgress = swingProgress;
    	} else {
	    	if(agent.swingArm) {
	    		agent.swingTick++;
	
	            
	            if(agent.swingTick == 8) {
	            	agent.swingTick = 0;
	            	agent.swingArm = false;
	            }
	        } else {
	        	agent.swingTick = 0;
	        }
	
	        swingProgress = (float)agent.swingTick / 8F;
    	}
    	
    	//swingProgress = 0.5F;
    	
    	
    }
    
    @Override
    public boolean isBreaking() {
		return isBreaking;
	}
    
    /*@Override
    public void setPrjOwner(int offset) {
    	super.setPrjOwner(offset);
    	
    	Entity ent = (Entity)worldObj.loadedEntityList.get(worldObj.loadedEntityList.size()-offset);
    	if (ent instanceof EntityBullet) {
    		((EntityBullet) ent).owner = this;
    	}
    }*/
    
    public void leftClickItem(Entity var1) {
    	
    }
    
    protected void updateAITasks()
    {
    	agent.updateAITasks();
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
    
    public float getMoveSpeed() {
    	return (float) this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e();
    }
    
    
    public boolean isValidLightLevel() {
    	return true;
    }
    
    @Override
    public boolean getCanSpawnHere()
    {
        return /*this.isValidLightLevel() && */super.getCanSpawnHere();
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

	@Override
	protected void func_110147_ax() {
		super.func_110147_ax();
		float speed = 0.5F;
		float health = 20;
		try {
			health = Float.valueOf(LevelConfig.get(LevelConfig.nbtStrWaveHealth));
			float healthAmp = Float.valueOf(LevelConfig.get(LevelConfig.nbtStrWaveHealthAmp));
			float entBaseSpeed = Float.valueOf(LevelConfig.get(LevelConfig.nbtStrWaveSpeedBase));
			float entRandSpeedAdd = Float.valueOf(LevelConfig.get(LevelConfig.nbtStrWaveSpeedRand));
			float amp_Speed = Float.valueOf(LevelConfig.get(LevelConfig.nbtStrWaveSpeedAmp));
			float speedMax = Float.valueOf(LevelConfig.get(LevelConfig.nbtStrWaveSpeedAmpMax));
			
			if (!(this instanceof BaseEntAI_Ally)) {
				WaveManager wMan = ZCServerTicks.zcGame.wMan;
				speed = entBaseSpeed + (entRandSpeedAdd * worldObj.rand.nextFloat()) + (amp_Speed * wMan.wave_Stage);
				if (speed > speedMax) speed = speedMax;
				health = (int)(health + (health * (healthAmp * wMan.wave_Stage)));
				setEntityHealth(health);
			} else {
				speed = 0.55F;
			}
		} catch (Exception ex) {
			//occurs when ents near player load before games initialized LevelConfig
			//not really a big deal as these entities should get cleared out elsewhere on load if not in active ZC game anyways
		}
		agent.setSpeedFleeAdditive(0.1F);
		agent.setSpeedNormalBase(speed);
		agent.applyEntityAttributes();
		//System.out.println("setting ZC zombie health/speed to: " + health + "/" + speed);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(health);
	}
    
    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
	@Override
    public void onLivingUpdate()
    {
        if (curCooldown_FireGun > 0) { curCooldown_FireGun--; }
        if (curCooldown_Reload > 0) { curCooldown_Reload--; }
        
        //cancel any swing arm attempts while reloading so client doesnt spaz out thinking its firing
        if (curCooldown_Reload > 0) {
        	//System.out.println("cancel swing!");
        	agent.swingArm = false;
        }
        
		this.updateArmSwingProgress();
		agent.onLivingUpdateTick();
    	//entityCollisionReduction = 0.9F;
    	
    	if (worldObj.isRemote && getDataWatcher().getWatchableObjectInt(27) == 1) {
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
	    		
	    		//ent.lungeFactor += ent.lungeFactor * (wMan.amp_Lunge * wMan.wave_Stage);
	    		
	    		//setMoveSpeed(0.23F);
	    		
	    		//random variation
	    		//ent.setMoveSpeed(this.getMoveSpeed() + (0.1F * worldObj.rand.nextFloat()));
	    		
	    		if (!worldObj.isRemote) {
	    			//System.out.println("speed was: " + this.getMoveSpeed());
	    		}
	    		
	    		//ent.setMoveSpeed(this.getMoveSpeed() + (0.002F * wMan.wave_Stage));
	    		//if (ent.getMoveSpeed() > wMan.amp_Speed_Max) ent.setMoveSpeed(wMan.amp_Speed_Max);
	    		//if (ent.lungeFactor > wMan.amp_Lunge_Max) ent.lungeFactor = wMan.amp_Lunge_Max;
	    		
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
    	
    	JobBase jb = agent.jobMan.getPrimaryJob();
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
	public int overrideBlockPathOffset(ICoroAI ent, int id, int meta, int x, int y, int z) {
    	ICoroAI entAI = (ICoroAI)ent;
    	
		if (id == ZCBlocks.barricadePlaceable.blockID) {
			if (entAI instanceof BaseEntAI_Ally) {
				return -2;
			}
			if (((EntityLiving)entAI).worldObj.rand.nextInt(10) != 0) {
				return 1;
			}
			return -2;
		} else if (Block.blocksList[id] instanceof BlockBarricade) {
			if (entAI instanceof BaseEntAI_Ally) {
				return 0;
			} else {
				return 1;
			}
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

	public void headshot() {
		//System.out.println("headshot");
		if (getDataWatcher().getWatchableObjectInt(27) == 0) {
			worldObj.playSoundAtEntity(this, ZombieCraftMod.modID + ":zc.headshot", 0.6F, 1.0F);
		}
		getDataWatcher().updateObject(27, Integer.valueOf(1));
	}
	
	//ICoroAI overrides \

	@Override
	public AIAgent getAIAgent() {
		return agent;
	}

	@Override
	public void setPathResultToEntity(PathEntity pathentity) {
		agent.setPathToEntity(pathentity);
	}

	@Override
	public boolean isEnemy(Entity ent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCooldownMelee() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCooldownRanged() {
		return 3;
	}

	@Override
	public void attackMelee(Entity ent, float dist) {
		
		ZCGame.instance().pvpFixPre();
    	
		int damage = 1;
    	
    	if (ZCGame.instance().wMan.difficulty == EnumDifficulty.MEDIUM) {
    		damage = 2;
    	} else if (ZCGame.instance().wMan.difficulty == EnumDifficulty.HARD) {
    		damage = 3;
    	}
    	
    	if (ent instanceof EntityPlayer) {
    		EntityPlayer entP = (EntityPlayer)ent;
    		
    		if ((Integer)ZCGame.instance().getData(entP, zombiecraft.Core.DataTypes.juggTime) > 0) {
    			damage /= 2;
    		}
    	}
    	
    	if (ent instanceof EntityLivingBase) {
	    	
    		DamageSource ds = DamageSource.causeMobDamage((EntityLivingBase)ent);
    		ds.damageType = "zc.zombie";
    		
    		ent.attackEntityFrom(ds, damage);
	    	//super.attackEntityWithNothing(var1);
    		ent.velocityChanged = false;
	    	
	    	
    	}
    	
    	ZCGame.instance().pvpFixPost();
	}

	@Override
	public void attackRanged(Entity ent, float dist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanup() {
		agent = null;
	}
	
	//ICoroAI overrides /
	
	public void checkAgent() {
		if (agent == null) agent = new AIAgent(this, false);
	}

	@Override
	public boolean canClimbWalls() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canClimbLadders() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getDropSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	
    @Override
    public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
    	agent.readEntityFromNBT(par1nbtTagCompound);
    	super.readEntityFromNBT(par1nbtTagCompound);
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
    	agent.writeEntityToNBT(par1nbtTagCompound);
    	super.writeEntityToNBT(par1nbtTagCompound);
    }
}
