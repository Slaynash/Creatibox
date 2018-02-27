package fr.slaynash.creatibox.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import fr.slaynash.communication.rudp.RUDPServer;
import fr.slaynash.creatibox.common.BytesUtils;
import fr.slaynash.creatibox.common.GameValues;
import fr.slaynash.creatibox.common.LogTimer;
import fr.slaynash.creatibox.common.RessourceLoader;
import slaynash.sgengine.Configuration;

public class Server {
	
	private static RUDPServer server;
	private static List<Client> clients = Collections.synchronizedList(new ArrayList<Client>());

	public static void start(String decodedPath, String[] args, InputStream stream) {

		Configuration.setInstallPath(decodedPath);
		
		try {
			RessourceLoader.loadRessources();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			server = new RUDPServer(GameValues.SERVER_GAME_PORT);
		} catch (SocketException e) {
			System.err.println(LogTimer.getTimeForLog()+"[Server] Unable to start server on port "+GameValues.SERVER_GAME_PORT+":");
			e.printStackTrace();
			return;
		}
		
		server.setClientPacketHandler(Client.class);
		CommandRegistery.registerBasicCommands();
		WorldManager.init();
		WorldManager.startWorld();
		server.start();
		
		Scanner sc = new Scanner(stream);
		while(true){
			String in = sc.nextLine();
			if(in.equals("stop")){
				stop();
				break;
			}
			else{
				String[] data = in.split(" ");
				String[] cmdargs = new String[data.length-1];
				for(int i=1;i<data.length;i++) {
					cmdargs[i-1] = data[i];
				}
				CommandRegistery.runCommand(data[0], cmdargs);
			}
		}
		sc.close();
	}
	
	public static List<Client> getClients() {
		return clients;
	}

	public static void addUser(Client client) {
		synchronized (client) {
			byte[] packet = client.toPlayerAddPacket();
			for(Client c:clients) c.getRudp().sendReliablePacket(packet);
			clients.add(client);
			System.out.println(LogTimer.getTimeForLog()+"[Server] User ID"+client.uuid+" connected as "+client.username);
		}
	}

	public static void userDisconnected(Client client, String reason) {
		System.out.println(LogTimer.getTimeForLog()+"[Server] User "+client.uuid+" disconnected ("+reason+")");
		synchronized (clients) {
			clients.remove(client);
			byte[] reasonB = new byte[]{};
			try {
				reasonB = reason.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			byte[] uuidB = BytesUtils.toBytes(client.uuid);
			byte[] data = new byte[reasonB.length+5];
			System.arraycopy(reasonB, 0, data, 5, reasonB.length);
			data[0] = GameValues.packets.PLAYER_DISCONNECTED_EVENT;
			data[1] = uuidB[0];
			data[2] = uuidB[1];
			data[3] = uuidB[2];
			data[4] = uuidB[3];
			for(Client c:clients) c.getRudp().sendReliablePacket(data);
		}
	}
	
	public static void stop() {
		server.stop();
		WorldManager.stopWorld();
	}

}
