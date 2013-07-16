package zombiecraft.Core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AmmoDataLatcher {
	
	public HashMap values;
	
	public AmmoDataLatcher() {
		values = new HashMap();
		
		//Defaulting values so you dont need to nullcheck
		//values.put(DataTypes.ammo, 0);
		
	}
	
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
		
		try { 
			
			Collection playerDataCl = par1NBTTagCompound.getTags();
			Iterator it = playerDataCl.iterator();
			
			while (it.hasNext()) {
				
				NBTTagInt var16 = (NBTTagInt)it.next();
				
				values.put(Integer.valueOf(var16.getName()), var16.data);
		        
		        //System.out.println("reading: " + var16.getName());
				
			}
			
			
			
			/*Iterator it = values.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        values.put((Integer)pairs.getKey(), par1NBTTagCompound.getInteger(pairs.getKey().toString()));
		        
		        System.out.println("reading: " + pairs.getKey());
		    }*/
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
		
		/*try { 
	    	for (DataTypes dt : DataTypes.values()) {
	    		System.out.println("dt.toString(): " + dt.toString());
	    		values.put(dt, par1NBTTagCompound.getInteger(dt.toString()));
	    	}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}*/
    }
	
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
		
		Iterator it = values.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        
	        par1NBTTagCompound.setInteger(pairs.getKey().toString(), (Integer)pairs.getValue());
	        
	        //System.out.println("writing: " + pairs.getKey() + " - " + pairs.getValue());
	        
	        //values.put(pairs.getKey(), par1NBTTagCompound.getInteger(dt.toString()));
	        //dataInt[index++] = (Integer)pairs.getKey();
	        //dataInt[index++] = (Integer)pairs.getValue();
	    }
		
		/*try { 
	    	for (DataTypes dt : DataTypes.values()) {
	    		System.out.println("dt.toString(): " + dt.toString());
	    		values.put(dt, par1NBTTagCompound.getInteger(dt.toString()));
	    	}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}*/
    }
}
