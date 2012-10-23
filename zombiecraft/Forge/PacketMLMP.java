package zombiecraft.Forge;

public class PacketMLMP {

	public int packetType;
	public int[] dataInt;
	public String[] dataString;
	
	public PacketMLMP() {
		dataInt = new int[20];
		dataString = new String[10];
	}
}
