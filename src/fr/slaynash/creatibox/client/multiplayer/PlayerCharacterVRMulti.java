package fr.slaynash.creatibox.client.multiplayer;

import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import fr.slaynash.creatibox.client.connection.ConnectionManager;
import fr.slaynash.creatibox.common.BytesUtils;
import fr.slaynash.creatibox.common.EntityMatrixUtils;
import fr.slaynash.creatibox.common.GameValues;
import slaynash.sgengine.playercharacters.PlayerCharacter;
import slaynash.sgengine.utils.DisplayManager;
import slaynash.sgengine.utils.VRUtils;
import slaynash.sgengine.utils.vr.VRController;

public class PlayerCharacterVRMulti extends PlayerCharacter {
	
	private static final float RAD_TO_DEG = (float) (180f/Math.PI);
	
	private static final float ROTATION_DELTA_ALLOWED = 2f;//en degres
	
	private float rotX = 0, rotY = 0, rotZ = 0;
	private float lrotX = 0, lrotY = 0, lrotZ = 0;
	
	private float lastSendMS = 0;
	private static boolean initialized = false;
	private static boolean[] controllersEnabled;
	
	public PlayerCharacterVRMulti() {
		super();
		this.isUsingPitchRoll = false;
	}
	
	@Override
	public void update() {
		
		if(!initialized) {
			initialized = true;
			controllersEnabled = new boolean[VRUtils.getAllControllers().length];
				
		}
		
		Vector3f p = VRUtils.getPosition();
		position.x = p.x;
		position.y = VRUtils.getRawPosition().y;
		position.z = p.z;
		viewPosition.x = p.x;
		viewPosition.y = p.y;
		viewPosition.z = p.z;
		
		Vector3f vd = VRUtils.getForward();
		
		viewDirection.x = vd.x;
		viewDirection.y = vd.y;
		viewDirection.z = vd.z;
		
		viewMatrix = VRUtils.getViewMatrix(VRUtils.EYE_CENTER);
		
		
		Vector3f rot = EntityMatrixUtils.getRotation(VRUtils.getHmdPose());
		rotX = rot.x*RAD_TO_DEG;
		rotY = rot.y*RAD_TO_DEG;
		rotZ = rot.z*RAD_TO_DEG;
		
		
		
		if(lastSendMS+(1/GameValues.SEND_PER_SECOND)*1000 < DisplayManager.getCurrentTime() && ConnectionManager.getState() != ConnectionManager.DISCONNECTED){
			
			lastSendMS = DisplayManager.getCurrentTime();
			byte[] data = new byte[13];//POSX, POSY, POSZ
			byte[] posXBytes = ByteBuffer.allocate(4).putFloat(0, viewPosition.x).array();
			byte[] posYBytes = ByteBuffer.allocate(4).putFloat(0, viewPosition.y).array();
			byte[] posZBytes = ByteBuffer.allocate(4).putFloat(0, viewPosition.z).array();
			
			data[0] = GameValues.packets.PLAYER_VR_POSITION_UPDATE_FROMCLIENT;
			
			data[1] = posXBytes[0];
			data[2] = posXBytes[1];
			data[3] = posXBytes[2];
			data[4] = posXBytes[3];
			
			data[5] = posYBytes[0];
			data[6] = posYBytes[1];
			data[7] = posYBytes[2];
			data[8] = posYBytes[3];
			
			data[9] = posZBytes[0];
			data[10] = posZBytes[1];
			data[11] = posZBytes[2];
			data[12] = posZBytes[3];
			
			ConnectionManager.getRudpClient().sendPacket(data);
			
			
			for(int i=0;i<VRUtils.getAllControllers().length;i++){
				VRController controller = VRUtils.getAllControllers()[i];
				if((controller.isValid() && controller.isPoseValid()) != controllersEnabled[i]){
					controllersEnabled[i] = !controllersEnabled[i];
					byte[] dataCS = new byte[6];//ID, STATE
					byte[] idBytesCS = BytesUtils.toBytes(i);
					
					
					dataCS[0] = GameValues.packets.PLAYER_VR_CONTROLLER_STATE_UPDATE_FROMCLIENT;
					
					dataCS[1] = idBytesCS[0];
					dataCS[2] = idBytesCS[1];
					dataCS[3] = idBytesCS[2];
					dataCS[4] = idBytesCS[3];
					dataCS[5] = (byte) (controllersEnabled[i] ? 1 : 0);
					
					ConnectionManager.getRudpClient().sendReliablePacket(dataCS);
				}
				
				if(controller.isValid()){
					
					Matrix4f cp = controller.getPose();
					Matrix4f cpr = Matrix4f.invert(cp, null);
					//System.out.println("Controller "+i+"\n"+cp);
					
					Vector3f rotC = EntityMatrixUtils.getRotation(cpr);

					float rotXC = rotC.x*RAD_TO_DEG;
					float rotYC = rotC.y*RAD_TO_DEG;
					float rotZC = rotC.z*RAD_TO_DEG;
					
					//System.out.println(cp);
					
					byte[] dataC = new byte[29];//POSX, POSY, POSZ, ROTX, ROTY, ROTZ
					byte[] posXBytesC = BytesUtils.toBytes(cp.m30);
					byte[] posYBytesC = BytesUtils.toBytes(cp.m31);
					byte[] posZBytesC = BytesUtils.toBytes(cp.m32);
					byte[] rotXBytesC = BytesUtils.toBytes(rotXC);
					byte[] rotYBytesC = BytesUtils.toBytes(rotYC);
					byte[] rotZBytesC = BytesUtils.toBytes(rotZC);
					byte[] controllerId = BytesUtils.toBytes(i);
					
					dataC[0] = GameValues.packets.PLAYER_VR_CONTROLLER_UPDATE_FROMCLIENT;
					
					dataC[1] = posXBytesC[0];
					dataC[2] = posXBytesC[1];
					dataC[3] = posXBytesC[2];
					dataC[4] = posXBytesC[3];
					
					dataC[5] = posYBytesC[0];
					dataC[6] = posYBytesC[1];
					dataC[7] = posYBytesC[2];
					dataC[8] = posYBytesC[3];
					
					dataC[9] = posZBytesC[0];
					dataC[10] = posZBytesC[1];
					dataC[11] = posZBytesC[2];
					dataC[12] = posZBytesC[3];
					
					
					dataC[13] = rotXBytesC[0];
					dataC[14] = rotXBytesC[1];
					dataC[15] = rotXBytesC[2];
					dataC[16] = rotXBytesC[3];
					
					dataC[17] = rotYBytesC[0];
					dataC[18] = rotYBytesC[1];
					dataC[19] = rotYBytesC[2];
					dataC[20] = rotYBytesC[3];
					
					dataC[21] = rotZBytesC[0];
					dataC[22] = rotZBytesC[1];
					dataC[23] = rotZBytesC[2];
					dataC[24] = rotZBytesC[3];
					
					dataC[25] = controllerId[0];
					dataC[26] = controllerId[1];
					dataC[27] = controllerId[2];
					dataC[28] = controllerId[3];
					
					ConnectionManager.getRudpClient().sendPacket(dataC);
					
					
				}
			}
			
			if(diff(lrotX, rotX, ROTATION_DELTA_ALLOWED) || diff(lrotY, rotY, ROTATION_DELTA_ALLOWED) || diff(lrotZ, rotZ, ROTATION_DELTA_ALLOWED)){
				lrotX = rotX;
				lrotY = rotY;
				lrotZ = rotZ;
				
				byte[] dataRot = new byte[13];//POSX, POSY, POSZ
				byte[] rotXBytes = ByteBuffer.allocate(4).putFloat(0, rotX).array();
				byte[] rotYBytes = ByteBuffer.allocate(4).putFloat(0, rotY).array();
				byte[] rotZBytes = ByteBuffer.allocate(4).putFloat(0, rotZ).array();
				
				dataRot[0] = GameValues.packets.PLAYER_VR_ROTATION_UPDATE_FROMCLIENT;
				
				dataRot[1] = rotXBytes[0];
				dataRot[2] = rotXBytes[1];
				dataRot[3] = rotXBytes[2];
				dataRot[4] = rotXBytes[3];
				
				dataRot[5] = rotYBytes[0];
				dataRot[6] = rotYBytes[1];
				dataRot[7] = rotYBytes[2];
				dataRot[8] = rotYBytes[3];
				
				dataRot[9] = rotZBytes[0];
				dataRot[10] = rotZBytes[1];
				dataRot[11] = rotZBytes[2];
				dataRot[12] = rotZBytes[3];
				
				ConnectionManager.getRudpClient().sendPacket(dataRot);
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void warp(Vector3f warp) {
		Vector3f envPos = new Vector3f();
		Vector3f p = VRUtils.getRawPosition();
		envPos.x = warp.x-p.x;
		envPos.y = warp.y;
		envPos.z = warp.z-p.z;
		
		VRUtils.setEnvPosition(envPos);
		
		update();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static boolean diff(float v1, float v2, float delta) {
		return (v1 < v2+delta || v2+delta < v1);
	}
}
