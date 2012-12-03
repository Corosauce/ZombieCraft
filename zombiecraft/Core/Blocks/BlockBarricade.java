package zombiecraft.Core.Blocks;

import net.minecraft.src.*;

import java.util.List;
import java.util.Random;

import zombiecraft.Core.ZCBlocks;
import zombiecraft.Core.ZCItems;
import zombiecraft.Core.Entities.BaseEntAI;
import zombiecraft.Core.Entities.BaseEntAI_Ally;
import zombiecraft.Core.GameLogic.ZCGame;


public class BlockBarricade extends BlockDoor
{
	
	public int state = 5;
	
	//public int stateToBlockID[] = {mod_ZombieCraft.barricadeS0.blockID,mod_ZombieCraft.barricadeS1.blockID,mod_ZombieCraft.barricadeS2.blockID,mod_ZombieCraft.barricadeS3.blockID,mod_ZombieCraft.barricadeS4.blockID,mod_ZombieCraft.barricadeS5.blockID};
	public int stateToBlockID[];
	public int barricadeTopTexIDs[];
	
    public BlockBarricade(int i, Material material)
    {
        super(i, material);
        //blockIndexInTexture = 97;
        updateTexture();
        if(material == Material.iron)
        {
            blockIndexInTexture++;
        }
        float f = 0.5F;
        float f1 = 1.0F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
        
    }
    
    public BlockBarricade(int parStateToBlockID[], int parBarricadeTopTexIDs[], Material material, int stateTemp)
    {
        super(parStateToBlockID[stateTemp], material);
        stateToBlockID = parStateToBlockID;
        barricadeTopTexIDs = parBarricadeTopTexIDs;
        state = stateTemp;
        //blockIndexInTexture = 97;
        //updateTexture();
        blockIndexInTexture = barricadeTopTexIDs[6];
        this.setHardness(3F);
        
        float f = 0.5F;
        float f1 = 1.0F;
        
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
        
    }
    
    
    public void updateTexture() {
    	
    	blockIndexInTexture = barricadeTopTexIDs[state];
    	
    	if (true) return;
    	
    	if(state == 5)
		{
			blockIndexInTexture = 97;
		}
		else if(state == 4)
		{
			blockIndexInTexture = 98;
		}
		else if(state == 3)
		{
			blockIndexInTexture = 150;				
		}
		else if(state == 2)
		{
			blockIndexInTexture = 151;
		}
		else if(state == 1)
		{
			blockIndexInTexture = 152;
		}
		else if(state == 0)
		{
			blockIndexInTexture = 153;
		}
    }

    /*@Override not on server side*/
    public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
    	return this.getBlockTextureFromSideAndMetadata(par4, par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }
    
    @Override
    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
    	//System.out.println("side = " + i + " meta = " + j);
    	if ((j & 8) != 0) {
    		//System.out.println("state: " + state);
    		return barricadeTopTexIDs[state];
    	}
        if(i == 0 || i == 1)
        {
            return blockIndexInTexture;
        }
        int k = func_312_c(j);
        if((k == 0 || k == 2) ^ (i <= 3))
        {
            return blockIndexInTexture;
        }
        int l = k / 2 + (i & 1 ^ k);
        l += (j & 4) / 4;
        int i1 = blockIndexInTexture/* - (j & 8) * 2*/;
        if((l & 1) != 0)
        {
            //i1 = -i1;
        }
        //System.out.println("texid = " + i1);
        return i1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return 7;
    }

    /*@Override*/
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int par2, int par3, int par4)
    {
    	//System.out.println("ooo");
    	//if (true) return null;
        setBlockBoundsBasedOnState(world, par2, par3, par4);
        //setBlockBounds(0.0F, 0.0F, 0.0F, 0.1F, 0.1F, 0.1F);
        return AxisAlignedBB.getBoundingBox((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ);
        //return super.getSelectedBoundingBoxFromPool(world, i, j, k);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
    	if (ZCGame.instance() == null) return null;
    	//if ((world.getBlockMetadata(i, j, k) & 8) != 0 || (state == 0 && (mod_EntAI.playerRef == null || (mod_EntAI.playerRef.barrierX != i && mod_EntAI.playerRef.barrierY != j && mod_EntAI.playerRef.barrierZ != k)))) return null;
    	if ((world.getBlockMetadata(i, j, k) & 8) != 0 || (state == 0) || ZCGame.instance().mapMan.doorNoClip) return null;
        setBlockBoundsBasedOnState(world, i, j, k);
    	//setBlockBounds(0.0F, 0.0F, 0.0F, 0.1F, 0.1F, 0.1F);
        return super.getCollisionBoundingBoxFromPool(world, i, j, k);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
    	setDoorRotation(func_312_c(iblockaccess.getBlockMetadata(i, j, k)));
    }

    public void setDoorRotation(int i)
    {
        float f = 0.1875F;
        float e = (1F-f)/2F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        
        /*if (i == 0) {
        	setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F);        	
        }*/
        
        if(i == 0)
        {
            setBlockBounds(0.0F, 0.0F, e, 1.0F, 1.0F, e+f);
        }
        if(i == 1)
        {
            setBlockBounds(1.0F - (e+f), 0.0F, 0.0F, e+f, 1.0F, 1.0F);
        }
        if(i == 2)
        {
            setBlockBounds(0.0F, 0.0F, 1.0F - (e+f), 1.0F, 1.0F, e+f);
        }
        if(i == 3)
        {
            setBlockBounds(e, 0.0F, 0.0F, e+f, 1.0F, 1.0F);
        }
    }

    @Override
    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
    	//System.out.println("xyz: " + i + " " + j + " " + k);
		if (ZCGame.instance().canEdit(entityplayer)) tryRepairDoor(world, i, j, k, entityplayer);
		//tryDamageDoor(world, i, j, k, entityplayer);
    }
    
    public boolean damageDoor(World world, int i, int j, int k, EntityPlayer entityplayer, int meta) {
    	
    	if(world.getBlockId(i, j + 1, k) == blockID)
        {
    		//System.out.print("updating: ");
    		//System.out.println(state-1);
    		if (state > 0) {
    			world.setBlock(i, j + 1, k, stateToBlockID[state-1]);
    		} else {
    			//world.setBlock(i, j + 1, k, stateToBlockID[5]);
				// dont repair when broken
    		}
            //world.setBlockMetadataWithNotify(i, j + 1, k, (meta ^ 4) + 8);
        }
    	if (state > 0) {
			world.setBlock(i, j, k, stateToBlockID[state-1]);
			return true;
		} else {
			//world.setBlock(i, j, k, stateToBlockID[5]);
			// dont repair when broken
			return false;
		}
        //world.setBlockMetadataWithNotify(i, j, k, (meta ^ 4) + stateToBit(state));
        //world.markBlocksDirty(i, j - 1, k, i, j, k);
    }
    
    public boolean upgradeDoor(World world, int i, int j, int k, EntityPlayer entityplayer, int meta) {
    	
    	if(world.getBlockId(i, j + 1, k) == blockID)
        {
    		//System.out.print("updating: ");
    		//System.out.println(state+1);
    		if (state < 5) {
    			world.setBlock(i, j + 1, k, stateToBlockID[state+1]);
    		}
            //world.setBlockMetadataWithNotify(i, j + 1, k, (meta ^ 4) + 8);
        }
    	if (state < 5) {
			world.setBlock(i, j, k, stateToBlockID[state+1]);
			return true;
		} else {
			return false;
		}
        //world.setBlockMetadataWithNotify(i, j, k, (meta ^ 4) + stateToBit(state));
        //world.markBlocksDirty(i, j - 1, k, i, j, k);
    }
    
    public void updateDoors(World world, int i, int j, int k, EntityPlayer entityplayer, boolean repair) {
    	
    	
    	
    	int ii = i-1;
    	int jj = j;
    	int kk = k;
    	
    	int l = world.getBlockMetadata(ii, jj, kk);
    	if(isBarricade(world, ii, jj, kk)) {
    		if (repair) {
    			upgradeDoor(world, ii, jj, kk, entityplayer, (l));
    		} else {
    			damageDoor(world, ii, jj, kk, entityplayer, (l));
    		}
    		updateDoorMeta(world, ii, jj, kk, entityplayer, (l));
    	}
    	ii+=2;
    	l = world.getBlockMetadata(ii, jj, kk);
    	if(isBarricade(world, ii, jj, kk)) {
    		if (repair) {
    			upgradeDoor(world, ii, jj, kk, entityplayer, (l));
    		} else {
    			damageDoor(world, ii, jj, kk, entityplayer, (l));
    		}
    		updateDoorMeta(world, ii, jj, kk, entityplayer, (l));
    	}
    	ii-=1;
    	kk+=1;
    	l = world.getBlockMetadata(ii, jj, kk);
    	if(isBarricade(world, ii, jj, kk)) {
    		if (repair) {
    			upgradeDoor(world, ii, jj, kk, entityplayer, (l));
    		} else {
    			damageDoor(world, ii, jj, kk, entityplayer, (l));
    		}
    		updateDoorMeta(world, ii, jj, kk, entityplayer, (l));
    	}
    	kk-=2;
    	l = world.getBlockMetadata(ii, jj, kk);
    	if(isBarricade(world, ii, jj, kk)) {
    		if (repair) {
    			upgradeDoor(world, ii, jj, kk, entityplayer, (l));
    		} else {
    			damageDoor(world, ii, jj, kk, entityplayer, (l));
    		}
    		updateDoorMeta(world, ii, jj, kk, entityplayer, (l));
    	}
    	kk+=2;
    	l = world.getBlockMetadata(ii, jj, kk);
    	if(isBarricade(world, ii, jj, kk)) {
    		if (repair) {
    			upgradeDoor(world, ii, jj, kk, entityplayer, (l));
    		} else {
    			damageDoor(world, ii, jj, kk, entityplayer, (l));
    		}
    		updateDoorMeta(world, ii, jj, kk, entityplayer, (l));
    	}
    	
    }
    
    public void updateDoorMeta(World world, int i, int j, int k, EntityPlayer entityplayer, int l) {
    	//int l = world.getBlockMetadata(i, j, k);
    	if(isBarricade(world, i, j + 1, k))
        {
            world.setBlockMetadata(i, j + 1, k, (l) + 8);
        }
        world.setBlockMetadata(i, j, k, (l));
        //world.markBlocksDirty(i, j - 1, k, i, j, k);
    }
    
    public boolean isFixableBarricade(World world, int i, int j, int k) {
    	//subtle change, < to <=
    	int id = world.getBlockId(i, j, k);
    	if (id > 0) {
        	//System.out.println("barricade check id: " + id);
    	}
    	if (world.getBlockId(i, j, k) >= ZCBlocks.barricadeS0.blockID && world.getBlockId(i, j, k) < ZCBlocks.barricadeS5.blockID) {
    		return true;
    	}
    	return false;
    }
    
    public boolean isBarricade(World world, int i, int j, int k) {
    	
    	if (world.getBlockId(i, j, k) >= ZCBlocks.barricadeS0.blockID && world.getBlockId(i, j, k) <= ZCBlocks.barricadeS5.blockID) {
    		return true;
    	}
    	return false;
    }
    
    public boolean tryRepairDoor(World world, int i, int j, int k, EntityPlayer entityplayer) {
    	int l = world.getBlockMetadata(i, j, k);
    
    	int idbottom = world.getBlockId(i, j - 1, k);
    	
	    if((l & 8) != 0)
	    {
	    	//if(this.isBarricade(world, i, idbottom, k))
	        if(world.getBlockId(i, j - 1, k) == blockID)
	        {
	        	return tryRepairDoor(world, i, j - 1, k, entityplayer);
	        }
	        //return true;
	    }
		
		boolean success = upgradeDoor(world, i, j, k, entityplayer, l);
		
		if (success) {
    		updateDoors(world, i, j, k, entityplayer, true);
    	}
		
		updateDoorMeta(world, i, j, k, entityplayer, l);
		
		if(isBarricade(world, i, j + 1, k))
	    {
	        world.setBlockMetadataWithNotify(i, j + 1, k, (l) + 8);
	    }
	    world.setBlockMetadataWithNotify(i, j, k, (l));
	    world.markBlockRangeForRenderUpdate(i, j - 1, k, i, j, k);
		
	    /*System.out.print("Setting meta: ");
	    System.out.println((l ^ 4) + stateToBit(state));*/
		
	    
		return success;
	}

    public boolean tryDamageDoor(World world, int i, int j, int k, EntityPlayer entityplayer) {
    	return damageDoor(world, i, j, k, entityplayer);
    }
	
    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
    	
    	int oldid = world.getBlockId(i, j - 1, k);
    	
    	if (world.isRemote) {
    		if (entity instanceof EntityPlayer) {
    			//if (!ZCGame.instance().canEdit((EntityPlayer)entity)) {
    				if (/*((EntityPlayer)entity).pushDelay == 0 && */oldid == ZCBlocks.barricadeS0.blockID) {
    					double speed = Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);
    					if (speed < 0.0001) speed = 0.1D;
    					if (speed < 0.5) {
    						entity.motionX = entity.motionX*-(1F/speed);
    						entity.motionZ = entity.motionZ*-(1F/speed);
    						//entity.attackEntityFrom(DamageSource.causeThrownDamage(entity, entity), 0);
    					}
    					
    					//((EntityPlayer)entity).pushDelay = 5;
    				}
    		}
    		return;
    	}
    	
    	
		if (world.getBlockId(i, j, k) != ZCBlocks.barricadeS0.blockID && (oldid != ZCBlocks.barricadeS0.blockID && oldid != 1) && entity instanceof BaseEntAI && !(entity instanceof BaseEntAI_Ally) && ((BaseEntAI)entity).getHealth() > 0)
		{
			//System.out.println(oldid);
			BaseEntAI ent = (BaseEntAI)entity;
			ent.curBlockDamage++;
			//ent.isJumping = false;
			ent.isBreaking = true;
			ent.motionY = -0.4F;
			//System.out.println(ent.curBlockDamage);
			
			if (ent.curBlockDamage % 65 == 0) {
				ent.swingItem();
			}
			
			if (ent.curBlockDamage % 130 == 0)
			{
				ent.curBlockDamage = 0;
				
				List<EntityPlayer> players = ZCGame.instance().getPlayers();
				if (players != null && players.size() > 0) {
					tryDamageDoor(world,i,j,k,players.get(0));
					ZCGame.instance().notifyBlockUpdates(i,j,k);
				}
				
				
				//((BaseEntAI)entity).noMoveTicks = 0;
				int newid = world.getBlockId(i, j - 1, k);
				
				Random rand = new Random();
				
				if (newid != oldid) {
		            if(newid == ZCBlocks.barricadeS0.blockID) {
		            	world.playSoundAtEntity(entity, "zc.barricadecollapse", 1.0F, 1.0F / rand.nextFloat() * 0.1F + 0.95F);
		            } else {
		            	world.playSoundAtEntity(entity, "zc.woodbreak", 1.0F, 1.0F / rand.nextFloat() * 0.1F + 0.95F);
		            }
				}
				//if (success) {
					
					//world.playSoundAtEntity(entity, "mob.sheep", 0.5F, 1.0F / entity.rand.nextFloat() * 0.1F + 0.95F);
					
					//System.out.println(success);
				//}
			}
		}
		
		if (entity instanceof EntityPlayer) {
			//if (!ZCGame.instance().canEdit((EntityPlayer)entity)) {
				if (/*((EntityPlayer)entity).pushDelay == 0 && */oldid == ZCBlocks.barricadeS0.blockID) {
					double speed = Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);
					if (speed < 0.0001) speed = 0.1D;
					if (speed < 0.5) {
						entity.motionX = entity.motionX*-(1F/speed);
						entity.motionZ = entity.motionZ*-(1F/speed);
						//entity.attackEntityFrom(DamageSource.causeThrownDamage(entity, entity), 0);
					}
					
					//((EntityPlayer)entity).pushDelay = 5;
				}
			//}
			//entity.prevPosX = entity.posX;
			//entity.prevPosZ = entity.posZ;
			//entity.posZ += entity.motionZ*-5F;
			//entity.motionX = 0;
			//entity.motionZ = 0;
			//System.out.println("player collide");
		}
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
    	
    	if (!ZCGame.instance().canEdit(entityplayer)) return false;
    	return damageDoor(world, i, j, k, entityplayer);
    	
    	
    }
    
    public boolean damageDoor(World world, int i, int j, int k, EntityPlayer entityplayer) {
    	int l = world.getBlockMetadata(i, j, k);
    	
    	
    	//state++;
    	
    	//
    	
    	//if (state > 128) { state = 0; }
    	
    	//world.setBlockMetadataWithNotify(i, j, k, i);
    	
    	//int state = getState(l);
    	//state--;
    	
    	//if (state < 0) { state = 5; }
    	//state = 1;
    	/*
    	//state 5
    	state = l + 16;
    	
    	//state 4
    	state = l + 32;
    	
    	//state 3
    	state = l + 64;
    	
    	//state 2
    	state = l + 128;
    	
    	//state 1
    	state = l + 256;
    	*/
    	//state 0
    	//not set
    	
    	
    	
    	
    	//update top and bottom block part of door, as well as notify bottom if top is clicked
    	if((l & 8) != 0)
        {
            if(world.getBlockId(i, j - 1, k) == blockID)
            {
            	damageDoor(world, i, j - 1, k, entityplayer);
            }
            return true;
        }
    	
    	boolean success = damageDoor(world, i, j, k, entityplayer, l);
    	if (success) {
    		updateDoors(world, i, j, k, entityplayer, false);
    		
    	}
    	
    	updateDoorMeta(world, i, j, k, entityplayer, l);
    	
    	if(isBarricade(world, i, j + 1, k))
        {
            world.setBlockMetadataWithNotify(i, j + 1, k, (l) + 8);
        }
        world.setBlockMetadataWithNotify(i, j, k, (l));
        world.markBlockRangeForRenderUpdate(i, j - 1, k, i, j, k);
    	
        /*System.out.print("Setting meta: ");
        System.out.println((l ^ 4) + stateToBit(state));*/
    	
        int hm = world.getBlockMetadata(i, j, k);
        
        
        
    	return success;
    	
        /*if(blockMaterial == Material.iron)
        {
            return true;
        }
        int l = world.getBlockMetadata(i, j, k);
        if((l & 8) != 0)
        {
            if(world.getBlockId(i, j - 1, k) == blockID)
            {
                blockActivated(world, i, j - 1, k, entityplayer);
            }
            return true;
        }
        if(world.getBlockId(i, j + 1, k) == blockID)
        {
            world.setBlockMetadataWithNotify(i, j + 1, k, (l ^ 4) + 8);
        }
        world.setBlockMetadataWithNotify(i, j, k, l ^ 4);
        world.markBlocksDirty(i, j - 1, k, i, j, k);
        if(Math.random() < 0.5D)
        {
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.door_open", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
        } else
        {
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.door_close", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
        }
        return true;*/
    }

    @Override
    public void onPoweredBlockChange(World world, int i, int j, int k, boolean flag)
    {
        int l = world.getBlockMetadata(i, j, k);
        if((l & 8) != 0)
        {
            if(world.getBlockId(i, j - 1, k) == blockID)
            {
            	onPoweredBlockChange(world, i, j - 1, k, flag);
            }
            return;
        }
        boolean flag1 = (world.getBlockMetadata(i, j, k) & 4) > 0;
        if(flag1 == flag)
        {
            return;
        }
        if(world.getBlockId(i, j + 1, k) == blockID)
        {
            world.setBlockMetadataWithNotify(i, j + 1, k, (l ^ 4) + 8);
        }
        world.setBlockMetadataWithNotify(i, j, k, l ^ 4);
        world.markBlockRangeForRenderUpdate(i, j - 1, k, i, j, k);
        if(Math.random() < 0.5D)
        {
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.door_open", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
        } else
        {
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.door_close", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        int i1 = world.getBlockMetadata(i, j, k);
        if((i1 & 8) != 0)
        {
            if(world.getBlockId(i, j - 1, k) != blockID)
            {
                world.setBlockWithNotify(i, j, k, 0);
            }
            if(l > 0 && Block.blocksList[l].canProvidePower())
            {
                onNeighborBlockChange(world, i, j - 1, k, l);
            }
        } else
        {
            boolean flag = false;
            if(world.getBlockId(i, j + 1, k) != blockID)
            {
                world.setBlockWithNotify(i, j, k, 0);
                flag = true;
            }
            if(!world.isBlockOpaqueCube(i, j - 1, k))
            {
                world.setBlockWithNotify(i, j, k, 0);
                flag = true;
                if(world.getBlockId(i, j + 1, k) == blockID)
                {
                    world.setBlockWithNotify(i, j + 1, k, 0);
                }
            }
            if(flag)
            {
                dropBlockAsItem(world, i, j, k, i1, 1);
            } else
            if(l > 0 && Block.blocksList[l].canProvidePower())
            {
                boolean flag1 = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k);
                onPoweredBlockChange(world, i, j, k, flag1);
            }
        }
    }

    @Override
    public int idDropped(int i, Random random, int j)
    {
        if((i & 8) != 0)
        {
            return 0;
        }
        return ZCItems.barricade.shiftedIndex;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 vec3d, Vec3 vec3d1)
    {
    	//called when block is selected
    	//System.out.println("what?!");
        setBlockBoundsBasedOnState(world, i, j, k);
        return super.collisionRayTrace(world, i, j, k, vec3d, vec3d1);
    }
    
    public int func_312_c(int i)
    {
        if((i & 4) == 0)
        {
            return i - 1 & 3;
        } else
        {
            return i & 3;
        }
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par3 >= 255 ? false : par1World.isBlockNormalCube(par2, par3 - 1, par4) && super.canPlaceBlockAt(par1World, par2, par3, par4)/* && super.canPlaceBlockAt(par1World, par2, par3 + 1, par4)*/;
    }

}
