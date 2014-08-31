package zombiecraft.Core.Camera;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumCameraState 
{
	OFF, FOLLOW, FREE, SCRIPT;
	
	private static final Map<Integer, EnumCameraState> lookup = new HashMap<Integer, EnumCameraState>();
    static { for(EnumCameraState e : EnumSet.allOf(EnumCameraState.class)) { lookup.put(e.ordinal(), e); } }
    public static EnumCameraState get(int intValue) { return lookup.get(intValue); }
}
