package fr.slaynash.creatibox.client.multiplayer;

import org.lwjgl.util.vector.Vector3f;

import fr.slaynash.creatibox.common.BytesUtils;
import fr.slaynash.creatibox.common.EntityMatrixUtils;
import fr.slaynash.creatibox.common.GameValues;
import fr.slaynash.creatibox.common.LogTimer;
import slaynash.sgengine.models.Renderable3dModel;
import slaynash.sgengine.models.utils.ModelManager;
import slaynash.sgengine.shaders.ShaderManager;

public class Player {

	private static final String DEFAULT_MODEL_PATH_PC = "res/models/character1.obj";
	private static final String DEFAULT_MODEL_PATH_VR = "res/vr/headset.obj";
	private static final String DEFAULT_MODEL_CONTROLLER_PATH_VR = "res/vr/controller.obj";
	public static Renderable3dModel modelPC;
	public static Renderable3dModel modelVR;
	public static Renderable3dModel modelVRController;

	public int uuid = 0;
	public String username;
	public String mode = "";

	private float posX = 0, posY = 0, posZ = 0;
	private float rotX = 0, rotY = 0, rotZ = 0;
	
	//private List<RemoteVRController> vrcontrollers = new ArrayList<RemoteVRController>();
	
	private VRControllerRemote[] vrcontrollers = new VRControllerRemote[16];
	
	public Player(byte[] data) {
		if(data[0] == GameValues.packets.CONNECT_ADD_PLAYER){
			uuid = BytesUtils.toInt(new byte[]{data[1], data[2], data[3], data[4]});
			posX = BytesUtils.toInt(new byte[]{data[5], data[6], data[7], data[8]});
			posY = BytesUtils.toInt(new byte[]{data[9], data[10], data[11], data[12]});
			posZ = BytesUtils.toInt(new byte[]{data[13], data[14], data[15], data[16]});
			rotX = BytesUtils.toInt(new byte[]{data[17], data[18], data[19], data[20]});
			rotY = BytesUtils.toInt(new byte[]{data[21], data[22], data[23], data[24]});
			rotZ = BytesUtils.toInt(new byte[]{data[25], data[26], data[27], data[28]});
			String mu = new String(data, 29, data.length-29);
			mode = mu.substring(0, 1).equals(GameValues.gamemodeIdentifier.GAMEMODE_PC) ? GameValues.gamemode.GAMEMODE_PC : GameValues.gamemode.GAMEMODE_VR;
			username = mu.substring(1);
			if(mode.equals(GameValues.gamemode.GAMEMODE_VR)){
				for(int i=0;i<vrcontrollers.length;i++) vrcontrollers[i] = new VRControllerRemote(i);
			}
		}
		else if(data[0] == GameValues.packets.PLAYING_ADD_PLAYER){
			uuid = BytesUtils.toInt(new byte[]{data[1], data[2], data[3], data[4]});
			posX = BytesUtils.toInt(new byte[]{data[5], data[6], data[7], data[8]});
			posY = BytesUtils.toInt(new byte[]{data[9], data[10], data[11], data[12]});
			posZ = BytesUtils.toInt(new byte[]{data[13], data[14], data[15], data[16]});
			rotX = BytesUtils.toInt(new byte[]{data[17], data[18], data[19], data[20]});
			rotY = BytesUtils.toInt(new byte[]{data[21], data[22], data[23], data[24]});
			rotZ = BytesUtils.toInt(new byte[]{data[25], data[26], data[27], data[28]});
			String mu = new String(data, 29, data.length-29);
			mode = mu.substring(0, 1).equals(GameValues.gamemodeIdentifier.GAMEMODE_PC) ? GameValues.gamemode.GAMEMODE_PC : GameValues.gamemode.GAMEMODE_VR;
			username = mu.substring(1);
			if(mode.equals(GameValues.gamemode.GAMEMODE_VR)){
				for(int i=0;i<vrcontrollers.length;i++) vrcontrollers[i] = new VRControllerRemote(i);
			}
		}
		System.out.println(LogTimer.getTimeForLog()+"[Player] User connected !\nUsername: "+username+"\nuuid: "+uuid+"\ntype: "+mode);
	}
	
	public void handlePostionPacket(byte[] data){
		//data[0] Reliability check
		//data[1] commande (PLAYER_POSITION_UPDATE)
		//data[2->5] uuid
		//data[6->9] PosX
		//data[10->13] PosY
		//data[14->17] PosZ
		posX = BytesUtils.toFloat(new byte[]{data[6], data[7], data[8], data[9]});
		posY = BytesUtils.toFloat(new byte[]{data[10], data[11], data[12], data[13]});
		posZ = BytesUtils.toFloat(new byte[]{data[14], data[15], data[16], data[17]});
	}
	
	public void handleRotationPacket(byte[] data){//marge de 2° par angle (éviter le spam d'une info peu utile pour les autres utilisateurs)
		//data[0] Reliability check
		//data[1] commande (PLAYER_ROTATION_UPDATE)
		//data[2->5] uuid
		//data[6->9] RotX
		//data[10->13] RotY
		//data[14->17] RotZ
		rotX = BytesUtils.toFloat(new byte[]{data[6], data[7], data[8], data[9]});
		rotY = BytesUtils.toFloat(new byte[]{data[10], data[11], data[12], data[13]});
		rotZ = BytesUtils.toFloat(new byte[]{data[14], data[15], data[16], data[17]});
	}
	


	public void handleControllerPacket(byte[] data) {
		int cid = BytesUtils.toInt(new byte[]{data[30], data[31], data[32], data[33]});
		vrcontrollers[cid].posX = BytesUtils.toFloat(new byte[]{data[6], data[7], data[8], data[9]});
		vrcontrollers[cid].posY = BytesUtils.toFloat(new byte[]{data[10], data[11], data[12], data[13]});
		vrcontrollers[cid].posZ = BytesUtils.toFloat(new byte[]{data[14], data[15], data[16], data[17]});
		

		vrcontrollers[cid].rotX = BytesUtils.toFloat(new byte[]{data[18], data[19], data[20], data[21]});
		vrcontrollers[cid].rotY = BytesUtils.toFloat(new byte[]{data[22], data[23], data[24], data[25]});
		vrcontrollers[cid].rotZ = BytesUtils.toFloat(new byte[]{data[26], data[27], data[28], data[29]});
	}
	
	public void setControllerState(int id, boolean valid){
		vrcontrollers[id].valid = valid;
	}

	private void initPCModel() {
		modelPC = ModelManager.loadObj(DEFAULT_MODEL_PATH_PC, "res/models/player.png", null, null);
	}
	
	private void initVRModels() {
		modelVR = ModelManager.loadObj(DEFAULT_MODEL_PATH_VR, "res/vr/headset.tga", null, null);
		modelVRController = ModelManager.loadObj(DEFAULT_MODEL_CONTROLLER_PATH_VR, "res/vr/controller_texture.png", null, "res/vr/controller_spec.png");
	}
	
	public void onDisconnect(String reason) {
		System.out.println(LogTimer.getTimeForLog()+"[Player] User "+username+" just disconnected ! ("+reason+")");
	}

	public void render() {
		if(mode.equals(GameValues.gamemode.GAMEMODE_VR)){
			if(modelVR == null) initVRModels();
			ShaderManager.shader_loadTransformationMatrix(EntityMatrixUtils.createEntityTransformationMatrix(new Vector3f(posX, posY, posZ), rotX, rotY, rotZ, 1));
			modelVR.render();
			for(VRControllerRemote controller:vrcontrollers) if(controller.valid) controller.render3D();
		}
		else{
			if(modelPC == null) initPCModel();
			ShaderManager.shader_loadTransformationMatrix(EntityMatrixUtils.createEntityTransformationMatrix(new Vector3f(posX, posY, posZ), 0, rotY, 0, 1));
			modelPC.render();
		}
	}
	
	public void renderVR(int eye) {
		
		if(mode.equals(GameValues.gamemode.GAMEMODE_VR)){
			if(modelVR == null) initVRModels();
			ShaderManager.shader_loadTransformationMatrix(EntityMatrixUtils.createEntityTransformationMatrix(new Vector3f(posX, posY, posZ), rotX, rotY, rotZ, 1));
			modelVR.renderVR(eye);
			for(VRControllerRemote controller:vrcontrollers) if(controller.valid) controller.renderVR();
		}
		else{
			if(modelPC == null) initPCModel();
			ShaderManager.shader_loadTransformationMatrix(EntityMatrixUtils.createEntityTransformationMatrix(new Vector3f(posX, posY, posZ), 0, rotY, 0, 1));
			modelPC.renderVR(eye);
		}
	}
}