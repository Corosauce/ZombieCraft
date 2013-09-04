package zombiecraft.Core.World;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class LevelConfig {

	//Ultimate map config!
	
	//existing stuff to pull over
	//map name
	//map size? wouldnt really be usefull to edit this way
	//time: lock to day/night, or cycle (note, locking means just add time to make day once night hits, dont actually stop world time)
	//dim has sun?
	//lighting rules:
	//- nether lighting?
	//- custom lighting for all 16 layers?
	
	//keep in mind, it would be good if these configs could be used as templates on tiles which can then customize it further for just that tile:
	//- make as a standard class shared between this and tiles for more detailed customizing
	
	//config, current hardcoded game logic that should be considered for config, with defaults of current hardcoded ways for backwards compatibility:
	
	//- wave config:
	//-- customizable down to per wave:
	//--- monsters used
	//--- total wave monster count, i guess this would override overall wave count increase calc
	//--- dont allow for rate or speed change per wave
	//-- rate of difficulty increase: count, rate, speed (i dont think rate increases in default)
	//-- base values: zombie count, rate, speed
	
	//- item prices:
	//-- anything in more detail needed here?
	
	//- mystery box items
	//-- usable to a per tile basis
	
	public List<ItemStack> mysteryBoxItems = new ArrayList<ItemStack>();
	
	public LevelConfig() {
		
	}
}
