package zombiecraft.Forge;

import zombiecraft.Core.DataTypes;
import zombiecraft.Core.ZombieSaveRecord;
import zombiecraft.Core.Blocks.TileEntityPurchasePlate;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.World.LevelConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import CoroUtil.packet.PacketHelper;
import CoroUtil.util.CoroUtilEntity;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EventHandlerPacket {
	
	//1.6.4 to 1.7.x, copying over as is BUT with reading of string packetCommand first, and expecting packet making part to add in that string
	
	public static int CMD_GAMESETUPJOINLEAVE = 0;
	public static int CMD_GAMESTART = 1;
	public static int CMD_TELEPORT = 2;
	public static int CMD_MAPSETNAME = 3;
	public static int CMD_MAPGENERATE = 4;
	
	@SubscribeEvent
	public void onPacketFromServer(FMLNetworkEvent.ClientCustomPacketEvent event) {
		
		try {
			//NBTTagCompound nbt = PacketHelper.readNBTTagCompound(event.packet.payload());
			
			ByteBuf dis = event.packet.payload();
			
			String packetCommand = ByteBufUtils.readUTF8String(event.packet.payload());//nbt.getString("packetCommand");
			
			System.out.println("zombiecraft packet command from server: " + packetCommand);
			
			if (packetCommand.equals("MLMP")) {
	        	
	        	PacketMLMP packetMLMP = new PacketMLMP();
	        	
	        	packetMLMP.packetType = dis.readInt();
	        	
	        	//System.out.println("packetType: " + packetMLMP.packetType);
	        	
        		for (int i = 0; i < 20; i++) {
        			
        			packetMLMP.dataInt[i] = dis.readInt();
        			//System.out.println(i + " - " + packetMLMP.dataInt[i]);
        		}
        		try {
        			packetMLMP.dataString[0] = ByteBufUtils.readUTF8String(dis);
        			//System.out.println("level string: " + packetMLMP.dataString[0]);
        		}
    			catch (Exception ex)
    	        {
    				//ex.printStackTrace();
    				//System.out.println(packetMLMP.packetType);
    	        	//System.out.println("no string error");
    	        }
        		
        		ZCClientTicks.zcGame.handlePacket(getPlayerClient(), packetMLMP);
				
			} else if (packetCommand.equals("TileEnt")) {
				int pt = dis.readInt();
	        	
	        	int x = dis.readInt();
	        	int y = dis.readInt();
	        	int z = dis.readInt();
	        	
	        	TileEntity tEnt = ZCGame.instance().getTileEntity(x, y, z);
	        	
        		//System.out.println("tEnt " + tEnt);
	    		if (tEnt != null && tEnt instanceof TileEntityPurchasePlate) {
	    			((TileEntityPurchasePlate)tEnt).itemIndex = dis.readInt();
	    		}
	        	
			} else if (packetCommand.equals("Session")) {
				NBTTagCompound nbt = PacketHelper.readNBTTagCompound(dis);//Packet.readNBTTagCompound(dis);
	        	
	        	ZCGame.nbtInfoClientSession = nbt;
	        	
			} else if (packetCommand.equals("MapConfig")) {
				NBTTagCompound nbt = PacketHelper.readNBTTagCompound(dis);//Packet.readNBTTagCompound(dis);
	        	
	        	LevelConfig.handleServerSentNBTMapConfig(nbt);
	        	
	        }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@SideOnly(Side.CLIENT)
	public EntityPlayer getPlayerClient() {
		return FMLClientHandler.instance().getClient().thePlayer;
	}
	
	@SubscribeEvent
	public void onPacketFromClient(FMLNetworkEvent.ServerCustomPacketEvent event) {
		EntityPlayerMP entP = ((NetHandlerPlayServer)event.handler).playerEntity;
		
		try {
			//NBTTagCompound nbt = PacketHelper.readNBTTagCompound(event.packet.payload());
			
			ByteBuf dis = event.packet.payload();
			
			String packetCommand = ByteBufUtils.readUTF8String(event.packet.payload());//nbt.getString("packetCommand");
			
			ZombieCraftMod.dbg("zombiecraft packet command from client: " + packetCommand);
			
			if (packetCommand.equals("MLMP")) {
				
				PacketMLMP packetMLMP = new PacketMLMP();
	        	
	        	packetMLMP.packetType = dis.readInt();
	        	
	        	//System.out.println("packetType: " + packetMLMP.packetType);
	        	
        		for (int i = 0; i < 20; i++) {
        			
        			packetMLMP.dataInt[i] = dis.readInt();
        			//System.out.println(i + " - " + packetMLMP.dataInt[i]);
        		}
        		try {
        			packetMLMP.dataString[0] = ByteBufUtils.readUTF8String(dis);
        			//System.out.println("level string: " + packetMLMP.dataString[0]);
        		}
    			catch (Exception ex)
    	        {
    				//ex.printStackTrace();
    				//System.out.println(packetMLMP.packetType);
    	        	//System.out.println("no string error");
    	        }
        		
        		ZCServerTicks.zcGame.handlePacket(entP, packetMLMP);
			} else if (packetCommand.equals("Session")) {
				NBTTagCompound nbt = PacketHelper.readNBTTagCompound(dis);//Packet.readNBTTagCompound(dis);
				
				//ANTI HACK SAFETY USERNAME FORCE OVERRIDE
    			nbt.setString("username", CoroUtilEntity.getName(entP));
    			
				handleClientSentNBTSession(((EntityPlayer)entP).worldObj, nbt);
	        	
			} else if (packetCommand.equals("MapConfig")) {
				NBTTagCompound nbt = PacketHelper.readNBTTagCompound(dis);//Packet.readNBTTagCompound(dis);
        		if (entP instanceof EntityPlayer && ZCGame.instance().isOp((EntityPlayer)entP)) { //proper is op check, doesnt use internal username
        			//ANTI HACK SAFETY USERNAME FORCE OVERRIDE
        			nbt.setString("username", CoroUtilEntity.getName(entP));
        			LevelConfig.handleClientSentNBTMapConfig(((EntityPlayer)entP).worldObj, nbt);
	        	}
	        	
	        }
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	public void handleClientSentNBTSession(World world, NBTTagCompound par1nbtTagCompound) {
		
		//move all this to a non tile bound thing once you need the gui to open from anywhere, or will we skip that and just have them warp back to lobby to click block?
		if (par1nbtTagCompound.hasKey("sync")) {
			sync();
		} else if (par1nbtTagCompound.hasKey("cmdID")) {
			int cmdID = par1nbtTagCompound.getInteger("cmdID");
			EntityPlayerMP entP = (EntityPlayerMP)world.getPlayerEntityByName(par1nbtTagCompound.getString("username"));
			
			if (cmdID == CMD_GAMESETUPJOINLEAVE) {
				if (ZCGame.instance().gameActive) {
					//join game - which is basically just teleporting, itll auto pull them in next round, add an easy way to spectate!
					if (entP != null) {
						ZombieCraftMod.teleportPlayerToggle(entP);
					}
				} else {
					if (ZCGame.instance().lobbyActive) {
						//if leader, close lobby, else join lobby
						if (ZCGame.instance().lobbyLeader.equals(par1nbtTagCompound.getString("username"))) {
							ZCGame.instance().lobbyActive = false;
							ZCGame.instance().lobbyLeader = "";
						} else {
							if (entP != null && entP.dimension != ZCGame.ZCDimensionID) {
								ZombieCraftMod.teleportPlayerToggle(entP);
							}
						}
					} else {
						//start lobby as leader
						ZCGame.instance().lobbyActive = true;
						ZCGame.instance().lobbyLeader = par1nbtTagCompound.getString("username");
						if (entP != null && entP.dimension != ZCGame.ZCDimensionID) {
							ZombieCraftMod.teleportPlayerToggle(entP);
						} else {
							ZCGame.instance().zcLevel.newGame(); //apparently just scans for players
						}
					}
					
					sync();
				}
				
				//if (nbtInfoServer.getBoolean("lobbyActive"))
			} else if (cmdID == CMD_GAMESTART) {
				if (ZCGame.instance().lobbyLeader.equals(par1nbtTagCompound.getString("username"))) {
					if (!ZCGame.instance().gameActive) {
						ZCGame.instance().wMan.prepGame();
						ZCGame.instance().wMan.waitingToStart = true; //should make the game start once it generates
					} else {
						ZCGame.instance().wMan.stopGame();
					}
					ZCGame.instance().handleSyncFixing(true);
				} else {
					//hacking attempt or bug
				}
			} else if (cmdID == CMD_TELEPORT) {
				if (entP != null) {
					ZombieCraftMod.teleportPlayerToggle(entP);
				}
			} else if (cmdID == CMD_MAPSETNAME) {
				if (entP != null) {
					String mapName = par1nbtTagCompound.getString("mapName");
					if (ZCGame.instance().lobbyLeader.equals(par1nbtTagCompound.getString("username")) && mapName.length() > 0) {
						ZCGame.instance().setMapName(entP, par1nbtTagCompound.getString("mapName"), true);
						ZCGame.instance().mapMan.loadLevel();
					}
				}
				ZCGame.instance().handleSyncFixing(true);
			} else if (cmdID == CMD_MAPGENERATE) {
				if (entP != null && ZCGame.instance().lobbyLeader.equals(par1nbtTagCompound.getString("username"))) {
					//ZCGame.instance().movePlayersToLobby();
					ZCGame.instance().mapMan.loadLevel();
		    		ZCGame.instance().mapMan.buildStart(entP);
		    		ZCGame.instance().trySetTexturePack(ZCGame.instance().zcLevel.texturePack);
				}
			}
		}
		
		/*if (entInt == null && par1nbtTagCompound.hasKey("buildStart")) {
			buildStart();
			sync();
		}*/
		
		//technically i should be doing some sanity checking here
		//readFromNBTPacket(par1nbtTagCompound);
		
		//System.out.println("handled client send data to " + this);
	}
	
	public void updateServerNBTForSync() {
		ZCGame zcg = ZCGame.instance();
		zcg.nbtInfoServerSession.setBoolean("lobbyActive", zcg.lobbyActive);
		//nbtInfoServer.setBoolean("gameActive", zcg.gameActive);
		zcg.nbtInfoServerSession.setString("lobbyLeader", zcg.lobbyLeader);
		//ok, try to use the existing packet methods for setting map name etc, lets not reinvent everything and cause potential weird desyncs....
		//we are only reading game active setting, not setting it here
		//nbtInfoServer.
		
		zcg.nbtInfoServerSession.setInteger("lobbyPlayerCount", zcg.zcLevel.playersInGame.size());
		for (int i = 0; i < zcg.zcLevel.playersInGame.size(); i++) {
			EntityPlayer entP = zcg.zcLevel.playersInGame.get(i);
			
			zcg.nbtInfoServerSession.setString("lobbyPlayerName_" + i, CoroUtilEntity.getName(entP));
			zcg.nbtInfoServerSession.setInteger("lobbyPlayerScore_" + i, (Integer)zcg.getData(entP, DataTypes.zcPointsTotal)); //use for active player info, unless this code gets repurposed for handling both?
		}
		
		zcg.loadMapList();
		zcg.nbtInfoServerSession.setInteger("mapCount", zcg.listMaps.size());
		for (int i = 0; i < zcg.listMaps.size(); i++) {
			ZombieSaveRecord save = zcg.listMaps.get(i);
			
			zcg.nbtInfoServerSession.setString("mapName_" + i, save.getText());
			//nbtInfoServer.setInteger("lobbyPlayerScore_" + i, (Integer)zcg.getData(entP, DataTypes.zcPointsTotal)); //use for active player info, unless this code gets repurposed for handling both?
		}
		
	}
    
    @SideOnly(Side.CLIENT)
    public String getSelfUsername() {
    	return CoroUtilEntity.getName(Minecraft.getMinecraft().thePlayer);
    }
    
    public void sync() {
    	updateServerNBTForSync();
    	ZombieCraftMod.eventChannel.sendToAll(PacketHelper.createPacketForNBTHandler("Session", ZombieCraftMod.eventChannelName, ZCGame.instance().nbtInfoServerSession));
    	//MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(PacketHelper.createPacketForNBTHandler("Session", ZombieCraftMod.eventChannelName, ZCGame.instance().nbtInfoServerSession));
    }
	
}
