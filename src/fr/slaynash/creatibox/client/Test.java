package fr.slaynash.creatibox.client;

import java.nio.charset.StandardCharsets;

import fr.slaynash.creatibox.client.connection.ConnectionManager;
import fr.slaynash.creatibox.common.BytesUtils;
import fr.slaynash.creatibox.common.GameValues;
import slaynash.sgengine.Configuration;

public class Test {
	
	public static void createObjectTest(String objectName) {
		
		
		byte[] name = objectName.getBytes(StandardCharsets.UTF_8);
		byte[] packet = new byte[25+name.length];
		
		packet[0] = GameValues.packets.CREATE_ENTITY_FROMCLIENT;
		byte[] px = BytesUtils.toBytes(Configuration.getPlayerCharacter().getViewPosition().x+Configuration.getPlayerCharacter().getViewDirection().x);
		byte[] py = BytesUtils.toBytes(Configuration.getPlayerCharacter().getViewPosition().y+Configuration.getPlayerCharacter().getViewDirection().y);
		byte[] pz = BytesUtils.toBytes(Configuration.getPlayerCharacter().getViewPosition().z+Configuration.getPlayerCharacter().getViewDirection().z);
		
		packet[1] = px[0];
		packet[2] = px[1];
		packet[3] = px[2];
		packet[4] = px[3];

		packet[5] = py[0];
		packet[6] = py[1];
		packet[7] = py[2];
		packet[8] = py[3];

		packet[9] = pz[0];
		packet[10] = pz[1];
		packet[11] = pz[2];
		packet[12] = pz[3];
		

		packet[13] = 0;
		packet[14] = 0;
		packet[15] = 0;
		packet[16] = 0;

		packet[17] = 0;
		packet[18] = 0;
		packet[19] = 0;
		packet[20] = 0;

		packet[21] = 0;
		packet[22] = 0;
		packet[23] = 0;
		packet[24] = 0;
		
		for(int i=0;i<name.length;i++) packet[25+i] = name[i];
		
		ConnectionManager.getRudpClient().sendReliablePacket(packet);
		
	}
	
}
