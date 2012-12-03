package zombiecraft.Core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumDifficulty 
{
	EASY, MEDIUM, HARD;
	
	private static final Map<Integer, EnumDifficulty> lookup = new HashMap<Integer, EnumDifficulty>();
    static { for(EnumDifficulty e : EnumSet.allOf(EnumDifficulty.class)) { lookup.put(e.ordinal(), e); } }
    public static EnumDifficulty get(int intValue) { return lookup.get(intValue); }
}
