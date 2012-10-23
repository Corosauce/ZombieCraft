package zombiecraft.Core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumAmmo 
{
	PISTOL, SHELL, AUTORIFLE, RIFLE, FUEL, ROCKET;
	
	private static final Map<Integer, EnumAmmo> lookup = new HashMap<Integer, EnumAmmo>();
    static { for(EnumAmmo e : EnumSet.allOf(EnumAmmo.class)) { lookup.put(e.ordinal(), e); } }
    public static EnumAmmo get(int intValue) { return lookup.get(intValue); }
}
