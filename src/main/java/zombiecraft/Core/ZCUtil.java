package zombiecraft.Core;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import zombiecraft.Core.Entities.Projectiles.EntityBullet;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.Items.ItemGun;
import zombiecraft.Forge.ZombieCraftMod;
import CoroUtil.OldUtil;
import CoroUtil.componentAI.ICoroAI;
import CoroUtil.util.CoroUtilBlock;
import CoroUtil.util.CoroUtilEntity;

public class ZCUtil {
	
	
	public static String field_obf_blockResistance = "field_72029_cc";
	public static String field_obf_rightClickDelayTimer = "field_71467_ac";
	public static String field_obf_equippedProgress = "field_78454_c";
	public static String field_obf_cameraZoom = "field_78503_V";
	public static String field_obf_particleGravity = "field_70545_g";
	
	public static String field_mcp_blockResistance = "blockResistance";
	public static String field_mcp_rightClickDelayTimer = "rightClickDelayTimer";
	public static String field_mcp_equippedProgress = "equippedProgress";
	public static String field_mcp_cameraZoom = "cameraZoom";
	public static String field_mcp_particleGravity = "particleGravity";
	
	public ZCUtil() {
		
	}
	
	public static Field tryGetField(Class theClass, String obf, String mcp) {
		Field field = null;
		try {
			field = theClass.getDeclaredField(obf);
			field.setAccessible(true);
		} catch (Exception ex) {
			try {
				field = theClass.getDeclaredField(mcp);
				field.setAccessible(true);
			} catch (Exception ex2) { ex2.printStackTrace(); }
		}
		return field;
	}
	
	//apparently the tick updates are weird, this function is now unused....
	//this will only really be needed for when the user drops a gun while holding it, as item onUpdate calls if item isnt selected too
	public static boolean lastTickGunInHand = false;
	public static void handleGunUseBinds(boolean gunInHand) {
		lastTickGunInHand = gunInHand;
	}
	
	public static boolean shouldBulletPassThrough(EntityBullet bullet, Block blockID) {
		if (CoroUtilBlock.isAir(blockID) || blockID == Blocks.fence || blockID == Blocks.iron_bars || blockID == Blocks.fence_gate || blockID == ZCBlocks.barrier || blockID == ZCBlocks.barricadePlaceable || (ZCBlocks.barricadeStates.contains(blockID))) {
			return true;
		}
		return false;
	}
	
	
	public static boolean shouldBulletHurt(EntityBullet bullet, Entity ent) {
    	if (ent instanceof ICoroAI) {
    		if (bullet.owner instanceof ICoroAI) {
    			//AI to AI
    			if (((ICoroAI)bullet.owner).getAIAgent().dipl_info.isEnemy(((ICoroAI)ent).getAIAgent().dipl_info)) {
    				return true;
    			} else {
    				return false;
    			}
    		} else {
    			//Player to AI - cancel if comrade
    			if (bullet.owner instanceof EntityPlayer && ((ICoroAI)ent).getAIAgent().dipl_info.type == "comrade") {
    				return false;
    			}
    			return true;
    		}
    	} else {
    		//Player to Player
    		if (bullet.owner instanceof EntityPlayer) {
    			if (ZCGame.instance() == null || ZCGame.instance().zcLevel == null) {
    				System.out.println("ZCGame.instance.zcLevel is null?!");
    				return true;
    			}
	    		if (ZCGame.instance().zcLevel.playersInGame.contains(ent)) {
	    			return ZCGame.instance().cfg_ff;
	    		}
	    	//AI to Player - cancel if comrade
    		} else if (bullet.owner instanceof ICoroAI) {
    			if (((ICoroAI)bullet.owner).getAIAgent().dipl_info.type == "comrade" && ZCGame.instance().zcLevel.playersInGame.contains(ent)) {
	    			return false;
	    		}
    		}
    		return true;
    	}
    }
	
	public static void ammoDLCheck(String name, int ammoID) {
		
		if (!((AmmoDataLatcher)getData(name, DataTypes.ammoAmounts)).values.containsKey(ammoID)) {
			((AmmoDataLatcher)getData(name, DataTypes.ammoAmounts)).values.put(ammoID, 0);
		}
	}
	
	public static void setAmmoData(EntityPlayer ent, int ammoID, int ammoAmount) {
		setAmmoData(CoroUtilEntity.getName(ent), ammoID, ammoAmount);
	}
	
	public static void setAmmoData(String name, int ammoID, int ammoAmount) {
		ZCGame.instance().check(name);
		ammoDLCheck(name, ammoID);
		
		((AmmoDataLatcher)getData(name, DataTypes.ammoAmounts)).values.put(ammoID, ammoAmount);
		
	}
	
	public static int getAmmoData(EntityPlayer ent, int ammoID) {
		return getAmmoData(CoroUtilEntity.getName(ent), ammoID);
	}
	
	public static int getAmmoData(String name, int ammoID) {
		ZCGame.instance().check(name);
		ammoDLCheck(name, ammoID);
		return (Integer)((AmmoDataLatcher)getData(name, DataTypes.ammoAmounts)).values.get(ammoID);
	}
	
	public static void setData(EntityPlayer ent, DataTypes dtEnum, Object obj) {
		//System.out.println("set: " + ent.entityId);
		//DataLatcher dl = (DataLatcher)entFields.get(ent.entityId);
		//System.out.println("set: " + ent.entityId + "|" + ((DataLatcher)entFields.get(ent.entityId)).values.get(dtEnum) + " -> " + obj.toString());
		((DataLatcher)ZCGame.instance().entFields.get(CoroUtilEntity.getName(ent))).values.put(dtEnum, obj);
	}
	
	public static Object getData(EntityPlayer ent, DataTypes dtEnum) {
		return getData(CoroUtilEntity.getName(ent), dtEnum);
	}
	
	public static Object getData(String name, DataTypes dtEnum) {
		//DataLatcher dl = (DataLatcher)entFields.get(ent.entityId);
		//System.out.println("get: " + ent.entityId + "|" + ((DataLatcher)entFields.get(ent.entityId)).values.get(dtEnum));
		try {
			return ((DataLatcher)ZCGame.instance().entFields.get(name)).values.get(dtEnum);
		} catch (Exception ex) {
			try {
				ZCGame.instance().check(name);
				return ((DataLatcher)ZCGame.instance().entFields.get(name)).values.get(dtEnum);
			} catch (Exception ex2) {
				ex.printStackTrace();
				return null;
			}
		}
	}
	
	public static void reload(EntityPlayer var0, int var1, World var2) {
		int ammoCount = getAmmoData(var0, var1);
		int clipSize = Item.itemsList[var1].getItemStackLimit();
		
		
		if (ammoCount > 0) {
			ammoCount -= ammoCount % clipSize;
			setAmmoData(var0, var1, ammoCount);
			
			//System.out.println("ammoCount: " + ammoCount);
			//System.out.println("ammoCount % clipSize: " + ammoCount % clipSize);
			
			System.out.println("RELOADING BROKEN!");
			//mod_ZCSdkGuns.reload(var2, (EntityPlayer)var0);
			
		}
	}
	
	//var1 = item ammo shifted index
	public static int useItemInInventory(EntityPlayer var0, String name, ItemGun item)
    {
		//ammoDLCheck(var0, var1);
        //if data > 0: if data quantifies to clipsize: return 2, else: return 1;, else: return 0;
		//if ZCGame.instance.getData(var0, dtEnum)
		int ammoCount = getAmmoData(name, item.ammoType.ordinal());
		int clipSize = item.magSize;
		
		//System.out.println("ammoCount for " + name + ": " + ammoCount);
		
		//TEMP HACK FOR SERVER UNLIMITED AMMO, broken datalatcher
		//ammoCount = 250;
		
		if (ammoCount > 0) {
			ammoCount--;
			setAmmoData(name, item.ammoType.ordinal(), ammoCount);
			try {
				//if its a real player, not an ai
				if (!name.contains("fakePlayer")) ZCGame.instance().updateAmmoData(var0);
			} catch (Exception ex) {
				
				System.out.println("Caught!"); ex.printStackTrace();
			}
			
			//System.out.println("ammoCount: " + ammoCount);
			//System.out.println("ammoCount % clipSize: " + ammoCount % clipSize);
			
			//Reload!
			if (ammoCount % clipSize == 0 && ammoCount != 0) {
				return 2;
			} else {
				return 1;
			}
		}
		
		//temp unlimited
		//if (true) return 1;
		
		return 0;
    }
	
	public static int getAmmoCount(EntityPlayer var0, int var1)
    {
		int ammo = getAmmoData(var0, var1);
		return ammo;
    }
	
	/*public static int getClipCount(EntityPlayer var0, int var1)
    {
		int ammo = getAmmoData(var0, var1);
		return ammo;
    }*/
	
	/*public static int getInventorySlotContainItem(InventoryPlayer var0, int var1)
    {
        for (int var2 = 0; var2 < var0.mainInventory.length; ++var2)
        {
            if (var0.mainInventory[var2] != null && var0.mainInventory[var2].5 == var1)
            {
                return var2;
            }
        }

        return -1;
    }*/
	
	public static boolean areBlocksMineable = true;
	public static float[] blockHardness;
	public static boolean areBlocksExplodable = true;
	public static float[] blockResistance;
	
	public static void setBlocksMineable(boolean var1)
	{
		areBlocksMineable = var1;
		
	}
	
	public static void setPrivateValueBoth(Class var0, Object var1, String srg, String mcp, Object var3) {
		OldUtil.setPrivateValueSRGMCP(var0, var1, srg, mcp, var3);
		//c_CoroAIUtil.setPrivateValueBoth(var0, var1, obf, mcp, var3);
    }
    
    public static Object getPrivateValueBoth(Class var0, Object var1, String srg, String mcp) {
    	return OldUtil.getPrivateValueSRGMCP(var0, var1, srg, mcp);
    	//return c_CoroAIUtil.getPrivateValueBoth(var0, var1, obf, mcp);
    }
    
    public static Object getPrivateValue(Class var0, Object var1, String var2) throws IllegalArgumentException, SecurityException, NoSuchFieldException
    {
        try
        {
            Field var3 = var0.getDeclaredField(var2);
            var3.setAccessible(true);
            return var3.get(var1);
        }
        catch (IllegalAccessException var4)
        {
            System.out.println("An impossible error has occured! " + var4);
            return null;
        }
    }

    static Field field_modifiers = null;

    public static void setPrivateValue(Class var0, Object var1, int var2, Object var3) throws IllegalArgumentException, SecurityException, NoSuchFieldException
    {
        try
        {
            Field var4 = var0.getDeclaredFields()[var2];
            var4.setAccessible(true);
            int var5 = field_modifiers.getInt(var4);

            if ((var5 & 16) != 0)
            {
                field_modifiers.setInt(var4, var5 & -17);
            }

            var4.set(var1, var3);
        }
        catch (IllegalAccessException var6)
        {
            //logger.throwing("ModLoader", "setPrivateValue", var6);
            //throwException("An impossible error has occured!", var6);
        }
    }

    public static void setPrivateValue(Class var0, Object var1, String var2, Object var3) throws IllegalArgumentException, SecurityException, NoSuchFieldException
    {
        try
        {
            if (field_modifiers == null)
            {
                field_modifiers = Field.class.getDeclaredField("modifiers");
                field_modifiers.setAccessible(true);
            }

            Field var4 = var0.getDeclaredField(var2);
            int var5 = field_modifiers.getInt(var4);

            if ((var5 & 16) != 0)
            {
                field_modifiers.setInt(var4, var5 & -17);
            }

            var4.setAccessible(true);
            var4.set(var1, var3);
        }
        catch (IllegalAccessException var6)
        {
            //logger.throwing("ModLoader", "setPrivateValue", var6);
            //throwException("An impossible error has occured!", var6);
        }
    }

	/*public void setBlocksExplodable(boolean var1)
    {
        try
        {
            int var2;

            if (!var1)
            {
                blockResistance = new float[Block.blocksList.length];

                for (var2 = 0; var2 < Block.blocksList.length; ++var2)
                {
                    if (Block.blocksList[var2] != null)
                    {
                        blockResistance[var2] = Block.blocksList[var2].blockResistance;

                        if (var2 != Block.glass.blockID || !mod_SdkGuns.bulletsDestroyGlass)
                        {
                            Block.blocksList[var2].blockResistance = 6000000.0F;
                        }
                    }
                }
            }
            else if (blockResistance != null)
            {
                for (var2 = 0; var2 < Block.blocksList.length; ++var2)
                {
                    if (Block.blocksList[var2] != null)
                    {
                        Block.blocksList[var2].blockResistance = blockResistance[var2];
                    }
                }
            }

            areBlocksExplodable = var1;
        }
        catch (Exception var3)
        {
            ModLoader.getLogger().throwing("mod_SdkFps", "setBlocksExplodable", var3);
            SdkTools.ThrowException(String.format("Error setting blocks explodable: %b.", new Object[] {Boolean.valueOf(var1)}), var3);
        }
    }*/
    
    
	
}
