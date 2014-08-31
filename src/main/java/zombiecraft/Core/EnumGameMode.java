package zombiecraft.Core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumGameMode 
{
	CLOSED, OPEN;
	
	private static final Map<Integer, EnumGameMode> lookup = new HashMap<Integer, EnumGameMode>();
    static { for(EnumGameMode e : EnumSet.allOf(EnumGameMode.class)) { lookup.put(e.ordinal(), e); } }
    public static EnumGameMode get(int intValue) { return lookup.get(intValue); }
}
