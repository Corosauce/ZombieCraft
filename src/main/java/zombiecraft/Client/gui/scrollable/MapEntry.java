package zombiecraft.Client.gui.scrollable;

import java.util.List;

import CoroUtil.client.IScrollingElement;
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

	@Override
	public List<String> getExtraInfo2() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
