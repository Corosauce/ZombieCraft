package zombiecraft.Client.gui.scrollable;

import zombiecraft.Core.ZombieSaveRecord;

public class MapEntry implements IScrollingElement {

	public ZombieSaveRecord entry;
	
	public MapEntry(ZombieSaveRecord parEntry) {
		entry = parEntry;
	}

	@Override
	public String getTitle() {
		return entry.text;
	}

	@Override
	public String getExtraInfo() {
		return entry.extraInfo;
	}
	
}
