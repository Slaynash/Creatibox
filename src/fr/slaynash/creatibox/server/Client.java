package fr.slaynash.creatibox.server;

import java.nio.charset.StandardCharsets;

import fr.slaynash.communication.rudp.ClientManager;
import fr.slaynash.communication.rudp.RUDPClient;
import fr.slaynash.creatibox.common.BytesUtils;
import fr.slaynash.creatibox.common.GameValues;
import fr.slaynash.creatibox.common.LogTimer;
import fr.slaynash.creatibox.common.entities.Entity;
import fr.slaynash.creatibox.common.entities.EntityManager;
import fr.slaynash.creatibox.common.entities.EntityProp;

public class Client extends ClientManager{

	private boolean init_version = false;
	private boolean init_auth = false;
	private boolean ready_for_connect = false;
	private boolean connected = false;
	
	int uuid = 0;
	String username = "<unknown>";
	private float posX, posY, posZ;
	private float rotX, rotY, rotZ;
	private String gameMode = "";
	private VRControllerRemote[] controllers = new VRControllerRemote[16];
	
	
	public Client(RUDPClient rudpClient) {
		super(rudpClient);
		for(int i=0;i<controllers.length;i++) controllers[i] = new VRControllerRemote(i);
	}

	@Override
	public void initializeClient() {}

	@Override
	public void onDisconnected(String reason) {
		if(connected){
			ready_for_connect = false;
			connected = false;
			Server.userDisconnected(this, reason);
		}
	}

	@Override
	public void handlePacket(byte[] data) {
		if(data[1] == GameValues.packets.PLAYER_PC_POSITION_UPDATE_FROMCLIENT){
			byte[] packet = new byte[17];
			
			byte[] uuidBytes = BytesUtils.toBytes(uuid);
			
			packet[0] = GameValues.packets.PLAYER_PC_POSITION_UPDATE;
			packet[1] = uuidBytes[0];
			packet[2] = uuidBytes[1];
			packet[3] = uuidBytes[2];
			packet[4] = uuidBytes[3];
			posX = BytesUtils.toFloat(new byte[]{data[2], data[3], data[4], data[5]});
			packet[5] = data[2];
			packet[6] = data[3];
			packet[7] = data[4];
			packet[8] = data[5];
			posY = BytesUtils.toFloat(new byte[]{data[6], data[7], data[8], data[9]});
			packet[9] = data[6];
			packet[10] = data[7];
			packet[11] = data[8];
			packet[12] = data[9];
			posZ = BytesUtils.toFloat(new byte[]{data[10], data[11], data[12], data[13]});
			packet[13] = data[10];
			packet[14] = data[11];
			packet[15] = data[12];
			packet[16] = data[13];
			
			synchronized (Server.getClients()) {
				for(Client client: Server.getClients()) client.rudp.sendPacket(packet);
			}
		}
		if(data[1] == GameValues.packets.PLAYER_PC_ROTATION_UPDATE_FROMCLIENT){
			byte[] packet = new byte[17];
			
			byte[] uuidBytes = BytesUtils.toBytes(uuid);
			
			packet[0] = GameValues.packets.PLAYER_PC_ROTATION_UPDATE;
			packet[1] = uuidBytes[0];
			packet[2] = uuidBytes[1];
			packet[3] = uuidBytes[2];
			packet[4] = uuidBytes[3];
			rotX = BytesUtils.toFloat(new byte[]{data[2], data[3], data[4], data[5]});
			packet[5] = data[2];
			packet[6] = data[3];
			packet[7] = data[4];
			packet[8] = data[5];
			rotY = BytesUtils.toFloat(new byte[]{data[6], data[7], data[8], data[9]});
			packet[9] = data[6];
			packet[10] = data[7];
			packet[11] = data[8];
			packet[12] = data[9];
			rotZ = BytesUtils.toFloat(new byte[]{data[10], data[11], data[12], data[13]});
			packet[13] = data[10];
			packet[14] = data[11];
			packet[15] = data[12];
			packet[16] = data[13];
			
			synchronized (Server.getClients()) {
				for(Client client: Server.getClients()) client.rudp.sendPacket(packet);
			}
		}
		if(data[1] == GameValues.packets.PLAYER_VR_POSITION_UPDATE_FROMCLIENT){
			byte[] packet = new byte[17];
			
			byte[] uuidBytes = BytesUtils.toBytes(uuid);
			
			packet[0] = GameValues.packets.PLAYER_VR_POSITION_UPDATE;
			packet[1] = uuidBytes[0];
			packet[2] = uuidBytes[1];
			packet[3] = uuidBytes[2];
			packet[4] = uuidBytes[3];
			posX = BytesUtils.toFloat(new byte[]{data[2], data[3], data[4], data[5]});
			packet[5] = data[2];
			packet[6] = data[3];
			packet[7] = data[4];
			packet[8] = data[5];
			posY = BytesUtils.toFloat(new byte[]{data[6], data[7], data[8], data[9]});
			packet[9] = data[6];
			packet[10] = data[7];
			packet[11] = data[8];
			packet[12] = data[9];
			posZ = BytesUtils.toFloat(new byte[]{data[10], data[11], data[12], data[13]});
			packet[13] = data[10];
			packet[14] = data[11];
			packet[15] = data[12];
			packet[16] = data[13];
			
			synchronized (Server.getClients()) {
				for(Client client: Server.getClients()) client.rudp.sendPacket(packet);
			}
		}
		if(data[1] == GameValues.packets.PLAYER_VR_ROTATION_UPDATE_FROMCLIENT){
			byte[] packet = new byte[17];
			
			byte[] uuidBytes = BytesUtils.toBytes(uuid);
			
			packet[0] = GameValues.packets.PLAYER_VR_ROTATION_UPDATE;
			packet[1] = uuidBytes[0];
			packet[2] = uuidBytes[1];
			packet[3] = uuidBytes[2];
			packet[4] = uuidBytes[3];
			rotX = BytesUtils.toFloat(new byte[]{data[2], data[3], data[4], data[5]});
			packet[5] = data[2];
			packet[6] = data[3];
			packet[7] = data[4];
			packet[8] = data[5];
			rotY = BytesUtils.toFloat(new byte[]{data[6], data[7], data[8], data[9]});
			packet[9] = data[6];
			packet[10] = data[7];
			packet[11] = data[8];
			packet[12] = data[9];
			rotZ = BytesUtils.toFloat(new byte[]{data[10], data[11], data[12], data[13]});
			packet[13] = data[10];
			packet[14] = data[11];
			packet[15] = data[12];
			packet[16] = data[13];
			
			synchronized (Server.getClients()) {
				for(Client client: Server.getClients()) client.rudp.sendPacket(packet);
			}
		}
		if(data[1] == GameValues.packets.PLAYER_VR_CONTROLLER_UPDATE_FROMCLIENT){
			byte[] packet = new byte[33];
			
			byte[] uuidBytes = BytesUtils.toBytes(uuid);
			
			int controllerId = BytesUtils.toInt(new byte[]{data[26], data[27], data[28], data[29]});
			packet[29] = data[26];
			packet[30] = data[27];
			packet[31] = data[28];
			packet[32] = data[29];
			
			packet[0] = GameValues.packets.PLAYER_VR_CONTROLLER_UPDATE;
			packet[1] = uuidBytes[0];
			packet[2] = uuidBytes[1];
			packet[3] = uuidBytes[2];
			packet[4] = uuidBytes[3];
			controllers[controllerId].posX = BytesUtils.toFloat(new byte[]{data[2], data[3], data[4], data[5]});
			packet[5] = data[2];
			packet[6] = data[3];
			packet[7] = data[4];
			packet[8] = data[5];
			controllers[controllerId].posY = BytesUtils.toFloat(new byte[]{data[6], data[7], data[8], data[9]});
			packet[9] = data[6];
			packet[10] = data[7];
			packet[11] = data[8];
			packet[12] = data[9];
			controllers[controllerId].posZ = BytesUtils.toFloat(new byte[]{data[10], data[11], data[12], data[13]});
			packet[13] = data[10];
			packet[14] = data[11];
			packet[15] = data[12];
			packet[16] = data[13];
			
			controllers[controllerId].rotX = BytesUtils.toFloat(new byte[]{data[14], data[15], data[16], data[17]});
			packet[17] = data[14];
			packet[18] = data[15];
			packet[19] = data[16];
			packet[20] = data[17];
			controllers[controllerId].rotY = BytesUtils.toFloat(new byte[]{data[18], data[19], data[20], data[21]});
			packet[21] = data[18];
			packet[22] = data[19];
			packet[23] = data[20];
			packet[24] = data[21];
			controllers[controllerId].rotZ = BytesUtils.toFloat(new byte[]{data[22], data[23], data[24], data[25]});
			packet[25] = data[22];
			packet[26] = data[23];
			packet[27] = data[24];
			packet[28] = data[25];
			
			synchronized (Server.getClients()) {
				for(Client client: Server.getClients()) client.rudp.sendPacket(packet);
			}
		}
	}

	@Override
	public void handleReliablePacket(byte[] data, long sendNS) {//cmd, version
		//System.out.println("RELIABLE PACKET");
		if(data[0] == GameValues.packets.CONNECT_GAME_VERSION){
			System.out.println(LogTimer.getTimeForLog()+"[Client] CONNECT_GAME_VERSION");
			byte[] clientVersionB = new byte[data.length-1];
			System.arraycopy(data, 1, clientVersionB, 0, clientVersionB.length-1);
			String clientVersion = "";
			clientVersion = new String(clientVersionB, StandardCharsets.UTF_8);
			if(clientVersion.equals(GameValues.GAME_VERSION)){
				rudp.disconnect(GameValues.disconnectError.DISCONNECTED_BAD_VERSION);
				return;
			}
			init_version = true;
			rudp.sendReliablePacket(new byte[]{GameValues.packets.VALIDATE});
		}
		else if(data[0] == GameValues.packets.CONNECT_UUID_AND_MODE){//cmd, uuid, token, username
			byte[] packetUsername = new byte[data.length-5];
			System.arraycopy(data, 5, packetUsername, 0, data.length-5);
			String usernameAndMode = new String(packetUsername, StandardCharsets.UTF_8);
			gameMode = usernameAndMode.substring(0, 1).equals(GameValues.gamemodeIdentifier.GAMEMODE_PC) ? GameValues.gamemode.GAMEMODE_PC : GameValues.gamemode.GAMEMODE_VR;
			username = usernameAndMode.substring(1);
			uuid = BytesUtils.toInt(new byte[]{data[1], data[2], data[3], data[4]});
			
			if(uuid <= 0){
				rudp.disconnect(GameValues.disconnectError.DISCONNECTED_BAD_UUID);
				return;
			}
			for(Client c:Server.getClients()) if(c.uuid == uuid){
				rudp.disconnect(GameValues.disconnectError.DISCONNECTED_ALREADY_CONNECTED);
				return;
			}
			
			init_auth = true;
			synchronized (Server.getClients()) {
				for(Client client:Server.getClients()) {
					rudp.sendReliablePacket(client.toPlayerLoginPacket());
					if(client.gameMode.equals(GameValues.gamemode.GAMEMODE_VR)) {
						client.sendVRControllerStates(rudp);
					}
				}
			}
			EntityManager.sendListToUser(rudp);
			rudp.sendReliablePacket(new byte[]{GameValues.packets.CONNECT_GAME_INFOS});
			ready_for_connect = true;
		}
		else if(data[0] == GameValues.packets.CONNECT_FINAL_VALIDATE){
			if(init_auth && init_version && ready_for_connect){
				Server.addUser(this);
				connected = true;
			}
		}
		else if(data[0] == GameValues.packets.PLAYER_VR_CONTROLLER_STATE_UPDATE_FROMCLIENT){
			
			byte[] packet = new byte[10];
			
			byte[] uuidBytes = BytesUtils.toBytes(uuid);
			
			packet[0] = GameValues.packets.PLAYER_VR_CONTROLLER_STATE_UPDATE;
			packet[1] = uuidBytes[0];
			packet[2] = uuidBytes[1];
			packet[3] = uuidBytes[2];
			packet[4] = uuidBytes[3];
			
			int controllerId = BytesUtils.toInt(new byte[]{data[1], data[2], data[3], data[4]});
			packet[5] = data[1];
			packet[6] = data[2];
			packet[7] = data[3];
			packet[8] = data[4];
			controllers[controllerId].valid = data[5] == (byte)1;
			packet[9] = data[5];
			
			synchronized (Server.getClients()) {
				for(Client client: Server.getClients()) client.rudp.sendReliablePacket(packet);
			}
		}
		else if(data[0] == GameValues.packets.CREATE_ENTITY_FROMCLIENT){
			
			byte[] packet = new byte[data.length+4];
			
			packet[0] = GameValues.packets.CREATE_ENTITY;
			
			float posX = BytesUtils.toFloat(new byte[]{data[1], data[2], data[3], data[4]});
			float posY = BytesUtils.toFloat(new byte[]{data[5], data[6], data[7], data[8]});
			float posZ = BytesUtils.toFloat(new byte[]{data[9], data[10], data[11], data[12]});

			float rotX = BytesUtils.toFloat(new byte[]{data[13], data[14], data[15], data[16]});
			float rotY = BytesUtils.toFloat(new byte[]{data[17], data[18], data[19], data[20]});
			float rotZ = BytesUtils.toFloat(new byte[]{data[21], data[22], data[23], data[24]});
			
			byte[] entityNameBytes = new byte[data.length-25];
			for(int i=0;i<entityNameBytes.length;i++) {
				entityNameBytes[i] = data[i+25];
			}
			
			Entity e = EntityManager.createEntity(new String(entityNameBytes, StandardCharsets.UTF_8), posX, posY, posZ, rotX, rotY, rotZ);
			if(e instanceof EntityProp) ((EntityProp)e).initCollisionModel();
			byte[] idb = BytesUtils.toBytes(e.getId());
			packet[1] = idb[0];
			packet[2] = idb[1];
			packet[3] = idb[2];
			packet[4] = idb[3];
			packet[5] = data[1];
			packet[6] = data[2];
			packet[7] = data[3];
			packet[8] = data[4];
			packet[9] = data[5];
			packet[10] = data[6];
			packet[11] = data[7];
			packet[12] = data[8];
			packet[13] = data[9];
			packet[14] = data[10];
			packet[15] = data[11];
			packet[16] = data[12];
			packet[17] = data[13];
			packet[18] = data[14];
			packet[19] = data[15];
			packet[20] = data[16];
			packet[21] = data[17];
			packet[22] = data[18];
			packet[23] = data[19];
			packet[24] = data[20];
			packet[25] = data[21];
			packet[26] = data[22];
			packet[27] = data[23];
			packet[28] = data[24];
			
			for(int i=0;i<entityNameBytes.length;i++) {
				packet[i+29] = entityNameBytes[i];
			}
			
			synchronized (Server.getClients()) {
				for(Client client: Server.getClients()) client.rudp.sendReliablePacket(packet);
			}
		}
		else if(data[0] == GameValues.packets.ENTITY_DESTROY_FROMCLIENT){
			int entityId = BytesUtils.toInt(new byte[]{data[1], data[2], data[3], data[4]});
			if(EntityManager.removeEntity(entityId)) {
				synchronized (Server.getClients()) {
					data[0] = GameValues.packets.ENTITY_DESTROY;
					for(Client client: Server.getClients()) client.rudp.sendReliablePacket(data);
				}
			}
		}
	}
	
	private void sendVRControllerStates(RUDPClient rudp) {
		for(VRControllerRemote controller:controllers) {
			if(controller.valid) {
				
				byte[] packet = new byte[10];
				
				byte[] uuidBytes = BytesUtils.toBytes(uuid);
				
				packet[0] = GameValues.packets.PLAYER_VR_CONTROLLER_STATE_UPDATE;
				packet[1] = uuidBytes[0];
				packet[2] = uuidBytes[1];
				packet[3] = uuidBytes[2];
				packet[4] = uuidBytes[3];
				byte[] controlleridBytes = BytesUtils.toBytes(controller.id);
				packet[5] = controlleridBytes[0];
				packet[6] = controlleridBytes[1];
				packet[7] = controlleridBytes[2];
				packet[8] = controlleridBytes[3];
				packet[9] = controller.valid ? (byte)1 : (byte)0;
				
				rudp.sendReliablePacket(packet);
			}
		}
	}

	private byte[] toPlayerLoginPacket() {//TODO add data added during game creation (pos, ang, hp/mp, ...)
		byte[] usernameAndModeB = ((gameMode.equals(GameValues.gamemode.GAMEMODE_PC)?GameValues.gamemodeIdentifier.GAMEMODE_PC:GameValues.gamemodeIdentifier.GAMEMODE_VR) + username).getBytes(StandardCharsets.UTF_8);
		byte[] packet = new byte[usernameAndModeB.length+29];
		System.arraycopy(usernameAndModeB, 0, packet, 29, usernameAndModeB.length);
		byte[] uuidB = BytesUtils.toBytes(uuid);
		packet[0] = GameValues.packets.CONNECT_ADD_PLAYER;
		packet[1] = uuidB[0];
		packet[2] = uuidB[1];
		packet[3] = uuidB[2];
		packet[4] = uuidB[3];
		byte[] posXB = BytesUtils.toBytes(posX);
		packet[5] = posXB[0];
		packet[6] = posXB[1];
		packet[7] = posXB[2];
		packet[8] = posXB[3];
		byte[] posYB = BytesUtils.toBytes(posY);
		packet[9] = posYB[0];
		packet[10] = posYB[1];
		packet[11] = posYB[2];
		packet[12] = posYB[3];
		byte[] posZB = BytesUtils.toBytes(posZ);
		packet[13] = posZB[0];
		packet[14] = posZB[1];
		packet[15] = posZB[2];
		packet[16] = posZB[3];
		byte[] rotXB = BytesUtils.toBytes(rotX);
		packet[17] = rotXB[0];
		packet[18] = rotXB[1];
		packet[19] = rotXB[2];
		packet[20] = rotXB[3];
		byte[] rotYB = BytesUtils.toBytes(rotY);
		packet[21] = rotYB[0];
		packet[22] = rotYB[1];
		packet[23] = rotYB[2];
		packet[24] = rotYB[3];
		byte[] rotZB = BytesUtils.toBytes(rotZ);
		packet[25] = rotZB[0];
		packet[26] = rotZB[1];
		packet[27] = rotZB[2];
		packet[28] = rotZB[3];
		
		return packet;
	}
	
	byte[] toPlayerAddPacket() {//TODO add data added during game creation (pos, ang, hp/mp, ...)
		byte[] usernameAndModeB = ((gameMode.equals(GameValues.gamemode.GAMEMODE_PC)?GameValues.gamemodeIdentifier.GAMEMODE_PC:GameValues.gamemodeIdentifier.GAMEMODE_VR) + username).getBytes(StandardCharsets.UTF_8);
		byte[] packet = new byte[usernameAndModeB.length+29];
		System.arraycopy(usernameAndModeB, 0, packet, 29, usernameAndModeB.length);
		byte[] uuidB = BytesUtils.toBytes(uuid);
		packet[0] = GameValues.packets.PLAYING_ADD_PLAYER;
		packet[1] = uuidB[0];
		packet[2] = uuidB[1];
		packet[3] = uuidB[2];
		packet[4] = uuidB[3];
		byte[] posXB = BytesUtils.toBytes(posX);
		packet[5] = posXB[0];
		packet[6] = posXB[1];
		packet[7] = posXB[2];
		packet[8] = posXB[3];
		byte[] posYB = BytesUtils.toBytes(posY);
		packet[9] = posYB[0];
		packet[10] = posYB[1];
		packet[11] = posYB[2];
		packet[12] = posYB[3];
		byte[] posZB = BytesUtils.toBytes(posZ);
		packet[13] = posZB[0];
		packet[14] = posZB[1];
		packet[15] = posZB[2];
		packet[16] = posZB[3];
		byte[] rotXB = BytesUtils.toBytes(rotX);
		packet[17] = rotXB[0];
		packet[18] = rotXB[1];
		packet[19] = rotXB[2];
		packet[20] = rotXB[3];
		byte[] rotYB = BytesUtils.toBytes(rotY);
		packet[21] = rotYB[0];
		packet[22] = rotYB[1];
		packet[23] = rotYB[2];
		packet[24] = rotYB[3];
		byte[] rotZB = BytesUtils.toBytes(rotZ);
		packet[25] = rotZB[0];
		packet[26] = rotZB[1];
		packet[27] = rotZB[2];
		packet[28] = rotZB[3];
		
		return packet;
	}
	
	public RUDPClient getRudp(){
		return rudp;
	}
	
	@Override
	public void disconnect(String reason) {
		super.disconnect(reason);
		onDisconnected(reason);
	}

}
