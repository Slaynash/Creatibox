package fr.slaynash.creatibox.server;

import java.util.HashMap;
import java.util.Map;

import fr.slaynash.creatibox.common.BytesUtils;
import fr.slaynash.creatibox.common.GameValues;
import fr.slaynash.creatibox.common.LogTimer;
import fr.slaynash.creatibox.server.commands.ServerCommand;

public class CommandRegistery {
	
	private static Map<String, ServerCommand> commands = new HashMap<String, ServerCommand>();
	
	public static void registerBasicCommands() {
		commands.put("warp", new ServerCommand() {
			
			@Override
			public void runCommand(String... args) {
				if(args.length != 4) {
					System.err.println(LogTimer.getTimeForLog()+"[Server][Commands handler] Usage: warp <playerid> <x> <y> <z>");
				}
				else {
					for(int i=0;i<Server.getClients().size();i++) {
						if(Server.getClients().get(i).uuid == Long.parseLong(args[0])) {
							try {
								
								Integer.parseInt(args[3]);
								Integer.parseInt(args[4]);

								byte[] packet = new byte[13];
								packet[0] = GameValues.packets.PLAYER_WARP;
								byte[] posX = BytesUtils.toBytes(Integer.parseInt(args[1]));
								byte[] posY = BytesUtils.toBytes(Integer.parseInt(args[2]));
								byte[] posZ = BytesUtils.toBytes(Integer.parseInt(args[3]));
								packet[1] = posX[0];
								packet[2] = posX[1];
								packet[3] = posX[2];
								packet[4] = posX[3];
								packet[5] = posY[0];
								packet[6] = posY[1];
								packet[7] = posY[2];
								packet[8] = posY[3];
								packet[9] = posZ[0];
								packet[10] = posZ[1];
								packet[11] = posZ[2];
								packet[12] = posZ[3];
								
								Server.getClients().get(i).getRudp().sendReliablePacket(packet);
								break;
							}
							catch(Exception e) {
								System.err.println(LogTimer.getTimeForLog()+"[Server][Commands handler] Usage: warp <playerid> <x> <y> <z>");
							}
							return;
						}
					}
					System.err.println(LogTimer.getTimeForLog()+"[Server][Commands handler] Warp: user not found");
				}
			}
		});
		commands.put("playerlist", new ServerCommand() {

			@Override
			public void runCommand(String... args) {
				String clientlist = "";
				for(int i=0;i<Server.getClients().size();i++) if(clientlist.equals("")) clientlist = "["+Server.getClients().get(i).uuid+"] "+Server.getClients().get(i).username; else clientlist += ", ["+Server.getClients().get(i).uuid+"] "+Server.getClients().get(i).username;
				System.out.println(LogTimer.getTimeForLog()+"[Server][Commands handler] "+Server.getClients().size()+" clients connected :");
			}
			
		});
		commands.put("kick", new ServerCommand() {

			@Override
			public void runCommand(String... args) {
				for(int i=0;i<Server.getClients().size();i++) {
					Client client = Server.getClients().get(i);
					if(client.uuid == Integer.parseInt(args[0])) {
						client.disconnect("Kicked by console");
						return;
					}
				}
				System.err.println(LogTimer.getTimeForLog()+"[Server][Commands handler] Kick: user not found");
			}
			
		});
	}
	
	public static void runCommand(String command, String... args) {
		ServerCommand scmd = commands.get(command);
		if(scmd != null) {
			try{
				scmd.runCommand(args);
			}
			catch(Exception e) {
				if(args.length == 0) {
					System.err.println(LogTimer.getTimeForLog()+"[Server][Commands handler] An error occured while trying to run command "+command+" with no parameters");
				}
				else {
					String argString = "";
					for(String arg:args) if(argString.equals("")) argString = arg; else argString += ", "+args;
					System.err.println(LogTimer.getTimeForLog()+"[Server][Commands handler] An error occured while trying to run command "+command+" with parameters {"+argString+"}");
				}
				e.printStackTrace();
			}
		}
		else {
			System.err.println(LogTimer.getTimeForLog()+"[Server][Commands handler] Command "+command+" not found.");
		}
	}
	
}
