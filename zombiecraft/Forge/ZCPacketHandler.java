package zombiecraft.Forge;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import zombiecraft.Core.DataTypes;
import zombiecraft.Core.ZombieSaveRecord;
import zombiecraft.Core.Blocks.TileEntityPurchasePlate;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.World.LevelConfig;
import CoroAI.packet.PacketHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class ZCPacketHandler implements IPacketHandler
{

	public static int CMD_GAMESETUPJOINLEAVE = 0;
	public static int CMD_GAMESTART = 1;
	public static int CMD_TELEPORT = 2;
	public static int CMD_MAPSETNAME = 3;
	public static int CMD_MAPGENERATE = 4;
	
    public ZCPacketHandler()
    {
    }

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
    	
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
    	
    	
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.data));

        try
        {
        
	        if ("Data".equals(packet.channel))
	        {
	            
	            	int what = 0;
	                //float val = dis.readFloat();
	                
	        } else if  ("TileEnt".equals(packet.channel)) {
	            
	        	int pt = dis.readInt();
	        	
	        	int x = dis.readInt();
	        	int y = dis.readInt();
	        	int z = dis.readInt();
	        	
	        	TileEntity tEnt = ZCGame.instance().getTileEntity(x, y, z);
	        	
	        	if (side == Side.SERVER) {
	        		
	        	} else {
	        		//System.out.println("tEnt " + tEnt);
		    		if (tEnt != null && tEnt instanceof TileEntityPurchasePlate) {
		    			((TileEntityPurchasePlate)tEnt).itemIndex = dis.readInt();
		    		}
	        	}
	        	
	        	
	    		
	        	
	        	
	        	//ZCGame.instance().handleTileEntityPacket(x, y, z, 0, var5, var6, var7)handleTileEntityPacket()
	            	
	        } else if  ("Input".equals(packet.channel)) {
	        	if (side == Side.SERVER) {
	        		// We are on the server side.
	        		//EntityPlayerMP entP = (EntityPlayerMP) player;
	        	} else if (side == Side.CLIENT) {
	        		//EntityClientPlayerMP entP = (EntityClientPlayerMP) player;
	        		// We are on the client side.
	        	} else {
	        		// We are on the Bukkit server. Bukkit is a server mod used for security.
	        	}
	        	
	        	EntityPlayer entP = (EntityPlayer) player;
	        	
	        	if (entP != null) {
		        	boolean mouseDown = false;
		        	try {
		        		mouseDown = dis.readBoolean();
		        	} catch (Exception ex) {
		        		ex.printStackTrace();
		        	}
		        	
		        	System.out.println(entP.username + " - " + mouseDown);
	        	}
	        } else if  ("MLMP".equals(packet.channel)) {
	        	
	        	EntityPlayer entP = (EntityPlayer) player;
	        	
	        	PacketMLMP packetMLMP = new PacketMLMP();
	        	
	        	packetMLMP.packetType = dis.readInt();
	        	
	        	//System.out.println("packetType: " + packetMLMP.packetType);
	        	
        		for (int i = 0; i < 20; i++) {
        			
        			packetMLMP.dataInt[i] = dis.readInt();
        			//System.out.println(i + " - " + packetMLMP.dataInt[i]);
        		}
        		try {
        			packetMLMP.dataString[0] = dis.readUTF();
        			//System.out.println("level string: " + packetMLMP.dataString[0]);
        		}
    			catch (Exception ex)
    	        {
    				//ex.printStackTrace();
    				//System.out.println(packetMLMP.packetType);
    	        	//System.out.println("no string error");
    	        }
	        	
	        	if (side == Side.SERVER) {
	        		ZCServerTicks.zcGame.handlePacket(entP, packetMLMP);
	        	} else if (side == Side.CLIENT) {
	        		ZCClientTicks.zcGame.handlePacket(entP, packetMLMP);
	        	}
	        	
	        } else if ("Session".equals(packet.channel)) {
	        	NBTTagCompound nbt = Packet.readNBTTagCompound(dis);
	        	if (side == Side.SERVER) {
	        		if (player instanceof EntityPlayer) {
	        			
	        			//ANTI HACK SAFETY USERNAME FORCE OVERRIDE
	        			nbt.setString("username", ((EntityPlayer)player).username);
	        			
						handleClientSentNBTSession(((EntityPlayer)player).worldObj, nbt);
		        	}
	        	} else if (side == Side.CLIENT) {
	        		ZCGame.nbtInfoClientSession = nbt;
	        	}
	        	
	        } else if (packet.channel.equals("MapConfig")) {
	        	NBTTagCompound nbt = Packet.readNBTTagCompound(dis);
	        	if (side == Side.SERVER) {
	        		if (player instanceof EntityPlayer && ZCGame.instance().isOp((EntityPlayer)player)) { //proper is op check, doesnt use internal username
	        			//ANTI HACK SAFETY USERNAME FORCE OVERRIDE
	        			nbt.setString("username", ((EntityPlayer)player).username);
	        			LevelConfig.handleClientSentNBTMapConfig(((EntityPlayer)player).worldObj, nbt);
		        	}
	        	} else if (side == Side.CLIENT) {
	        		LevelConfig.handleServerSentNBTMapConfig(nbt);
	        	}
	        }
        }
        catch (Exception ex)
        {
        	System.out.println("packet read fail");
            //ex.printStackTrace();
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
			
			zcg.nbtInfoServerSession.setString("lobbyPlayerName_" + i, entP.username);
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
    
    public void sync() {
    	updateServerNBTForSync();
    	MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(getSessionPacket(ZCGame.instance().nbtInfoServerSession));
    }
    
    public static Packet250CustomPayload getNBTPacket(NBTTagCompound data, String channel) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
        	PacketHelper.writeNBTTagCompound(data, dos);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = channel;
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        
        return pkt;
	}
    
    public static Packet250CustomPayload getSessionPacket(NBTTagCompound data) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
        	PacketHelper.writeNBTTagCompound(data, dos);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Session";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        
        return pkt;
	}
}
