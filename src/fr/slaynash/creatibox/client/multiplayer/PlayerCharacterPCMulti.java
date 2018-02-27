package fr.slaynash.creatibox.client.multiplayer;

import java.nio.ByteBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.input.Mouse;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.dynamics.ActionInterface;
import com.bulletphysics.linearmath.Transform;

import fr.slaynash.creatibox.client.connection.ConnectionManager;
import fr.slaynash.creatibox.common.GameValues;
import slaynash.sgengine.Configuration;
import slaynash.sgengine.inputs.KeyboardControlManager;
import slaynash.sgengine.playercharacters.PlayerCharacter;
import slaynash.sgengine.utils.DisplayManager;
import slaynash.sgengine.world3d.CharacterController;
import slaynash.sgengine.world3d.CollisionManager3d;

public class PlayerCharacterPCMulti extends PlayerCharacter {
	
	private static final float DEGREE_TO_RADIANS = (float) (Math.PI/180f);
	private static final float PI = 3.141592f;
	private static final float ROTATION_DELTA_ALLOWED = 2f;//en degrés
	
	
	private static final float WALK_SPEED = 0.02f;
	private static final float RUN_SPEED = 0.10f;
	private static final float PLAYER_WIDTH = 0.40f;
	private static final float PLAYER_HEIGHT = 1.60f;
	private static final float PLAYER_STEP_HEIGHT = 0.4f;
	private static final float GRAVITY = 0.98f;
	
	private static float forward, left;
	
	public static CharacterController controller;
	private static PairCachingGhostObject entity;
	
	private static float lrotX = 0, lrotY = 0;
	private static float lastSendMS = 0;
	
	public PlayerCharacterPCMulti() {
		super();
		BoxShape playerShape = new BoxShape(new Vector3f(PLAYER_WIDTH, PLAYER_HEIGHT*.5f, PLAYER_STEP_HEIGHT));
		Transform t = new Transform(); t.setIdentity();
		t.origin.set(0, 1, 0);
		
		entity = new PairCachingGhostObject();
		entity.setCollisionShape(playerShape);
		entity.setCollisionFlags(CollisionFlags.CHARACTER_OBJECT);
		entity.setWorldTransform(t);
		controller = new CharacterController(entity, playerShape, PLAYER_STEP_HEIGHT);
		controller.setGravity(GRAVITY);
		controller.setJumpSpeed(1.2f);
		
		CollisionManager3d.registerThePlayer(getEntity(), getController());
	}
	
	public void updateControls() {
		updateAimDir();
		checkInputs();
	}
	
	@Override
	public void update(){
		
		float dx = (float) (forward * Math.sin(yaw) + left * Math.cos(yaw));
		float dz = (float) (forward * Math.cos(yaw) - left * Math.sin(yaw));
		
		controller.setWalkDirection(new Vector3f(dx, 0f, dz));
		
		viewDirection.x = (float) -(Math.sin(yaw) * Math.cos(pitch));
		viewDirection.z = (float) -(Math.cos(yaw) * Math.cos(pitch));
		viewDirection.y = (float) Math.sin(pitch);
		
		Vector3f p = entity.getWorldTransform(new Transform()).origin;
		position.x = p.x;
		position.y = p.y-PLAYER_HEIGHT*.5f;
		position.z = p.z;
		viewPosition.x = p.x;
		viewPosition.y = p.y+PLAYER_HEIGHT*.5f;
		viewPosition.z = p.z;
		
		if(lastSendMS+(1/GameValues.SEND_PER_SECOND)*1000 < DisplayManager.getCurrentTime() && ConnectionManager.getState() != ConnectionManager.DISCONNECTED){
			lastSendMS = DisplayManager.getCurrentTime();
			
			byte[] data = new byte[13];//POSX, POSY, POSZ
			byte[] posXBytes = ByteBuffer.allocate(4).putFloat(0, position.x).array();
			byte[] posYBytes = ByteBuffer.allocate(4).putFloat(0, position.y).array();
			byte[] posZBytes = ByteBuffer.allocate(4).putFloat(0, position.z).array();
			
			data[0] = GameValues.packets.PLAYER_PC_POSITION_UPDATE_FROMCLIENT;
			
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
			
			if(diff(lrotX, pitch/DEGREE_TO_RADIANS, ROTATION_DELTA_ALLOWED) || diff(lrotY, yaw/DEGREE_TO_RADIANS, ROTATION_DELTA_ALLOWED)){
				lrotX = pitch/DEGREE_TO_RADIANS;
				lrotY = yaw/DEGREE_TO_RADIANS;
				
				byte[] dataRot = new byte[13];//POSX, POSY, POSZ
				byte[] rotXBytes = ByteBuffer.allocate(4).putFloat(0, pitch/DEGREE_TO_RADIANS).array();
				byte[] rotYBytes = ByteBuffer.allocate(4).putFloat(0, yaw/DEGREE_TO_RADIANS).array();
				byte[] rotZBytes = ByteBuffer.allocate(4).putFloat(0, 0).array();
				
				dataRot[0] = GameValues.packets.PLAYER_PC_ROTATION_UPDATE_FROMCLIENT;
				
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
	
	private static boolean diff(float v1, float v2, float delta) {
		return (v1 < v2+delta || v2+delta < v1);
	}
	
	private void updateAimDir() {
		yaw -= (Mouse.getDX())*0.01f*Configuration.getMouseSensibility();
		pitch += (Mouse.getDY())*0.01f*Configuration.getMouseSensibility();
		
		if (yaw >= PI*2f){
			yaw -= PI*2f;
		}
		else if (yaw < 0f){
			yaw += PI*2f;
		}
		
		if (pitch > PI*0.5f-0.0001f)
			pitch = PI/2-0.0001f;
		else if (pitch < -PI*0.5f+0.0001f)
			pitch = -PI/2+0.0001f;
		
		rotation.x = pitch/DEGREE_TO_RADIANS;
		rotation.y = yaw/DEGREE_TO_RADIANS;
		rotation.z = 0;
		
	}
	
	private void checkInputs(){
		if(KeyboardControlManager.isPressed("run")){
			if(KeyboardControlManager.isPressed("forward"))
				forward = -RUN_SPEED;
			else if(KeyboardControlManager.isPressed("backward"))
				forward = RUN_SPEED;
			else forward = 0;
			
			if(KeyboardControlManager.isPressed("right"))
				left = RUN_SPEED;
			else if(KeyboardControlManager.isPressed("left"))
				left = -RUN_SPEED;
			else left = 0;
		}
		else{
			if(KeyboardControlManager.isPressed("forward"))
				forward = -WALK_SPEED;
			else if(KeyboardControlManager.isPressed("backward"))
				forward = WALK_SPEED;
			else forward = 0;
			
			if(KeyboardControlManager.isPressed("right"))
				left = WALK_SPEED;
			else if(KeyboardControlManager.isPressed("left"))
				left = -WALK_SPEED;
			else left = 0;
		}
		
		if(KeyboardControlManager.isPressed("jump"))
			controller.jump();
	}
	



	public ActionInterface getController() {
		return controller;
	}


	public PairCachingGhostObject getEntity() {
		return entity;
	}

	@Override
	public void warp(org.lwjgl.util.vector.Vector3f warp){
		position.x = warp.x;
		position.y = warp.y;
		position.z = warp.z;
		viewPosition.x = warp.x;
		viewPosition.y = warp.y+PLAYER_HEIGHT;
		viewPosition.z = warp.z;
		
		controller.warp(new Vector3f(warp.x, warp.y, warp.z));
	}
}
