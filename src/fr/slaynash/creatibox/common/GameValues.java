package fr.slaynash.creatibox.common;

import fr.slaynash.communication.rudp.Values;

public class GameValues {
	public static final String GAME_VERSION = "0.2";
	public static final int SERVER_GAME_PORT = 26341;//changed for VR test, default: 36054
	
	public static final int SEND_PER_SECOND = 20;
	
	private static boolean gameserverDisconnectHandled = false;
	public static String serverAddress = "88.163.192.34";
	private static boolean isServer = false;
	
	public static class packets{
		public static final byte VALIDATE										= -128 + Values.RESEVED_COMMAND_BYTES;
		public static final byte CONNECT_GAME_VERSION							= -127 + Values.RESEVED_COMMAND_BYTES;
		public static final byte CONNECT_UUID_AND_MODE							= -126 + Values.RESEVED_COMMAND_BYTES;
		public static final byte CONNECT_ADD_PLAYER								= -125 + Values.RESEVED_COMMAND_BYTES;
		public static final byte CONNECT_GAME_INFOS								= -124 + Values.RESEVED_COMMAND_BYTES;
		public static final byte CONNECT_FINAL_VALIDATE							= -123 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_DISCONNECTED_EVENT						= -122 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYING_ADD_PLAYER								= -121 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_PC_POSITION_UPDATE						= -120 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_PC_POSITION_UPDATE_FROMCLIENT			= -119 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_PC_ROTATION_UPDATE						= -118 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_PC_ROTATION_UPDATE_FROMCLIENT			= -117 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_VR_POSITION_UPDATE						= -116 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_VR_POSITION_UPDATE_FROMCLIENT			= -115 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_VR_ROTATION_UPDATE						= -114 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_VR_ROTATION_UPDATE_FROMCLIENT			= -113 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_VR_CONTROLLER_UPDATE					= -112 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_VR_CONTROLLER_UPDATE_FROMCLIENT			= -111 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_VR_CONTROLLER_STATE_UPDATE				= -110 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_VR_CONTROLLER_STATE_UPDATE_FROMCLIENT	= -109 + Values.RESEVED_COMMAND_BYTES;
		public static final byte PLAYER_WARP									= -108 + Values.RESEVED_COMMAND_BYTES;
		public static final byte CREATE_ENTITY_FROMCLIENT						= -107 + Values.RESEVED_COMMAND_BYTES;
		public static final byte CREATE_ENTITY									= -106 + Values.RESEVED_COMMAND_BYTES;
		public static final byte ENTITY_UPDATE									= -105 + Values.RESEVED_COMMAND_BYTES;
		public static final byte ADD_ENTITY_ONCONNECT							= -104 + Values.RESEVED_COMMAND_BYTES;
		public static final byte ENTITY_DESTROY									= -103 + Values.RESEVED_COMMAND_BYTES;
		public static final byte ENTITY_DESTROY_FROMCLIENT						= -102 + Values.RESEVED_COMMAND_BYTES;
	}
	
	public static class disconnectError{
		public static final String DISCONNECTED_INTERNAL_SERVER_ERROR = "DISCONNECTED_INTERNAL_SERVER_ERROR";
		public static final String DISCONNECTED_BAD_VERSION = "DISCONNECTED_BAD_VERSION";
		public static final String DISCONNECTED_BAD_UUID = "DISCONNECTED_BAD_UUID";
		public static final String DISCONNECTED_ALREADY_CONNECTED = "DISCONNECTED_ALREADY_CONNECTED";
	}
	
	public static class gamemode{
		public static final String GAMEMODE_PC = "PC";
		public static final String GAMEMODE_VR = "VR";
	}
	
	public static class gamemodeIdentifier{
		public static final String GAMEMODE_PC = "P";
		public static final String GAMEMODE_VR = "V";
	}

	public static boolean isGameserverDisconnectHandled() {
		return gameserverDisconnectHandled;
	}

	public static void setGameserverDisconnectHandled(boolean gameserverDisconnectHandled) {
		GameValues.gameserverDisconnectHandled = gameserverDisconnectHandled;
	}

	public static void setServer(boolean serverMode) {
		isServer = serverMode;
	}
	
	public static boolean isServer() {
		return isServer;
	}
}
