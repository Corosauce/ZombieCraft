package zombiecraft.Core.Items;

import zombiecraft.Core.ZCBlocks;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZombieCraftMod;
import net.minecraft.src.*;

public class ItemEditTool extends Item {
    
	//THIS TOOL IS DESIGNED FOR CLIENT SIDE USE ONLY, SENDS PACKETS
	
    private boolean tmp;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	
	public int mode = 0;
	
	public int subMode = 0;
	
	public int x1 = 0;
	public int y1 = 0;
	public int z1 = 0;
	public int x2 = 0;
	public int y2 = 0;
	public int z2 = 0;

    public ItemEditTool(int par1, int parMode)
    {
        super(par1);
        this.maxStackSize = 1;
        mode = parMode;
        xCoord = 0;
		yCoord = 0;
		zCoord = 0;
		tmp = true;
    }
    
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	int i = (int)par3EntityPlayer.posX;
    	int j = (int)par3EntityPlayer.posY-1;
    	int k = (int)par3EntityPlayer.posZ;
    	
    	if (!par2World.isRemote) {
    		par3EntityPlayer.addPotionEffect(new PotionEffect(ZombieCraftMod.zcPotionExStatic.id, 600, 1));
    	}
    	
    	if (!par2World.isRemote) return par1ItemStack;
    	
    	if (mode == 0) {
	    	
    	} else if (mode == 1) {
    		if (subMode == 0) {
    			x1 = i; y1 = j; z1 = k;
    			ZCGame zcG = ZCGame.instance();
    			zcG.settingSize = true;
    			zcG.sx = x1; zcG.sy = y1; zcG.sz = z1;  
    			System.out.println("minCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			zcG.setModeMessage("minCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			subMode++;
    		} else {
    			x2 = i; y2 = j; z2 = k;
    			ZCGame zcG = ZCGame.instance();
    			zcG.settingSize = false;
    			System.out.println("maxCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			zcG.setModeMessage("maxCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			zcG.setLevelSize(x1, y1, z1, x2, y2, z2);
    			subMode = 0;
    		}
    	} else if (mode == 2) {
    		ZCGame.instance().setPlayerSpawn(par3EntityPlayer, i, j+1, k);
    	}
        return par1ItemStack;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int i, int j, int k, int par7, float f1, float f2, float f3)
    {
    	
    	
    	if (!par3World.isRemote) return true;
    	
    	
    	
    	if (mode == 0) {
    		if (par2EntityPlayer.worldObj.getBlockId(i,j,k) != ZCBlocks.b_mobSpawnerWave.blockID) {
				//ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("Debrisblock X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			ZCGame.instance().setModeMessage("Break block selected at X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k) + ", select spawner");
	    		//System.out.println("Debrisblock X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
				xCoord = i;
				yCoord = j;
				zCoord = k;
				tmp = !tmp;
			} else {
				TileEntity tileent =  par2EntityPlayer.worldObj.getBlockTileEntity(i,j,k);
				if (tileent instanceof TileEntityMobSpawnerWave)			
				{
					if (xCoord != 0 || yCoord != 0 || zCoord != 0)
					{
						if (tileent != null) {
							ZCGame.instance().setSpawnerWatch(par2EntityPlayer, i,j,k, xCoord, yCoord, zCoord);
							/*((TileEntityMobSpawnerWave)tileent).watchX = xCoord;
							((TileEntityMobSpawnerWave)tileent).watchY = yCoord;
							((TileEntityMobSpawnerWave)tileent).watchZ = zCoord;
							((TileEntityMobSpawnerWave)tileent).act_Watch = true;*/
							//ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("Linked to MobSpawner at X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
							ZCGame.instance().setModeMessage("Linked to Spawner at X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
							//System.out.println("Linked to MobSpawner at X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
							tmp = !tmp;				
						}	
					}
				}
			}
    	} else if (mode == 1) {
    		if (subMode == 0) {
    			x1 = i; y1 = j; z1 = k;
    			ZCGame zcG = ZCGame.instance();
    			zcG.settingSize = true;
    			zcG.sx = x1; zcG.sy = y1; zcG.sz = z1;  
    			System.out.println("minCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			zcG.setModeMessage("minCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			subMode++;
    		} else {
    			x2 = i; y2 = j; z2 = k;
    			ZCGame zcG = ZCGame.instance();
    			zcG.settingSize = false;
    			System.out.println("maxCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			zcG.setModeMessage("maxCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			zcG.setLevelSize(x1, y1, z1, x2, y2, z2);
    			subMode = 0;
    		}
    	} else {
    	
    		onItemRightClick(par1ItemStack, par3World, par2EntityPlayer);
    	}
    	return true;
    }
    
    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    	
    	if (!par2World.isRemote) return; // server and client share instances, this is a client item only
    	ZCGame inst = ZCGame.instance();
    	if (inst == null || inst.mapMan == null) return;
    	
    	mode = inst.mapMan.editToolMode;
    	
    	if (inst.mapMan.editMode && par5) {
    		inst.showEditToolMode();
    	}
    }
}
