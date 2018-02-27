package fr.slaynash.creatibox.client.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import fr.slaynash.communication.rudp.ClientManager;
import fr.slaynash.communication.rudp.RUDPClient;
import fr.slaynash.creatibox.client.UserData;
import fr.slaynash.creatibox.client.multiplayer.Player;
import fr.slaynash.creatibox.client.pages.MenuPage;
import fr.slaynash.creatibox.common.BytesUtils;
import fr.slaynash.creatibox.common.GameValues;
import fr.slaynash.creatibox.common.LogTimer;
import fr.slaynash.creatibox.common.entities.Entity;
import fr.slaynash.creatibox.common.entities.EntityManager;
import slaynash.sgengine.Configuration;
import slaynash.sgengine.audio.AudioManager;
import slaynash.sgengine.utils.SceneManager;
import slaynash.text.utils.Localization;

public class ConnectionManager extends ClientManager{
	
	public static final int DISCONNECTED		= 0;
	public static final int CONNECTING_STARTED	= 1;
	public static final int COMMUNICATION_OPEN	= 2;
	public static final int VERSION_CHECK		= 3;
	public static final int VERSION_VALIDATED	= 4;
	public static final int UUIDTOKEN_CHECK		= 5;
	public static final int CONNECTED			= 6;
	
	private static int state = DISCONNECTED;
	private List<Player> players = Collections.synchronizedList(new ArrayList<Player>());
	private static String disconnectMessage;
	private static ConnectionManager instance;
	
	public ConnectionManager(RUDPClient rudpClient) {
		super(rudpClient);
		instance = this;
	}
	private static ConnectionStateHandler connectionStateHandler = null;
	
	
	public static void connectASync(final ConnectionStateHandler handler){
		connectionStateHandler = handler;
		Thread connectThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					
					/* Lan autoconnect (currently for tests)
					DatagramSocket datagramSocket = new DatagramSocket();
					
					Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
				    while (en.hasMoreElements()) {
				    	NetworkInterface ni = en.nextElement();
						
						List<InterfaceAddress> list = ni.getInterfaceAddresses();
						Iterator<InterfaceAddress> it = list.iterator();
						
						while (it.hasNext()) {
							InterfaceAddress ia = it.next();
							InetAddress bc = ia.getBroadcast();
							if(bc != null && !bc.isLoopbackAddress()){
								System.out.println("Sending request to "+bc.getHostAddress()+" via "+ni.getDisplayName());
								byte[] packet = new byte[]{(byte)1, Values.commands.HANDSHAKE_START, 0, 0, 0, 0, 0, 0, 0, 0};//Faking connection to get an error as answer
								
								datagramSocket.send(new DatagramPacket(packet, packet.length, bc, GameValues.SERVER_GAME_PORT));
							}
						}
				    }
				    
				    datagramSocket.setSoTimeout(5000);
				    
				    byte[] buffer = new byte[1024];
				    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				    
				    try{
				    	datagramSocket.receive(packet);
				    }catch(SocketTimeoutException e){
						handler.handleConnectException(new Exception("No server found"));
						datagramSocket.close();
						return;
				    };
					datagramSocket.close();
					connect(packet.getAddress());
					*/
					connect(InetAddress.getByName(GameValues.serverAddress));
				} catch (IOException e) {
					updateState(DISCONNECTED);
					handler.handleConnectException(e);
				}
				//handler.connectionFinished();
				System.out.println(LogTimer.getTimeForLog()+"[ConnectionManager] Game connection thread finished");
			}
		}, "GameConnectThread");
		connectThread.setDaemon(true);
		connectThread.start();
	}
	

	
	private static void connect(InetAddress address) throws IOException{
		updateState(CONNECTING_STARTED);
		if(instance != null && instance.rudp.state != fr.slaynash.communication.rudp.Values.connectionStates.STATE_DISCONNECTED){
			throw new IOException(Localization.getTranslation("BAD_DISCONNECTION"));
		}
		instance = new ConnectionManager(new RUDPClient(address, GameValues.SERVER_GAME_PORT));
		instance.rudp.setPacketHandler(instance);
		
		instance.rudp.connect();
		updateState(COMMUNICATION_OPEN);
		
		byte[] version = GameValues.GAME_VERSION.getBytes("UTF-8");
		byte[] versionPacket = new byte[version.length+1];
		System.arraycopy(version, 0, versionPacket, 1, version.length);
		versionPacket[0] = GameValues.packets.CONNECT_GAME_VERSION;
		updateState(VERSION_CHECK);
		instance.rudp.sendReliablePacket(versionPacket);
	}
	
	
	
	

	@Override
	public void initializeClient() {}

	@Override
	public void onDisconnected(String reason) {
		disconnectMessage = Localization.getTranslation(reason);
		updateState(DISCONNECTED);
		synchronized (players) {
			players.clear();
		}
	}

	@Override
	public void handlePacket(byte[] data) {
		if(data[1] == GameValues.packets.PLAYER_PC_POSITION_UPDATE){
			int packetPlayerUUID = BytesUtils.toInt(new byte[]{data[2], data[3], data[4], data[5]});
			synchronized (players) {
				for(Player player:players) if(player.uuid == packetPlayerUUID){
					player.handlePostionPacket(data);
					break;
				}
			}
		}
		else if(data[1] == GameValues.packets.PLAYER_PC_ROTATION_UPDATE){
			int packetPlayerUUID = BytesUtils.toInt(new byte[]{data[2], data[3], data[4], data[5]});
			synchronized (players) {
				for(Player player:players) if(player.uuid == packetPlayerUUID){
					player.handleRotationPacket(data);
					break;
				}
			}
		}
		if(data[1] == GameValues.packets.PLAYER_VR_POSITION_UPDATE){
			int packetPlayerUUID = BytesUtils.toInt(new byte[]{data[2], data[3], data[4], data[5]});
			synchronized (players) {
				for(Player player:players) if(player.uuid == packetPlayerUUID){
					player.handlePostionPacket(data);
					break;
				}
			}
		}
		else if(data[1] == GameValues.packets.PLAYER_VR_ROTATION_UPDATE){
			int packetPlayerUUID = BytesUtils.toInt(new byte[]{data[2], data[3], data[4], data[5]});
			synchronized (players) {
				for(Player player:players) if(player.uuid == packetPlayerUUID){
					player.handleRotationPacket(data);
					break;
				}
			}
		}
		else if(data[1] == GameValues.packets.PLAYER_VR_CONTROLLER_UPDATE){
			int packetPlayerUUID = BytesUtils.toInt(new byte[]{data[2], data[3], data[4], data[5]});
			synchronized (players) {
				for(Player player:players) if(player.uuid == packetPlayerUUID){
					player.handleControllerPacket(data);
					break;
				}
			}
		}
		else if(data[1] == GameValues.packets.ENTITY_UPDATE){
			int entityID = BytesUtils.toInt(new byte[]{data[2], data[3], data[4], data[5]});
			
			float posX = BytesUtils.toFloat(new byte[]{data[6], data[7], data[8], data[9]});
			float posY = BytesUtils.toFloat(new byte[]{data[10], data[11], data[12], data[13]});
			float posZ = BytesUtils.toFloat(new byte[]{data[14], data[15], data[16], data[17]});

			float rotX = BytesUtils.toFloat(new byte[]{data[18], data[19], data[20], data[21]});
			float rotY = BytesUtils.toFloat(new byte[]{data[22], data[23], data[24], data[25]});
			float rotZ = BytesUtils.toFloat(new byte[]{data[26], data[27], data[28], data[29]});
			
			Entity entity = EntityManager.getEntity(entityID);
			entity.setPosition(posX, posY, posZ);
			entity.setRotation(rotX, rotY, rotZ);
		}
	}

	@Override
	public void handleReliablePacket(byte[] data, long sendNS) {
		if(data[0] == GameValues.packets.VALIDATE && state == VERSION_CHECK){
			updateState(VERSION_VALIDATED);
			byte[] ps = ((Configuration.isVR()?GameValues.gamemodeIdentifier.GAMEMODE_VR:GameValues.gamemodeIdentifier.GAMEMODE_PC)+UserData.playername).getBytes(StandardCharsets.UTF_8);
			byte[] packet = new byte[ps.length+5];
			System.arraycopy(ps, 0, packet, 5, ps.length);
			packet[0] = GameValues.packets.CONNECT_UUID_AND_MODE;
			byte[] id = BytesUtils.toBytes(UserData.uuid);
			System.out.println(LogTimer.getTimeForLog()+"[ConnectionManager] User ID"+UserData.uuid+" connected");
			packet[1] = id[0];
			packet[2] = id[1];
			packet[3] = id[2];
			packet[4] = id[3];
			updateState(UUIDTOKEN_CHECK);
			instance.rudp.sendReliablePacket(packet);
		}
		else if(data[0] == GameValues.packets.CONNECT_ADD_PLAYER || data[0] == GameValues.packets.PLAYING_ADD_PLAYER){
			synchronized(players) {
				players.add(new Player(data));
			}
		}
		else if(data[0] == GameValues.packets.CONNECT_GAME_INFOS){
			rudp.sendReliablePacket(new byte[]{GameValues.packets.CONNECT_FINAL_VALIDATE});
			updateState(CONNECTED);
		}
		else if(data[0] == GameValues.packets.PLAYER_DISCONNECTED_EVENT){
			Player playerToRemove = null;
			int playeruuid = BytesUtils.toInt(new byte[]{data[1], data[2], data[3], data[4]});
			String reason = new String(data, 5, data.length-5);
			synchronized(players) {
				for(Player player:players) if(player.uuid == playeruuid){
					player.onDisconnect(reason);
					playerToRemove = player;
					break;
				}
				players.remove(playerToRemove);
			}
		}
		else if(data[0] == GameValues.packets.PLAYER_VR_CONTROLLER_STATE_UPDATE){
			
			int playeruuid = BytesUtils.toInt(new byte[]{data[1], data[2], data[3], data[4]});
			
			int controllerId = BytesUtils.toInt(new byte[]{data[5], data[6], data[7], data[8]});

			synchronized (players) {
				for(Player player:players) if(player.uuid == playeruuid){
					player.setControllerState(controllerId, data[9] == (byte)1);
				}
			}
		}
		else if(data[0] == GameValues.packets.PLAYER_WARP){
			
			int posx = BytesUtils.toInt(new byte[]{data[1], data[2], data[3], data[4]});
			int posy = BytesUtils.toInt(new byte[]{data[5], data[6], data[7], data[8]});
			int posz = BytesUtils.toInt(new byte[]{data[9], data[10], data[11], data[12]});
			
			Configuration.getPlayerCharacter().warp(new Vector3f(posx, posy, posz));
			System.out.println("Warped to "+posx+";"+posy+";"+posz);
		}
		
		else if(data[0] == GameValues.packets.CREATE_ENTITY){
			int id = BytesUtils.toInt  (new byte[]{data[1], data[2], data[3], data[4]});
			float posX = BytesUtils.toFloat(new byte[]{data[5], data[6], data[7], data[8]});
			float posY = BytesUtils.toFloat(new byte[]{data[9], data[10], data[11], data[12]});
			float posZ = BytesUtils.toFloat(new byte[]{data[13], data[14], data[15], data[16]});

			float rotX = BytesUtils.toFloat(new byte[]{data[17], data[18], data[19], data[20]});
			float rotY = BytesUtils.toFloat(new byte[]{data[21], data[22], data[23], data[24]});
			float rotZ = BytesUtils.toFloat(new byte[]{data[25], data[26], data[27], data[28]});
			byte[] name = new byte[data.length-29];
			for(int i=0;i<name.length;i++) name[i] = data[i+29];
			EntityManager.createEntity(id, new String(name, StandardCharsets.UTF_8), posX, posY, posZ, rotX, rotY, rotZ);
			AudioManager.playSoundQuick("/res/sounds/pop.wav", posX, posY, posZ);
		}
		else if(data[0] == GameValues.packets.ADD_ENTITY_ONCONNECT){
			int id = BytesUtils.toInt  (new byte[]{data[1], data[2], data[3], data[4]});
			float posX = BytesUtils.toFloat(new byte[]{data[5], data[6], data[7], data[8]});
			float posY = BytesUtils.toFloat(new byte[]{data[9], data[10], data[11], data[12]});
			float posZ = BytesUtils.toFloat(new byte[]{data[13], data[14], data[15], data[16]});

			float rotX = BytesUtils.toFloat(new byte[]{data[17], data[18], data[19], data[20]});
			float rotY = BytesUtils.toFloat(new byte[]{data[21], data[22], data[23], data[24]});
			float rotZ = BytesUtils.toFloat(new byte[]{data[25], data[26], data[27], data[28]});
			byte[] name = new byte[data.length-29];
			for(int i=0;i<name.length;i++) name[i] = data[i+29];
			EntityManager.createEntity(id, new String(name, StandardCharsets.UTF_8), posX, posY, posZ, rotX, rotY, rotZ);
		}
		else if(data[0] == GameValues.packets.ENTITY_DESTROY){
			int entityId = BytesUtils.toInt(new byte[]{data[1], data[2], data[3], data[4]});
			EntityManager.removeEntity(entityId);
		}
	}
	
	public void disconnect(String reason){
		super.disconnect(reason);
		disconnectMessage = reason;
		updateState(DISCONNECTED);
	}

	public static void disconnectInternal(String reason){
		if(instance != null) instance.disconnect(reason);
		SceneManager.changeScene(MenuPage.class);
	}

	public static String getDisconnectMessage() {
		return disconnectMessage;
	}
	
	private static void updateState(int state){
		System.out.println(LogTimer.getTimeForLog()+"[ConnectionManager] Connection state: "+state);
		ConnectionManager.state = state;
		if(connectionStateHandler != null){
			connectionStateHandler.connectionStateChanged(state);
			if(state == CONNECTED){
				connectionStateHandler.connectionFinished();
				connectionStateHandler = null;//reset handler
			}
		}
	}
	
	public static int getState() {
		return state;
	}
	
	public static void setConnectionStateHandler(ConnectionStateHandler handler) {
		connectionStateHandler = handler;
	}
	
	public static RUDPClient getRudpClient(){
		return instance.rudp;
	}
	
	public static synchronized List<Player> getPlayers(){
		return instance.players;
	}
	
}
