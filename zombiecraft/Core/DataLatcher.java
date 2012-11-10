package zombiecraft.Core;

import java.util.HashMap;

import net.minecraft.src.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class DataLatcher {
	
	public HashMap values;
	
	public DataLatcher() {
		values = new HashMap();
		
		//Defaulting values so you dont need to nullcheck
		values.put(DataTypes.purchaseState, 0);
		values.put(DataTypes.purchaseTimeout, 0);
		values.put(DataTypes.purchaseItemIndex, 0);
		values.put(DataTypes.lastPoints, 0);
		values.put(DataTypes.zcPoints, 0);
		values.put(DataTypes.hasCharge, 0);
		values.put(DataTypes.ammoAmounts, new AmmoDataLatcher());
		
	}
	
	/**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	try {
    		
    		Iterator it = values.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        Object val = pairs.getValue();
		        if (val instanceof AmmoDataLatcher) {
		        	//System.out.println("running AmmoDataLatcher reader");
		        	AmmoDataLatcher adl = new AmmoDataLatcher();
		        	NBTTagCompound adlNBT = par1NBTTagCompound.getCompoundTag("AmmoDataLatcher");
		        	adl.readFromNBT(adlNBT);
		        	values.put(DataTypes.ammoAmounts, adl);
		        } else {
		        	values.put(pairs.getKey(), pairs.getValue());
		        }
		        
		        
		    }
    		
	    	/*for (DataTypes dt : DataTypes.values()) {
	    		System.out.println("dt.toString(): " + dt.toString());
	    		if (dt.equals(DataTypes.ammoAmounts)) {
	    			System.out.println("running AmmoDataLatcher reader");
	    			NBTTagCompound adl = par1NBTTagCompound.getCompoundTag("AmmoDataLatcher");
	    			((AmmoDataLatcher)values.get(DataTypes.ammoAmounts)).readFromNBT(adl);
	    		} else {
	    			//Integer!
	    			values.put(dt, par1NBTTagCompound.getInteger(dt.toString()));
	    		}
	    	}*/
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
		
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	try { 
    		
    		Iterator it = values.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        Object val = pairs.getValue();
		        if (val instanceof AmmoDataLatcher) {
		        	//System.out.println("running AmmoDataLatcher writer");
	    			NBTTagCompound adl = new NBTTagCompound();
	    			((AmmoDataLatcher)values.get(DataTypes.ammoAmounts)).writeToNBT(adl);
	    			//System.out.println("writing AmmoDataLatcher compoundtag");
	    			par1NBTTagCompound.setCompoundTag("AmmoDataLatcher", adl);
		        	/*AmmoDataLatcher adl = new AmmoDataLatcher();
		        	NBTTagCompound adlNBT = par1NBTTagCompound.getCompoundTag("AmmoDataLatcher");
		        	adl.readFromNBT(adlNBT);
		        	values.put(DataTypes.ammoAmounts, adl);*/
		        } else {
		        	par1NBTTagCompound.setInteger(pairs.getKey().toString(), (Integer)pairs.getValue());
		        	//values.put(pairs.getKey(), pairs.getValue());
		        }
		        
		        
		    }
    		
    		
	    	/*for (DataTypes dt : DataTypes.values()) {
	    		System.out.println("dt.toString(): " + dt.toString());
	    		if (dt.equals(DataTypes.ammoAmounts)) {
	    			System.out.println("running AmmoDataLatcher writer");
	    			NBTTagCompound adl = new NBTTagCompound();
	    			((AmmoDataLatcher)values.get(DataTypes.ammoAmounts)).writeToNBT(adl);
	    			System.out.println("writing AmmoDataLatcher compoundtag");
	    			par1NBTTagCompound.setCompoundTag("AmmoDataLatcher", adl);
	    			//par1NBTTagCompound.getCompoundTag("AmmoDataLatcher");
	    			
	    		} else {
	    			//Integer!
	    			par1NBTTagCompound.setInteger(dt.toString(), dt.)
	    			values.put(dt, par1NBTTagCompound.getInteger(dt.toString()));
	    		}
	    	}*/
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
}
